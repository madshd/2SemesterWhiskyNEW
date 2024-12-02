package Storage;

import Controllers.Production;
import Controllers.Warehousing;
import Enumerations.IngredientType;
import Enumerations.TastingNote;
import Enumerations.Unit;
import Production.Distillate;
import Production.Distiller;
import Warehousing.Supplier;
import Warehousing.Cask;
import Warehousing.Warehouse;
import Warehousing.StorageRack;
import Warehousing.Ingredient;

import Controllers.BatchArea;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import BatchArea.Formula;
import BatchArea.TasteProfile;
import BatchArea.Product;

public abstract class initStorage {

	public static void loadProduction() {
		Supplier testSupplier = Warehousing.createSupplier("Knud", "Knudvej 1", "Kan levere alt!",
				"Passion for god Whisky");

		Distiller distiller = Production.createDistiller("Per", "PP", "Den bedste!");

		Distillate d1 = Production.createDistillate("Jul 24", LocalDate.parse("2024-11-27"),
				LocalDate.parse("2024-12-24"), 100, distiller, Unit.LITERS);

		Cask cask = Warehousing.createCask(1, 50, Unit.LITERS, testSupplier);

		System.out.println("*** Distillate ****");
		System.out.println(d1.getListInfo());
		System.out.println("*** Cask ****");
		System.out.println(cask.getListInfo());
		System.out.println("*** Cask Filling Stack****");
		System.out.println(cask.getFillingTextLines());

		Production.fillDistillateIntoCask(d1, cask, 10, LocalDate.parse("2024-11-28"));

		System.out.println("*** Distillate ****");
		System.out.println(d1.getListInfo());
		System.out.println("*** Cask ****");
		System.out.println(cask.getListInfo());
		System.out.println("*** Cask Filling Stack****");
		System.out.println(cask.getFillingTextLines());

		System.out.println();

		Production.fillDistillateIntoCask(d1, cask, 20, LocalDate.parse("2024-11-28"));

		System.out.println("*** Distillate ****");
		System.out.println(d1.getListInfo());
		System.out.println("*** Cask ****");
		System.out.println(cask.getListInfo());
		System.out.println("*** Cask Filling Stack****");
		System.out.println(cask.getFillingTextLines());

		Production.fillDistillateIntoCask(d1, cask, 5, LocalDate.parse("2024-11-28"));

		System.out.println("*** Distillate ****");
		System.out.println(d1.getListInfo());
		System.out.println("*** Cask ****");
		System.out.println(cask.getListInfo());
		System.out.println("*** Cask Filling Stack****");
		System.out.println(cask.getFillingTextLines());

		Production.fillDistillateIntoCask(d1, cask, 20, LocalDate.parse("2024-11-28"));

		System.out.println("*** Distillate ****");
		System.out.println(d1.getListInfo());
		System.out.println("*** Cask ****");
		System.out.println(cask.getListInfo());
		System.out.println("*** Cask Filling Stack****");
		System.out.println(cask.getFillingTextLines());

	}

	public static void loadWarehousing() {
		Warehouse warehouse = Warehousing.createWarehouse("Lager 1", "Lagervej 1");
		Warehouse warehouse2 = Warehousing.createWarehouse("Lager 2", "Lagervej 2");

		Warehousing.createWarehouse("Warehouse 55", "Address 1");
		Warehousing.createWarehouse("Warehouse 77", "Address 2");

		StorageRack rack1 = Warehousing.createStorageRack("Rack 1", 100);
		StorageRack rack2 = Warehousing.createStorageRack("Rack 2", 100);
		StorageRack rack3 = Warehousing.createStorageRack("Rack 3", 100);
		StorageRack rack4 = Warehousing.createStorageRack("Rack 4", 100);

		warehouse.addStorageRack("W1R1", rack1);
		warehouse.addStorageRack("W1R2", rack2);

		Supplier supplier1 = new Supplier("Supplier 1", "Address 1", "Phone 1", "Nice story");

		Ingredient ingredient1 = new Ingredient("Lars Tyndskids Grain", "Finest Grain ", 1,
				LocalDate.of(2024, 11, 27), LocalDate.of(2024, 12, 27), 1, supplier1, Unit.TONNES,
				IngredientType.GRAIN);

		rack1.addItem(2, ingredient1);

	}

