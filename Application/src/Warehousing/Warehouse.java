package Warehousing;

import java.util.List;
import java.util.Map;

public class Warehouse implements ObservableItem {
    private String name;
    private String address;
    private Map<String, StorageRack> racks;
    private List<Observer> observers;

    public Warehouse(String name, String address) {
        this.name = name;
        this.address = address;
        this.racks = racks;
    }

    public void addStorageRack(String string, StorageRack storageRack) {
        racks.put(string, storageRack);
        notifyObservers();
    }

    @Override
    public void registerObserver(Observer o) {

    }

    @Override
    public void removeObserver(Observer o) {

    }

    @Override
    public void notifyObservers() {

    }
}
