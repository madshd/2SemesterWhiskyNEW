package Warehousing;

import Enumerations.Unit;
import Interfaces.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import BatchArea.TasteProfile;

public class Cask implements OberverQuantitySubject, Item, Serializable {
	private final int caskID;
	private final double maxQuantity;
	private final Unit unit;
	private final Supplier supplier;
	private final Stack<Filling> fillingStack = new Common.Stack<>();
	private final List<ObserverQuantityObserver> observers = new ArrayList<>();
	private TasteProfile tasteProfile = null;

	// Nullable
	private StorageRack storageRack;

	public Cask(int caskID, double maxQuantity, Unit unit, Supplier supplier) {
		this.caskID = caskID;
		this.maxQuantity = maxQuantity;
		this.unit = unit;
		this.supplier = supplier;
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
	public String getListInfo() {
		return String.format("ID: %-5d\t| Max capacity: %,.2f\t| Remaining capacity %,.2f", caskID, maxQuantity,
				getRemainingQuantity());
	}

	@Override
	public double updateQuantity(Filling fillDistillate) throws IllegalStateException {
		double newQuantity = fillDistillate.getQuantity() + getQuantityStatus();

		if (newQuantity <= maxQuantity && newQuantity >= 0) {
			fillingStack.push(fillDistillate);
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

	public String getFillingTextLines() {
		StringBuilder sb = new StringBuilder();
		for (Filling f : fillingStack) {
			sb.append(String.format("%s\n", f.toString()));
		}
		return sb.toString();
	}

	public TasteProfile getTasteProfile() {
		return this.tasteProfile;
	}

	public void setTasteProfile(TasteProfile tasteProfile) {
		this.tasteProfile = tasteProfile;
	}

}
