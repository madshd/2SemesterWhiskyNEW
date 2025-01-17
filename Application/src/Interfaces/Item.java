package Interfaces;

import Warehousing.StorageRack;

public interface Item extends Comparable<Item>{
    public String getListInfo();
    public double getQuantityStatus();
    public double updateQuantity(Filling fillDistillate) throws IllegalStateException;
    public double getRemainingQuantity();
    public int compareTo(Item o);
    public String getName();

    public void addObserver(ObserverQuantityObserver o);

    void setStorageRack(StorageRack storageRack);
}