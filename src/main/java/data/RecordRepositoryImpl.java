package data;

import lombok.Getter;
import model.Record;
import model.State;

import javax.enterprise.inject.Model;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Named("RecordRepository")
@Model
public class RecordRepositoryImpl implements RecordRepository{

    @PersistenceContext
    @Getter
    private EntityManager entityManager;

    @Override
    public List<Record> getNotDownloaded() {
        Query query = entityManager.createQuery("select r from Record r where r.state = :state order by r.date asc");
        query.setParameter("state", State.downloading);
        return query.getResultList();
    }

    public List<Record> get5First(){
        List<Record> notDownloaded = getNotDownloaded();
        notDownloaded.sort((a, b) -> a.getDate().compareTo(b.getDate()));
        return notDownloaded.stream().limit(5).collect(Collectors.toList());
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Record getById(UUID id) {
        return entityManager.find(Record.class,id);
    }

    @Override
    public Record save(Record object) {
        if (object == null) throw new IllegalArgumentException();
        if (getById(object.getId()) == null){
            entityManager.persist(object);
            return object;
        } else {
            return entityManager.merge(object);
        }
    }

    @Override
    public List<Record> getNotDownloaded(int limit) {
        Query query = entityManager.createQuery("select r from Record r where r.state = :state order by r.date asc");
        query.setParameter("state", State.downloading);
        query.setMaxResults(limit);
        return query.getResultList();
    }
}
