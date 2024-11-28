package Warehousing.Prototypes.Production;

import Warehousing.Prototypes.Interfaces.Filling;

import java.time.LocalDate;

public class FillDistillate implements Filling {
    private final LocalDate date;
    private final double quantity;

    public FillDistillate(LocalDate date, double quantity) {
        this.date = date;
        this.quantity = quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getQuantity() {
        return quantity;
    }
}
