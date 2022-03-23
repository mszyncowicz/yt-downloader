package service;

import data.SessionRepository;
import model.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SessionServiceTest {

    SessionServiceImpl sessionService;

    public static Map<String, Session> map = new HashMap<>();

    @BeforeEach
    public void init(){
        sessionService = new SessionServiceImpl();
        SessionRepository mock = mock(SessionRepository.class);
        sessionService.setSessionRepository(mock);
        sessionService.init();
        when(mock.getById(any())).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                String argumentAt = invocationOnMock.getArgument(0, String.class);
                return map.get(argumentAt);
            }
        });

        when(mock.save(any())).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Session argumentAt = invocationOnMock.getArgument(0, Session.class);
                if (argumentAt == null){
                    throw new IllegalArgumentException();
                }
                map.put(argumentAt.getToken(),argumentAt);
                return argumentAt;
            }
        });

        when(mock.getByToken(anyString())).thenAnswer(invocation -> {
            String argumentAt = invocation.getArgument(0, String.class);
            return map.get(argumentAt);
        });
    }

    @Test
    public void createSession(){
        Session session = sessionService.createSession();

        Assertions.assertNotNull(session);

        Assertions.assertNotNull(session.getId());

        Assertions.assertNotNull(session.getToken());

        Assertions.assertFalse(session.getToken().isEmpty());

        Session sessionByToken = map.get(session.getToken());

        Assertions.assertTrue(session.equals(sessionByToken));
    }

    @Test
    public void getByToken(){
        Session session = new Session();
        session.setId(UUID.randomUUID());
        session.setToken(Session.generateRandom(20));
        map.put(session.getToken(),session);

        Session sessionByToken = sessionService.getSessionByToken(session.getToken());

        Assertions.assertTrue(session.equals(sessionByToken));

    }

    @Test
    @Disabled
    public void cacheOnTest(){
        Session session = sessionService.createSession();

        Assertions.assertTrue(map.containsKey(session.getToken()));

        Session sessionByToken = sessionService.getSessionByToken(session.getToken());

        Assertions.assertTrue(session.equals(sessionByToken));

        map.remove(session.getToken());

        sessionByToken = sessionService.getSessionByToken(session.getToken());

        Assertions.assertTrue(session.equals(sessionByToken));

    }

    @Test
    @Disabled
    public void cacheShouldKeepOnly1000(){
        Session session = sessionService.createSession();

        Assertions.assertTrue(map.containsKey(session.getToken()));

        Session sessionByToken = sessionService.getSessionByToken(session.getToken());

        Assertions.assertTrue(session.equals(sessionByToken));

        map.remove(session.getToken());

        sessionByToken = sessionService.getSessionByToken(session.getToken());

        Assertions.assertTrue(session.equals(sessionByToken));

        for (int i = 0; i< 1200; i++){
            Session sessionPop = sessionService.createSession();
            sessionByToken = sessionService.getSessionByToken(sessionPop.getToken());
        }
        sessionByToken = sessionService.getSessionByToken(session.getToken());
        Assertions.assertNull(sessionByToken);
    }




}
