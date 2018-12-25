package data;

import lombok.Getter;
import model.Configuration;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

@Model
public class ConfigurationRepositoryImpl implements ConfigurationRepository {

    @Getter
    @Inject
    EntityManager entityManager;

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Configuration getById(UUID id) {
        return entityManager.find(Configuration.class,id);
    }

    @Override
    public Configuration save(Configuration object) {
        if (object == null) throw new IllegalArgumentException();
        if (getById(object.getId()) == null){
            entityManager.persist(object);
            return object;
        } else {
            return entityManager.merge(object);
        }
    }

    @Override
    public Configuration getAny() {
        List res = entityManager.createQuery("SELECT s from Configuration s").setMaxResults(1).getResultList();
        return res.size() > 0 ? (Configuration) res.get(0) : null;
    }
}
