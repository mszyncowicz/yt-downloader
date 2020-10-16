package controller;


import downloader.LogObserver;

import dto.LogObserverDTO;
import dto.LogObserversDTO;
import dto.YTParametersDTO;
import lombok.extern.slf4j.Slf4j;
import model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import service.*;
import javax.transaction.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@Slf4j
public class DownloadControllerTest {

    @Mock
    private DownloadInfoServiceInterface downloadInfoService;

    @Mock
    private YtDownloaderServiceInterface ytDownloaderServiceInterface;

    @Mock
    private SessionService sessionServiceMock;

    @InjectMocks
    @Spy
    private DownloadController downloadController;

    private  Set<Object> saved;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        saved = new HashSet<>();
        when(ytDownloaderServiceInterface.initializeDownload(any(),any())).then(a -> {
            Record rec = new Record();
            saved.add(rec);
            return rec;
        });

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
            error.printStackTrace();
        }
        Assert.assertTrue(saved.size() > 0);
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
        when(downloadInfoService.getStartedDownloads()).thenReturn(downloads);

        doAnswer(a -> {
            Object argumentAt = a.getArgumentAt(0, Object.class);
            boolean result = false;
            if (argumentAt instanceof LogObserversDTO){
                LogObserversDTO logOberversDto = (LogObserversDTO) argumentAt;
                Assert.assertEquals(1,logOberversDto.getLogObserversDTOList().size());
                LogObserverDTO logObserverDTO = logOberversDto.getLogObserversDTOList().get(0);
                Assert.assertEquals(last_message,logObserverDTO.getLastMessage());
                Assert.assertEquals(record.getId().toString(),(logObserverDTO.getRecordUUID()));
                Assert.assertEquals(record.getState().name(),logObserverDTO.getState());
                Assert.assertEquals(0,Float.compare(percentage,logObserverDTO.getPercentage()));
                Assert.assertFalse(logOberversDto.hasAnyErrors);
                result = true;
            }
            atomicBoolean.set(result);
            return null;
        }).when(downloadController).returnEntity(anyObject());

        downloadController.getCurrentDownloadInfo(token);

        verify(downloadInfoService,times(1)).getStartedDownloads();
        verify(observer,times(1)).getLastMessage();
        verify(observer,times(1)).getPercentage();
        verify(observer,times(1)).getRecord();
        verify(downloadController,times(1)).sessionExist(anyString());
        verify(downloadInfoService).hasErrorForSession(token);

        Assert.assertTrue(atomicBoolean.get());

    }

    @Test
    public void getSingleLogObserver(){

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
        when(downloadInfoService.getStartedDownloads()).thenReturn(downloads);

        doAnswer(a -> {
            Object argumentAt = a.getArgumentAt(0, Object.class);
            boolean result = false;
            if (argumentAt instanceof LogObserver){
                result = true;
            }
            atomicBoolean.set(result);
            return null;
        }).when(downloadController).returnEntity(anyObject());

        atomicBoolean.set(false);
        downloadController.getLogObserver(token, id.toString());
        Assert.assertTrue(atomicBoolean.get());

    }


}