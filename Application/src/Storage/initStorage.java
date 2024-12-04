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

import static Controllers.Production.addDescriotionToDistillate;

public abstract class initStorage {

	// =================== TASTE PROFILES =================

	static ArrayList<TastingNote> notesTEST = new ArrayList<>();
	static TasteProfile tasteProfileTEST = BatchArea.createNewTasteProfile("TEST",
			"TEST", notesTEST);

	static ArrayList<TastingNote> notes1 = new ArrayList<>();
	static TasteProfile tasteProfile1 = BatchArea.createNewTasteProfile("Velvet Ember",
			"\'Velvet Ember\' is a taste profile that combines the silky smoothness of honeyed vanilla with a subtle smoky warmth, finished with a whisper of spiced citrus zest for a lingering, refined complexity.",
			notes1);

	static ArrayList<TastingNote> notes2 = new ArrayList<>();
	static TasteProfile tasteProfile2 = BatchArea.createNewTasteProfile("Frosted Grove",
			"\'Frosted Grove\' delivers a crisp and refreshing blend of cool mint and green apple, accented by delicate floral undertones and a hint of piney freshness for an invigorating finish.",
			notes2);

	static ArrayList<TastingNote> notes3 = new ArrayList<>();
	static TasteProfile tasteProfile3 = BatchArea.createNewTasteProfile("Crimson Ember",
			"\'Crimson Ember\' offers a bold fusion of dark cherry and spiced cinnamon, balanced with earthy undertones of roasted cacao and a whisper of smoky oak for a rich, warming experience.",
			notes3);

	static ArrayList<TastingNote> notes4 = new ArrayList<>();
	static TasteProfile tasteProfile4 = BatchArea.createNewTasteProfile("Golden Harvest",
			"\'Golden Harvest\' combines the buttery sweetness of caramelized pears with toasted almonds, complemented by a hint of nutmeg and a smooth, creamy finish reminiscent of warm custard.",
			notes4);

	// =================== FORMULAE =================

	static HashMap<TasteProfile, Double> blueprintTEST = new HashMap<>();

	static HashMap<TasteProfile, Double> blueprint1 = new HashMap<>();
	Formula formula1 = BatchArea.createNewFormula("Formula 1", blueprint1);

	static HashMap<TasteProfile, Double> blueprint2 = new HashMap<>();
	Formula formula2 = BatchArea.createNewFormula("Formula 2", blueprint2);

	static HashMap<TasteProfile, Double> blueprint3 = new HashMap<>();
	Formula formula3 = BatchArea.createNewFormula("Formula 3", blueprint3);

	static HashMap<TasteProfile, Double> blueprint4 = new HashMap<>();
	Formula formula4 = BatchArea.createNewFormula("Formula 4", blueprint4);

	// =================== PRODUCTS =================

	Product product1 = BatchArea.createNewProduct("Product 1", 700);
	Product product2 = BatchArea.createNewProduct("Product 2", 500);
	Product product3 = BatchArea.createNewProduct("Product 3", 1000);
	Product product4 = BatchArea.createNewProduct("Product TEST", 1000);

	// =================== CASKS =================

