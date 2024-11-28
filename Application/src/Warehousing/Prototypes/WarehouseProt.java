package Warehousing.Prototypes;

import Warehousing.WarehousingObserver;
import Warehousing.WarehousingSubject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarehouseProt implements WarehousingSubject {
    private String name;
    private String address;

    private Map<String, StorageRack2dArray> racks = new HashMap<>();

    private List<WarehousingObserver> warehousingObservers;

    public WarehouseProt(String name, String address) {
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

    public Map<String, StorageRack2dArray> getRacks() {
        return racks;
    }


    public void addStorageRack(String string, StorageRack2dArray rack) {
        racks.put(string, rack);
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
//            who.update(this);
        }
    }
}
