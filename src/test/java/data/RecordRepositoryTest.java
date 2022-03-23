package data;

import annotation.InjectEntityMangaer;
import model.Record;
import model.*;
import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(SessionFactoryExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecordRepositoryTest {

    @InjectEntityMangaer
    public RecordRepositoryImpl recordRepository;

    @InjectEntityMangaer
    public SessionRepositoryImpl sessionRepository;

    @BeforeAll
    public void init(){
        sessionRepository = new SessionRepositoryImpl();
        recordRepository = new RecordRepositoryImpl();
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
        Assertions.assertTrue(clone.toString().equals(record.toString()));
        Assertions.assertTrue(record.equals(clone));
    }

    @Test
    public void saveTest(){
        Record record =  generateRandomRecord();
        recordRepository.getEntityManager().getTransaction().begin();

        Record save = recordRepository.save(record);
        recordRepository.getEntityManager().getTransaction().commit();

        Assertions.assertNotNull(save);

        Record byId = recordRepository.getById(record.getId());
        Assertions.assertNotNull(save);
        Assertions.assertTrue(record.equals(save));
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
        Assertions.assertNotNull(notDownloaded);
        Assertions.assertTrue(savedNotDownloaded.size() == notDownloaded.size() );

        for (Record rec : savedNotDownloaded){
            Assertions.assertTrue(notDownloaded.contains(rec));
        }

        for (int i =0; i<notDownloaded.size(); i++){
            if (i > 0){
                Assertions.assertTrue(notDownloaded.get(i-1).getDate().before(notDownloaded.get(i).getDate()));
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
        Assertions.assertNotNull(notDownloaded);
        Assertions.assertTrue(notDownloaded.size() == 3 );

        for (Record rec : notDownloaded){
            Assertions.assertTrue(savedNotDownloaded.contains(rec));
        }

        Assertions.assertTrue(notDownloaded.get(0).getDate().before(notDownloaded.get(1).getDate()));
        Assertions.assertTrue(notDownloaded.get(1).getDate().before(notDownloaded.get(2).getDate()));


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
        Assertions.assertNotNull(byId.getDate());
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
        Assertions.assertTrue(first != null);
        Assertions.assertTrue(first.size() == 5);
        LocalDateTime localDateTime = LocalDateTime.fromDateFields(first.get(0).getDate());
        LocalDateTime readablePartial = LocalDateTime.fromDateFields(first.get(1).getDate());
        System.out.println(localDateTime.toString() + " " + readablePartial.toString());
        Assertions.assertTrue(localDateTime.isBefore(readablePartial));
    }
    @Test
    public void nullSave(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> sessionRepository.save(null));

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

        Assertions.assertTrue(record.getMediaType().equals(save.getMediaType()));
        Assertions.assertTrue(record.equals(save));
    }
}
