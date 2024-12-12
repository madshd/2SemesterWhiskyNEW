package Warehousing;

import Interfaces.Item;
import Interfaces.WarehousingObserver;
import Interfaces.WarehousingSubject;

import java.io.Serializable;
import java.util.*;

public class Warehouse implements WarehousingSubject, Serializable {
    private String name;
    private String address;
    private Map<String, StorageRack> racks = new HashMap<>();

    private transient List<WarehousingObserver> warehousingObservers;

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
        if (!warehousingObservers.isEmpty()) {
            notifyWarehousingObserversWithDetails("Storage rack added: " + storageRack.getId());
        }
    }

    public void removeStorageRack(StorageRack storageRack) {
        if (storageRack != null && racks.containsValue(storageRack)) {
            boolean isEmpty = true;
            for (Item item : storageRack.getList()) {
                if (item != null) {
                    isEmpty = false;
                    break;
                }
            }
            if (isEmpty) {
                racks.values().remove(storageRack);
                storageRack.setWarehouse(null);
                if (!warehousingObservers.isEmpty()) {
                    notifyWarehousingObserversWithDetails("Storage rack removed: " + storageRack.getId());
                }
            }
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
        if (!warehousingObservers.isEmpty()) {
            for (WarehousingObserver observer : warehousingObservers) {
                observer.update(this, "General update in warehouse: " + this.name);
            }
        }
    }

    /**
     * Restore observers list after deserialization if necessary
     * @return
     */
    private Object readResolve() {
        if (warehousingObservers == null) {
            this.warehousingObservers = new ArrayList<>();
        }
        return this;
    }
}