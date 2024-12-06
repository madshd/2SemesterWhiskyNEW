package Warehousing;

import Enumerations.IngredientType;
import Enumerations.Unit;
import Interfaces.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Ingredient implements OberverQuantitySubject, Item, Serializable, WarehousingSubject {
	private final String name;
	private String description;
	private final int batchNo;
	private final LocalDate productionDate;
	private final LocalDate expirationDate;
	private double quantity;
	private Supplier supplier;
	private IngredientType ingredientType;
	private final Unit unit;
	private final List<Filling> fillIngredients = new ArrayList<>();
	private final List<ObserverQuantityObserver> observers = new ArrayList<>();

	// Nullable
	private StorageRack storageRack;

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

	public StorageRack getStorageRack() {
		return storageRack;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setStorageRack(StorageRack storageRack) {
		this.storageRack = storageRack;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
		this.quantityChanged();
	}

	public void quantityChanged() {

	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public IngredientType getIngredientType() {
		return ingredientType;
	}

	public void measurementsChanged() {
		this.notifyObservers();
	}

	public void setIngredientType(IngredientType ingredientType) {
		this.ingredientType = ingredientType;
	}

	@Override
	public double getQuantityStatus() {
		double quantity = 0;

		for (Filling f : fillIngredients) {
			quantity += (((FillIngredient) f).isDecrease()) ? f.getQuantity() * -1 : f.getQuantity();
		}

		return quantity;
	}

	@Override
	public double updateQuantity(Filling fillingredient) throws IllegalStateException {
		double fill = (((FillIngredient) fillingredient).isDecrease()) ? fillingredient.getQuantity() * -1
				: fillingredient.getQuantity();
		double newQuantity = fill + getRemainingQuantity();

		if (newQuantity <= quantity && newQuantity >= 0) {
			fillIngredients.add(fillingredient);
			return newQuantity;
		} else {
			throw new IllegalStateException("Provided quantity exceeds what is present.");
		}
	}

	public boolean removeFilling(Filling filling){
		return fillIngredients.remove(filling);
	}

	@Override
	public double getRemainingQuantity() {
		return quantity + getQuantityStatus();
	}

	public List<Filling> getfillIngredients(){
		return new ArrayList<>(fillIngredients);
	}

	@Override
	public int compareTo(Item o) {
		return this.name.compareTo(o.getName());
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
	public void registerWarehousingObserver(WarehousingObserver o) {

	}

	@Override
	public void removeWarehousingObserver(WarehousingObserver o) {

	}

	@Override
	public void notifyWarehousingObservers() {

	}

	@Override
	public String toString() {
		return name + " " + description +
				" " + getRemainingQuantity() + " " + unit;
	}

	public String getListInfo() {
		int maxNameLength = 27;
		String subName = supplier.getName();

		String listName = (name.trim().length() > maxNameLength) ? name.substring(0,maxNameLength - 3) + "..." :
				name.trim() + " ".repeat(maxNameLength - name.trim().length());

		String listSub = (subName.trim().length() > maxNameLength) ? subName.substring(0,maxNameLength - 3) + "..." :
				subName.trim() + " ".repeat(maxNameLength - subName.trim().length());

		return String.format("#: %-4d | %s | %s | %s",
				batchNo, listName, listSub, ingredientType);
	}
}
