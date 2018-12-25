package bean;

import model.Download;
import model.Record;

import java.io.IOException;


public interface FileMoverBeanInterface {

    boolean moveToConfigLocation(Download downloadedRecord) throws IOException;
}
