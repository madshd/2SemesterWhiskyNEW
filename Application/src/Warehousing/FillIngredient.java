package Warehousing;

import Interfaces.Filling;
import Production.Distillate;

import java.time.LocalDate;

public class FillIngredient implements Filling {
    private final LocalDate date;
    private final double quantity;
    private final Distillate distillate;
    private final Ingredient ingredient;

    public FillIngredient(LocalDate date, double quantity, Distillate distillate, Ingredient ingredient) {
        this.date = date;
        this.quantity = quantity;
        this.distillate = distillate;
        this.ingredient = ingredient;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public double getQuantity() {
        return quantity;
    }

    public Distillate getDistillate() {
        return distillate;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }
}
