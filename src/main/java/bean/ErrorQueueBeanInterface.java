package bean;

import model.ErrorMessage;

import java.util.Deque;

public interface ErrorQueueBeanInterface {
    ErrorMessage dequeForSession(String sessionToken);
    void enqueue(ErrorMessage errorMessage);
    boolean hasErrorForSession(String sessionToken);
}
