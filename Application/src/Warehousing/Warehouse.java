package Warehousing;

import Interfaces.Item;
import Interfaces.WarehousingObserver;
import Interfaces.WarehousingSubject;

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

    public List<WarehousingObserver> getWarehousingObservers() {
        return warehousingObservers;
    }

    public void addStorageRack(String id, StorageRack storageRack) {
        racks.put(id, storageRack);
        storageRack.setWarehouse(this); // Bind rack to warehouse
        notifyWarehousingObserversWithDetails("StorageRack added: " + id);
    }


    public void removeStorageRack(String id) {
        StorageRack removedRack = racks.remove(id);
        if (removedRack != null) {
            notifyWarehousingObserversWithDetails("StorageRack removed: " + id);
        }
    }

    public void notifyWarehousingObserversWithDetails(String details) {
        for (WarehousingObserver observer : warehousingObservers) {
            observer.update(this, details);
        }
    }

    @Override
    public String toString() {
        return name + ", " + address;
    }

    public String getItemLocation(Item item) {
        for (Map.Entry<String, StorageRack> entry : racks.entrySet()) {
            StorageRack rack = entry.getValue();
            int location = rack.getItemLocation(item);
            if (location != -1) {
                return "Rack: " + entry.getKey() + ", Shelf: " + location;
            }
        }
        return "Item not found in any rack";
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
        for (WarehousingObserver observer : warehousingObservers) {
            observer.update(this, "General update in warehouse: " + this.name);
        }
    }
}
