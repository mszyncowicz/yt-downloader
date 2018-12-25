package downloader;

import api.FileLocator;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.text.html.Option;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.fail;

@Slf4j
public class YoutubeDLCustomizationIT {
    File file = null;

    @Test(expected = WrongParametersException.class)
    public void exTest( ) throws WrongParametersException {
        YoutubeDLCommander customization = YoutubeDLCommander.getBestAudio();
        Map<String,String > args = new HashMap<>();
        System.out.println(customization.getCommand(args));
    }

    @Test
    public void basicTest() throws WrongParametersException {
        YoutubeDLCommander customization = YoutubeDLCommander.getBestAudio();
        Map<String,String > args = new HashMap<>();
        args.put("url","https://www.youtube.com/watch?v=YO8c7CjmS_E");
        String value = UUID.randomUUID().toString();
        args.put("uuid", value);
        String command = customization.getCommand(args);

        Assert.assertTrue(command.contains(args.get("url")));
        Assert.assertTrue(command.split(" ").length >= 5);

        MediaToolExecutor executor = MediaToolExecutor.createExecutor(customization,args);
        Object object = executor.execute(customization,args);

        Assert.assertTrue(executor.isDone());
        if (executor.isError()){
            file = new File(value);
            file = file.listFiles()[0];
        }
        Assert.assertFalse(executor.isError());
        Assert.assertNotNull(object);
        log.info(object.toString());
        Assert.assertTrue(object instanceof Optional);

        Optional<File> optional = (Optional<File>) object;
        if (optional.isPresent()){
            file = optional.get();
        }else{
            file = new File(value);
            fail();
        }
        Assert.assertTrue(file.exists());
        log.info(file.getParent());


    }

    @After
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
            Assert.assertFalse(parent.exists());
        }
    }
}