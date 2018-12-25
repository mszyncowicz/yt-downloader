package downloader;

import api.Commander;
import api.Executor;
import api.Locator;

import api.Observable;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import api.Observer;
import org.opensaml.xmlsec.signature.P;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
public class MediaToolExecutor extends Thread implements Executor, Observable {
    public final static boolean SHOW_LOGS = true;

    public static volatile int processId = 0;
    private boolean isDone =false;
    private List<Observer> observers;

    @Getter
    private Object outputFile;
    private boolean isError = false;

    @Getter
    @Setter
    Locator fileLocator = DefaultFileLocator.defaultFileLocator;

    @Getter
    @Setter
    private Commander commander;

    @Getter
    @Setter
    private Map<String, String> args;

    @Getter
    private String errorMessage;


    private MediaToolExecutor(){

    }

    private MediaToolExecutor(Commander commander, Map<String,String> args){
        this.commander = commander;
        this.args = args;
        this.observers = new ArrayList<>();
    }

    public static MediaToolExecutor basicExecutor(){
        MediaToolExecutor mediaToolExecutor = new MediaToolExecutor();
        mediaToolExecutor.observers = new LinkedList<>();
        mediaToolExecutor.setFileLocator(null);
        return mediaToolExecutor;
    }

    public static MediaToolExecutor createExecutor(Commander commander, Map<String,String> args){
        return new MediaToolExecutor(commander,args);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        if (observers.contains(observer)){
            observers.remove(observer);
        }
    }

    @Override
    public List<Observer> getObservers() {
        return observers;
    }

    @Override
    public Object execute(Commander commander, Map<String, String> args) throws WrongParametersException {
        StringBuffer errorMessageBuffer = new StringBuffer();
        outputFile = null;
        try {
            String line;
            ProcessBuilder pb = new ProcessBuilder(commander.getCommand(args).split(" "));
            Process p = null;
            String processName = "P" + getProcessId();
            String uuid = args.get("uuid");
            setDirectory(pb, uuid);

            p = pb.start();
            log.info(processName + ": Starting new process " + commander.getCommand(args));
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));

            BufferedReader bre = new BufferedReader
                    (new InputStreamReader(p.getErrorStream()));

            if (SHOW_LOGS){
                showLogs(bri);
            }
            boolean lvIsError = false;
            while ((line = bre.readLine()) != null) {
                if (line.toUpperCase().contains("ERROR")){
                    log.error(line);
                    lvIsError = true;
                }
                errorMessageBuffer.append(line);
            }
            bre.close();
            if (lvIsError){
                isError = true;
            }

            p.waitFor();
            p.destroy();
            pb = null;

            String output = args.get("output");
            if (!isError && output != null){
                outputFile =  Optional.ofNullable(new File(uuid + "/" + output));
            }
            else if (!isError && fileLocator != null){
                outputFile = fileLocator.locate(bri, uuid);
            }
            else outputFile = null;
            bri.close();

            log.info(processName + ": Process finished successfully");
        }
        catch (Exception err) {
            err.printStackTrace();
            isError = true;
            errorMessageBuffer.append(err.getMessage());
            throw new RuntimeException(err.getMessage());
        }finally {
            isDone =true;
            errorMessage = errorMessageBuffer.toString();
            return outputFile;
        }
    }

    private void showLogs(BufferedReader bri){
        try{
        bri.mark(50000);
        String line;
        while ((line = bri.readLine()) != null) {
            log.info(line);
            updateObservers(line);
            //if (line.toLowerCase().contains("destination") || line.toLowerCase().contains("file")) files.add(line);
        }
            bri.reset();
        } catch (Exception e){

        }
    }

    public void updateObservers(String line){
        for (Observer o : observers){
            o.update(this,line);
        }
    }

    private void setDirectory(ProcessBuilder pb, String directory){
        if (directory != null){
            File fileDir = new File(directory);
            fileDir.mkdirs();
            pb.directory(fileDir);
        }
    }

    private static synchronized int getProcessId() {
        return ++processId;
    }

    public boolean isDone() {
        return isDone;
    }

    public boolean isError() {
        return isError;
    }

    public void run(){
        try {
            execute(commander,args);
        } catch (WrongParametersException e) {
            e.printStackTrace();
        }
    }
}
