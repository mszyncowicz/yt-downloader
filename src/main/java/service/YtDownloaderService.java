package service;

import bean.DownloadBeanInterface;
import downloader.YoutubeDLCommanderFactoryInterface;
import downloader.YoutubeDlCommanderParameters;
import dto.YTParametersDTO;
import lombok.Setter;
import model.Record;
import model.Session;
import qualifier.DownloadBeanQualifier;

import javax.ejb.Singleton;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class YtDownloaderService implements YtDownloaderServiceInterface {

    @DownloadBeanQualifier
    @Setter
    @Inject
    DownloadBeanInterface downloadBeanInterface;

    @Any
    @Setter
    @Inject
    DownloadRecorderServiceInterface downloadRecorder;

    @Any
    @Setter
    @Inject
    YoutubeDLCommanderFactoryInterface ytCommanderFactoryInterface;

    @Override
    @Transactional
    public Record initializeDownload(Session session, YTParametersDTO parametersDTO) {

        Record record = createRecord(parametersDTO);
        downloadRecorder.addRecordDownload(session,record);
        downloadBeanInterface.prepareDownload(record, parametersDTO.isOverwrite(), Collections.emptyList());
        return record;
    }

    private Record createRecord(YTParametersDTO parametersDTO) {
        String link = parametersDTO.getUrl();
        UUID id = UUID.randomUUID();
        Map<String, String> parameters = createParametersMap(link, id);
        String mediaType = parametersDTO.getMediaType();
        String command = ytCommanderFactoryInterface.getCommand(parametersDTO, parameters).getCommand(parameters);

        Record record = new Record(link,command);
        record.setMediaType(model.MediaType.valueOf(mediaType));
        record.setId(id);
        return record;
    }

    private Map<String, String> createParametersMap(String link, UUID id) {
        Map<String,String> parameters = new HashMap<>();
        parameters.put(YoutubeDlCommanderParameters.uuid.name(),id.toString());
        parameters.put(YoutubeDlCommanderParameters.url.name(),link);
        return parameters;
    }
}
