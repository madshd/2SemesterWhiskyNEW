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
import Warehousing.LoggerObserver;

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

		Supplier sub_06 = Warehousing.createSupplier(
				"Golden Fields Agriculture",
				"123 Highland Drive, Inverness, Scotland",
				"Supplier of premium barley for whisky production.",
				"Golden Fields Agriculture has been the trusted supplier of barley to distilleries across Scotland since 1985. Their focus is on delivering high-enzyme barley varieties grown in Scotland’s fertile Highlands."
		);

		Supplier sub_07 = Warehousing.createSupplier(
				"Loch Ness Water Co.",
				"45 Spring Road, Loch Ness, Scotland",
				"Provider of pure natural spring water for whisky distillation.",
				"Loch Ness Water Co. sources its water from pristine natural springs in the heart of the Scottish Highlands. Their water is known for its purity and consistency, which enhances whisky fermentation and distillation."
		);

		Supplier sub_08 = Warehousing.createSupplier(
				"Brewer's Choice Yeast Ltd.",
				"78 Brewery Lane, Edinburgh, Scotland",
				"Supplier of high-quality brewer's yeast for efficient fermentation.",
				"Brewer's Choice Yeast Ltd. specializes in cultivating yeast strains tailored for whisky production, ensuring rich flavor complexity and consistent alcohol yields."
		);

		Supplier sub_09 = Warehousing.createSupplier(
				"Islay Peat Moss",
				"99 Peatland Road, Islay, Scotland",
				"Provider of premium peat for smoking barley and infusing whisky with smoky flavors.",
				"Islay Peat Moss has been sourcing peat from the island’s famous mosslands for over a century, helping distilleries create their signature smoky whisky profiles."
		);

		Supplier sub_10 = Warehousing.createSupplier(
				"Highland Sweets Co.",
				"50 Sugar Street, Glasgow, Scotland",
				"Refined sugar supplier for boosting fermentation efficiency.",
				"Highland Sweets Co. provides top-quality sugar for the whisky industry's fermentation process, ensuring higher alcohol yields and consistent results. The company is family-owned and deeply rooted in tradition."
		);

		Supplier sub_11 = Warehousing.createSupplier(
				"Sall Barley Fields",
				"Sall Bygvej 12, 8410 Rønde, Denmark",
				"Local Danish supplier of high-quality barley for whisky production.",
				"Sall Barley Fields is owned by the founders of Sall Whisky and specializes in cultivating premium barley varieties such as Evergreen, Stairway, and Irna. The fields provide not only for Sall Whisky's own production but are also trusted suppliers for some of the most renowned whisky brands globally. With a focus on sustainability and quality, Sall Barley Fields ensures every harvest meets the highest standards."
		);

		Supplier sub_12 = Warehousing.createSupplier(
				"Malt & More Grain Co.",
				"12 Field Street, Dundee, Scotland",
				"Specialist in malted grains for distilleries nationwide.",
				"Malt & More Grain Co. has been crafting malted grains like Golden Promise and Optic, ensuring consistent high-quality grain with malting characteristics tailored to whisky-making."
		);

		Supplier sub_13 = Warehousing.createSupplier(
				"Burns & Co. Caramel",
				"25 Sweet Lane, Edinburgh, Scotland",
				"Trusted supplier of E150a caramel for whisky coloring.",
				"Burns & Co. Caramel has served the whisky industry for decades with premium caramel coloring, providing master blenders with an option to create aesthetically striking and consistent batches."
		);

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

		// *** Ingredients ***

		Ingredient barleyEvergreen = Warehousing.createIngredient(
				"Barley - Evergreen",
				"Locally sourced barley from the Evergreen field. Known for its high enzyme activity and natural sweetness.",
				2001,
				LocalDate.parse("2024-03-01"),
				LocalDate.parse("2025-03-01"),
				500.0,
				sub_06, // Golden Fields Agriculture
				Unit.KILOGRAM,
				IngredientType.GRAIN
		);

		Ingredient barleyStairway = Warehousing.createIngredient(
				"Barley - Stairway",
				"A premium barley grown in the Stairway field. Offers a smooth and balanced flavor profile to the distillate.",
				2002,
				LocalDate.parse("2024-03-05"),
				LocalDate.parse("2025-03-05"),
				450.0,
				sub_11, // Pure Scotch Locals
				Unit.KILOGRAM,
				IngredientType.GRAIN
		);

		Ingredient barleyIrna = Warehousing.createIngredient(
				"Barley - Irna",
				"High-quality barley from the Irna field, prized for its robust malting characteristics.",
				2003,
				LocalDate.parse("2024-02-15"),
				LocalDate.parse("2025-02-15"),
				400.0,
				sub_11, // Pure Scotch Locals
				Unit.KILOGRAM,
				IngredientType.GRAIN
		);

		Ingredient barleyGoldenPromise = Warehousing.createIngredient(
				"Barley - Golden Promise",
				"A classic barley variety often used in whisky production for its creamy and malty flavor.",
				2004,
				LocalDate.parse("2024-01-20"),
				LocalDate.parse("2025-01-20"),
				600.0,
				sub_12, // Malt & More Grain Co.
				Unit.KILOGRAM,
				IngredientType.GRAIN
		);

		Ingredient barleyOptic = Warehousing.createIngredient(
				"Barley - Optic",
				"A versatile barley variety that delivers clean and consistent results during malting.",
				2005,
				LocalDate.parse("2024-02-10"),
				LocalDate.parse("2025-02-10"),
				400.0,
				sub_12, // Malt & More Grain Co.
				Unit.KILOGRAM,
				IngredientType.GRAIN
		);

		Ingredient waterSpring = Warehousing.createIngredient(
				"Pure Spring Water",
				"Sourced from pristine local springs, this water ensures clean and smooth fermentation.",
				3001,
				LocalDate.parse("2024-01-10"),
				LocalDate.parse("2025-01-10"),
				1500.0,
				sub_07, // Loch Ness Water Co.
				Unit.LITERS,
				IngredientType.WATER
		);

		Ingredient yeastBrewers = Warehousing.createIngredient(
				"Brewer's Yeast",
				"A specialized yeast strain used for efficient fermentation, producing complex fruity esters.",
				3002,
				LocalDate.parse("2024-02-05"),
				LocalDate.parse("2024-06-05"),
				50.0,
				sub_08, // Brewer's Choice Yeast Ltd.
				Unit.KILOGRAM,
				IngredientType.YEAST
		);

		Ingredient peatMoss = Warehousing.createIngredient(
				"Islay Peat Moss",
				"Used to smoke barley, imparting a bold and smoky character typical of Islay whiskies.",
				3003,
				LocalDate.parse("2024-02-20"),
				LocalDate.parse("2025-02-20"),
				200.0,
				sub_09, // Islay Peat Moss
				Unit.KILOGRAM,
				IngredientType.ADDITIVE
		);

		Ingredient sugarFermentation = Warehousing.createIngredient(
				"Fermentation Sugar",
				"Refined sugar used to assist fermentation, boosting alcohol yield in the distillate.",
				3004,
				LocalDate.parse("2024-01-15"),
				LocalDate.parse("2024-12-15"),
				100.0,
				sub_10, // Highland Sweets Co.
				Unit.KILOGRAM,
				IngredientType.ADDITIVE
		);

		Ingredient caramelColor = Warehousing.createIngredient(
				"Caramel Coloring",
				"E150a coloring used for achieving a consistent appearance in blended whiskies.",
				3005,
				LocalDate.parse("2024-01-01"),
				LocalDate.parse("2025-01-01"),
				25.0,
				sub_13, // Burns & Co. Caramel
				Unit.LITERS,
				IngredientType.ADDITIVE
		);


		// *** Fillings ***
		// One distallate capacity fully used across casks
		Production.fillDistillateIntoCask(distillate_06,cask_01,100,LocalDate.parse("2024-01-01"));
		Production.fillDistillateIntoCask(distillate_06,cask_02,100,LocalDate.parse("2024-01-01"));
		Production.fillDistillateIntoCask(distillate_06,cask_03,100,LocalDate.parse("2024-01-01"));
		Production.fillDistillateIntoCask(distillate_06,cask_04,150,LocalDate.parse("2024-01-01"));

		// Cask holding more than two distillates and fully used
		Production.fillDistillateIntoCask(distillate_10,cask_02,50,LocalDate.parse("2024-02-01"));
		Production.fillDistillateIntoCask(distillate_08,cask_02,100,LocalDate.parse("2024-02-01"));

		// Cask having more than two distillates and having remaining capacity
		Production.fillDistillateIntoCask(distillate_07,cask_04,50,LocalDate.parse("2024-03-01"));
		Production.fillDistillateIntoCask(distillate_08,cask_04,25,LocalDate.parse("2024-04-01"));
		Production.fillDistillateIntoCask(distillate_09,cask_04,15,LocalDate.parse("2024-05-01"));
		Production.fillDistillateIntoCask(distillate_10,cask_04,75,LocalDate.parse("2024-06-01"));

		// *** Bottling ***
		Production.caskBottling(cask_04,25,LocalDate.parse("2024-12-01"));

	}

	public static void loadWarehousing() {
		Warehouse warehouse = Warehousing.createWarehouse("Lager 1", "Lagervej 1");
		Warehouse warehouse2 = Warehousing.createWarehouse("Lager 2", "Lagervej 2");

		LoggerObserver logger = new LoggerObserver();

		// Register the observer
		warehouse.registerWarehousingObserver(logger);

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
		rack1.moveItem(ingredient1, 2, 0);

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
