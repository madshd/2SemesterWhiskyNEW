package Warehousing;

import Enumerations.Unit;
import Interfaces.*;
import Production.Supplier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cask implements OberverQuantitySubject, Item, Serializable {
    private final int caskID;
    private final double maxQuantity;
    private final Unit unit;
    private final Supplier supplier;
    private final Stack<Filling> fillingStack = new Common.Stack<>();
    private final List<ObserverQuantityObserver> observers = new ArrayList<>();

    public Cask(int caskID, double maxQuantity, Unit unit, Supplier supplier) {
        this.caskID = caskID;
        this.maxQuantity = maxQuantity;
        this.unit = unit;
        this.supplier = supplier;
    }

    @Override
    public void addObserver(ObserverQuantityObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(ObserverQuantityObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (ObserverQuantityObserver o : observers)
            o.update(this);
    }

    @Override
    public double getRemainingQuantity() {
        return maxQuantity - getQuantityStatus();
    }

    @Override
    public String getListInfo() {
        return String.format("ID: %-5d\t| Max capacity: %-4d\t| Remaining capacity %-4d"
        ,caskID,maxQuantity,getRemainingQuantity());
    }

    @Override
    public double updateQuantity(Filling fillDistillate) throws IllegalStateException {
        double newQuantity = fillDistillate.getQuantity() + getQuantityStatus();

        if (newQuantity <= maxQuantity && newQuantity >= 0){
            fillingStack.push(fillDistillate);
            return newQuantity;
        }else {
            throw new IllegalStateException("Provided quantity does not fit into this cask");
        }
    }

    public double getQuantityStatus(){
        double quantity = 0;

        for (Filling f : fillingStack){
            quantity += f.getQuantity();
        }

        return quantity;
    }
}
