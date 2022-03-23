package service;


import model.Record;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QueueServiceTest {

    QueueServiceImpl queueService;
    Record record1,record2,record3;
    @BeforeEach
    public void init(){
        queueService = new QueueServiceImpl();
        record1 = new Record();
        record2 = new Record();
        record3 = new Record();

        queueService.enqueue(record2);
        queueService.enqueue(record3);
        queueService.enqueue(record1);
    }

    @Test
    public void testAdd(){
        Assertions.assertTrue(queueService.size() == 3);
    }

    @Test
    public void testRemove(){
        Record lvRecord2 = queueService.dequeue();
        Record lvRecord3 = queueService.dequeue();
        Record lvRecord1 = queueService.dequeue();

        Assertions.assertTrue(lvRecord2.equals(record2));
        Assertions.assertTrue(lvRecord3.equals(record3));
        Assertions.assertTrue(lvRecord1.equals(record1));
    }

}
