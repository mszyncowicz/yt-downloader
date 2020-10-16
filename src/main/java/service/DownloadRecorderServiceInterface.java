package service;

import dto.YTParametersDTO;
import model.Record;
import model.Session;

import javax.transaction.*;

public interface DownloadRecorderServiceInterface {
    Record addRecordDownload(Session session, Record record);
}
