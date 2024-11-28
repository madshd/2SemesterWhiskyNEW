package Warehousing.Prototypes.Warehousing.Prototypes.Interfaces;

import Warehousing.Prototypes.Interfaces.ObserverQuantityObserver;

public interface OberverQuantitySubject {
    public void addObserver(ObserverQuantityObserver o);
    public void removeObserver(ObserverQuantityObserver o);
    public void notifyObservers();
}
