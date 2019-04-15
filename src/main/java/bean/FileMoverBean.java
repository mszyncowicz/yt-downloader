package bean;

import model.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import service.ConfigurationService;
import service.RecordService;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Stateless
public class FileMoverBean implements FileMoverBeanInterface {

    @Any
    @Inject
    ConfigurationService configurationService;

    @Any
    @Inject
    RecordService recordService;

    @Inject
    Logger logger;

    @Override
    @TransactionAttribute
    public boolean moveToConfigLocation(Download download) throws IOException {
        Record downloadedRecord = download.getRecord();
        if (downloadedRecord == null || !downloadedRecord.getState().equals(State.finalizing)){
            return false;
        }

        Optional<File> outputFile = (Optional<File>) download.getExecutor().getOutputFile();
        if (!outputFile.isPresent()){
            throw new IllegalArgumentException("File does not exist");
        }

        File dir;

        if (downloadedRecord.getMediaType().equals(MediaType.audio)){
            dir = configurationService.getAudioFolder();
        } else {
            dir = configurationService.getVideoFolder();

        }
        logger.info("Copying to folder " + dir.getAbsolutePath());
        if (!dir.exists()){
            dir.mkdirs();
        }

        FileUtils.copyFileToDirectory(outputFile.get(),dir);
        downloadedRecord.setState(State.finished);
        recordService.save(downloadedRecord);
        return true;
    }
}
