package bean;

import api.Observer;
import model.Download;
import model.Record;

import java.util.List;

public interface DownloadBeanInterface {

    Download prepareDownload(Record record, boolean overwrite, List<Observer> observers);

    boolean cancel(Record record);

    boolean isFull();

    List<Download> getStartedDownloads();
}
