package service;

import model.Record;
import org.hibernate.Session;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.enterprise.event.Event;
import javax.persistence.EntityManager;


@Stateless
public class RecordRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Record> memberEventSrc;

    public void register(Record record) throws Exception {
        log.info("Registering " + record.toString());
        Session session = (Session) em.getDelegate();
        session.persist(record);
        memberEventSrc.fire(record);
    }
}