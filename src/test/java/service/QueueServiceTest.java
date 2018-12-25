package service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import model.Record;

public class QueueServiceTest {

    QueueServiceImpl queueService;
    Record record1,record2,record3;
    @Before
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
        Assert.assertTrue(queueService.size() == 3);
    }

    @Test
    public void testRemove(){
        Record lvRecord2 = queueService.dequeue();
        Record lvRecord3 = queueService.dequeue();
        Record lvRecord1 = queueService.dequeue();

        Assert.assertTrue(lvRecord2.equals(record2));
        Assert.assertTrue(lvRecord3.equals(record3));
        Assert.assertTrue(lvRecord1.equals(record1));
    }

}
