package service;

import dto.YTParametersDTO;
import model.Record;
import model.Session;

public interface YtDownloaderServiceInterface {
    Record initializeDownload(Session session, YTParametersDTO parametersDTO);
}
