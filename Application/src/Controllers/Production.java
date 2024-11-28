package Controllers;

import Enumerations.Unit;
import Interfaces.Filling;
import Interfaces.StorageInterface;
import Production.Distillate;
import Production.Distiller;
import Production.FillDistillate;
import Warehousing.Cask;

import java.time.LocalDate;

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

}
