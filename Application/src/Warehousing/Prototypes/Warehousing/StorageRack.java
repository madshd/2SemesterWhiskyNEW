package Warehousing.Prototypes.Warehousing;

import Warehousing.Prototypes.Interfaces.Item;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StorageRack {
    private String id;
    private int shelves;

//    Nullable
    private Warehouse warehouse;

    private LinkedList<Item> list = new LinkedList<>();

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

    public void addItem(int shelfNo, Item item) {
        if (shelfNo >= 0 && shelfNo < shelves && list.get(shelfNo) == null) {
            list.add(shelfNo, item);
            System.out.println("Item added to shelf " + shelfNo);
        } else {
            throw new IllegalStateException("No more space on the storage rack");
        }
    }

    public void removeItem(int shelfNo) {
        list.remove(shelfNo);
        System.out.println("Item removed at " + shelfNo);
    }

    public void moveItem(Item item, int atIndex, int toIndex) {
        if (list.get(atIndex) == item && list.get(toIndex) == null) {
            list.set(toIndex, item);
            list.set(atIndex, null);
            System.out.println("Item moved from " + atIndex + " to " + toIndex);
        } else {
            throw new IllegalStateException("No more space on the storage rack");
        }
    }

    public List<Item> getItems() {
        return new ArrayList<>(list);
    }
}
