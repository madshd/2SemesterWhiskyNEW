package Interfaces;

public interface OberverQuantitySubject {
    public void addObserver(ObserverQuantityObserver o);
    public void removeObserver(ObserverQuantityObserver o);
    public void notifyObservers();
}
