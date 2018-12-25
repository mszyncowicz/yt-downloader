package data;

import model.Session;

public interface SessionRepository extends Repository<Session> {

    Session getByToken(String token);
}
