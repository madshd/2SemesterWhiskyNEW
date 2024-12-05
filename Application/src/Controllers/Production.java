package Controllers;

import Enumerations.Unit;
import Interfaces.Filling;
import Interfaces.Item;
import Interfaces.StorageInterface;
import Production.Distillate;
import Production.Distiller;
import Production.FillDistillate;
import Production.StoryLine;
import Production.ProductCutInformation;
import Production.AlcoholPercentage;
import Storage.Storage;
import Warehousing.Cask;
import Warehousing.Ingredient;
import Warehousing.FillIngredient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Methods that is mainly used within the production area
 */
public abstract class Production {
	private static StorageInterface storage;

	public static void setStorage(StorageInterface storage){
		Production.storage = storage;
	}

	/**
	 *
	 * @param name
	 * @param startDate
	 * @param endDate
	 * @param quantity
	 * @param distiller
	 * @param unit
	 * @return
	 */
	public static Distillate createDistillate(String name, LocalDate startDate, LocalDate endDate, double quantity,
									   Distiller distiller, Unit unit){
		Distillate distillate = new Distillate(name,startDate,endDate,quantity,distiller,unit);
		storage.storeDistillate(distillate);
		return distillate;
	}

	public static void addDescriotionToDistillate(Distillate distillate, String description){
		distillate.setDescription(description);
	}

	public static void addStoryToDestillate(Distillate distillate, String story, LocalDate date){
		StoryLine storyLine = new StoryLine(date,story);
		distillate.addStoryLine(storyLine);
	}

	/**
	 *
	 * @param name
	 * @param initials
	 * @param story
	 * @return
	 */
	public static Distiller createDistiller(String name, String initials, String story){
		Distiller distiller = new Distiller(name,initials,story);
		storage.storeDistiller(distiller);
		return distiller;
	}

	/**
	 * Will increase quantity on distillate and decrease quantity on ingrediant.
	 * @param distillate
	 * @param ingredient
	 * @param quantity
	 * @param date
	 */
	public static void addIngredientToDistillate(Distillate distillate, Ingredient ingredient, double quantity, LocalDate date){
		Filling fillingIncrease = new FillIngredient(date,quantity,distillate,ingredient,false);
		Filling fillingDecrease = new FillIngredient(date,quantity,distillate,ingredient,true);

		distillate.addIngredientFilling(fillingIncrease);
		ingredient.updateQuantity(fillingDecrease);
	}

	public static List<Distiller> getDistillers(){
		return storage.getDistillers();
	}

	/**
	 *
	 * @param distillate
	 * @param cask
	 * @param quantity
	 * @param date
	 * @return
	 */
	public static double fillDistillateIntoCask(Distillate distillate, Cask cask, double quantity ,LocalDate date){
		Filling filling = new FillDistillate(date,quantity,cask,distillate,null);
		distillate.updateQuantity(filling);
		return cask.updateQuantity(filling);
	}

	public static List<Item> getDistillates(){
		List<Item> distillates = new ArrayList<>();

		for (Item f : storage.getDistillates()){
			if (f.getRemainingQuantity() > 0){
				distillates.add(f);
			}
		}

		Collections.sort(distillates);
		return distillates;
	}

	/**
	 * Will return story based on storylines and fillings.
	 * @param cask
	 * @param date
	 * @return
	 */
	public static String getCaskStory(Cask cask, LocalDate date){
		//TODO by leander

		return "To be implemented";
	}

	/**
	 * Used for bottling and updateing quantity after measuring.
	 * @param cask
	 * @param Quantity
	 * @param date
	 */
	public static void caskBottling(Cask cask, double quantity, LocalDate date){
		//TODO by leander
		double caskQauntity = cask.getQuantityStatus();
		int caskLifeCycle = cask.getLifeCycle();
		double calculationFactor = quantity/caskQauntity;

		if (quantity > caskQauntity) throw new IllegalArgumentException("Bottling quantity is higher than cask quantity");

		List<Filling> fillings = cask.getFillingsStackByLifeCycle(caskLifeCycle);

		for (Filling f : fillings){
			Distillate distillate = ((FillDistillate) f).getDistillate();
			double decrease = f.getQuantity() * calculationFactor * -1;
			Filling newFilling = new FillDistillate(date,decrease,cask,distillate,null);
			cask.updateQuantity(newFilling);
		}

	}
}
