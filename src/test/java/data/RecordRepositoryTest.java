package data;

import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.validator.cfg.defs.AssertTrueDef;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.fail;

public class RecordRepositoryTest {

    @Rule
    public SessionFactoryRule sessionFactoryRule = new SessionFactoryRule();

    public RecordRepositoryImpl recordRepository;
    public SessionRepositoryImpl sessionRepository;

    @Before
    public void init(){
        sessionRepository = new SessionRepositoryImpl();
        recordRepository = new RecordRepositoryImpl();
        sessionFactoryRule.injectManager(recordRepository);
        sessionFactoryRule.injectManager(sessionRepository);
    }

    @Test
    public void equalsTest(){
        Record record = generateRandomRecord();
        Record clone = new Record();
        clone.setId(record.getId());
        clone.setCommand(record.getCommand());
        clone.setState(record.getState());
        clone.setDate(record.getDate());
        clone.setMediaType(record.getMediaType());
        clone.setLink(record.getLink());
        clone.setSession(record.getSession());
        Assert.assertTrue(clone.toString().equals(record.toString()));
        Assert.assertTrue(record.equals(clone));
    }

    @Test
    public void saveTest(){
        Record record =  generateRandomRecord();
        recordRepository.getEntityManager().getTransaction().begin();

        Record save = recordRepository.save(record);
        recordRepository.getEntityManager().getTransaction().commit();

        Assert.assertNotNull(save);

        Record byId = recordRepository.getById(record.getId());
        Assert.assertNotNull(save);
        Assert.assertTrue(record.equals(save));
    }

    @Test
    public void getNotDownloaded(){
        List<Record> savedNotDownloaded = new ArrayList<>(4);

        for (int i = 0; i< 4; i++){
            recordRepository.getEntityManager().getTransaction().begin();

            Record object = generateRandomRecord();
            savedNotDownloaded.add(object);
            recordRepository.save(object);
            recordRepository.getEntityManager().getTransaction().commit();

        }
        recordRepository.getEntityManager().getTransaction().begin();

        for(int i =0; i<6;i++){
            Record record = generateRandomRecord();
            record.setState(State.converting);
        }

        for (int i=0; i<2; i++){
            Record record = generateRandomRecord();
            record.setState(State.failed);
        }
        recordRepository.getEntityManager().getTransaction().commit();

        List<Record> notDownloaded = recordRepository.getNotDownloaded();
        Assert.assertNotNull(notDownloaded);
        Assert.assertTrue(savedNotDownloaded.size() == notDownloaded.size() );

        for (Record rec : savedNotDownloaded){
            Assert.assertTrue(notDownloaded.contains(rec));
        }

        for (int i =0; i<notDownloaded.size(); i++){
            if (i > 0){
                Assert.assertTrue(notDownloaded.get(i-1).getDate().before(notDownloaded.get(i).getDate()));
            }
        }

    }

    @Test
    public void getNotDownloadedLimited(){
        List<Record> savedNotDownloaded = new ArrayList<>(4);

        for (int i = 0; i< 4; i++){
            recordRepository.getEntityManager().getTransaction().begin();

            Record object = generateRandomRecord();
            savedNotDownloaded.add(object);
            recordRepository.save(object);
            recordRepository.getEntityManager().getTransaction().commit();

        }
        recordRepository.getEntityManager().getTransaction().begin();

        for(int i =0; i<6;i++){
            Record record = generateRandomRecord();
            record.setState(State.converting);
        }

        for (int i=0; i<2; i++){
            Record record = generateRandomRecord();
            record.setState(State.failed);
        }
        recordRepository.getEntityManager().getTransaction().commit();

        List<Record> notDownloaded = recordRepository.getNotDownloaded(3);
        Assert.assertNotNull(notDownloaded);
        Assert.assertTrue(notDownloaded.size() == 3 );

        for (Record rec : notDownloaded){
            Assert.assertTrue(savedNotDownloaded.contains(rec));
        }

        Assert.assertTrue(notDownloaded.get(0).getDate().before(notDownloaded.get(1).getDate()));
        Assert.assertTrue(notDownloaded.get(1).getDate().before(notDownloaded.get(2).getDate()));


    }
    public Record generateRandomRecord(){
        Record record = new Record(Session.generateRandom(10), Session.generateRandom(10));
        record.setId(UUID.randomUUID());
        record.setMediaType(MediaType.audio);
        Session session = new Session();
        sessionRepository.getEntityManager().getTransaction().begin();

        sessionRepository.save(session);
        sessionRepository.getEntityManager().getTransaction().commit();

        record.setSession(session);
        return record;
    }

    @Test
    public void dateTest(){
        recordRepository.getEntityManager().getTransaction().begin();

        Record object = generateRandomRecord();
        recordRepository.save(object);
        recordRepository.getEntityManager().getTransaction().commit();

        Record byId = recordRepository.getById(object.getId());
        Assert.assertNotNull(byId.getDate());
        System.out.println(byId.getDate().toString());
    }

    @Test
    public void get5First(){

        for (int i = 0; i< 15; i++){
            recordRepository.getEntityManager().getTransaction().begin();
            Record object = generateRandomRecord();
            recordRepository.save(object);
            recordRepository.getEntityManager().getTransaction().commit();


        }
        List<Record> first = recordRepository.get5First();
        Assert.assertTrue(first != null);
        Assert.assertTrue(first.size() == 5);
        LocalDateTime localDateTime = LocalDateTime.fromDateFields(first.get(0).getDate());
        LocalDateTime readablePartial = LocalDateTime.fromDateFields(first.get(1).getDate());
        System.out.println(localDateTime.toString() + " " + readablePartial.toString());
        Assert.assertTrue(localDateTime.isBefore(readablePartial));
    }
    @Test(expected = IllegalArgumentException.class)
    public void nullSave(){
        sessionRepository.save(null);
    }

    @Test
    public void updateTest(){
        Record record = generateRandomRecord();
        recordRepository.getEntityManager().getTransaction().begin();
        recordRepository.save(record);
        recordRepository.getEntityManager().getTransaction().commit();

        record.setMediaType(MediaType.video);
        recordRepository.getEntityManager().getTransaction().begin();
        Record save = recordRepository.save(record);
        recordRepository.getEntityManager().getTransaction().commit();

        Assert.assertTrue(record.getMediaType().equals(save.getMediaType()));
        Assert.assertTrue(record.equals(save));
    }
}
