package bean;

import api.Executor;
import api.Observable;
import api.Observer;
import api.QueueService;
import downloader.MediaToolExecutor;
import downloader.YoutubeDLCommander;
import downloader.YoutubeDlCommanderParameters;
import lombok.Getter;
import lombok.Setter;
import model.Download;
import model.Record;
import org.slf4j.Logger;
import service.QueueServiceImpl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import javax.ws.rs.NotSupportedException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static javax.ejb.LockType.READ;

@Stateless
public class ExecutorBean implements ExecutorBeanInterface{

    @Inject
    private Logger logger;

    private int avaibleThreaeds;

   // private ThreadPoolExecutor executorService;s

    @PostConstruct
    public void initQueue(){
        logger.info("Started ExecutorBean ");
       // avaibleThreaeds = 1;
        //executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(avaibleThreaeds);
    }

    @Override
    public void setThreadsNumber(int n) {
        this.avaibleThreaeds = n;
    }

    @Override
    public int getThreadsNumber() {
        return avaibleThreaeds;
    }

    @Override
    @Asynchronous
    public void start(Runnable runnable) {
        logger.info("starting");
       runnable.run();
    }

    @Override
    public boolean removeWhenNotStarted(Runnable runnable){
       return true;//executorService.getQueue().remove(runnable);
    }

    @Override
    public void stop() {
        //executorService.shutdown();
    }



}
