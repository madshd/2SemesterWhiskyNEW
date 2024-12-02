package Interfaces;

public interface Item extends Comparable<Item> {
import Warehousing.StorageRack;

public interface Item {
    public String getListInfo();
    public double getQuantityStatus();
    public double updateQuantity(Filling fillDistillate) throws IllegalStateException;
    public double getRemainingQuantity();
    public int compareTo(Item o);
    public String getName();

    void setStorageRack(StorageRack storageRack);
}