package downloader;

import api.FileLocator;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
public class DefaultFileLocator implements FileLocator {
    public static DefaultFileLocator defaultFileLocator = new DefaultFileLocator();

    public DefaultFileLocator(){
    }

    @Override
    public Optional<File> locate(Reader reader, String directory) {
        String line;
        List<String> files = new ArrayList<>();
        BufferedReader bri = (BufferedReader) reader;
        File result = null;
        try {
            log.info("Begining localization proccess");
            while ((line = bri.readLine()) != null) {
                if (line.toLowerCase().contains("destination") || line.toLowerCase().contains("file")){
                    files.add(line);
                    log.info("added " + line);
                }
            }
            result = getFile(files,directory);
        }catch (IOException ex){
            ex.printStackTrace();
        }finally {

            Optional<File> result1 = Optional.ofNullable(result);
            log.info("Found? " + result1.isPresent());
            return result1;
        }

    }

    private File getFile(List<String> names, String directory){
        File result = null;
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                for (String name : names){

                    String nameReplaceAll = name.replaceAll("[^\\w\\s]", "");
                    String fileNameReplaceAll = file.getName().replaceAll("[^\\w\\s]", "");
                    log.info(nameReplaceAll + " vs " + fileNameReplaceAll + " = "+ nameReplaceAll.contains(fileNameReplaceAll));
                    if (nameReplaceAll.contains(fileNameReplaceAll)){
                        result = file;
                        break;
                    }
                }
            }

        }
        return result;
    }
}
