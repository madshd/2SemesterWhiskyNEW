package Warehousing;

public interface ObservableItem {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}
