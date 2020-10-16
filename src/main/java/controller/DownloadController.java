package controller;

import api.Commander;
import api.Observer;

import bean.DownloadBeanInterface;
import downloader.*;
import dto.*;

import java.util.*;
import java.util.stream.Collectors;

import lombok.Setter;
import model.Download;
import model.Session;
import printer.PrinterExecutor;
import printer.YoutubeDLPrinter;
import printer.YtJsonReader;
import qualifier.DownloadInfoServiceQualifier;
import service.*;

import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.transaction.*;
import javax.transaction.NotSupportedException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Record;


//TODO make Session check in AOP
@Path("/download")
public class DownloadController {

    @DownloadInfoServiceQualifier
    @Setter
    @Inject
    DownloadInfoServiceInterface downloadInfo;

    @Any
    @Setter
    @Inject
    YtDownloaderServiceInterface downloaderService;

    @Any
    @Setter
    @Inject
    SessionService sessionService;


    @POST
    @Path("/initialize")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response initializeDownload(@HeaderParam("X-session-token") String token, YTParametersDTO parametersDTO) throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException, NotSupportedException {
        if (!sessionExist(token)){
            return Response.status(405).entity(GeneralMessageDTO.getInvalidSession()).build();
        }

        Session session = sessionService.getSessionByToken(token);
        return returnEntity(downloaderService.initializeDownload(session, parametersDTO));
    }

    @POST
    @Path("/getInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVideoInfo(@HeaderParam("X-session-token") String token, String url ){
        if (!sessionExist(token)){
            return Response.status(405).entity(GeneralMessageDTO.getInvalidSession()).build();
        }
        if (url == null || url.isEmpty() || !url.matches("http[s]{0,1}://[-a-zA-Z0-9+&@#/%?=~_|!,.;]+")){
            return Response.status(400).entity(GeneralMessageDTO.getMessage(400,"No url specified")).build();
        }
        try {
            YtMediaDetails ytMediaDetails = printJsonForUrl(url);
            return Response.ok(ytMediaDetails).build();
        } catch (WrongParametersException e) {
            e.printStackTrace();
            return Response.status(500).entity(GeneralMessageDTO.getMessage(500,"Internal Error: Unexpected error at parsing printed json")).build();
        }
    }

    private YtMediaDetails printJsonForUrl(String url) throws WrongParametersException {
        Commander commander = new YoutubeDLPrinter();
        Map<String, String> params = new HashMap<>();
        params.put(YoutubeDLCommander.URL_PARAM, url);
        PrinterExecutor printerExecutor = new PrinterExecutor();
        Object execute = printerExecutor.execute(commander, params);
        YtJsonReader jsonReader = new YtJsonReader();
        if (execute != null) return jsonReader.getDataFrom(execute.toString());
        else return null;
    }

    @GET
    @Path("/current/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentDownloadInfo(@HeaderParam("X-session-token") String token){
        if (!sessionExist(token)){
            return Response.status(405).entity(GeneralMessageDTO.getInvalidSession()).build();
        }
        LogObserversDTO logObserversDTO = new LogObserversDTO();
        LinkedList<LogObserverDTO> logObserverDTOList = new LinkedList<>();
        logObserversDTO.setLogObserversDTOList(logObserverDTOList);

        List<Download> startedDownloads = downloadInfo.getStartedDownloads()
                .stream().filter(d -> d.getRecord().getSession().getToken().equals(token)).collect(Collectors.toList());

        for (Download d : startedDownloads){
            Optional<Observer> any = d.getObservers().stream().filter(o -> o instanceof LogObserver).findAny();
            if (any.isPresent()){
                LogObserver observer = (LogObserver) any.get();
                LogObserverDTO dto = new LogObserverDTO();
                dto.setLastMessage(observer.getLastMessage());
                Record observerRecord = observer.getRecord();
                dto.setRecordUUID(observerRecord.getId().toString());
                dto.setPercentage(observer.getPercentage());
                dto.setState(observerRecord.getState().name());
                dto.setTitle(observer.getTitle());
                logObserverDTOList.add(dto);
            }
        }

        if(downloadInfo.hasErrorForSession(token)){
            logObserversDTO.setHasAnyErrors(true);
        }

        return returnEntity(logObserversDTO);
    }

    @GET
    @Path("/current")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogObserver(@HeaderParam("X-session-token") String token, String uuid){
        if (!sessionExist(token)){
            return Response.status(405).entity(GeneralMessageDTO.getInvalidSession()).build();
        }

        Optional<Download> any = downloadInfo.getStartedDownloads().stream().filter(d -> d.getRecord().getId().toString().equals(uuid)).findAny();
        if (!any.isPresent()) {
            return Response.status(500).entity(GeneralMessageDTO.getMessage(500,"Could not find observer with id")).build();

        }

        return returnEntity(any.get()
                .getObservers()
                .stream()
                .filter(d -> d instanceof LogObserver)
                .findAny()
                .get());
    }

    @GET
    @Path("/error")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getError(@HeaderParam("X-session-token") String token){
        if (!sessionExist(token)){
            return Response.status(405).entity(GeneralMessageDTO.getInvalidSession()).build();
        }

       if (downloadInfo.hasErrorForSession(token)){
           return returnEntity(downloadInfo.dequeForSession(token));
       } else {
           return Response.status(404).entity(GeneralMessageDTO.getMessage(404,"Error message was not found")).build();
       }
    }

    Response returnEntity(Object entity){
        return Response.ok().entity(entity).build();
    }

    boolean sessionExist(String token){
        if (token == null || token.length() != Session.scale) return false;
        return sessionService.getSessionByToken(token) != null;
    }


}