	@SuppressWarnings("unused")
	public static void loadBatchArea() {

		// =================== TASTE PROFILES =================

		ArrayList<TastingNote> notes1 = new ArrayList<>();
		notes1.add(TastingNote.SMOKEY);
		notes1.add(TastingNote.APPLE);
		notes1.add(TastingNote.LEMON);
		notes1.add(TastingNote.VANILLA);
		TasteProfile tasteProfile1 = BatchArea.createNewTasteProfile("Velvet Ember",
				"\'Velvet Ember\' is a taste profile that combines the silky smoothness of honeyed vanilla with a subtle smoky warmth, finished with a whisper of spiced citrus zest for a lingering, refined complexity.",
				notes1);

		ArrayList<TastingNote> notes2 = new ArrayList<>();
		notes2.add(TastingNote.VANILLA);
		notes2.add(TastingNote.BLACK_PEPPER);
		TasteProfile tasteProfile2 = BatchArea.createNewTasteProfile("Frosted Grove",
				"\'Frosted Grove\' delivers a crisp and refreshing blend of cool mint and green apple, accented by delicate floral undertones and a hint of piney freshness for an invigorating finish.",
				notes2);

		ArrayList<TastingNote> notes3 = new ArrayList<>();
		notes3.add(TastingNote.COFFEE);
		notes3.add(TastingNote.LEMON);
		TasteProfile tasteProfile3 = BatchArea.createNewTasteProfile("Crimson Ember",
				"\'Crimson Ember\' offers a bold fusion of dark cherry and spiced cinnamon, balanced with earthy undertones of roasted cacao and a whisper of smoky oak for a rich, warming experience.",
				notes3);

		ArrayList<TastingNote> notes4 = new ArrayList<>();
		notes4.add(TastingNote.OAK);
		notes4.add(TastingNote.BLACK_PEPPER);
		notes4.add(TastingNote.HONEY);
		TasteProfile tasteProfile4 = BatchArea.createNewTasteProfile("Golden Harvest",
				"\'Golden Harvest\' combines the buttery sweetness of caramelized pears with toasted almonds, complemented by a hint of nutmeg and a smooth, creamy finish reminiscent of warm custard.",
				notes4);

		// =================== FORMULAE =================

		HashMap<TasteProfile, Integer> blueprint1 = new HashMap<>();
		blueprint1.put(tasteProfile1, 50);
		blueprint1.put(tasteProfile2, 25);
		blueprint1.put(tasteProfile3, 25);
		Formula formula1 = BatchArea.createNewFormula("Formula 1", blueprint1);

		HashMap<TasteProfile, Integer> blueprint2 = new HashMap<>();
		blueprint2.put(tasteProfile1, 50);
		blueprint2.put(tasteProfile4, 50);
		Formula formula2 = BatchArea.createNewFormula("Formula 2", blueprint2);

		HashMap<TasteProfile, Integer> blueprint3 = new HashMap<>();
		blueprint3.put(tasteProfile4, 100);
		Formula formula3 = BatchArea.createNewFormula("Formula 3", blueprint3);

		HashMap<TasteProfile, Integer> blueprint4 = new HashMap<>();
		blueprint4.put(tasteProfile1, 25);
		blueprint4.put(tasteProfile3, 50);
		blueprint4.put(tasteProfile4, 25);
		Formula formula4 = BatchArea.createNewFormula("Formula 4", blueprint4);

		// =================== PRODUCTS =================

		Product product1 = BatchArea.createNewProduct("Product 1", 700);
		Product product2 = BatchArea.createNewProduct("Product 2", 500);
		Product product3 = BatchArea.createNewProduct("Product 3", 1000);
	}

}
