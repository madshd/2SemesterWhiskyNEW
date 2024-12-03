package Warehousing;

import Enumerations.Unit;
import Interfaces.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.List;
import BatchArea.Batch;

import BatchArea.TasteProfile;

public class Cask implements OberverQuantitySubject, Item, Serializable {
	private final int caskID;
	private final double maxQuantity;
	private final String caskType;
	private final Unit unit;
	private int maturityMonths;
	private final HashMap<Batch, Double> reservedBatchesAmount = new HashMap<>();
	private final List<ObserverQuantityObserver> observers = new ArrayList<>();
	private final Stack<Filling> fillingStack = new Common.Stack<>();
	private final ArrayList<LocalDate> emptyDates = new ArrayList<>();
	private final Supplier supplier;
	private TasteProfile tasteProfile = null;

	// Nullable
	private StorageRack storageRack;

	public Cask(int caskID, double maxQuantity, Unit unit, Supplier supplier, String caskType) {
		this.caskType = caskType;
		this.caskID = caskID;
		this.maxQuantity = maxQuantity;
		this.unit = unit;
		this.supplier = supplier;
		emptyDates.add(LocalDate.now());
		maturityMonths = 0;
	}

	public Unit getUnit() {
		return unit;
	}

	public int getMaturityMonths() {
		return maturityMonths;
	}

	public String getCaskType() {
		return caskType;
	}

	public StorageRack getStorageRack() {
		return storageRack;
	}

	public void setStorageRack(StorageRack storageRack) {
		this.storageRack = storageRack;
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
	public int compareTo(Item o) {
		return this.getName().compareTo(o.getName());
	}

	@Override
	public String toString() {
		return String.format("ID: %-5d\t| Max capacity: %,.2f\t| Remaining capacity %,.2f", caskID, maxQuantity,
				getRemainingQuantity());
	}

	public String getListInfo() {
		int maxNameLength = 15;
		String supplierName = supplier.getName();
		String listName = (supplierName.trim().length() > maxNameLength)
				? supplierName.substring(0, maxNameLength - 3) + "..."
				: supplierName.trim() + " ".repeat(maxNameLength - supplierName.trim().length());

		return String.format(
				"ID: %-4d | \t Supplier: %s \t | \t  Max capacity: %,-4.2f\t | \tRemaining capacity %,-4.2f", caskID,
				listName, maxQuantity, getRemainingQuantity());
	}

	/**
	 *
	 * @param fillDistillate
	 * @return
	 * @throws IllegalStateException
	 */
	@Override
	public double updateQuantity(Filling fillDistillate) throws IllegalStateException {
		double newQuantity = fillDistillate.getQuantity() + getQuantityStatus();

		if (newQuantity <= maxQuantity && newQuantity >= 0) {
			fillingStack.push(fillDistillate);
			notifyObservers();
			return newQuantity;
		} else {
			throw new IllegalStateException("Provided quantity does not fit into this cask");
		}
	}

	public double getQuantityStatus() {
		double quantity = 0;

		for (Filling f : fillingStack) {
			quantity += f.getQuantity();
		}

		return quantity;
	}

	public TasteProfile getTasteProfile() {
		return this.tasteProfile;
	}

	public void setTasteProfile(TasteProfile tasteProfile) {
		this.tasteProfile = tasteProfile;
	}

	public String getFillingTextLines() {
		StringBuilder sb = new StringBuilder();
		for (Filling f : fillingStack) {
			sb.append(String.format("%s\n", f.toString()));
		}
		return sb.toString();
	}

	public String getName() {
		return Integer.toString(caskID);
	}

	public String getDetails() {
		return String.format("""
				*****\t Supplier description\t *****
				%s

				*****\t Filling details\t *****
				%s
				""", supplier.getDescription(), getFillingTextLines());
	}

	public List<Filling> getFillingStack(){
		List<Filling> fillings = new ArrayList<>();

		for (Filling f : fillingStack){
			fillings.add(f);
		}

		return fillings;
	}
	// FAKE METHOD FOR INJECTING FAKE DATA
	public double getFakeQuantity() {
		return 1000;
	}

}
