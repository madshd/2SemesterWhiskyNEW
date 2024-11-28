package Warehousing.Prototypes.Warehousing.Prototypes.Interfaces;

import Warehousing.Prototypes.Interfaces.Filling;

public interface Item {
    public String getListInfo();
    public double getQuantityStatus();
    public double updateQuantity(Filling fillDistillate) throws IllegalStateException;
    public double getRemainingQuantity();
}
