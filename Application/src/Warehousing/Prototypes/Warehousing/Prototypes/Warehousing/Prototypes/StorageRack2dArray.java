package Warehousing.Prototypes.Warehousing.Prototypes.Warehousing.Prototypes;

import Warehousing.Prototypes.Interfaces.Item;
import Warehousing.Prototypes.Warehousing.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class StorageRack2dArray {
    private String id;
    private Item[][] rack;

    //    Nullable
    private Warehouse warehouse;

    public StorageRack2dArray(String id, int rows, int columns) {
        this.id = id;
        rack = new Item[rows][columns];
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

    public void addItem(int row, int column, Item item) {
        if (rack[row][column] == null) {
            rack[row][column] = item;
            System.out.println("Item added to row " + row + " and column " + column);
        } else {
            throw new IllegalStateException("No more space on the storage rack");
        }

    }

    public void removeItem(Item item) {
        for (int i = 0; i < rack.length; i++) {
            for (int j = 0; j < rack[i].length; j++) {
                if (rack[i][j] == item) {
                    rack[i][j] = null;
                    System.out.println("Item removed at row " + i + " and column " + j);
                }
            }
        }
    }

    public void moveitem(int fromRow, int fromColumn, int toRow, int toColumn) {
        if (rack[fromRow][fromColumn] != null && rack[toRow][toColumn] == null) {
            rack[toRow][toColumn] = rack[fromRow][fromColumn];
            rack[fromRow][fromColumn] = null;
            System.out.println("Item moved from row " + fromRow + " and column " + fromColumn + " to row " + toRow + " and column " + toColumn);
        } else {
            throw new IllegalStateException("No more space on the storage rack");
        }
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < rack.length; i++) {
            for (int j = 0; j < rack[i].length; j++) {
                if (rack[i][j] != null) {
                    items.add(rack[i][j]);
                }
            }
        }
        return items;
    }
}
