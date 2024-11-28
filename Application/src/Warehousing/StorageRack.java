package Warehousing;

import java.util.ArrayList;
import java.util.List;

public class StorageRack {
    private String id;
    private int shelves;

//    Nullable
    private Warehouse warehouse;

    private final List<Object> list;

    public StorageRack(String id, int shelves) {
        this.id = id;
        this.shelves = shelves;
        this.list = new ArrayList<>(shelves);
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}
