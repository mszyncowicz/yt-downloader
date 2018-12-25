package data;

import model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import service.SessionServiceImpl;

import java.util.UUID;

import static junit.framework.TestCase.fail;

public class SessionRepositoryTest {

    @Rule
    public SessionFactoryRule sessionFactoryRule = new SessionFactoryRule();

    public SessionRepositoryImpl sessionRepository;

    public RecordRepositoryImpl recordRepository;

    @Before
    public void init() {
        sessionRepository = new SessionRepositoryImpl();
        recordRepository = new RecordRepositoryImpl();
        sessionFactoryRule.injectManager(sessionRepository);
        sessionFactoryRule.injectManager(recordRepository);
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

        Assert.assertTrue(session.equals(save));

        Session byId = sessionRepository.getById(session.getId());
        Assert.assertNotNull(byId);
        Assert.assertTrue(session.equals(byId));
    }

    @Test
    public void getByTokenTest(){
        Session session = generateSession();

        sessionRepository.getEntityManager().getTransaction().begin();
        sessionRepository.save(session);
        sessionRepository.getEntityManager().getTransaction().commit();

        Session byToken = sessionRepository.getByToken(session.getToken());
        Assert.assertTrue(session.equals(byToken));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSave(){
        sessionRepository.save(null);
    }

    @Test
    public void updateTest(){
        Session session = generateSession();
        sessionRepository.getEntityManager().getTransaction().begin();
        Session save = sessionRepository.save(session);
        sessionRepository.getEntityManager().getTransaction().commit();

        Assert.assertTrue(session.equals(save));

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
        Assert.assertFalse(save == null);

        Session byToken = sessionRepository.getByToken(session.getToken());
        Assert.assertTrue(session.equals(byToken));
        Assert.assertEquals(1, byToken.getRecords().size());
    }

}
