package downloader;

import api.*;

import api.Observable;
import api.Observer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;

import org.opensaml.xmlsec.signature.P;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class MediaToolExecutor extends Thread implements Executor, Observable {
    public final static boolean SHOW_LOGS = true;

    public static volatile int processId = 0;
    private boolean isDone =false;
    private List<Observer> observers;

    @Getter
    private Optional<File> outputFile;
    private boolean isError = false;

    @Getter
    @Setter
    FileLocator fileLocator = DefaultFileLocator.defaultFileLocator;

    @Getter
    @Setter
    private Commander commander;

    @Getter
    @Setter
    private Map<String, String> args;

    @Getter
    private String errorMessage;

    private StringBuffer errorMessageBuffer;

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
        observers.remove(observer);
    }

    @Override
    public List<Observer> getObservers() {
        return observers;
    }

    @Override
    public Object execute(Commander commander, Map<String, String> args) throws WrongParametersException {
        errorMessageBuffer = new StringBuffer();
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
            BufferedReader bri = p.inputReader();

            BufferedReader bre = p.errorReader();

            if (SHOW_LOGS){
                showLogs(bri);
            }


            p.waitFor();
            p.destroy();
            boolean lvIsError = false;
            lvIsError = isLvIsError(bre, lvIsError);
            bre.close();
            if (lvIsError){
                isError = true;
            }
            String output = args.get("output");
            if (!isError && output != null){
                outputFile =  Optional.ofNullable(new File(uuid + "/" + output));
            }
            else if (!isError && fileLocator != null){
                outputFile = fileLocator.locate(bri, uuid);
            }
            else outputFile = null;
            bri.close();

            if (outputFile.isPresent() && outputFile.get().getName().toLowerCase().endsWith("webm"))
            {
                convertToMp4(outputFile.get(),uuid);
            }

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

    private boolean isLvIsError(BufferedReader bre, boolean isError) throws IOException {
        String line;
        while ((line = bre.readLine()) != null) {
            if (line.toUpperCase().contains("ERROR")) {
                log.error(line);
                isError = true;
            }
            errorMessageBuffer.append(line);
        }
        return isError;
    }

    private void convertToMp4(File file, String uuid) throws Exception {
        String mp4Name = file.getName().replace(".webm",".mp4");
        Map<String,String> localArgs = new HashMap<>();
        localArgs.put(FFMPEGCommander.INPUT_PARAM, file.getName());
        localArgs.put(FFMPEGCommander.OUTPUT_PARAM, mp4Name);
        FFMPEGCommander ffmpegCommander = new FFMPEGCommander();
        ProcessBuilder pb = new ProcessBuilder(ffmpegCommander.getCommand(localArgs).split(FFMPEGCommander.SPLIT));
        Process p = null;
        setDirectory(pb, uuid);
        p = pb.start();
        BufferedReader bri = new BufferedReader
                (new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));

        BufferedReader bre = new BufferedReader
                (new InputStreamReader(p.getErrorStream()));

        if (SHOW_LOGS){
            new Thread(() ->{
                showLogs(bri);
            }).start();
            new Thread(() ->{
                showLogs(bre);
            }).start();
        }

        p.waitFor();
        p.destroy();
        boolean lvIsError = false;
        String line = null;
        lvIsError = isLvIsError(bre, lvIsError);
        bre.close();
        bri.close();
        if (lvIsError){
            isError = true;
        }
        if (!isError) {
            outputFile = Optional.of(new File(uuid + "/" + mp4Name));
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
            e.printStackTrace();
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
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
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
