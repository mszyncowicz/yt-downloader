package data;

import lombok.Getter;
import model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qualifier.SessionRepositoryQualifier;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import java.util.UUID;

@RequestScoped
@Default
public class SessionRepositoryImpl implements SessionRepository {

    @PersistenceContext
    @Getter
    private EntityManager entityManager;

    public static Logger logger = LoggerFactory.getLogger(SessionRepository.class);

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Session getById(UUID id) {
        return entityManager.find(Session.class,id);
    }

    @Override
    public Session save(Session object) {
        if (object == null) throw new IllegalArgumentException();
        if (getById(object.getId()) == null){
            entityManager.persist(object);
            return object;
        } else {
            return entityManager.merge(object);
        }
    }

    @Override
    public Session getByToken(String token) {
        try {
            logger.info(entityManager.toString());
            Query query = entityManager.createQuery("SELECT s from Session s WHERE s.token = :token", Session.class);
            query.setParameter("token", token);
            Session singleResult = (Session) query.getSingleResult();
            return singleResult;
        }catch (NoResultException e){
            return null;
        }
    }
}
