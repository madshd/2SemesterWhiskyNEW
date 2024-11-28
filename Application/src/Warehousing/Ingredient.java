package Warehousing;

import Enumerations.IngredientType;
import Enumerations.Unit;
import Interfaces.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Ingredient implements OberverQuantitySubject, Item, Serializable, ObservableItem {
    private final String name;
    private String description;
    private final int batchNo;
    private final LocalDate productionDate;
    private final LocalDate expirationDate;
    private double quantity;
    private Supplier supplier;
    private IngredientType ingredientType;
    private final Unit unit;
    private final Stack<Filling> fillingStack = new Common.Stack<>();
    private final List<ObserverQuantityObserver> observers = new ArrayList<>();

    //Nullable
    private StorageRack storageRack;
    private List<Observer> itemObservers;

    public Ingredient(String name, String description, int batchNo, LocalDate productionDate, LocalDate expirationDate,
                      double quantity, Supplier supplier, Unit unit, IngredientType ingredientType) {
        this.name = name;
        this.description = description;
        this.batchNo = batchNo;
        this.productionDate = productionDate;
        this.expirationDate = expirationDate;
        this.unit = unit;
        this.quantity = quantity;
        this.supplier = supplier;
        this.ingredientType = ingredientType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBatchNo() {
        return batchNo;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }
    public StorageRack getStorageRack () {
        return storageRack;
    }

    public LocalDate getExpirationDate () {
        return expirationDate;
    }
    public void setStorageRack (StorageRack storageRack){
        this.storageRack = storageRack;
    }

    public double getQuantity () {
        return quantity;
    }

    public void setQuantity ( double quantity){
        this.quantity = quantity;
        this.quantityChanged();
    }

    public void quantityChanged () {

    }
    public Supplier getSupplier () {
        return supplier;
    }

    public void setSupplier (Supplier supplier){
        this.supplier = supplier;
    }

    public IngredientType getIngredientType () {
        return ingredientType;
    }
    public void measurementsChanged () {
        this.notifyObservers();
    }

    public void setIngredientType (IngredientType ingredientType){
        this.ingredientType = ingredientType;
    }

    @Override
    public String getListInfo () {
        //TODO
        return "";
    }

    @Override
    public double getQuantityStatus () {
        double quantity = 0;

        for (Filling f : fillingStack) {
            quantity += f.getQuantity();
        }

        return quantity;
    }

    @Override
    public double updateQuantity (Filling fillingredient) throws IllegalStateException {
        double newQuantity = fillingredient.getQuantity() + getQuantityStatus();

        if (newQuantity <= quantity && newQuantity >= 0) {
            fillingStack.push(fillingredient);
            return newQuantity;
        } else {
            throw new IllegalStateException("Provided quantity does not fit into this cask");
        }
    }

    @Override
    public double getRemainingQuantity () {
        return quantity - getQuantityStatus();
    }

    @Override
    public void addObserver (ObserverQuantityObserver o){
        observers.add(o);
    }

    @Override
    public void removeObserver (ObserverQuantityObserver o){
        observers.remove(o);
    }

    @Override
    public void registerObserver(Observer o) {

    }

    @Override
    public void removeObserver(Observer o) {

    }

    @Override
        public void notifyObservers () {
            for (ObserverQuantityObserver o : observers)
                o.update(this);
        }
}
