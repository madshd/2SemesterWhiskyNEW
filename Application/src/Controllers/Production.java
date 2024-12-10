package Controllers;

import Enumerations.FillType;
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
import Warehousing.Cask;
import Warehousing.Ingredient;
import Warehousing.FillIngredient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

	public static void addStoryToDistillate(Distillate distillate, String story, LocalDate date){
		StoryLine storyLine = new StoryLine(date,story);
		distillate.addStoryLine(storyLine);
	}

	public static boolean removeStoryFromDistillate(Distillate distillate, StoryLine storyLine){
		return distillate.removeStoryLine(storyLine);
	}

	public static void addAlcoholPercentage(Distillate distillate, double percentage, LocalDate date){
		AlcoholPercentage alcoholPercentage = new AlcoholPercentage(date,percentage);
		distillate.addAlcoholPercentage(alcoholPercentage);
	}

	public static boolean removeAlcholPercentage(Distillate distillate, AlcoholPercentage alcoholPercentage){
		return distillate.removeAlcoholPercentage(alcoholPercentage);
	}

	public static void addProductionCutInformation(Distillate distillate, String info, LocalDate date){
		ProductCutInformation productCutInformation = new ProductCutInformation(date,info);
		distillate.addProductCutInformation(productCutInformation);
	}

	public static boolean removeProductionCutInformation(Distillate distillate, ProductCutInformation productCutInformation){
		return distillate.removeProductionCutInformation(productCutInformation);
	}

	public static boolean distillateUpdateBasicInfo(Distillate distillate, String name, Distiller distiller, double quantity, LocalDate startDate,
											 LocalDate endDate, String description){
		distillate.setName(name);
		distillate.setDistiller(distiller);
		distillate.setQuantity(quantity);
		distillate.setStartDate(startDate);
		distillate.setEndDate(endDate);
		distillate.setDescription(description);
		return true;
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

	public static boolean removeIngredientFromDistillate(Distillate distillate, Item ingredient){
		distillate.getFillIngredients().forEach(filling -> {
			Ingredient ing = ((FillIngredient) filling).getIngredient();

			if (ing.equals(ingredient)){
				distillate.removeIngredientFilling(filling);
			}
		});

		((Ingredient) ingredient).getfillIngredients().forEach(filling -> {
			Distillate dis = ((FillIngredient) filling).getDistillate();

			if (dis.equals(distillate)){
				((Ingredient) ingredient).removeFilling(filling);
			}
		});
		return true;
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
	public static double fillDistillateIntoCask(Distillate distillate, Cask cask, double quantity ,LocalDate date)
			throws IllegalStateException{
		Filling fillingInCrease = new FillDistillate(date,quantity,cask,distillate,null,
				false,FillType.FILLING);
		Filling fillingDeCrease = new FillDistillate(date,quantity,cask,distillate,null,
				true,FillType.FILLING);
		try {
			distillate.updateQuantity(fillingDeCrease);
			return cask.updateQuantity(fillingInCrease);
		}catch (IllegalArgumentException e){
			throw new IllegalArgumentException("Provided quantity does not fit this distillate");
		}
	}

	/**
	 *Tranfers quantity from one cask to another
	 * @param caskFrom
	 * @param caskTo
	 * @param quantity
	 * @param date
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static boolean caskToCaskTransfer(Item caskFrom, Item caskTo, double quantity, LocalDate date)
			throws IllegalArgumentException{
		double caskFromQuantity = caskFrom.getQuantityStatus();
		double caskToRemainingQuantity = caskTo.getRemainingQuantity();

		if (caskFromQuantity < quantity){
			throw new IllegalArgumentException("Quantity exeeds what remaining in 'From Cask': " + caskFrom.getName());
		}

		if (caskToRemainingQuantity < quantity){
			throw new IllegalArgumentException("Quantity exeeds the capacity of 'To Cask': " + caskTo.getName());
		}

		if (((Cask)caskTo).getFillingsStackByLifeCycle(((Cask) caskTo).getLifeCycle()).isEmpty()){
			Distillate distillate = null;

			for (Distillate d : storage.getDistillates()){
				if (d.getName().equalsIgnoreCase("caskIsEmpty")){
					distillate = d;
				}
			}
			Filling f = new FillDistillate(date,0,((Cask) caskTo),distillate,null,true,FillType.CASKHISTORY);
			caskTo.updateQuantity(f);
		}

		double fillStackSum = 0;
		for (Filling f : ((Cask)caskFrom).getFillingsStackByLifeCycle(((Cask) caskFrom).getLifeCycle())){
			fillStackSum += Math.abs(f.getQuantity());
		}

		double calculationFactor = quantity/fillStackSum;

		((Cask) caskFrom).getFillingsStackByLifeCycle(((Cask) caskFrom).getLifeCycle()).forEach(filling -> {
//			if (!filling.isDecrease()){
				double changeQauntity = Math.abs(filling.getQuantity()) * calculationFactor;
				Distillate distillate = ((FillDistillate) filling).getDistillate();
				Filling encreaseQuantity = new FillDistillate(date,changeQauntity, ((Cask) caskTo), distillate,
						((FillDistillate) filling),false,FillType.TRANSFER);
				Filling decreaseQuantity = new FillDistillate(date,changeQauntity, ((Cask) caskFrom), distillate,
						((FillDistillate) filling),true,FillType.TRANSFER);

				caskFrom.updateQuantity(decreaseQuantity);
				caskTo.updateQuantity(encreaseQuantity);
//			}
		});
		return true;
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
	 * Function used for generating label text.
	 * @param cask
	 * @param date
	 * @param storyIsSimple
	 * @return
	 */
	public static String getCaskStory(Cask cask, LocalDate date, boolean storyIsSimple){
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
		double caskQauntity = cask.getQuantityStatus();
		int caskLifeCycle = cask.getLifeCycle();
		double calculationFactor = quantity/caskQauntity;

		if (quantity > caskQauntity) throw new IllegalArgumentException("Bottling quantity is higher than cask quantity");

		List<Filling> fillings = cask.getFillingsStackHavingLifeCycleGroupedByDistillate(caskLifeCycle, date);

		for (Filling f : fillings){
			if (!f.isDecrease()){
				Distillate distillate = ((FillDistillate) f).getDistillate();
				double decreaseQuantity = f.getQuantity() * calculationFactor;
				Filling newFilling = new FillDistillate(date,decreaseQuantity,cask,distillate,null,
						true, FillType.BOTTLING);
				cask.updateQuantity(newFilling);
			}
		}

	}
}
