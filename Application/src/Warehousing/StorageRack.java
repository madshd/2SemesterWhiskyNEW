package Warehousing;

import Interfaces.Item;
import Interfaces.WarehousingObserver;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class StorageRack {
    private String id;
    private int shelves;
//    Nullable
    private Warehouse warehouse;

    private LinkedList<Item> list = new LinkedList<>();

    private Set<WarehousingObserver> warehousingObservers = new HashSet<>();

    public StorageRack(String id, int shelves) {
        this.id = id;
        this.shelves = shelves;
        for (int i = 0; i < shelves; i++) {
            list.add(null);
        }
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public String getId() {
        return id;
    }

    public Set<WarehousingObserver> getWarehousingObservers() {
        return warehousingObservers;
    }

    public LinkedList<Item> getList() {
        return list;
    }

    public void addItem(int shelfNo, Item item) {
        if (shelfNo >= 0 && shelfNo < shelves && list.get(shelfNo) == null) {
            item.setStorageRack(this);
            list.set(shelfNo, item);
            notifyWarehousingObservers( item + " added to shelf " + shelfNo + ": " + item.getListInfo());
        } else {
            throw new IllegalStateException("Shelf is occupied or invalid");
        }
    }

    public void removeItem(Item item, int shelfNo) {
        if (list.get(shelfNo) != item) {
            throw new IllegalStateException(item + " not found at " + shelfNo);
        }
        item.setStorageRack(null);
        list.remove(item);
        notifyWarehousingObservers( item + " removed at shelf " + shelfNo);
    }

    public void moveItem(Item item, int atIndex, int toIndex) {
        if (list.get(atIndex) == item && list.get(toIndex) == null) {
            list.set(toIndex, item);
            list.set(atIndex, null);
//            System.out.println("Item moved from " + atIndex + " to " + toIndex);
            notifyWarehousingObservers(item + " moved from shelf " + atIndex + " to shelf " + toIndex);
        } else {
            throw new IllegalStateException("No more space on the storage rack");
        }
    }

    public int getItemLocation(Item item) {
        return list.indexOf(item);
    }

    private void notifyWarehousingObservers(String details) {
        if (warehouse != null) { // Ensure rack is part of a warehouse
            for (WarehousingObserver observer : warehouse.getWarehousingObservers()) {
                observer.update(warehouse,  id + ": " + details);
            }
        }
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Shelves: " + shelves;
    }
}
