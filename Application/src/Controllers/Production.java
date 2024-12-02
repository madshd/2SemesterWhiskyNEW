package Controllers;

import Enumerations.Unit;
import Interfaces.Filling;
import Interfaces.Item;
import Interfaces.StorageInterface;
import Production.Distillate;
import Production.Distiller;
import Production.FillDistillate;
import Storage.Storage;
import Warehousing.Cask;

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

	/**
	 *
	 * @param name
	 * @param initials
	 * @param story
	 * @return
	 */
	public static Distiller createDistiller(String name, String initials, String story){
		Distiller distiller = new Distiller(name,initials,story);
		return distiller;
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
		Filling filling = new FillDistillate(date, quantity);
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
	 */
	public static void caskBottling(Cask cask, double Quantity){
		//TODO by leander

	}
}
