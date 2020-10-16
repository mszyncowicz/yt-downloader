package service;

import lombok.Setter;
import model.Record;
import model.Session;

import javax.ejb.Singleton;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.transaction.*;

@Singleton
public class DownloadRecorderService implements DownloadRecorderServiceInterface {
    @Any
    @Setter
    @Inject
    RecordService recordService;

    @Any
    @Setter
    @Inject
    SessionService sessionService;

    @Override
    @Transactional(Transactional.TxType.MANDATORY)
    public Record addRecordDownload(Session session, Record record) {
        record.setSession(session);
        session.getRecords().add(record);
        recordService.save(record);
        sessionService.save(session);
        return record;
    }


}
