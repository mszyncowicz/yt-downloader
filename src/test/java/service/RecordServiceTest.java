package service;

import data.RecordRepository;
import model.State;
import org.hibernate.validator.cfg.defs.AssertTrueDef;
import org.junit.Assert;
import org.junit.Before;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import model.Record;
import org.junit.Test;

public class RecordServiceTest {

    RecordServiceImpl recordService;

    RecordRepository recordRepositoryMock;

    List<Record> objectList;

    @Before
    public void init(){
        recordService = new RecordServiceImpl();
        RecordRepository recordRepositoryMock = mock(RecordRepository.class);
        recordService.setRecordRepository(recordRepositoryMock);
        objectList = new LinkedList<>();

        when(recordRepositoryMock.save(anyObject())).then(a ->{
            Record argumentAt = a.getArgumentAt(0, Record.class);
            objectList.add(argumentAt);
            return argumentAt;
        });

        when(recordRepositoryMock.getNotDownloaded()).then( a->
                objectList.stream()
                        .filter(f -> f.getState().equals(State.downloading))
                        .collect(Collectors.toList())
        );

    }


    @Test
    public void saveTest(){
        Record record = new Record();
        Record save = recordService.save(record);
        Assert.assertTrue(objectList.size() > 0);
        Assert.assertTrue(record.equals(save));
    }

    @Test
    public void getNotDownloadedTest(){
        Random random = new Random();
        int numOfConverting = random.nextInt(10);
        int numOfDownloading = random.nextInt(10);
        int numOfFails = random.nextInt(10);
        int numOfFinalizing = random.nextInt(10);

        for (int i = 0 ; i<numOfConverting; i++){
            Record record = new Record();
            record.setState(State.converting);
            recordService.save(record);
        }
        for (int i = 0 ; i<numOfFails; i++){
            Record record = new Record();
            record.setState(State.failed);
            recordService.save(record);
        }
        for (int i = 0 ; i<numOfDownloading; i++){
            Record record = new Record();
            record.setState(State.downloading);
            recordService.save(record);
        }
        for (int i = 0 ; i<numOfFinalizing; i++){
            Record record = new Record();
            record.setState(State.finalizing);
            recordService.save(record);
        }

        List<Record> notDownloaded = recordService.getNotDownloaded();
        Assert.assertTrue(notDownloaded != null);
        Assert.assertTrue(notDownloaded.size() == numOfDownloading);
    }
}
