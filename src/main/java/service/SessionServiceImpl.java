package service;

import com.google.common.cache.*;
import data.SessionRepository;
import data.SessionRepositoryImpl;
import lombok.Getter;
import lombok.Setter;
import model.Session;
import qualifier.SessionRepositoryQualifier;
import qualifier.SessionServiceQualifier;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

@Singleton
@SessionServiceQualifier
public class SessionServiceImpl implements SessionService{

    public static final int TOKEN_SIZE = Session.scale;

    LoadingCache<String,Session> sessions;


    public void init(){
        sessions = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(Duration.ofMinutes(15))
                .removalListener(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
                        Session val = (Session) removalNotification.getValue();
                        //sessionRepository.setInactive(true);
                    }
                })
                .build(
                        new CacheLoader<String, Session>() {
                            @Override
                            public Session load(String s) throws Exception {
                                return sessionRepository.getByToken(s);
                            }
                        }
                );
    }

    @Inject
    @Setter
    @Getter
    SessionRepository sessionRepository;

    @Override
    public Session createSession() {
        Session session = new Session();
        session.setRecords(Collections.emptyList());
        session.setId(UUID.randomUUID());
        session.setToken(Session.generateRandom(TOKEN_SIZE));

        return sessionRepository.save(session);

    }

    @Override
    public Session getSessionByToken(String token) {
       return sessionRepository.getByToken(token);
    }

    @Override
    @TransactionAttribute
    public Session save(Session session) {
        return sessionRepository.save(session);
    }
}
