package Interfaces;

import java.time.LocalDate;

public interface Filling {
    public LocalDate getDate();
    public double getQuantity();
    public boolean isDecrease();
}
