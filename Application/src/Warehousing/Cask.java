package Warehousing;

import Common.CommonMethods;
import Enumerations.FillType;
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

public int getMaturityMonths() {
    List<Filling> fillings = getFillingStack();

    try {
        if (fillings.isEmpty()) throw new IllegalArgumentException("No fillings available");

        fillings.removeIf(filling -> ((FillDistillate) filling).getLifeCycle() != lifeCycle);

        fillings.sort((f1, f2) ->
                ((FillDistillate) f1).getDistillate().getEndDate().compareTo(((FillDistillate) f2).getDistillate().getEndDate()));

        LocalDate lastEndDate = ((FillDistillate) fillings.get(fillings.size() - 1)).getDistillate().getEndDate();
        return (int) ChronoUnit.MONTHS.between(lastEndDate, LocalDate.now());
    } catch (Exception e) {
        System.err.println("Error calculating maturity months: " + e.getMessage());
        return 0;
    }
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
		Set<Item> tranferCasks = getCasksAddedByTransfer(lifeCycle);
		StringBuilder sbCask = new StringBuilder();
		for (Item i : tranferCasks){
			sbCask.append(String.format("""
					ID: %s | Supplier: %s
					""", i.getName(), ((Cask)i).getSupplier().getName()));
		}

		List<Filling> fillings = getFillingsStackHavingLifeCycleGroupedByDistillate(lifeCycle,LocalDate.now());
		StringBuilder sbFill = new StringBuilder();

		for (Filling f : fillings){
			Distillate distillate = ((FillDistillate)f).getDistillate();
			sbFill.append(String.format("""
					ID: %-20s | Name: %-20s | Quantity: %,-6.2f
					""",distillate.getNewMakeID(),distillate.getName(), getQuantityStatusByDistillate(distillate,lifeCycle)));
		}

		return String.format("""
				*****\t Supplier description \t *****
				%s
				
				****\t Cask life cycle \t *****
				Current life cycle: %d
				
				****\t Content cask(s) list \t *****
				%s
				****\t Distillate list \t *****
				%s
				Total quantity: %,-6.2f
				
				*****\t Filling details \t *****
				%s
				""", supplier.getDescription(), lifeCycle,sbCask.toString(),sbFill.toString(), getQuantityStatus() ,getFillingTextLines());
	}

	/**
	 * Get a start or end  filling date based on provided life cycle
	 *
	 * @param lifeCycle
	 * @param startDate
	 * @return
	 */
	public LocalDate getDateForLifeCycle(int lifeCycle, boolean startDate) throws IllegalArgumentException {
		if (lifeCycle < 1 || lifeCycle > this.lifeCycle) {
			throw new IllegalArgumentException("Provided life cycle is not valid");
		}

		List<Filling> fillings = getFillingStack();

		if (fillings.isEmpty()) throw new IllegalArgumentException("No fillings availble");

		fillings.forEach(filling -> {
			if (((FillDistillate) filling).getLifeCycle() != lifeCycle) {
				fillings.remove(filling);
			}
		});

		fillings.sort((f1, f2) ->
				f1.getDate().compareTo(f2.getDate()));

		if (startDate) {
			return ((FillDistillate) fillings.getFirst()).getDistillate().getEndDate();
		} else {
			return ((FillDistillate) fillings.getLast()).getDistillate().getEndDate();
		}
	}

	/**
	 * Will return lifecucle based on filling dates.
	 *
	 * @param date
	 * @return
	 */
	public int getLifeCycleByDate(LocalDate date) {
		List<Filling> fillings = getFillingStack();
		if (fillings.isEmpty()) throw new IllegalArgumentException("No fillings availble");

		fillings.sort((f1, f2) ->
				f1.getDate().compareTo(f2.getDate()));

		LocalDate firstDate = ((FillDistillate) fillings.getFirst()).getDistillate().getEndDate();
		LocalDate lastDate = ((FillDistillate) fillings.getLast()).getDistillate().getEndDate();

		if (date.isBefore(firstDate)) {
			throw new IllegalArgumentException("Provided date is before the first filling in this cask");
		}

		if (date.isAfter(lastDate)) {
			if (getQuantityStatus() != 0) {
				return lifeCycle;
			}
		}

		Filling foundFilling = null;

		for (int i = 1; i < fillings.size(); i++) {
			LocalDate beforeDate = fillings.get(i - 1).getDate();
			LocalDate thisDate = fillings.get(i).getDate();

			if (CommonMethods.isDateBetween(date, beforeDate, thisDate)) {
				foundFilling = fillings.get(i);
			}
		}

		return ((FillDistillate) foundFilling).getLifeCycle();
	}

	/**
	 * @param fillDistillate
	 * @return
	 * @throws IllegalStateException
	 */
	@Override
	public double updateQuantity(Filling fillDistillate) throws IllegalStateException {
		double newQuantity = fillDistillate.getQuantity() + getQuantityStatus();

		// Ensure that a new cask will have an empty date that fits the first filling.
		if (fillingStack.isEmpty()) {
			lifeCycle++;
		}

		if (newQuantity <= maxQuantity && newQuantity >= 0) {
			fillingStack.push(fillDistillate);
			notifyObservers();
			// This will start a new life cycle in the cask
			if (newQuantity == 0 && !((FillDistillate)fillDistillate).getFillType().equals(FillType.CASKHISTORY)) {
				lifeCycle++;
			}
			return newQuantity;
		} else {
			throw new IllegalArgumentException("Provided quantity does not fit into this cask");
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
		System.out.println("Reservation in batch" + batch);
    reservedBatchesAmount.put(batch, amount);
		System.out.println(reservedBatchesAmount);
}

/**
 * Spends a reservation for a specified batch with a given amount.
 * If the reserved amount becomes zero, the batch is completely removed from the reservations.
 *
 * @param batch the batch to spend the reservation from.
 * @param amount the amount to spend.
 */
public void spendReservation(Batch batch, double amount) {

		System.out.println(reservedBatchesAmount);
			
    double reservedAmount = reservedBatchesAmount.get(batch);
    if (reservedAmount - amount == 0) {
        reservedBatchesAmount.remove(batch);
    } else {
        reservedBatchesAmount.put(batch, reservedAmount - amount);
    }
}
	public double getQuantityStatusByDistillate(Distillate distillate, int lifeCycle){
		double quantity = 0;

		for (Filling f : getFillingStack()){
			if (((FillDistillate)f).getLifeCycle() == lifeCycle){
				if (((FillDistillate)f).getDistillate().equals(distillate)){
					quantity += f.getQuantity();
				}
			}
		}
		return quantity;
	}

	public List<Filling> getFillingStack() {
		List<Filling> fillings = new ArrayList<>();

		for (Filling f : fillingStack) {
			fillings.add(f);
		}

		return fillings;
	}


	public List<Filling> getFillingsStackByLifeCycle(int lifeCycle){
		List<Filling> fillings = new ArrayList<>();

		for (Filling f : getFillingStack()){
			FillDistillate filling = (FillDistillate) f;
			if (filling.getLifeCycle() == lifeCycle){
				fillings.add(f);
			}
		}

		return fillings;
	}

	/**
	 * Groups fillings by distillate. This function should not be used in regards to any transfer actions as the history
	 * chain might be broken.
	 * @param lifeCycle
	 * @param date
	 * @return
	 */
	public List<Filling> getFillingsStackHavingLifeCycleGroupedByDistillate(int lifeCycle, LocalDate date) {
		List<Filling> fillings = new ArrayList<>();
		Map<Distillate,Double> groupedFillings = new HashMap<>();

		for (Filling f : getFillingStack()){
			FillDistillate currentFilling = (FillDistillate) f;
			if (currentFilling.getLifeCycle() == lifeCycle){
				Distillate distillate = currentFilling.getDistillate();
				groupedFillings.merge(distillate,currentFilling.getQuantity(),Double::sum);
			}
		}

		for (Map.Entry<Distillate,Double> entry : groupedFillings.entrySet()){
			Distillate distillate = entry.getKey();
			double quantity = entry.getValue();
			Filling filling = new FillDistillate(date,quantity,this,distillate,null,
					false, FillType.FILLING);
			fillings.add(filling);
		}
		return fillings;
	}

	/**
	 * Returns alle fillings related to a given cask life cycle.
	 * @param LifeCycle
	 * @return
	 */
	public Set<Item> getCasksAddedByTransfer(int LifeCycle){
		Set<Item> casks = new HashSet<>();

		for (Filling f : getFillingStack()){
			if (((FillDistillate)f).getLifeCycle() == lifeCycle){
				FillDistillate filling = (FillDistillate) f;
				casks.add(filling.getPrevFillingsRecursive().getCask());
			}
		}
		return casks;
	}

	public void makeReservation(Batch batch, double amount) {
		reservedBatchesAmount.put(batch, amount);
	}

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
		return String.format("Life cycle: %-2d\t | ID: %-5d\t| Max capacity: %,.2f\t| Remaining capacity %,.2f", lifeCycle, caskID, maxQuantity,
				getRemainingQuantity());
	}

	public double getReservedAmountPerBatch(Batch batch){
		return reservedBatchesAmount.get(batch);
	}

	public TasteProfile getTasteProfile() {
		return this.tasteProfile;
	}

	public void setTasteProfile(TasteProfile tasteProfile) {
		this.tasteProfile = tasteProfile;
	}

	public int getCaskID() {
		return caskID;
	}

	
	public String getCaskIDString() {
		return String.valueOf(caskID);
	}

	public double getMaxQuantity() {
		return maxQuantity;
	}


	public Supplier getSupplier() {
		return supplier;
	}
}
