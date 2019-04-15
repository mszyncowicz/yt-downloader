package bean;

import api.Observable;
import api.Observer;
import ch.qos.logback.core.util.FileUtil;
import downloader.LogObserver;
import lombok.Getter;
import lombok.Setter;
import model.Download;
import model.Record;
import model.State;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import service.RecordService;


import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.transaction.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.ejb.ConcurrencyManagementType.CONTAINER;
import static javax.ejb.LockType.READ;

@DependsOn("ExecutorBean")
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Named("DownloadBean")
public class DownloadBean implements DownloadBeanInterface {

    @Setter
    @Inject
    ExecutorBeanInterface executorBean;

    @Any
    @Setter
    @Inject
    RecordService recordService;

    @Inject
    @Setter
    FileMoverBeanInterface fileMoverBeanInterface;

    @Getter
    @Setter
    @Inject
    Logger logger;

    @Getter
    @Setter
    private List<Download> downloadList;

    @PostConstruct
    public void init(){
        logger.info("Initializing DownloadBean");
        downloadList = new ArrayList<>(executorBean.getThreadsNumber());
        downloadList = new ArrayList<>(executorBean.getThreadsNumber());
        scanForNotDownloaded();
    }

    void scanForNotDownloaded() {
        List<Record> notDownloaded = recordService.getNotDownloaded();
        for (Record record : notDownloaded){
           removeUnfinished(record);
        }
    }

    void removeUnfinished(Record record){
        String pathname = record.getId().toString();
        logger.info("Removing " + pathname);
        File file = new File(pathname);
        if (file.exists()){
            File[] files = file.listFiles();
            for (File f : files){
                f.delete();
            }
            file.delete();
        }
        record.setState(State.failed);
        recordService.save(record);
    }

    @Override
    @Lock(READ)
    public Download prepareDownload(Record record, boolean overwrite, List<Observer> observers) {
        Download prepare = new Download(record,overwrite);
        prepare.addObserver(new DownloadObserver());
        prepare.addObserver(new LogObserver(record));
        for (Observer o : observers){
            prepare.addObserver(o);
        }
        downloadList.add(prepare);
        executorBean.start(prepare);
        logger.info("started");
        return prepare;
    }

    @Override
    public boolean cancel(Record record) {
        Optional<Download> any = downloadList.stream()
                .filter(f -> f.getRecord().getId().equals(record.getId()))
                .findAny();

        if (any.isPresent()){
            Download runnable = any.get();
            if (executorBean.removeWhenNotStarted(runnable)){
                downloadList.remove(runnable);
                return true;
            }
        }

        return false;
    }

    void removeFinished(){
        List<Download> collect = downloadList.stream()
                .filter(d -> d.getRecord().getState().equals(State.finalizing))
                .collect(Collectors.toList());
        downloadList.removeAll(collect);
    }

    void removeFinished(Download download){
        downloadList.remove(download);

        if (download.getExecutor() != null && download.getExecutor().getOutputFile() != null){
            Optional<File> outputFile = (Optional<File> )download.getExecutor().getOutputFile();

            if (outputFile.isPresent()){
                String parent = outputFile.get().getParent();
                try {
                    FileUtils.deleteDirectory(new File(parent));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

    }

    @Override
    public boolean isFull(){
        return downloadList.size() >= 20000;
    }

    @Override
    public List<Download> getStartedDownloads(){
        return downloadList.stream()
                .filter(d->d.getObservers().stream()
                        .filter(o -> o instanceof DownloadObserver)
                        .filter(o -> ((DownloadObserver) o).isStarted)
                        .findAny().isPresent()
                ).collect(Collectors.toList());
    }

    class DownloadObserver implements Observer{
        @Getter
        @Setter
        private boolean isStarted = false;

        @Override
        public void update(Observable observable, Object o) {

            if (!isStarted) isStarted = true;
            if (observable instanceof Download){
                Download download = (Download) observable;

                recordService.save(download.getRecord());

                try {
                    fileMoverBeanInterface.moveToConfigLocation(download);
                } catch (IOException e) {
                    e.printStackTrace();
                    download.getRecord().setState(State.failed);
                    recordService.save(download.getRecord());
                }
                removeFinished(download);
            }
        }
    }
}
