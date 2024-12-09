package Warehousing;

import Interfaces.Filling;
import Production.Distillate;

import java.time.LocalDate;

public class FillIngredient implements Filling {
    private final LocalDate date;
    private final double quantity;
    private final Distillate distillate;
    private final Ingredient ingredient;
    private final boolean decrease; // If true then quantity is negative.

    public FillIngredient(LocalDate date, double quantity, Distillate distillate, Ingredient ingredient, boolean decrease) {
        this.date = date;
        this.quantity = quantity;
        this.distillate = distillate;
        this.ingredient = ingredient;
        this.decrease = decrease;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public double getQuantity() {
        return (decrease) ? quantity * -1 : quantity;
    }

    public Distillate getDistillate() {
        return distillate;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public boolean isDecrease() {
        return decrease;
    }
}
