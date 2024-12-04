package Warehousing;

import Common.CommonMethods;
import Enumerations.Unit;
import Interfaces.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import BatchArea.Batch;

import BatchArea.TasteProfile;
import Interfaces.Stack;
import Production.FillDistillate;

public class Cask implements OberverQuantitySubject, Item, Serializable {
	private final int caskID;
	private final double maxQuantity;
	private final String caskType;
	private final Unit unit;
	private final HashMap<Batch, Double> reservedBatchesAmount = new HashMap<>();
	private final Set<ObserverQuantityObserver> observers = new HashSet<>();
	private final Stack<Filling> fillingStack = new Common.Stack<>();
	private int lifeCycle = 0;
	private final Supplier supplier;
	// Nullable
	private TasteProfile tasteProfile = null;

	// Nullable
	private StorageRack storageRack;

	public Cask(int caskID, double maxQuantity, Unit unit, Supplier supplier, String caskType) {
		this.caskType = caskType;
		this.caskID = caskID;
		this.maxQuantity = maxQuantity;
		this.unit = unit;
		this.supplier = supplier;
	}

	public Unit getUnit() {
		return unit;
	}

	/**
	 * Will return count of month from the latest end date to LocalDate.now from a distillate in curent lifecycle.
	 * @return
	 */
	public int getMaturityMonths() {
		List<Filling> fillings =  getFillingStack();

		if (fillings.isEmpty()) throw new IllegalArgumentException("No fillings availble");

		fillings.forEach(filling -> {
			if (((FillDistillate) filling).getLifeCycle() != lifeCycle){
				fillings.remove(filling);
			}
		});

		fillings.sort((f1, f2) ->
				((FillDistillate) f1).getDistillate().getEndDate().compareTo(((FillDistillate) f2).getDistillate().getEndDate()));

		LocalDate lastEndDate = ((FillDistillate) fillings.getLast()).getDistillate().getEndDate();
		return (int) ChronoUnit.MONTHS.between(lastEndDate, LocalDate.now());
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

	public int getLifeCycle() {
		return lifeCycle;
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
				
				****\t Cask life cycle\t *****
				Current life cycle: %d
				
				*****\t Filling details\t *****
				%s
				""", supplier.getDescription(), lifeCycle, getFillingTextLines());
	}

	/**
	 * Get a start or end  filling date based on provided life cycle
	 * @param lifeCycle
	 * @param startDate
	 * @return
	 */
	public LocalDate getDateForLifeCycle(int lifeCycle, boolean startDate) throws IllegalArgumentException{
		if (lifeCycle < 1 || lifeCycle > this.lifeCycle){
			throw new IllegalArgumentException("Provided life cycle is not valid");
		}

		List<Filling> fillings =  getFillingStack();

		if (fillings.isEmpty()) throw new IllegalArgumentException("No fillings availble");

		fillings.forEach(filling -> {
			if (((FillDistillate) filling).getLifeCycle() != lifeCycle){
				fillings.remove(filling);
			}
		});

		fillings.sort((f1, f2) ->
				f1.getDate().compareTo(f2.getDate()));

		if (startDate){
			return ((FillDistillate) fillings.getFirst()).getDistillate().getEndDate();
		}else {
			return ((FillDistillate) fillings.getLast()).getDistillate().getEndDate();
		}
	}

	/**
	 * Will return lifecucle based on filling dates.
	 * @param date
	 * @return
	 */
	public int getLifeCycleByDate(LocalDate date){
		List<Filling> fillings =  getFillingStack();
		if (fillings.isEmpty()) throw new IllegalArgumentException("No fillings availble");

		fillings.sort((f1, f2) ->
				f1.getDate().compareTo(f2.getDate()));

		LocalDate firstDate = ((FillDistillate) fillings.getFirst()).getDistillate().getEndDate();
		LocalDate lastDate = ((FillDistillate) fillings.getLast()).getDistillate().getEndDate();

		if (date.isBefore(firstDate)){
			throw new IllegalArgumentException("Provided date is before the first filling in this cask");
		}

		if (date.isAfter(lastDate)){
			if (getQuantityStatus() != 0){
				return lifeCycle;
			}
		}

		Filling foundFilling = null;

		for (int i = 1; i < fillings.size(); i++) {
			LocalDate beforeDate = fillings.get(i-1).getDate();
			LocalDate thisDate = fillings.get(i).getDate();

			if (CommonMethods.isDateBetween(date,beforeDate,thisDate)){
				foundFilling = fillings.get(i);
			}
		}

		return ((FillDistillate) foundFilling).getLifeCycle();
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

		// Ensure that a new cask will an empty date that fits the first filling.
		if(fillingStack.isEmpty()){
			lifeCycle++;
		}

		if (newQuantity <= maxQuantity && newQuantity >= 0) {
			fillingStack.push(fillDistillate);
			notifyObservers();
			// This will start a new life cycle in the cask
			if (newQuantity == 0){
				lifeCycle++;
			}
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

/**
 * Calculates the total reserved amount from all batches.
 *
 * @return the total reserved amount.
 */
public double getTotalReservedAmount() {
    double totalReservedAmount = 0;
    for (double reservedAmount : reservedBatchesAmount.values()) {
        totalReservedAmount += reservedAmount;
    }
    return totalReservedAmount;
}

/**
 * Calculates the legal quantity by subtracting the total reserved amount from the quantity status.
 *
 * @return the legal quantity.
 */
public double getLegalQuantity() {
    return getQuantityStatus() - getTotalReservedAmount();
}

/**
 * Makes a reservation for a specified batch with a given amount.
 *
 * @param batch the batch to reserve.
 * @param amount the amount to reserve.
 */
public void makeReservation(Batch batch, double amount) {
    reservedBatchesAmount.put(batch, amount);
}

/**
 * Spends a reservation for a specified batch with a given amount.
 * If the reserved amount becomes zero, the batch is completely removed from the reservations.
 *
 * @param batch the batch to spend the reservation from.
 * @param amount the amount to spend.
 */
public void spendReservation(Batch batch, double amount) {
    double reservedAmount = reservedBatchesAmount.get(batch);
    if (reservedAmount - amount == 0) {
        reservedBatchesAmount.remove(batch);
    } else {
        reservedBatchesAmount.put(batch, reservedAmount - amount);
    }
}

	@Override
	public int compareTo(Item o) {
		return this.getName().compareTo(o.getName());
	}

	@Override
	public String toString() {
		return String.format("Life cycle: %-2d\t | ID: %-5d\t| Max capacity: %,.2f\t| Remaining capacity %,.2f",lifeCycle ,caskID, maxQuantity,
				getRemainingQuantity());
	}

	public String getListInfoReservation(Batch batch){
		StringBuilder sb = new StringBuilder();
		sb.append("CaskID: " + this.caskID + " --- ");
		sb.append();


		return sb.toString();
	}

	public TasteProfile getTasteProfile() {
		return this.tasteProfile;
	}

	public void setTasteProfile(TasteProfile tasteProfile) {
		this.tasteProfile = tasteProfile;
	}

	public List<Filling> getFillingStack(){
		List<Filling> fillings = new ArrayList<>();

		for (Filling f : fillingStack){
			fillings.add(f);
		}

		return fillings;
	}
}
