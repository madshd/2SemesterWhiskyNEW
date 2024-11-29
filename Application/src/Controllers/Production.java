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

	public static Distillate createDistillate(String name, LocalDate startDate, LocalDate endDate, double quantity,
									   Distiller distiller, Unit unit){
		Distillate distillate = new Distillate(name,startDate,endDate,quantity,distiller,unit);
		storage.storeDistillate(distillate);
		return distillate;
	}

	public static Distiller createDistiller(String name, String initials, String story){
		Distiller distiller = new Distiller(name,initials,story);
		return distiller;
	}

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
}
