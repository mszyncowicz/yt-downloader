package printer;

import api.Commander;
import downloader.WrongParametersException;
import downloader.YoutubeDLCommander;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.HashMap;

public class PrinterExecutorIT {

    Commander commander;

    Map<String, String> args;
    @Before
    public void init(){
        commander = new YoutubeDLPrinter();
        args = new HashMap<>();
        args.put(YoutubeDLCommander.URL_PARAM,"https://www.youtube.com/watch?v=YO8c7CjmS_E");
    }


    @Test
    public void test() throws WrongParametersException {
        PrinterExecutor executor = new PrinterExecutor();
        System.out.println(executor.execute(commander,args));
    }
}
