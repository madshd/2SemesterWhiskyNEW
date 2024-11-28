package Warehousing;

import Interfaces.Filling;

import java.time.LocalDate;

public class FillIngredient implements Filling {
    private final LocalDate date;
    private final double quantity;

    public FillIngredient(LocalDate date, double quantity) {
        this.date = date;
        this.quantity = quantity;
    }


    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public double getQuantity() {
        return quantity;
    }
}
