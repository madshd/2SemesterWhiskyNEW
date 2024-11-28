package Production;

import Enumerations.Unit;
import Interfaces.Item;
import Interfaces.OberverQuantitySubject;
import Interfaces.ObserverQuantityObserver;
import Interfaces.Stack;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Distillate implements Item, OberverQuantitySubject, Serializable {
    private final String name;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final double quantity;
    private final Distiller distiller;
    private final Unit unit;
    private String description;
    private final Stack<FillDistillate> fillingStack = new Common.Stack<>();
    private List<ObserverQuantityObserver> observers = new ArrayList<>();

    public Distillate(String name, LocalDate startDate, LocalDate endDate, double quantity, Distiller distiller, Unit unit) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quantity = quantity;
        this.distiller = distiller;
        this.unit = unit;
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
        for (ObserverQuantityObserver o : observers){
            o.update(this);
        }
    }

    @Override
    public double getRemainingQuantity() {
        return this.quantity - getQuantityStatus();
    }

    @Override
    public String getListInfo() {
        return String.format("Name: %-5s\t| Start capacity: %-4d\t| Remaining capacity %-4d"
                ,this.name,this.quantity,getRemainingQuantity());
    }

    @Override
    public double updateQuantity(FillDistillate fillDistillate) throws IllegalStateException{
        double newQuantity = getRemainingQuantity() - fillDistillate.getQuantity();

        if (newQuantity >= 0){
            fillingStack.push(fillDistillate);
            return newQuantity;
        }else {
            throw new IllegalStateException("Provided quantity does not fit this distillate");
        }
    }

    public double getQuantityStatus(){
        double quantity = 0;

        for (FillDistillate f : fillingStack){
            quantity += f.getQuantity();
        }

        return quantity;
    }
}
