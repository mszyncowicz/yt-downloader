package controller;

import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.Setter;
import model.Session;
import service.SessionService;
import java.util.List;

@Path("/session")
public class SessionController {

    @Any
    @Setter
    @Inject
    SessionService sessionService;

    @Inject
    private UserTransaction userTransaction;

    @GET
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Session createSession() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        userTransaction.begin();
        Session session = sessionService.createSession();
        userTransaction.commit();
        return session;
    }

    @GET
    @Path("/get/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Session getByToken(@PathParam("token") String token) throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        userTransaction.begin();
        Session sessionByToken = sessionService.getSessionByToken(token);
        sessionByToken.getRecords().size();
        userTransaction.commit();
        return sessionByToken;
    }

}
