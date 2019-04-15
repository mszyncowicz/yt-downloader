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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.io.FileUtils.copyFile;

@Stateless
public class FileMoverBean implements FileMoverBeanInterface {

    public static final Pattern EXTENSION_PATTERN = Pattern.compile("\\..{3}$");

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
        if (downloadedRecord == null || !downloadedRecord.getState().equals(State.finalizing)) {
            return false;
        }

        Optional<File> outputFile = (Optional<File>) download.getExecutor().getOutputFile();
        if (!outputFile.isPresent()) {
            throw new IllegalArgumentException("File does not exist");
        }

        File dir;

        if (downloadedRecord.getMediaType().equals(MediaType.audio)) {
            dir = configurationService.getAudioFolder();
        } else {
            dir = configurationService.getVideoFolder();

        }
        logger.info("Copying to folder " + dir.getAbsolutePath());
        if (!dir.exists()) {
            dir.mkdirs();
        }

        copyFileToDirectory(outputFile.get(), dir, download.isOverwrite());
        downloadedRecord.setState(State.finished);
        recordService.save(downloadedRecord);
        return true;
    }

    public static void copyFileToDirectory(File srcFile, File destDir, boolean overwrite) throws IOException {
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (destDir.exists() && !destDir.isDirectory()) {
            throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
        } else {
            String fileName = srcFile.getName();
            if (!overwrite) {
                fileName = getNewFileName(destDir, fileName);
            }
            File destFile = new File(destDir, fileName);
            copyFile(srcFile, destFile, true);
        }
    }

    public static String getNewFileName(File destDir, String fileName) {
        String resultName = fileName;
        int i = 0;
        while (new File(destDir, resultName).exists()){
            Matcher matcher = EXTENSION_PATTERN.matcher(fileName);
            if (matcher.find()) {
                String ext = matcher.group();
                String name = fileName.replaceFirst(EXTENSION_PATTERN.pattern(),"");
                name += " (" + (++i) + ")" + ext;
                resultName = name;
            }else {
                resultName += " (" + (++i) + ")";
            }
        }
        return resultName;
    }
}
