package bean;

import api.Observable;
import api.Observer;
import downloader.MediaToolExecutor;
import model.*;

import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;
import service.RecordService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DownloadBeanTest {

    @Spy
    @InjectMocks
    DownloadBean downloadBean;

    @Mock
    ExecutorBeanInterface executorBeanInterface;

    @Mock
    RecordService recordService;

    @Before
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
        Assert.assertTrue(downloadBean.isFull());
    }

    @Test
    public void testDownloadPrepared(){
        List<Download> downloads = new ArrayList<>();
        downloadBean.setDownloadList(downloads);

        TestObserver mock = mock(TestObserver.class);
        Download download = downloadBean.prepareDownload(new Record(), true, Arrays.asList(mock));

        Assert.assertTrue(downloads.contains(download));
        Assert.assertTrue(download.getObservers().contains(mock));

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

        Assert.assertTrue(downloadObserver.isStarted());
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

        Assert.assertFalse(downloads.contains(downloadToRemove));

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
        Assert.assertFalse(downloads.contains(downloadToRemove));
    }

    @Test
    public void cancel(){
        List<Runnable> addedToQueue = new LinkedList<>();
        doAnswer(a->{
            Runnable argumentAt = a.getArgumentAt(0, Runnable.class);
            addedToQueue.add(argumentAt);
            return null;
        }).when(executorBeanInterface).start(anyObject());

        doAnswer(a->{
            Runnable argumentAt = a.getArgumentAt(0, Runnable.class);
            return addedToQueue.remove(argumentAt);
        }).when(executorBeanInterface).removeWhenNotStarted(anyObject());

        Record record = new Record();
        record.setId(UUID.randomUUID());
        Download download = new Download( record, true);
        List<Download> downloads = new ArrayList<>();
        downloads.add(download);

        downloadBean.setDownloadList(downloads);
        executorBeanInterface.start(download);
        Assert.assertTrue(addedToQueue.contains(download));
        downloadBean.cancel(record);

        Assert.assertFalse(downloads.contains(download));
        Assert.assertFalse(addedToQueue.contains(download));

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

        verify(downloadBean,times(0)).prepareDownload(anyObject(),anyBoolean(),anyObject());
        verify(downloadBean,times(recordService.getNotDownloaded().size())).removeUnfinished(anyObject());

        verify(executorBeanInterface,times(0)).start(any());

        Assert.assertTrue(downloadList.isEmpty());
    }

    @Test
    public void getStartedDownloadsTest(){
        List<Download> downloads = new ArrayList<>();
        IntStream.range(0,45)
                .peek(p -> downloads.add(generateDownloadWichObserverIs(true)))
                .count();
        List<Download> started = new ArrayList<>();
        started.addAll(downloads);
        IntStream.range(0,23)
                .peek(p -> downloads.add(generateDownloadWichObserverIs(false)))
                .count();

        downloadBean.setDownloadList(downloads);
        List<Download> startedDownloads = downloadBean.getStartedDownloads();

        Assert.assertTrue(started != null);
        Assert.assertEquals(45, startedDownloads.size());

        for (Download d : started){
            Assert.assertTrue(startedDownloads.contains(d));
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
