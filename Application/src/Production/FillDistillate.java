package Production;

import Enumerations.FillType;
import Interfaces.Filling;
import Warehousing.Cask;

import java.time.LocalDate;

public class FillDistillate implements Filling, Comparable<FillDistillate>{
    private final LocalDate date;
    private double quantity;
    private final Cask cask;
    private final Distillate distillate;
    private final FillDistillate prevDistillate;
    private final int lifeCycle;
    private final boolean decrease; // If true then quantity is negative.
    private final FillType fillType;

    /**
     *
     * @param date
     * @param quantity
     * @param cask
     * @param distillate
     * @param prevDistillate shall be set to null if filling is not related to a transfer.
     */
    public FillDistillate(LocalDate date, double quantity, Cask cask, Distillate distillate,
                          FillDistillate prevDistillate, boolean decrease, FillType fillType) {
        this.date = date;
        this.quantity = quantity;
        this.cask = cask;
        this.distillate = distillate;
        this.prevDistillate = prevDistillate;
        this.lifeCycle = (cask.getFillingStack().isEmpty()) ? cask.getLifeCycle() +1 : cask.getLifeCycle();
        this.decrease = decrease;
        this.fillType = fillType;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getQuantity() {
        return (decrease) ? quantity * -1 : quantity;
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

    public boolean isDecrease() {
        return decrease;
    }

    public FillType getFillType() {
        return fillType;
    }

    public Cask getCask() {
        return cask;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public FillDistillate getPrevFillingsRecursive(){
        if (prevDistillate == null) return this;

        return  prevDistillate.getPrevFillingsRecursive();
    }

    @Override
    public String toString() {
        return String.format("%-8s | Life cycle: %-2d | %-15s | %-12s | Quantity: %,-6.2f",
                fillType,lifeCycle,distillate.getName(), date.toString(),getQuantity());
    }

    @Override
    public int compareTo(FillDistillate o) {
        return this.date.compareTo(o.getDate());
    }
}
