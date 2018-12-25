package service;

import model.Record;

import java.util.List;

public interface RecordService {
    List<Record> getNotDownloaded();

    List<Record> getNotDownloaded(int limit);

    Record save(Record record);
}
