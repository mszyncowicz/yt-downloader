package controller;

import api.Observer;
import bean.DownloadBeanInterface;
import downloader.LogObserver;
import downloader.YoutubeDLCommander;
import downloader.YoutubeDlCommanderParameters;
import dto.GeneralMessageDTO;
import dto.LogObserverDTO;
import dto.LogObserversDTO;
import dto.YTParametersDTO;

import java.util.*;
import java.util.stream.Collectors;

import lombok.Setter;
import model.Download;
import model.Session;
import org.opensaml.xmlsec.signature.P;
import service.RecordService;
import service.SessionService;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.transaction.*;
import javax.transaction.NotSupportedException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Record;
import sun.rmi.runtime.Log;


//TODO make Session check in AOP
@Path("/download")
public class DownloadController {

    @Any
    @Setter
    @Inject
    SessionService sessionService;

    @Any
    @Setter
    @Inject
    RecordService recordService;

    @Setter
    @Inject
    UserTransaction userTransaction;

    @Any
    @Setter
    @Inject
    DownloadBeanInterface downloadBeanInterface;


    @POST
    @Path("/initialize")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response initializeDownload(@HeaderParam("X-session-token") String token, YTParametersDTO parametersDTO) throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException, NotSupportedException {
        if (sessionService == null) throw new IllegalStateException("Session service is null");
        if (recordService == null) throw new IllegalStateException("Record service is null");
        if (downloadBeanInterface == null) throw new IllegalStateException("downloadBeanInterface is null");
        if (!sessionExist(token)){
            return Response.status(405).entity(GeneralMessageDTO.getInvalidSession()).build();
        }
        Session session = sessionService.getSessionByToken(token);
        String link = parametersDTO.getUrl();
        UUID id = UUID.randomUUID();
        Map<String,String> parameters = new HashMap<>();
        parameters.put(YoutubeDlCommanderParameters.uuid.name(),id.toString());
        parameters.put(YoutubeDlCommanderParameters.url.name(),link);
        String mediaType = parametersDTO.getMediaType();
        String command;
        if (mediaType.equals(model.MediaType.audio.name())){
            command = YoutubeDLCommander.getBestAudio().getCommand(parameters);
        } else {
            mediaType = "video";
            command = YoutubeDLCommander.getBestVideo().getCommand(parameters);
        }
        Record record = new Record(link,command);
        record.setMediaType(model.MediaType.valueOf(mediaType));

        record.setId(id);
        userTransaction.begin();
        session = sessionService.getSessionByToken(session.getToken());
        record.setSession(session);
        session.getRecords().add(record);
        recordService.save(record);
        sessionService.save(session);
        userTransaction.commit();
        downloadBeanInterface.prepareDownload(record, Collections.emptyList());

        return returnEntity(record);
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

        List<Download> startedDownloads = downloadBeanInterface.getStartedDownloads()
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

        return returnEntity(logObserversDTO);
    }

    @GET
    @Path("/current")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogObserver(@HeaderParam("X-session-token") String token, String uuid){
        if (!sessionExist(token)){
            return Response.status(405).entity(GeneralMessageDTO.getInvalidSession()).build();
        }

        Optional<Download> any = downloadBeanInterface.getStartedDownloads().stream().filter(d -> d.getRecord().getId().toString().equals(uuid)).findAny();
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

    Response returnEntity(Object entity){
        return Response.ok().entity(entity).build();
    }

    boolean sessionExist(String token){
        if (token == null || token.length() != Session.scale) return false;
        return sessionService.getSessionByToken(token) != null;
    }
}
