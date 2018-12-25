package service;

import model.Session;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public interface SessionService {

    public Session createSession();

    public Session getSessionByToken(String token);

    Session save(Session session);
}
