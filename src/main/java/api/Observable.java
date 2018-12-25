package api;

import java.util.List;

public interface Observable {
    public void addObserver(Observer observer);
    public void removeObserver(Observer observer);
    public List<Observer> getObservers();
}
