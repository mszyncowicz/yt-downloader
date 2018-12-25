package model;


import api.Observable;
import api.Observer;
import downloader.LogObserver;
import downloader.MediaToolExecutor;
import downloader.YoutubeDlCommanderParameters;
import lombok.Getter;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Download implements Runnable, Observable {

    @Getter
    Record record;

    List<Observer> observers;

    @Getter
    MediaToolExecutor executor;

    public Download(Record record){
        this.record = record;
        observers = new LinkedList<>();

    }

    @Override
    public void run() {
        Map<String,String> args = new HashMap<>();
        args.put(YoutubeDlCommanderParameters.uuid.name(),record.getId().toString());
        args.put(YoutubeDlCommanderParameters.url.name(), record.getLink());

        executor = MediaToolExecutor.createExecutor(c-> record.getCommand(),args);
        for (Observer o : observers){
            executor.addObserver(o);
        }
        executor.run();
        if (!executor.isError()){
            record.setState(State.finalizing);
        } else {
            record.setState(State.failed);
        }
        for (Observer observer : observers){
            observer.update(this, executor);
        }
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
}