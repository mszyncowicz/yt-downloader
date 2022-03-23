package bean;

import api.Observable;
import api.Observer;
import downloader.MediaToolExecutor;
import model.*;

import java.util.UUID;

import model.Record;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.LoggerFactory;
import service.RecordService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class DownloadBeanTest {

    @Spy
    @InjectMocks
    DownloadBean downloadBean;

    @Mock
    ExecutorBeanInterface executorBeanInterface;

    @Mock
    RecordService recordService;

    @BeforeEach
    public void init(){
        downloadBean.setExecutorBean(executorBeanInterface);
        downloadBean.setRecordService(recordService);
        downloadBean.setLogger(LoggerFactory.getLogger(DownloadBeanTest.class));
        downloadBean.setErrorQueeBeanInterface(mock(ErrorQueueBeanInterface.class));
        List<Record> notDownloaded = new LinkedList<>();
        notDownloaded.add(new Record());
        notDownloaded.add(new Record());
        notDownloaded.add(new Record());
        when(recordService.getNotDownloaded()).thenReturn(notDownloaded);

    }

    @Test
    public void testIsFull(){
        List mockList = mock(List.class);
        when(mockList.size()).thenReturn(20000);
        downloadBean.setDownloadList(mockList);
        Assertions.assertTrue(downloadBean.isFull());
    }

    @Test
    public void testDownloadPrepared(){
        List<Download> downloads = new ArrayList<>();
        downloadBean.setDownloadList(downloads);

        TestObserver mock = mock(TestObserver.class);
        Download download = downloadBean.prepareDownload(new Record(), true, Arrays.asList(mock));

        Assertions.assertTrue(downloads.contains(download));
        Assertions.assertTrue(download.getObservers().contains(mock));

        verify(executorBeanInterface,times(1)).start(download);

    }

    @Test
    public void testDownloadObserver(){
        List<Download> downloads = new ArrayList<>();
        downloadBean.setDownloadList(downloads);

        DownloadBean.DownloadObserver downloadObserver = downloadBean.new DownloadObserver();

        MediaToolExecutor mediaToolExecutor = MediaToolExecutor.basicExecutor();
        mediaToolExecutor.addObserver(downloadObserver);
        mediaToolExecutor.updateObservers("line");

        Assertions.assertTrue(downloadObserver.isStarted());
    }

    @Test
    public void removeFinishedDownloadTest(){
        List<Download> downloads = new ArrayList<>();
        Record record = new Record();
        record.setState(State.finalizing);
        Download downloadToRemove = new Download( record, true);
        downloads.add(downloadToRemove);
        downloadBean.setDownloadList(downloads);
        downloadBean.removeFinished(downloadToRemove);

        Assertions.assertFalse(downloads.contains(downloadToRemove));

    }

    @Test
    public void removeFinishedDownload_failed(){
        List<Download> downloads = new ArrayList<>();
        Record record = new Record();
        record.setState(State.finalizing);
        Session session = new Session();
        session.setToken("token");
        record.setSession(session);
        Download downloadToRemove = mock(Download.class, Mockito.CALLS_REAL_METHODS);
        MediaToolExecutor mockedExecutor = mock(MediaToolExecutor.class);
        when(mockedExecutor.isError()).thenReturn(true);
        when(downloadToRemove.getRecord()).thenReturn(record);
        when(downloadToRemove.getExecutor()).thenReturn(mockedExecutor);
        when(mockedExecutor.getErrorMessage()).thenReturn("error");
        downloads.add(downloadToRemove);
        downloadBean.setDownloadList(downloads);
        downloadBean.removeFinished(downloadToRemove);

        verify(downloadBean.errorQueeBeanInterface).enqueue(new ErrorMessage("error",record.getLink(),record.getSession().getToken()));
        Assertions.assertFalse(downloads.contains(downloadToRemove));
    }

    @Test
    public void cancel(){
        List<Runnable> addedToQueue = new LinkedList<>();
        doAnswer(a->{
            Runnable argumentAt = a.getArgument(0, Runnable.class);
            addedToQueue.add(argumentAt);
            return null;
        }).when(executorBeanInterface).start(any());

        doAnswer(a->{
            Runnable argumentAt = a.getArgument(0, Runnable.class);
            return addedToQueue.remove(argumentAt);
        }).when(executorBeanInterface).removeWhenNotStarted(any());

        Record record = new Record();
        record.setId(UUID.randomUUID());
        Download download = new Download( record, true);
        List<Download> downloads = new ArrayList<>();
        downloads.add(download);

        downloadBean.setDownloadList(downloads);
        executorBeanInterface.start(download);
        Assertions.assertTrue(addedToQueue.contains(download));
        downloadBean.cancel(record);

        Assertions.assertFalse(downloads.contains(download));
        Assertions.assertFalse(addedToQueue.contains(download));

    }

    @Test
    public void testScanRecordsOn(){
        downloadBean.setDownloadList(new ArrayList<>());

        downloadBean.init();
        verify(downloadBean,times(1)).scanForNotDownloaded();
    }

    @Test
    public void shouldScanForRecordsAndNotStartDownloading(){
        ArrayList<Download> downloadList = new ArrayList<>();
        downloadBean.setDownloadList(downloadList);

        downloadBean.scanForNotDownloaded();

        verify(downloadBean,times(0)).prepareDownload(any(),anyBoolean(),any());
        verify(downloadBean,times(recordService.getNotDownloaded().size())).removeUnfinished(any());

        verify(executorBeanInterface,times(0)).start(any());

        Assertions.assertTrue(downloadList.isEmpty());
    }

    @Test
    public void getStartedDownloadsTest(){
        List<Download> downloads = new ArrayList<>();
        IntStream.range(0,45)
                .forEach(p -> downloads.add(generateDownloadWichObserverIs(true)));

        List<Download> started = new ArrayList<>();
        started.addAll(downloads);
        IntStream.range(0,23)
                .forEach(p -> downloads.add(generateDownloadWichObserverIs(false)));


        downloadBean.setDownloadList(downloads);
        List<Download> startedDownloads = downloadBean.getStartedDownloads();

        Assertions.assertTrue(started != null);
        Assertions.assertEquals(45, startedDownloads.size());

        for (Download d : started){
            Assertions.assertTrue(startedDownloads.contains(d));
        }

    }

    private Download generateDownloadWichObserverIs(boolean started){
        Download download = new Download( null,false);
        DownloadBean.DownloadObserver downloadObserver = downloadBean.new DownloadObserver();
        downloadObserver.setStarted(started);
        download.addObserver((a,b) -> {
            return;
        });
        download.addObserver(downloadObserver);
        return download;
    }
    class TestObserver implements Observer {
        @Override
        public void update(Observable observable, Object o) {

        }
    }
}
