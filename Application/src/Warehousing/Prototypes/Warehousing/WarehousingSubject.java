package Warehousing.Prototypes.Warehousing;

public interface WarehousingSubject {
    void registerWarehousingObserver(WarehousingObserver o);
    void removeWarehousingObserver(WarehousingObserver o);
    void notifyWarehousingObservers();
}
