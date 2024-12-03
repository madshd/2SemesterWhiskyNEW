package Production;

import Interfaces.Filling;
import Warehousing.Cask;

import java.time.LocalDate;

public class FillDistillate implements Filling, Comparable<FillDistillate>{
    private final LocalDate date;
    private final double quantity;
    private final Cask cask;
    private final Distillate distillate;
    private final FillDistillate prevDistillate;
    private final int lifeCycle;

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
        this.lifeCycle = (cask.getFillingStack().isEmpty()) ? cask.getLifeCycle() +1 : cask.getLifeCycle();
    }

    public LocalDate getDate() {
        return date;
    }

    public double getQuantity() {
        return quantity;
    }

    public Distillate getDistillate() {
        return distillate;
    }

    public FillDistillate getPrevDistillate() {
        return prevDistillate;
    }

    public int getLifeCycle() {
        return lifeCycle;
    }

    @Override
    public String toString() {
        return String.format("Cask life cycle: %d | Distillate: %s |%s | Quantity: %,.2f",lifeCycle,distillate.getName(), date.toString(),quantity);
    }

    @Override
    public int compareTo(FillDistillate o) {
        return this.date.compareTo(o.getDate());
    }
}
