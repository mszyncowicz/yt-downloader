package bean;

import model.ErrorMessage;
import qualifier.ErrorQueueBeanQualifier;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import java.util.*;

@Singleton
@ErrorQueueBeanQualifier
public class ErrorQueueBean implements ErrorQueueBeanInterface {

    Map<String,Deque<ErrorMessage>> queue;

    @PostConstruct
    public void init(){
        queue = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public synchronized ErrorMessage dequeForSession(String sessionToken) {
        if (!queue.containsKey(sessionToken)) return null;

        Deque<ErrorMessage> errorMessages = queue.get(sessionToken);
        ErrorMessage errorMessage = errorMessages.removeFirst();
        if (errorMessages.isEmpty()){
            queue.remove(sessionToken);
        }
        return errorMessage;
    }

    @Override
    public synchronized void enqueue(ErrorMessage errorMessage) {
        if(!queue.containsKey(errorMessage.getSessionToken())){
            queue.put(errorMessage.getSessionToken(), new LinkedList<>(Arrays.asList(errorMessage)));
        } else{
            queue.get(errorMessage.getSessionToken()).addLast(errorMessage);
        }
    }

    @Override
    public synchronized boolean hasErrorForSession(String sessionToken) {
        return queue.containsKey(sessionToken) && queue.get(sessionToken) != null && !queue.get(sessionToken).isEmpty();
    }
}