	public static void loadProduction() {
		Supplier sub_01 = Warehousing.createSupplier(
				"Highland Barley Co.",
				"123 Grain Street, Inverness, Scotland",
				"Supplier of premium malted barley.",
				"Highland Barley Co. has been delivering high-quality, locally grown barley to distilleries since 1975.");

		Supplier sub_02 = Warehousing.createSupplier(
				"Pure Spring Water Co.",
				"45 Riverbank Lane, Loch Ness, Scotland",
				"Provider of freshwater for distillation.",
				"Pure Spring Water Co. sources its water from the pristine Loch Ness, ensuring the highest purity for whisky production.");

		Supplier sub_03 = Warehousing.createSupplier(
				"Oak Master Barrels",
				"7 Barrel Road, Speyside, Scotland",
				"Supplier of American oak and European oak casks.",
				"Crafting barrels from sustainable sources, Oak Master Barrels has supported traditional whisky making for over a century.");

		Supplier sub_04 = Warehousing.createSupplier(
				"Peat & Smolder",
				"9 Rookery Path, Islay, Scotland",
				"Supplier of high-quality peat for smoking malts.",
				"Peat & Smolder specializes in Islay peat, perfect for imparting the rich, smoky profile loved by whisky connoisseurs.");

		Supplier sub_05 = Warehousing.createSupplier(
				"Sherry Bliss Co.",
				"28 Winery Drive, Jerez, Spain",
				"Supplier of seasoned sherry casks.",
				"Known for their rich sherry heritage, Sherry Bliss Co. provides barrels seasoned with fine Spanish sherry for finishing whisky.");

		Distiller distiller_01 = Production.createDistiller(
				"Ewan MacGregor",
				"EMG",
				"An expert in fine single malt production with a focus on classic Highland styles.");

		Distiller distiller_02 = Production.createDistiller(
				"Lachlan Stewart",
				"LS",
				"Passionate about combining traditional whisky techniques with innovative flavor profiles.");

		Distiller distiller_03 = Production.createDistiller(
				"Fiona Campbell",
				"FC",
				"Dedicated to perfecting peat-smoked whiskies inspired by Islay tradition.");

		Distiller distiller_04 = Production.createDistiller(
				"Iain MacDougall",
				"IMD",
				"Focused on crafting small batch whiskies that celebrate natural ingredients.");

		Distiller distiller_05 = Production.createDistiller(
				"Moira Graham",
				"MG",
				"Whisky innovator with a love for experimenting with unique cask finishes.");

		Distiller distiller_06 = Production.createDistiller(
				"Douglas Sinclair",
				"DS",
				"A distilling veteran known for creating rich and complex whiskies with deep character.");

		Distiller distiller_07 = Production.createDistiller(
				"Isla Ferguson",
				"IF",
				"Specialist in blending and balancing bold and delicate whisky flavors.");

		Cask cask_01 = Warehousing.createCask(1, 200, Unit.LITERS, sub_03, "Bourbon Cask"); // Bourbon cask from Oak Master Barrels
		Cask cask_02 = Warehousing.createCask(2, 250, Unit.LITERS, sub_03, "American Oak Cask"); // American Oak cask from Oak Master Barrels
		Cask cask_03 = Warehousing.createCask(3, 225, Unit.LITERS, sub_05, "Sherry Cask"); // Sherry cask from Sherry Bliss Co.
		Cask cask_04 = Warehousing.createCask(4, 500, Unit.LITERS, sub_03, "Large European Oak Cask"); // Large European Oak cask from Oak Master Barrels
		Cask cask_05 = Warehousing.createCask(5, 300, Unit.LITERS, sub_05, "Seasoned Sherry Cask"); // Seasoned sherry cask from Sherry Bliss Co.
		Cask cask_06 = Warehousing.createCask(6, 200, Unit.LITERS, sub_03, "Toasted Oak Cask"); // Toasted Oak cask from Oak Master Barrels
		Cask cask_07 = Warehousing.createCask(7, 225, Unit.LITERS, sub_03, "Ex-Port Cask"); // Ex-Port cask from Oak Master Barrels
		Cask cask_08 = Warehousing.createCask(8, 180, Unit.LITERS, sub_03, "Peated Oak Cask"); // Peated Oak cask from Peat & Smolder
		Cask cask_09 = Warehousing.createCask(9, 300, Unit.LITERS, sub_05, "Spanish Oak Cask"); // Spanish Oak cask from Sherry Bliss Co.
		Cask cask_10 = Warehousing.createCask(10, 600, Unit.LITERS, sub_03, "Large Blending Cask"); // Large blending cask from Oak Master

		Distillate distillate_01 = Production.createDistillate(
				"Highland Essence",
				LocalDate.parse("2024-11-27"),
				LocalDate.parse("2024-12-24"),
				500,
				distiller_01,
				Unit.LITERS);

		Distillate distillate_02 = Production.createDistillate(
				"Islay Smoke",
				LocalDate.parse("2024-10-15"),
				LocalDate.parse("2024-11-10"),
				300,
				distiller_02,
				Unit.LITERS);

		Distillate distillate_03 = Production.createDistillate(
				"Classic Malt",
				LocalDate.parse("2024-09-01"),
				LocalDate.parse("2024-09-30"),
				400,
				distiller_03,
				Unit.LITERS);

		Distillate distillate_04 = Production.createDistillate(
				"Peat Fire",
				LocalDate.parse("2024-08-20"),
				LocalDate.parse("2024-09-15"),
				200,
				distiller_04,
				Unit.LITERS);

		Distillate distillate_05 = Production.createDistillate(
				"Sherry Kiss",
				LocalDate.parse("2024-07-10"),
				LocalDate.parse("2024-08-05"),
				350,
				distiller_05,
				Unit.LITERS);

		Distillate distillate_06 = Production.createDistillate(
				"Bourbon Blend",
				LocalDate.parse("2024-06-15"),
				LocalDate.parse("2024-07-10"),
				450,
				distiller_06,
				Unit.LITERS);

		Distillate distillate_07 = Production.createDistillate(
				"Loch Nectar",
				LocalDate.parse("2024-05-01"),
				LocalDate.parse("2024-05-30"),
				500,
				distiller_07,
				Unit.LITERS);

		Distillate distillate_08 = Production.createDistillate(
				"Speyside Gold",
				LocalDate.parse("2024-04-15"),
				LocalDate.parse("2024-05-12"),
				400,
				distiller_01,
				Unit.LITERS);

		Distillate distillate_09 = Production.createDistillate(
				"Oak Reserve",
				LocalDate.parse("2024-03-10"),
				LocalDate.parse("2024-04-05"),
				375,
				distiller_02,
				Unit.LITERS);

		Distillate distillate_10 = Production.createDistillate(
				"Golden Highland",
				LocalDate.parse("2024-02-20"),
				LocalDate.parse("2024-03-15"),
				250,
				distiller_03,
				Unit.LITERS);

		addDescriotionToDistillate(distillate_01,
				"A rich and complex distillate characterized by honeyed sweetness and notes of dried fruits.");
		addDescriotionToDistillate(distillate_02,
				"Crafted with Islay peat, this distillate exudes bold smoky aromas and a hint of seaweed brine.");
		addDescriotionToDistillate(distillate_03,
				"A delicate distillate with floral undertones, hints of vanilla, and a creamy texture, perfect for blending.");
		addDescriotionToDistillate(distillate_04,
				"Matured using Highland water, this distillate balances citrus zest with a malty backbone.");
		addDescriotionToDistillate(distillate_05,
				"Rich in spices and toffee notes, this distillate showcases the influence of sherry-seasoned oak.");
		addDescriotionToDistillate(distillate_06,
				"A light distillate with green apple freshness and subtle nutty undertones from American oak aging.");
		addDescriotionToDistillate(distillate_07,
				"This distillate boasts caramelized sugar aromas and a velvety depth of chocolate and dried cherries.");
		addDescriotionToDistillate(distillate_08,
				"Crafted with barley grown in local fields, this distillate carries earthy tones and a hint of caramel.");
		addDescriotionToDistillate(distillate_09,
				"Peaty and robust, this distillate reflects the rugged character of coastal winds and marine influence.");
		addDescriotionToDistillate(distillate_10,
				"A harmonious distillate with subtle hints of fresh bread, honey, and a lingering smoky finish.");

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

	public static void loadBatchArea() {

		notesTEST.add(TastingNote.SMOKEY);
		blueprintTEST.put(tasteProfileTEST, 100.0);

		notes1.add(TastingNote.SMOKEY);
		notes1.add(TastingNote.APPLE);
		notes1.add(TastingNote.LEMON);
		notes1.add(TastingNote.VANILLA);
		notes2.add(TastingNote.VANILLA);
		notes2.add(TastingNote.BLACK_PEPPER);
		notes3.add(TastingNote.COFFEE);
		notes3.add(TastingNote.LEMON);
		notes4.add(TastingNote.OAK);
		notes4.add(TastingNote.BLACK_PEPPER);
		notes4.add(TastingNote.HONEY);
		blueprint1.put(tasteProfile1, 50.0);
		blueprint1.put(tasteProfile2, 25.0);
		blueprint1.put(tasteProfile3, 25.0);
		blueprint2.put(tasteProfile1, 50.0);
		blueprint2.put(tasteProfile4, 50.0);
		blueprint3.put(tasteProfile4, 10.0);
		blueprint4.put(tasteProfile1, 25.0);
		blueprint4.put(tasteProfile3, 50.0);
		blueprint4.put(tasteProfile4, 25.0);

	}

}
