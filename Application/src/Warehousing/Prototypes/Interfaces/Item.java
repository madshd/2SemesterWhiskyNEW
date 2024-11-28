package Warehousing.Prototypes.Interfaces;

public interface Item {
    public String getListInfo();
    public double getQuantityStatus();
    public double updateQuantity(Filling fillDistillate) throws IllegalStateException;
    public double getRemainingQuantity();
}
