package Production;

import Enumerations.Unit;
import Interfaces.*;
import Interfaces.Stack;
import Warehousing.FillIngredient;
import Warehousing.Ingredient;
import Warehousing.StorageRack;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Distillate implements Item, OberverQuantitySubject, Serializable {
	private String name;
	private LocalDate startDate;
	private LocalDate endDate;
	private double quantity;
	private Distiller distiller;
	private final Unit unit;
	private String description = "";
	private final Stack<Filling> fillingStack = new Common.Stack<>();
	private final Set<ObserverQuantityObserver> observers = new HashSet<>();
	private final List<StoryLine> storyLines = new ArrayList<>();
	private final List<ProductCutInformation> productCutInformations = new ArrayList<>();
	private final List<AlcoholPercentage> alcoholPercentages = new ArrayList<>();
	private final List<Filling> fillIngredients = new ArrayList<>();

	public Distillate(String name, LocalDate startDate, LocalDate endDate, double quantity, Distiller distiller,
			Unit unit) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.quantity = quantity;
		this.distiller = distiller;
		this.unit = unit;
	}

	// Functions regarding filling type distillate

	/**
	 *
	 * @param fillDistillate
	 * @return
	 * @throws IllegalStateException
	 */
	@Override
	public double updateQuantity(Filling fillDistillate) throws IllegalStateException {
		double newQuantity = getRemainingQuantity() + fillDistillate.getQuantity();

		if (newQuantity >= 0) {
			fillingStack.push(fillDistillate);
			notifyObservers();
			return newQuantity;
		} else {
			throw new IllegalStateException("Provided quantity does not fit this distillate");
		}
	}

	public double getQuantityStatus() {
		double quantity = 0;

		for (Filling f : fillingStack) {
			quantity += f.getQuantity();
		}
		return quantity;
	}

	@Override
	public double getRemainingQuantity() {
		return this.quantity + getQuantityStatus();
	}

	@Override
	public String toString() {
		return String.format("Name:\t %s \t | \t Start capacity:\t %,-6.2f \t | \t Remaining capacity: \t %,.2f", name,
				this.quantity, getRemainingQuantity());
	}

	/**
	 * Used for presenting info in listviews
	 * @return
	 */
	public String getListInfo() {
		int maxNameLength = 20;
		String listName = (name.trim().length() > maxNameLength) ? name.substring(0,maxNameLength - 3) + "..." :
				name.trim() + " ".repeat(maxNameLength - name.trim().length());
		return String.format("Name: %s \t | \t Start capacity: %,-4.2f \t | \t Remaining capacity: %,-4.2f", listName,
				this.quantity, getRemainingQuantity());
	}


	// Functions regarding filling type ingredient
	public void addIngredientFilling(Filling filling){
		notifyObservers();
		fillIngredients.add(filling);
	}

	public boolean removeIngredientFilling(Filling filling){
		notifyObservers();
		return fillIngredients.remove(filling);
	}

	public List<Item> getIngredientsInDistillate(){
		List<Item> items = new ArrayList<>();

		for (Filling f :fillIngredients){
			Item item = ((FillIngredient)f).getIngredient();

			if (!items.contains(item)){
				items.add(item);
			}
		}

		return items;
	}

	public String getInfoOnIngredientUsed(Ingredient ingredient){
		return String.format("""
				*****\t Production details\t *****
				Date added: %s
				Total quantity: %,-4.2f
				
				*****\t Ingredient description\t *****
				%s
				""",getIngredientAddDates(ingredient), getIngredientUsedQuantity(ingredient),ingredient.getDescription());
	}

	public double getIngredientUsedQuantity(Ingredient ingredient){
		double quantity = 0;

		for (Filling f : fillIngredients){
			FillIngredient filling = ((FillIngredient)f);

			if (filling.getIngredient().equals(ingredient)){
				quantity += filling.getQuantity();
			}
		}
		return quantity;
	}

	public String getIngredientAddDates(Ingredient ingredient){
		StringBuilder sb = new StringBuilder();

		for (Filling f : fillIngredients){
			FillIngredient filling = ((FillIngredient)f);

			if (filling.getIngredient().equals(ingredient)){
				sb.append(String.format("%s\n",filling.getDate().toString()));
			}
		}
		return sb.toString();
	}

	// Simple getter/setter functions
	@Override
	public int compareTo(Item o) {
		return this.name.compareTo(o.getName());
	}

	public List<Filling> getFillIngredients(){
		return new ArrayList<>(fillIngredients);
	}

	public void addStoryLine(StoryLine storyLine) {
		notifyObservers();
		storyLines.add(storyLine);
	}

	public boolean removeStoryLine(StoryLine storyLine){
		notifyObservers();
		return storyLines.remove(storyLine);
	}

	public void addProductCutInformation(ProductCutInformation productCutInformation) {
		notifyObservers();
		productCutInformations.add(productCutInformation);
	}

	public void addAlcoholPercentage(AlcoholPercentage alcoholPercentage) {
		notifyObservers();
		alcoholPercentages.add(alcoholPercentage);
	}

	public boolean removeAlcoholPercentage(AlcoholPercentage alcoholPercentage){
		notifyObservers();
		return alcoholPercentages.remove(alcoholPercentage);
	}

	public List<StoryLine> getStoryLines() {
		return new ArrayList<>(storyLines);
	}

	public List<ProductCutInformation> getProductCutInformations() {
		return new ArrayList<>(productCutInformations);
	}

	public boolean removeProductionCutInformation(ProductCutInformation productCutInformation){
		notifyObservers();
		return productCutInformations.remove(productCutInformation);
	}

	public List<AlcoholPercentage> getAlcoholPercentages() {
		return new ArrayList<>(alcoholPercentages);
	}

	public String getName() {
		return name;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public Distiller getDistiller() {
		return distiller;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNewMakeID(){
		return String.format("SWD-Y%dM%dD%d%s", startDate.getYear(), startDate.getMonthValue(),
				startDate.getDayOfYear(), distiller.getInitials());
	}

	public void setName(String name) {
		this.name = name;
		notifyObservers();
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
		notifyObservers();
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
		notifyObservers();
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
		notifyObservers();
	}

	public void setDistiller(Distiller distiller) {
		this.distiller = distiller;
		notifyObservers();
	}

	public double getQuantity() {
		return quantity;
	}

	// Functions regarding observer pattern

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


	// Future possibility to add a StorageRack association to a Distillate
	@Override
	public void setStorageRack(StorageRack storageRack) {

	}
}
