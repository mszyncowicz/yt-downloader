package data;

import model.Record;

import java.util.List;

public interface RecordRepository extends Repository<Record>{
    List<Record> getNotDownloaded();

    List<Record> getNotDownloaded(int limit);

}
