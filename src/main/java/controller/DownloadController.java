package controller;

import api.Commander;
import api.Observer;
import bean.DownloadBeanInterface;
import java.time.Duration;
import downloader.LogObserver;
import downloader.WrongParametersException;
import downloader.YoutubeDLCommander;
import downloader.YoutubeDlCommanderParameters;
import dto.*;

import java.util.*;
import java.util.stream.Collectors;

import lombok.Setter;
import model.Download;
import model.Session;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.opensaml.xmlsec.signature.P;
import printer.PrinterExecutor;
import printer.YoutubeDLPrinter;
import printer.YtJsonReader;
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

    static final String TIME_MATCHER = "[0-9]+";

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
        String command = getCommand(parametersDTO, mediaType, parameters).getCommand(parameters);

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
        downloadBeanInterface.prepareDownload(record, parametersDTO.isOverwrite(),Collections.emptyList());

        return returnEntity(record);
    }


    YoutubeDLCommander getCommand(YTParametersDTO parametersDTO, String mediaType, Map<String,String> parameters) {
        if (mediaType.equals(model.MediaType.audio.name())){
            if (parametersDTO.isTimed()){
                Optional<String> postArgs = getPostArgs(parametersDTO.getTimeFrom(), parametersDTO.getTimeTo());
                if (postArgs.isPresent()){
                    parameters.put(YoutubeDLCommander.TIME_PARAM, postArgs.get());
                    return YoutubeDLCommander.getBestAudioTimed();
                }
            }
            return YoutubeDLCommander.getBestAudio();

        } else {
            if (parametersDTO.isTimed()){
                Optional<String> postArgs = getPostArgs(parametersDTO.getTimeFrom(), parametersDTO.getTimeTo());
                if (postArgs.isPresent()){
                    parameters.put(YoutubeDLCommander.TIME_PARAM, postArgs.get());
                    return YoutubeDLCommander.getBestVideoTimed();
                }
            }
            return YoutubeDLCommander.getBestVideo();
        }
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

    YtMediaDetails printJsonForUrl(String url) throws WrongParametersException {
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

    Optional<String> getPostArgs(String timeFrom, String timeTo) {
        if (timeFrom == null || timeTo == null) return Optional.ofNullable(null);

        else if (!timeFrom.matches(TIME_MATCHER) || !timeTo.matches(TIME_MATCHER)) return Optional.ofNullable(null);
        else {
            final Integer timeFromInt = Integer.valueOf(timeFrom);
            final Integer timeToInt = Integer.valueOf(timeTo);
            Duration durationFrom0ToStart = Duration.ofSeconds(timeFromInt);
            Duration durationFrom0ToEnd = Duration.ofSeconds(timeToInt - timeFromInt);
            String lvResult = "-ss " + DurationFormatUtils.formatDuration(durationFrom0ToStart.toMillis(),"HH:mm:ss", true) + " -t " + DurationFormatUtils.formatDuration(durationFrom0ToEnd.toMillis(),"HH:mm:ss", true);
            return Optional.of(lvResult);
        }
    }
}
