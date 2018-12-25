package controller;

import bean.DownloadBeanInterface;
import downloader.LogObserver;
import dto.LogObserverDTO;
import dto.LogObserversDTO;
import dto.YTParametersDTO;
import lombok.extern.slf4j.Slf4j;
import model.Download;
import model.Session;
import model.State;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import service.RecordService;

import javax.enterprise.concurrent.ManagedExecutorService;
import javax.transaction.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import model.Record;
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

        verify(downloadBeanMock, times(1)).prepareDownload(anyObject(),eq(Collections.emptyList()));

    }

    @Test
    public void getStartedLogObservers(){

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        List<Download> downloads = new ArrayList<>();
        Record record = new Record();
        UUID id = UUID.randomUUID();
        record.setId(id);
        record.setState(State.downloading);

        Download download = new Download(record);
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

        downloadController.getCurrentDownloadInfo(Session.generateRandom(Session.scale));

        verify(downloadBeanMock,times(1)).getStartedDownloads();
        verify(observer,times(1)).getLastMessage();
        verify(observer,times(1)).getPercentage();
        verify(observer,times(1)).getRecord();
        verify(downloadController,times(1)).sessionExist(anyString());

        Assert.assertTrue(atomicBoolean.get());
        atomicBoolean.set(false);
        downloadController.getLogObserver(Session.generateRandom(Session.scale), id.toString());
        Assert.assertTrue(atomicBoolean.get());

    }
}