package controller;

import bean.DownloadBeanInterface;
import downloader.LogObserver;
import downloader.YoutubeDLCommander;
import dto.LogObserverDTO;
import dto.LogObserversDTO;
import dto.YTParametersDTO;
import lombok.extern.slf4j.Slf4j;
import model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import service.RecordService;

import javax.enterprise.concurrent.ManagedExecutorService;
import javax.transaction.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import service.SessionService;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@Slf4j
public class DownloadControllerTest {

    DownloadController downloadController;

    UserTransactionMock userTransactionMock;

    List<List<Object>> objectListsToSave;

    Set<Object> saved = new HashSet<>();

    DownloadBeanInterface downloadBeanMock;

    @Before
    public void init() {
        downloadController = spy(DownloadController.class);
        RecordService recordServiceMock = mock(RecordService.class);
        ManagedExecutorService managedExecutorService = mock(ManagedExecutorService.class);
        //downloadController.setExecutorService(managedExecutorService);
        objectListsToSave = new LinkedList<>();

        userTransactionMock = new UserTransactionMock() {
            @Override
            public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
                int status = getStatus();
                if (status < 1 || objectListsToSave.get(status - 1) == null) {
                    log.warn("No transaction or objects to commit");
                } else {
                    for (Object o : objectListsToSave.get(status - 1)) {
                        saved.add(o);
                    }
                }
                rollback(); //for decrement of status
            }
        };
        downloadController.setUserTransaction(userTransactionMock);
        when(recordServiceMock.save(any())).then(a -> {
            Record rec = a.getArgumentAt(0, Record.class);
            int status = userTransactionMock.getStatus();
            if (status == 0) {
                //do nothing not commited saves wont save
            } else {
                if (status > 0) {
                    if (objectListsToSave.size() < status) {
                        while (objectListsToSave.size() < status) {
                            objectListsToSave.add(new LinkedList<>());
                        }
                    }
                    objectListsToSave.get(status - 1).add(rec);
                }
            }
            return rec;
        });
        downloadController.setUserTransaction(userTransactionMock);
        downloadController.setRecordService(recordServiceMock);
        downloadBeanMock = mock(DownloadBeanInterface.class);
        downloadController.setDownloadBeanInterface(downloadBeanMock);
        SessionService sessionServiceMock = mock(SessionService.class);
        downloadController.setSessionService(sessionServiceMock);
        when(sessionServiceMock.getSessionByToken(anyString())).thenReturn(new Session());
    }

    @Test
    public void testSave() throws HeuristicRollbackException, RollbackException, NotSupportedException, HeuristicMixedException, SystemException {
        YTParametersDTO ytParametersDTO = new YTParametersDTO();
        ytParametersDTO.setMediaType("audio");
        ytParametersDTO.setUrl("www.rosyjskatuletka.pl");
        ytParametersDTO.setTimed(false);
        ytParametersDTO.setOverwrite(true);
        try {
            downloadController.initializeDownload(Session.generateRandom(Session.scale), ytParametersDTO);
        } catch (AbstractMethodError error) {

        }
        Assert.assertTrue(saved.size() > 0);
        boolean savedRecord = false;
        for (Object o : saved) {
            if (o instanceof Record) {
                savedRecord = true;
            }
        }
        Assert.assertTrue(savedRecord);

        verify(downloadBeanMock, times(1)).prepareDownload(anyObject(),eq(ytParametersDTO.isOverwrite()),eq(Collections.emptyList()));

    }

    @Test
    public void getStartedLogObservers(){

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        List<Download> downloads = new ArrayList<>();
        Record record = new Record();
        UUID id = UUID.randomUUID();
        String token = Session.generateRandom(Session.scale);

        record.setId(id);
        record.setState(State.downloading);
        Session session = new Session();
        record.setSession(session);
        session.setToken(token);
        Download download = new Download(record,true);
        LogObserver observer = mock(LogObserver.class);
        String last_message = "Last Message";
        when(observer.getLastMessage()).thenReturn(last_message);
        when(observer.getRecord()).thenReturn(record);
        float percentage = 55.5f;
        when(observer.getPercentage()).thenReturn(percentage);

        download.addObserver(observer);
        downloads.add(download);
        when(downloadBeanMock.getStartedDownloads()).thenReturn(downloads);

        doAnswer(a -> {
            Object argumentAt = a.getArgumentAt(0, Object.class);
            boolean result = false;
            if (argumentAt instanceof LogObserversDTO){
                LogObserversDTO logOberversDto = (LogObserversDTO) argumentAt;
                Assert.assertTrue(logOberversDto.getLogObserversDTOList().size() == 1);
                LogObserverDTO logObserverDTO = logOberversDto.getLogObserversDTOList().get(0);
                Assert.assertTrue(last_message.equals(logObserverDTO.getLastMessage()));
                Assert.assertTrue(record.getId().toString().equals(logObserverDTO.getRecordUUID()));
                Assert.assertTrue(record.getState().name().equals(logObserverDTO.getState()));
                Assert.assertTrue(Float.compare(percentage,logObserverDTO.getPercentage()) == 0);
                result = true;
            } else if (argumentAt instanceof LogObserver){
                result = true;
            }
            atomicBoolean.set(result);
            return null;
        }).when(downloadController).returnEntity(anyObject());

        downloadController.getCurrentDownloadInfo(token);

        verify(downloadBeanMock,times(1)).getStartedDownloads();
        verify(observer,times(1)).getLastMessage();
        verify(observer,times(1)).getPercentage();
        verify(observer,times(1)).getRecord();
        verify(downloadController,times(1)).sessionExist(anyString());

        Assert.assertTrue(atomicBoolean.get());
        atomicBoolean.set(false);
        downloadController.getLogObserver(token, id.toString());
        Assert.assertTrue(atomicBoolean.get());

    }

    @Test
    public void shouldNotMatchHours(){
        String toMatch = "00:03:23";
        Assert.assertFalse(toMatch.matches(DownloadController.TIME_MATCHER));
    }

    @Test
    public void postArgsTest_shouldNotBePresentIfAnyNull1(){
        Assert.assertFalse(downloadController.getPostArgs(null,null).isPresent());
    }
    @Test
    public void postArgsTest_shouldNotBePresentIfAnyNull2(){
        Assert.assertFalse(downloadController.getPostArgs("0",null).isPresent());
    }
    @Test
    public void postArgsTest_shouldNotBePresentIfAnyNull3(){
        Assert.assertFalse(downloadController.getPostArgs(null,"12").isPresent());
    }
    @Test
    public void postArgsTest_shouldNotBePresentIfAnyNotMatched1(){
        Assert.assertFalse(downloadController.getPostArgs("20","12:123123:23").isPresent());
    }
    @Test
    public void postArgsTest_shouldNotBePresentIfAnyNotMatched2(){
        Assert.assertFalse(downloadController.getPostArgs("00:00:120","120").isPresent());
    }

    @Test
    public void postArgsTest_shouldReturnParticularString(){
        Optional<String> postArgs = downloadController.getPostArgs("12", String.valueOf(24 * 60));
        Assert.assertTrue(postArgs.isPresent());
        String shouldReturn = "-ss 00:00:12 -t 00:23:48";
        Assert.assertEquals(shouldReturn,postArgs.get());
    }

    @Test
    public void shouldReturnTimedAudio(){
        YTParametersDTO parametersDTO = new YTParametersDTO();
        String mediaType = MediaType.audio.name();

        parametersDTO.setMediaType(mediaType);
        parametersDTO.setTimed(true);
        parametersDTO.setTimeFrom("12");
        parametersDTO.setTimeTo("13");

        YoutubeDLCommander command = downloadController.getCommand(parametersDTO, mediaType, new HashMap<>());
        verify(downloadController).getPostArgs(parametersDTO.getTimeFrom(),parametersDTO.getTimeTo());
        Assert.assertTrue(command.equals(YoutubeDLCommander.getBestAudioTimed()));
    }
    @Test
    public void shouldReturnAudio(){
        YTParametersDTO parametersDTO = new YTParametersDTO();
        String mediaType = MediaType.audio.name();

        parametersDTO.setMediaType(mediaType);

        YoutubeDLCommander command = downloadController.getCommand(parametersDTO, mediaType, new HashMap<>());
        verify(downloadController,times(0)).getPostArgs(parametersDTO.getTimeFrom(),parametersDTO.getTimeTo());
        Assert.assertTrue(command.equals(YoutubeDLCommander.getBestAudio()));
    }
    @Test
    public void shouldReturnAudio2(){
        YTParametersDTO parametersDTO = new YTParametersDTO();
        String mediaType = MediaType.audio.name();

        parametersDTO.setMediaType(mediaType);
        parametersDTO.setTimed(true);
        parametersDTO.setTimeFrom("00:00:12");
        parametersDTO.setTimeTo("00:00:132");

        YoutubeDLCommander command = downloadController.getCommand(parametersDTO, mediaType, new HashMap<>());
        verify(downloadController).getPostArgs(parametersDTO.getTimeFrom(),parametersDTO.getTimeTo());
        Assert.assertTrue(command.equals(YoutubeDLCommander.getBestAudio()));
    }

    @Test
    public void shouldReturnTimedVideo(){
        YTParametersDTO parametersDTO = new YTParametersDTO();
        String mediaType = MediaType.video.name();

        parametersDTO.setMediaType(mediaType);
        parametersDTO.setTimed(true);
        parametersDTO.setTimeFrom("12");
        parametersDTO.setTimeTo("13");

        YoutubeDLCommander command = downloadController.getCommand(parametersDTO, mediaType, new HashMap<>());
        verify(downloadController).getPostArgs(parametersDTO.getTimeFrom(),parametersDTO.getTimeTo());
        Assert.assertTrue(command.equals(YoutubeDLCommander.getBestVideoTimed()));
    }

    @Test
    public void shouldReturnVideo(){
        YTParametersDTO parametersDTO = new YTParametersDTO();
        String mediaType = MediaType.video.name();

        parametersDTO.setMediaType(mediaType);


        YoutubeDLCommander command = downloadController.getCommand(parametersDTO, mediaType, new HashMap<>());
        verify(downloadController, times(0)).getPostArgs(parametersDTO.getTimeFrom(),parametersDTO.getTimeTo());
        Assert.assertTrue(command.equals(YoutubeDLCommander.getBestVideo()));
    }

    @Test
    public void shouldReturnVideo2(){
        YTParametersDTO parametersDTO = new YTParametersDTO();
        String mediaType = MediaType.video.name();

        parametersDTO.setMediaType(mediaType);
        parametersDTO.setTimed(true);
        parametersDTO.setTimeFrom("00:00:12");
        parametersDTO.setTimeTo("00:00:123");

        YoutubeDLCommander command = downloadController.getCommand(parametersDTO, mediaType, new HashMap<>());
        verify(downloadController).getPostArgs(parametersDTO.getTimeFrom(),parametersDTO.getTimeTo());
        Assert.assertTrue(command.equals(YoutubeDLCommander.getBestVideo()));
    }
}