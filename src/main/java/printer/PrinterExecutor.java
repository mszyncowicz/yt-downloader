package printer;

import api.Commander;
import api.Executor;
import downloader.WrongParametersException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@Slf4j
public class PrinterExecutor implements Executor {

    @Getter
    private boolean isError = false;

    @Override
    public Object execute(Commander commander, Map<String, String> args) throws WrongParametersException {
        String result = null;

        ProcessBuilder builder = new ProcessBuilder(commander.getCommand(args).split(" "));
        try {
            Process p = builder.start();
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));

            BufferedReader bre = new BufferedReader
                    (new InputStreamReader(p.getErrorStream()));

            String line;
            StringBuilder resultBuilder = new StringBuilder();
            while ((line = bri.readLine()) != null){
                resultBuilder.append(line);
            }
            while ((line = bre.readLine()) != null){
                isError = true;
                log.error(line);
            }
            p.waitFor();
            p.destroy();
            return resultBuilder.toString();
        } catch (IOException e) {
            isError = true;
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            isError = true;

        }

        return result;

    }
}
