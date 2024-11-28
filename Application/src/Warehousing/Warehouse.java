package Warehousing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Warehouse implements WarehousingSubject {
    private String name;
    private String address;
    private Map<String, StorageRack> racks = new HashMap<>();

    private List<WarehousingObserver> warehousingObservers;

    public Warehouse(String name, String address) {
        this.name = name;
        this.address = address;
        this.racks = racks;
        this.warehousingObservers = new ArrayList<>(); // Initialize the list
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Map<String, StorageRack> getRacks() {
        return racks;
    }

    public void addStorageRack(String string, StorageRack storageRack) {
        racks.put(string, storageRack);
        notifyWarehousingObservers();
    }


    public void removeStorageRack(String string) {
        racks.remove(string);
        notifyWarehousingObservers();
    }

    @Override
    public void registerWarehousingObserver(WarehousingObserver o) {
        warehousingObservers.add(o);
    }

    @Override
    public void removeWarehousingObserver(WarehousingObserver o) {
        warehousingObservers.remove(o);
    }

    @Override
    public void notifyWarehousingObservers() {
        for (WarehousingObserver who : warehousingObservers) {
            who.update(this);
        }
    }
}
