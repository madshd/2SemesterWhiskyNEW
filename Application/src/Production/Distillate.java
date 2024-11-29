package Production;

import Enumerations.Unit;
import Interfaces.*;

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
	private final Stack<Filling> fillingStack = new Common.Stack<>();
	private final List<ObserverQuantityObserver> observers = new ArrayList<>();
	private final List<StoryLine> storyLines = new ArrayList<>();
	private final List<ProductCutInformation> productCutInformations = new ArrayList<>();
	private final List<AlcoholPercentage> alcoholPercentages = new ArrayList<>();

	public Distillate(String name, LocalDate startDate, LocalDate endDate, double quantity, Distiller distiller,
			Unit unit) {
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
		for (ObserverQuantityObserver o : observers) {
			o.update(this);
		}
	}

	@Override
	public double getRemainingQuantity() {
		return this.quantity - getQuantityStatus();
	}

	@Override
	public int compareTo(Item o) {
		return this.name.compareTo(o.getName());
	}

	@Override
	public String toString() {
		return String.format("Name:\t %s \t | \t Start capacity:\t %,-6.2f \t | \t Remaining capacity: \t %,.2f", name,
				this.quantity, getRemainingQuantity());
	}

	public String getListInfo() {
		int maxNameLength = 20;
		String listName = (name.trim().length() > maxNameLength) ? name.substring(0,maxNameLength - 3) + "..." :
				name.trim() + " ".repeat(maxNameLength - name.trim().length());
		return String.format("Name: %s \t | \t Start capacity: %,-4.2f \t | \t Remaining capacity: %,-4.2f", listName,
				this.quantity, getRemainingQuantity());
	}

	/**
	 *
	 * @param fillDistillate
	 * @return
	 * @throws IllegalStateException
	 */
	@Override
	public double updateQuantity(Filling fillDistillate) throws IllegalStateException {
		double newQuantity = getRemainingQuantity() - fillDistillate.getQuantity();

		if (newQuantity >= 0) {
			fillingStack.push(fillDistillate);
			return newQuantity;
		} else {
			throw new IllegalStateException("Provided quantity does not fit this distillate");
		}
	}

	public double getQuantityStatus() {
		double quantity = 0;

		for (

		Filling f : fillingStack) {
			quantity += f.getQuantity();
		}

		return quantity;
	}

	public void addStoryLine(StoryLine storyLine) {
		storyLines.add(storyLine);
	}

	public void addProductCutInformation(ProductCutInformation productCutInformation) {
		productCutInformations.add(productCutInformation);
	}

	public void addAlcoholPercentage(AlcoholPercentage alcoholPercentage) {
		alcoholPercentages.add(alcoholPercentage);
	}

	public List<StoryLine> getStoryLines() {
		return new ArrayList<>(storyLines);
	}

	public List<ProductCutInformation> getProductCutInformations() {
		return new ArrayList<>(productCutInformations);
	}

	public List<AlcoholPercentage> getAlcoholPercentages() {
		return new ArrayList<>(alcoholPercentages);
	}

	public String getName() {
		return name;
	}
}
