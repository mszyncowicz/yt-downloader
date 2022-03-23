package data;

import annotation.InjectEntityMangaer;
import model.*;
import model.Record;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

@ExtendWith({SessionFactoryExtension.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SessionRepositoryTest {



    @InjectEntityMangaer
    public SessionRepositoryImpl sessionRepository;

    @InjectEntityMangaer
    public RecordRepositoryImpl recordRepository;

    @BeforeAll
    public void init() {
        sessionRepository = new SessionRepositoryImpl();
        recordRepository = new RecordRepositoryImpl();
    }
    private Session generateSession(){
        Session session = new Session();
        session.setId(UUID.randomUUID());
        session.setToken(Session.generateRandom(20));
        return session;
    }
    @Test
    public void getByUUIDAndSaveTest(){
        Session session = generateSession();
        sessionRepository.getEntityManager().getTransaction().begin();
        Session save = sessionRepository.save(session);
        sessionRepository.getEntityManager().getTransaction().commit();

        Assertions.assertTrue(session.equals(save));

        Session byId = sessionRepository.getById(session.getId());
        Assertions.assertNotNull(byId);
        Assertions.assertTrue(session.equals(byId));
    }

    @Test
    public void getByTokenTest(){
        Session session = generateSession();

        sessionRepository.getEntityManager().getTransaction().begin();
        sessionRepository.save(session);
        sessionRepository.getEntityManager().getTransaction().commit();

        Session byToken = sessionRepository.getByToken(session.getToken());
        Assertions.assertTrue(session.equals(byToken));
    }

    @Test
    public void nullSave(){
        Assertions.assertThrows(IllegalArgumentException.class, () ->  sessionRepository.save(null));
    }

    @Test
    public void updateTest(){
        Session session = generateSession();
        sessionRepository.getEntityManager().getTransaction().begin();
        Session save = sessionRepository.save(session);
        sessionRepository.getEntityManager().getTransaction().commit();

        Assertions.assertTrue(session.equals(save));

        Record e = new Record();
        e.setId(UUID.randomUUID());
        e.setState(State.downloading);
        e.setLink("bla");
        e.setMediaType(MediaType.audio);
        e.setSession(save);
        save.getRecords().add(e);
        recordRepository.getEntityManager().getTransaction().begin();
        recordRepository.save(e);
        recordRepository.getEntityManager().getTransaction().commit();
        sessionRepository.getEntityManager().getTransaction().begin();
        save = sessionRepository.save(save);
        sessionRepository.getEntityManager().getTransaction().commit();
        Assertions.assertFalse(save == null);

        Session byToken = sessionRepository.getByToken(session.getToken());
        Assertions.assertTrue(session.equals(byToken));
        Assertions.assertEquals(1, byToken.getRecords().size());
    }

}
