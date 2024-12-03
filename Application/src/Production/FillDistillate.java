package Production;

import Interfaces.Filling;
import Warehousing.Cask;

import java.time.LocalDate;

public class FillDistillate implements Filling {
    private final LocalDate date;
    private final double quantity;
    private final Cask cask;
    private final Distillate distillate;
    private final FillDistillate prevDistillate;

    /**
     *
     * @param date
     * @param quantity
     * @param cask
     * @param distillate
     * @param prevDistillate shall be set to null if filling is not related to a transfer.
     */
    public FillDistillate(LocalDate date, double quantity, Cask cask, Distillate distillate, FillDistillate prevDistillate) {
        this.date = date;
        this.quantity = quantity;
        this.cask = cask;
        this.distillate = distillate;
        this.prevDistillate = prevDistillate;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return String.format("Distillate: %s |%s | Quantity: %,.2f",distillate.getName(), date.toString(),quantity);
    }
}
