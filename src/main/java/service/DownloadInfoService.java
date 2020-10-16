package service;

import api.Observer;
import bean.DownloadBeanInterface;
import bean.ErrorQueueBeanInterface;
import lombok.Setter;
import model.Download;
import model.ErrorMessage;
import model.Record;
import qualifier.DownloadBeanQualifier;
import qualifier.DownloadInfoServiceQualifier;
import qualifier.ErrorQueueBeanQualifier;

import javax.ejb.Stateful;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Stateful
@DownloadInfoServiceQualifier
public class DownloadInfoService implements DownloadInfoServiceInterface {

    @DownloadBeanQualifier
    @Setter
    @Inject
    DownloadBeanInterface downloadBeanInterface;

    @ErrorQueueBeanQualifier
    @Setter
    @Inject
    ErrorQueueBeanInterface errorQueueBeanInterface;

    public List<Download> getStartedDownloads() {
        return downloadBeanInterface.getStartedDownloads();
    }

    public boolean hasErrorForSession(String token) {
        return errorQueueBeanInterface.hasErrorForSession(token);
    }

    @Override
    public Download prepareDownload(Record record, boolean overwrite, List<Observer> observers) {
        return downloadBeanInterface.prepareDownload(record,overwrite,observers);
    }

    @Override
    public boolean cancel(Record record) {
        return downloadBeanInterface.cancel(record);
    }

    @Override
    public boolean isFull() {
        return downloadBeanInterface.isFull();
    }

    @Override
    public ErrorMessage dequeForSession(String sessionToken) {
        return errorQueueBeanInterface.dequeForSession(sessionToken);
    }

    @Override
    public void enqueue(ErrorMessage errorMessage) {
        errorQueueBeanInterface.enqueue(errorMessage);
    }
}