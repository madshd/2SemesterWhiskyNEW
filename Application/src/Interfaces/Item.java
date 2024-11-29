package Interfaces;

public interface Item extends Comparable<Item> {
    public String getListInfo();
    public double getQuantityStatus();
    public double updateQuantity(Filling fillDistillate) throws IllegalStateException;
    public double getRemainingQuantity();
    public int compareTo(Item o);
    public String getName();
}