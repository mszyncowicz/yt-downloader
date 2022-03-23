package downloader;


import api.Observable;
import api.Observer;
import data.RecordRepository;
import data.RecordRepositoryImpl;
import lombok.Getter;
import model.Record;
import model.State;

import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogObserver implements Observer {

    @Getter
    private String lastMessage;

    @Getter
    private Float percentage = 0F;

    @Getter
    private String title;

    private static final Pattern percentPattern = Pattern.compile("[0-9]{1,3}[\\.]{1}[0-9]*%");

    @Getter
    private Record record;

    @Inject
    public LogObserver(Record record) {
        this.record = record;
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof String){
            String string = (String) o;
            lastMessage = string;

            if (title == null){
                if (string.toLowerCase().contains("destination")) {
                    int start = string.indexOf(':') + 1;
                    int end = string.lastIndexOf('-');
                    if (end == -1)
                    {
                        end = string.length();
                    }
                    title = string.substring(start,end);
                }
            }

            if (string.contains("%")){
                Matcher matcher = percentPattern.matcher(string);
                if (matcher.find()){
                    String group = matcher.group();
                    group = group.replaceAll("%","");
                    percentage = Float.valueOf(group);

                    if (percentage == 100f){
                        record.setState(State.converting);
                    }
                }
            }
        }
    }

}
