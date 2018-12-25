package service;

import data.RecordRepository;
import lombok.Getter;
import lombok.Setter;
import model.Record;
import qualifier.RecordServiceQualifier;

import javax.ejb.TransactionAttribute;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;
import java.util.List;

@Model
@RecordServiceQualifier
public class RecordServiceImpl implements RecordService{


    @Getter
    @Setter
    @Inject
    RecordRepository recordRepository;

    @Override
    public List<Record> getNotDownloaded() {

        return recordRepository.getNotDownloaded();
    }

    @Override
    @TransactionAttribute
    public Record save(Record record) {
        return recordRepository.save(record);
    }

    @Override
    public List<Record> getNotDownloaded(int limit) {
        return Collections.emptyList();
    }
}
