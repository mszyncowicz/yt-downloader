package downloader;

import api.FileLocator;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.Option;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class YoutubeDLCustomizationIT {
    File file = null;

    @Test
    public void exTest( ) throws WrongParametersException {
        Assertions.assertThrows(WrongParametersException.class, () -> {
            YoutubeDLCommander customization = YoutubeDLCommander.getBestAudio();
            Map<String, String> args = new HashMap<>();
            System.out.println(customization.getCommand(args));
        });
    }

    @Test
    public void basicTest() throws WrongParametersException {
        YoutubeDLCommander customization = YoutubeDLCommander.getBestAudioVideo();
        Map<String,String > args = new HashMap<>();
        args.put("url","https://www.youtube.com/watch?v=jn3Eu_Fvw6E");
        String value = UUID.randomUUID().toString();
        args.put("uuid", value);
        String command = customization.getCommand(args);

        Assertions.assertTrue(command.contains(args.get("url")));
        Assertions.assertTrue(command.split(" ").length >= 2);

        MediaToolExecutor executor = MediaToolExecutor.createExecutor(customization,args);
        Object object = executor.execute(customization,args);

        Assertions.assertTrue(executor.isDone());
        if (executor.isError()){
            file = new File(value);
            file = file.listFiles()[0];
        }
        Assertions.assertFalse(executor.isError());
        Assertions.assertNotNull(object);
        log.info(object.toString());
        Assertions.assertTrue(object instanceof Optional);

        Optional<File> optional = (Optional<File>) object;
        if (optional.isPresent()){
            file = optional.get();
        }else{
            file = new File(value);
            fail();
        }
        Assertions.assertTrue(file.exists());
        log.info(file.getParent());


    }

    @AfterEach
    public void after(){
        if (file != null){
            File parent = new File(file.getParent());
            log.info(parent.getAbsolutePath());
            File[] files = parent.listFiles();
            for (File file1 : files){
                log.info(file1.getName());
                if (file1.exists()) file1.delete();
            }
            parent.delete();
            Assertions.assertFalse(parent.exists());
        }
    }
}