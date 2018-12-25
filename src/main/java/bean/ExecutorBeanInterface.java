package bean;

import api.QueueService;

import java.util.List;

public interface ExecutorBeanInterface {

    void setThreadsNumber(int n);

    int getThreadsNumber();

    void start(Runnable runnable);

    boolean removeWhenNotStarted(Runnable runnable);

    void stop();

}
