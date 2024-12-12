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
import static Controllers.Production.addIngredientToDistillate;
import static Controllers.Production.addAlcoholPercentage;
import static Controllers.Production.addProductionCutInformation;
import static Controllers.Production.addStoryToDistillate;

public abstract class initStorage {

	public static TasteProfile tasteProfile1;
	public static TasteProfile tasteProfile2;
	public static TasteProfile tasteProfile3;
	public static TasteProfile tasteProfile4;
	public static TasteProfile tasteProfile5;
	public static TasteProfile tasteProfile6;
	public static TasteProfile tasteProfile7;
	public static TasteProfile tasteProfile8;
	public static TasteProfile tasteProfile9;
	public static TasteProfile tasteProfile10;
	public static TasteProfile tasteProfile11;
	public static TasteProfile tasteProfile12;

	public static Cask cask_01;
	public static Cask cask_02;
	public static Cask cask_03;
	public static Cask cask_04;
	public static Cask cask_05;
	public static Cask cask_06;
	public static Cask cask_07;
	public static Cask cask_08;
	public static Cask cask_09;
	public static Cask cask_10;
	public static Cask cask_11;
	public static Cask cask_12;
	public static Cask cask_13;
	public static Cask cask_14;
	public static Cask cask_15;

	@SuppressWarnings("unused")
	public static void loadProduction() {
		Warehouse warehouse = Warehousing.createWarehouse("Sherlock Whisky", "Baker Street 221 B");

		LoggerObserver logger = new LoggerObserver();

		// Register the observer
		warehouse.registerWarehousingObserver(logger);

		StorageRack rack1 = Warehousing.createStorageRack("221 B", 100);

		warehouse.addStorageRack("221 B", rack1);

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

		Distiller system = Production.createDistiller(
				"labelTales",
				"EAA",
				"Must only be used for testing and internal use.");

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

		cask_01 = Warehousing.createCaskAndAdd(1, 200, Unit.LITERS, sub_03, "Bourbon Cask", warehouse, rack1); // Bourbon cask from Oak Master Barrels
		cask_02 = Warehousing.createCaskAndAdd(2, 250, Unit.LITERS, sub_03, "American Oak Cask", warehouse, rack1); // American Oak cask from Oak Master Barrels
		cask_03 = Warehousing.createCaskAndAdd(3, 225, Unit.LITERS, sub_05, "Sherry Cask", warehouse, rack1); // Sherry cask from Sherry Bliss Co.
		cask_04 = Warehousing.createCaskAndAdd(4, 500, Unit.LITERS, sub_03, "Large European Oak Cask", warehouse, rack1); // Large European Oak cask from Oak Master Barrels
		cask_05 = Warehousing.createCaskAndAdd(5, 300, Unit.LITERS, sub_05, "Seasoned Sherry Cask", warehouse, rack1); // Seasoned sherry cask from Sherry Bliss Co.
		cask_06 = Warehousing.createCaskAndAdd(6, 200, Unit.LITERS, sub_03, "Toasted Oak Cask", warehouse, rack1); // Toasted Oak cask from Oak Master Barrels
		cask_07 = Warehousing.createCaskAndAdd(7, 500, Unit.LITERS, sub_03, "Ex-Port Cask", warehouse, rack1); // Ex-Port cask from Oak Master Barrels
		cask_08 = Warehousing.createCaskAndAdd(8, 400, Unit.LITERS, sub_03, "Peated Oak Cask", warehouse, rack1); // Peated Oak cask from Peat & Smolder
		cask_09 = Warehousing.createCaskAndAdd(9, 300, Unit.LITERS, sub_05, "Spanish Oak Cask", warehouse, rack1); // Spanish Oak cask from Sherry Bliss Co.
		cask_10 = Warehousing.createCaskAndAdd(10, 600, Unit.LITERS, sub_03, "Large Blending Cask", warehouse, rack1); // Large blending cask from Oak Master
		cask_11 = Warehousing.createCaskAndAdd(11, 450, Unit.LITERS, sub_03, "French Oak Cask", warehouse, rack1); // French Oak cask from Oak Master Barrels
		cask_12 = Warehousing.createCaskAndAdd(12, 675, Unit.LITERS, sub_05, "Rum Cask", warehouse, rack1); // Rum cask from Caribbean Spirits
		cask_13 = Warehousing.createCaskAndAdd(13, 650, Unit.LITERS, sub_03, "Maple Wood Cask", warehouse, rack1); // Maple Wood cask from Maple Masters
		cask_14 = Warehousing.createCaskAndAdd(14, 600, Unit.LITERS, sub_05, "Cherry Wood Cask", warehouse, rack1); // Cherry Wood cask from Cherry Bliss Co.
		cask_15 = Warehousing.createCaskAndAdd(15, 500, Unit.LITERS, sub_03, "Chestnut Cask", warehouse, rack1); // Chestnut cask from Oak Master Barrels

		Distillate caskIsEmpty = Production.createDistillate(
				"caskIsEmpty",
				LocalDate.parse("2000-01-01"),
				LocalDate.parse("2100-01-01"),
				0,
				distiller_01,
				Unit.LITERS);

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

		Distillate distillate_11 = Production.createDistillate(
			"Silver Valley",
			LocalDate.parse("2023-05-10"),
			LocalDate.parse("2023-06-10"),
			300,
			distiller_04,
			Unit.LITERS);

		Distillate distillate_12 = Production.createDistillate(
			"Bronze Mountain",
			LocalDate.parse("2022-08-15"),
			LocalDate.parse("2022-09-15"),
			275,
			distiller_05,
			Unit.LITERS);

		Distillate distillate_13 = Production.createDistillate(
			"Highland Harmony",
			LocalDate.parse("2020-03-01"),
			LocalDate.parse("2020-04-01"),
			800,
			distiller_06,
			Unit.LITERS);

		Distillate distillate_14 = Production.createDistillate(
			"Island Intrigue",
			LocalDate.parse("2020-05-01"),
			LocalDate.parse("2020-06-01"),
			450,
			distiller_07,
			Unit.LITERS);

		Distillate distillate_15 = Production.createDistillate(
			"Coastal Calm",
			LocalDate.parse("2020-07-01"),
			LocalDate.parse("2020-08-01"),
			500,
			distiller_03,
			Unit.LITERS);

		Distillate distillate_16 = Production.createDistillate(
			"Mountain Mist",
			LocalDate.parse("2020-09-01"),
			LocalDate.parse("2020-10-01"),
			400,
			distiller_01,
			Unit.LITERS);

		Distillate distillate_17 = Production.createDistillate(
			"Valley Vigor",
			LocalDate.parse("2020-11-01"),
			LocalDate.parse("2020-12-01"),
			300,
			distiller_05,
			Unit.LITERS);

		addDescriotionToDistillate(caskIsEmpty,
				"A Cast To Cask transfer has been made to an empty cask, this distillate i used to link filling history together.");
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
		addDescriotionToDistillate(distillate_11,
				"A robust distillate with deep oak flavors and a touch of dark chocolate, aged in charred barrels.");
		addDescriotionToDistillate(distillate_12,
				"This distillate offers a unique blend of tropical fruit notes and a hint of coconut, perfect for summer.");
		addDescriotionToDistillate(distillate_13,
				"Highland Harmony is a balanced distillate with notes of heather, honey, and a subtle smoky finish.");
		addDescriotionToDistillate(distillate_14,
				"Island Intrigue features bold maritime flavors with a touch of sea salt and a lingering peat smoke.");
		addDescriotionToDistillate(distillate_15,
				"Coastal Calm is a smooth distillate with fresh citrus notes and a gentle briny character.");
		addDescriotionToDistillate(distillate_16,
				"Mountain Mist is a crisp distillate with hints of pine, fresh herbs, and a clean, refreshing finish.");
		addDescriotionToDistillate(distillate_17,
				"Valley Vigor is a vibrant distillate with rich malt flavors, a touch of spice, and a warm, lingering finish.");

		// *** Ingredients ***

		Ingredient barleyEvergreen = Warehousing.createIngredientAndAdd(
				"Barley - Evergreen",
				"Locally sourced barley from the Evergreen field. Known for its high enzyme activity and natural sweetness.",
				2001,
				LocalDate.parse("2024-03-01"),
				LocalDate.parse("2025-03-01"),
				5000.0,
				sub_06, // Golden Fields Agriculture
				Unit.KILOGRAM,
				IngredientType.GRAIN,
				warehouse,
				rack1
		);

		Ingredient barleyStairway = Warehousing.createIngredientAndAdd(
				"Barley - Stairway",
				"A premium barley grown in the Stairway field. Offers a smooth and balanced flavor profile to the distillate.",
				2002,
				LocalDate.parse("2024-03-05"),
				LocalDate.parse("2025-03-05"),
				4450.0,
				sub_11, // Pure Scotch Locals
				Unit.KILOGRAM,
				IngredientType.GRAIN,
				warehouse,
				rack1
		);

		Ingredient barleyIrna = Warehousing.createIngredientAndAdd(
				"Barley - Irna",
				"High-quality barley from the Irna field, prized for its robust malting characteristics.",
				2003,
				LocalDate.parse("2024-02-15"),
				LocalDate.parse("2025-02-15"),
				3400.0,
				sub_11, // Pure Scotch Locals
				Unit.KILOGRAM,
				IngredientType.GRAIN,
				warehouse,
				rack1
		);

		Ingredient barleyGoldenPromise = Warehousing.createIngredientAndAdd(
				"Barley - Golden Promise",
				"A classic barley variety often used in whisky production for its creamy and malty flavor.",
				2004,
				LocalDate.parse("2024-01-20"),
				LocalDate.parse("2025-01-20"),
				600.0,
				sub_12, // Malt & More Grain Co.
				Unit.KILOGRAM,
				IngredientType.GRAIN,
				warehouse,
				rack1
		);

		Ingredient barleyOptic = Warehousing.createIngredientAndAdd(
				"Barley - Optic",
				"A versatile barley variety that delivers clean and consistent results during malting.",
				2005,
				LocalDate.parse("2024-02-10"),
				LocalDate.parse("2025-02-10"),
				400.0,
				sub_12, // Malt & More Grain Co.
				Unit.KILOGRAM,
				IngredientType.GRAIN,
				warehouse,
				rack1
		);

		Ingredient waterSpring = Warehousing.createIngredientAndAdd(
				"Pure Spring Water",
				"Sourced from pristine local springs, this water ensures clean and smooth fermentation.",
				3001,
				LocalDate.parse("2024-01-10"),
				LocalDate.parse("2025-01-10"),
				55000.0,
				sub_07, // Loch Ness Water Co.
				Unit.LITERS,
				IngredientType.WATER,
				warehouse,
				rack1
		);

		Ingredient yeastBrewers = Warehousing.createIngredientAndAdd(
				"Brewer's Yeast",
				"A specialized yeast strain used for efficient fermentation, producing complex fruity esters.",
				3002,
				LocalDate.parse("2024-02-05"),
				LocalDate.parse("2024-06-05"),
				500.0,
				sub_08, // Brewer's Choice Yeast Ltd.
				Unit.KILOGRAM,
				IngredientType.YEAST,
				warehouse,
				rack1
		);

		Ingredient peatMoss = Warehousing.createIngredientAndAdd(
				"Islay Peat Moss",
				"Used to smoke barley, imparting a bold and smoky character typical of Islay whiskies.",
				3003,
				LocalDate.parse("2024-02-20"),
				LocalDate.parse("2025-02-20"),
				600.0,
				sub_09, // Islay Peat Moss
				Unit.KILOGRAM,
				IngredientType.ADDITIVE,
				warehouse,
				rack1
		);

		Ingredient sugarFermentation = Warehousing.createIngredientAndAdd(
				"Fermentation Sugar",
				"Refined sugar used to assist fermentation, boosting alcohol yield in the distillate.",
				3004,
				LocalDate.parse("2024-01-15"),
				LocalDate.parse("2024-12-15"),
				500.0,
				sub_10, // Highland Sweets Co.
				Unit.KILOGRAM,
				IngredientType.ADDITIVE,
				warehouse,
				rack1
		);

		Ingredient caramelColor = Warehousing.createIngredientAndAdd(
				"Caramel Coloring",
				"E150a coloring used for achieving a consistent appearance in blended whiskies.",
				3005,
				LocalDate.parse("2024-01-01"),
				LocalDate.parse("2025-01-01"),
				25.0,
				sub_13, // Burns & Co. Caramel
				Unit.LITERS,
				IngredientType.ADDITIVE,
				warehouse,
				rack1
		);

		// Add ingredients to distillates
		// Tilføj ingredienser til Highland Essence
		addIngredientToDistillate(distillate_01, barleyEvergreen, 1000.0, LocalDate.parse("2024-03-01"));
		addIngredientToDistillate(distillate_01, waterSpring, 4375.0, LocalDate.parse("2024-02-15"));
		addIngredientToDistillate(distillate_01, yeastBrewers, 12.5, LocalDate.parse("2024-02-01"));
		addIngredientToDistillate(distillate_01, sugarFermentation, 25.0, LocalDate.parse("2024-02-10"));
		addIngredientToDistillate(distillate_01, peatMoss, 37.5, LocalDate.parse("2024-01-20"));

		// Tilføj ingredienser til Islay Smoke
		addIngredientToDistillate(distillate_02, barleyGoldenPromise, 600.0, LocalDate.parse("2024-03-01"));
		addIngredientToDistillate(distillate_02, waterSpring, 2625.0, LocalDate.parse("2024-02-15"));
		addIngredientToDistillate(distillate_02, yeastBrewers, 7.5, LocalDate.parse("2024-02-01"));
		addIngredientToDistillate(distillate_02, sugarFermentation, 15.0, LocalDate.parse("2024-02-10"));
		addIngredientToDistillate(distillate_02, peatMoss, 22.5, LocalDate.parse("2024-01-20"));

		// Tilføj ingredienser til Classic Malt
		addIngredientToDistillate(distillate_03, barleyEvergreen, 800.0, LocalDate.parse("2024-03-01"));
		addIngredientToDistillate(distillate_03, waterSpring, 3500.0, LocalDate.parse("2024-02-15"));
		addIngredientToDistillate(distillate_03, yeastBrewers, 10.0, LocalDate.parse("2024-02-01"));
		addIngredientToDistillate(distillate_03, sugarFermentation, 20.0, LocalDate.parse("2024-02-10"));
		addIngredientToDistillate(distillate_03, peatMoss, 30.0, LocalDate.parse("2024-01-20"));

		// Tilføj ingredienser til Peat Fire
		addIngredientToDistillate(distillate_04, barleyEvergreen, 400.0, LocalDate.parse("2024-03-01"));
		addIngredientToDistillate(distillate_04, waterSpring, 1750.0, LocalDate.parse("2024-02-15"));
		addIngredientToDistillate(distillate_04, yeastBrewers, 5.0, LocalDate.parse("2024-02-01"));
		addIngredientToDistillate(distillate_04, sugarFermentation, 10.0, LocalDate.parse("2024-02-10"));
		addIngredientToDistillate(distillate_04, peatMoss, 15.0, LocalDate.parse("2024-01-20"));

		// Tilføj ingredienser til Sherry Kiss
		addIngredientToDistillate(distillate_05, barleyEvergreen, 700.0, LocalDate.parse("2024-03-01"));
		addIngredientToDistillate(distillate_05, waterSpring, 3062.5, LocalDate.parse("2024-02-15"));
		addIngredientToDistillate(distillate_05, yeastBrewers, 8.75, LocalDate.parse("2024-02-01"));
		addIngredientToDistillate(distillate_05, sugarFermentation, 17.5, LocalDate.parse("2024-02-10"));
		addIngredientToDistillate(distillate_05, peatMoss, 26.25, LocalDate.parse("2024-01-20"));

		// Tilføj ingredienser til Bourbon Blend
		addIngredientToDistillate(distillate_06, barleyEvergreen, 900.0, LocalDate.parse("2024-03-01"));
		addIngredientToDistillate(distillate_06, waterSpring, 3937.5, LocalDate.parse("2024-02-15"));
		addIngredientToDistillate(distillate_06, yeastBrewers, 11.25, LocalDate.parse("2024-02-01"));
		addIngredientToDistillate(distillate_06, sugarFermentation, 22.5, LocalDate.parse("2024-02-10"));
		addIngredientToDistillate(distillate_06, peatMoss, 33.75, LocalDate.parse("2024-01-20"));

		// Tilføj ingredienser til Loch Nectar
		addIngredientToDistillate(distillate_07, barleyEvergreen, 1000.0, LocalDate.parse("2024-03-01"));
		addIngredientToDistillate(distillate_07, waterSpring, 4375.0, LocalDate.parse("2024-02-15"));
		addIngredientToDistillate(distillate_07, yeastBrewers, 12.5, LocalDate.parse("2024-02-01"));
		addIngredientToDistillate(distillate_07, sugarFermentation, 25.0, LocalDate.parse("2024-02-10"));
		addIngredientToDistillate(distillate_07, peatMoss, 37.5, LocalDate.parse("2024-01-20"));

		// Tilføj ingredienser til Speyside Gold
		addIngredientToDistillate(distillate_08, barleyStairway, 800.0, LocalDate.parse("2024-03-01"));
		addIngredientToDistillate(distillate_08, waterSpring, 3500.0, LocalDate.parse("2024-02-15"));
		addIngredientToDistillate(distillate_08, yeastBrewers, 10.0, LocalDate.parse("2024-02-01"));
		addIngredientToDistillate(distillate_08, sugarFermentation, 20.0, LocalDate.parse("2024-02-10"));
		addIngredientToDistillate(distillate_08, peatMoss, 30.0, LocalDate.parse("2024-01-20"));

		// Tilføj ingredienser til Oak Reserve
		addIngredientToDistillate(distillate_09, barleyIrna, 750.0, LocalDate.parse("2024-03-01"));
		addIngredientToDistillate(distillate_09, waterSpring, 3281.25, LocalDate.parse("2024-02-15"));
		addIngredientToDistillate(distillate_09, yeastBrewers, 9.375, LocalDate.parse("2024-02-01"));
		addIngredientToDistillate(distillate_09, sugarFermentation, 18.75, LocalDate.parse("2024-02-10"));
		addIngredientToDistillate(distillate_09, peatMoss, 28.125, LocalDate.parse("2024-01-20"));

		// Tilføj ingredienser til Golden Highland
		addIngredientToDistillate(distillate_10, barleyIrna, 500.0, LocalDate.parse("2024-03-01"));
		addIngredientToDistillate(distillate_10, waterSpring, 2187.5, LocalDate.parse("2024-02-15"));
		addIngredientToDistillate(distillate_10, yeastBrewers, 6.25, LocalDate.parse("2024-02-01"));
		addIngredientToDistillate(distillate_10, sugarFermentation, 12.5, LocalDate.parse("2024-02-10"));
		addIngredientToDistillate(distillate_10, peatMoss, 18.75, LocalDate.parse("2024-01-20"));

		// MF tilf�j ingredienser
		// addIngredientToDistillate(distillate_11, barleyEvergreen, 850.0, LocalDate.parse("2024-03-01"));
		// addIngredientToDistillate(distillate_11, waterSpring, 3750.0, LocalDate.parse("2024-02-15"));
		// addIngredientToDistillate(distillate_11, yeastBrewers, 11.0, LocalDate.parse("2024-02-01"));
		// addIngredientToDistillate(distillate_11, sugarFermentation, 22.0, LocalDate.parse("2024-02-10"));
		// addIngredientToDistillate(distillate_11, peatMoss, 33.0, LocalDate.parse("2024-01-20"));
		//
		// addIngredientToDistillate(distillate_12, barleyGoldenPromise, 700.0, LocalDate.parse("2024-03-01"));
		// addIngredientToDistillate(distillate_12, waterSpring, 3062.5, LocalDate.parse("2024-02-15"));
		// addIngredientToDistillate(distillate_12, yeastBrewers, 8.75, LocalDate.parse("2024-02-01"));
		// addIngredientToDistillate(distillate_12, sugarFermentation, 17.5, LocalDate.parse("2024-02-10"));
		// addIngredientToDistillate(distillate_12, peatMoss, 26.25, LocalDate.parse("2024-01-20"));
		//
		// addIngredientToDistillate(distillate_13, barleyEvergreen, 900.0, LocalDate.parse("2024-03-01"));
		// addIngredientToDistillate(distillate_13, waterSpring, 3937.5, LocalDate.parse("2024-02-15"));
		// addIngredientToDistillate(distillate_13, yeastBrewers, 11.25, LocalDate.parse("2024-02-01"));
		// addIngredientToDistillate(distillate_13, sugarFermentation, 22.5, LocalDate.parse("2024-02-10"));
		// addIngredientToDistillate(distillate_13, peatMoss, 33.75, LocalDate.parse("2024-01-20"));
		//
		// addIngredientToDistillate(distillate_14, barleyStairway, 800.0, LocalDate.parse("2024-03-01"));
		// addIngredientToDistillate(distillate_14, waterSpring, 3500.0, LocalDate.parse("2024-02-15"));
		// addIngredientToDistillate(distillate_14, yeastBrewers, 10.0, LocalDate.parse("2024-02-01"));
		// addIngredientToDistillate(distillate_14, sugarFermentation, 20.0, LocalDate.parse("2024-02-10"));
		// addIngredientToDistillate(distillate_14, peatMoss, 30.0, LocalDate.parse("2024-01-20"));
		//
		// addIngredientToDistillate(distillate_15, barleyIrna, 750.0, LocalDate.parse("2024-03-01"));
		// addIngredientToDistillate(distillate_15, waterSpring, 3281.25, LocalDate.parse("2024-02-15"));
		// addIngredientToDistillate(distillate_15, yeastBrewers, 9.375, LocalDate.parse("2024-02-01"));
		// addIngredientToDistillate(distillate_15, sugarFermentation, 18.75, LocalDate.parse("2024-02-10"));
		// addIngredientToDistillate(distillate_15, peatMoss, 28.125, LocalDate.parse("2024-01-20"));
		//
		// addIngredientToDistillate(distillate_16, barleyIrna, 500.0, LocalDate.parse("2024-03-01"));
		// addIngredientToDistillate(distillate_16, waterSpring, 2187.5, LocalDate.parse("2024-02-15"));
		// addIngredientToDistillate(distillate_16, yeastBrewers, 6.25, LocalDate.parse("2024-02-01"));
		// addIngredientToDistillate(distillate_16, sugarFermentation, 12.5, LocalDate.parse("2024-02-10"));
		// addIngredientToDistillate(distillate_16, peatMoss, 18.75, LocalDate.parse("2024-01-20"));
		//
		// addIngredientToDistillate(distillate_17, barleyEvergreen, 1000.0, LocalDate.parse("2024-03-01"));
		// addIngredientToDistillate(distillate_17, waterSpring, 4375.0, LocalDate.parse("2024-02-15"));
		// addIngredientToDistillate(distillate_17, yeastBrewers, 12.5, LocalDate.parse("2024-02-01"));
		// addIngredientToDistillate(distillate_17, sugarFermentation, 25.0, LocalDate.parse("2024-02-10"));
		// addIngredientToDistillate(distillate_17, peatMoss, 37.5, LocalDate.parse("2024-01-20"));
		
		// Measuring alcohol percentage
		// Målinger for Highland Essence
		addAlcoholPercentage(distillate_01, 75.0, LocalDate.parse("2024-11-27")); // Start af produktionen
		addAlcoholPercentage(distillate_01, 72.5, LocalDate.parse("2024-11-30")); // Midlertidig måling
		addAlcoholPercentage(distillate_01, 70.0, LocalDate.parse("2024-12-10")); // Færdiggørelse

		// Målinger for Islay Smoke
		addAlcoholPercentage(distillate_02, 76.0, LocalDate.parse("2024-10-15")); // Start af produktionen
		addAlcoholPercentage(distillate_02, 74.0, LocalDate.parse("2024-10-20")); // Midt under processen
		addAlcoholPercentage(distillate_02, 71.5, LocalDate.parse("2024-11-01")); // Færdiggørelse

		// Målinger for Classic Malt
		addAlcoholPercentage(distillate_03, 72.0, LocalDate.parse("2024-09-01")); // Start af produktionen
		addAlcoholPercentage(distillate_03, 70.5, LocalDate.parse("2024-09-15")); // Midterste del
		addAlcoholPercentage(distillate_03, 68.0, LocalDate.parse("2024-09-30")); // Slutproces

		// Målinger for Peat Fire
		addAlcoholPercentage(distillate_04, 74.0, LocalDate.parse("2024-08-20")); // Start af produktionen
		addAlcoholPercentage(distillate_04, 71.5, LocalDate.parse("2024-08-30")); // Midt under processen
		addAlcoholPercentage(distillate_04, 70.0, LocalDate.parse("2024-09-10")); // Færdiggørelse

		// Målinger for Sherry Kiss
		addAlcoholPercentage(distillate_05, 73.5, LocalDate.parse("2024-07-10")); // Start af produktionen
		addAlcoholPercentage(distillate_05, 71.0, LocalDate.parse("2024-07-20")); // Midt under processen
		addAlcoholPercentage(distillate_05, 69.0, LocalDate.parse("2024-08-05")); // Færdiggørelse

		// Målinger for Bourbon Blend
		addAlcoholPercentage(distillate_06, 75.5, LocalDate.parse("2024-06-15")); // Start af produktionen
		addAlcoholPercentage(distillate_06, 73.0, LocalDate.parse("2024-06-25")); // Midt under processen
		addAlcoholPercentage(distillate_06, 70.5, LocalDate.parse("2024-07-05")); // Færdiggørelse

		// Målinger for Loch Nectar
		addAlcoholPercentage(distillate_07, 76.5, LocalDate.parse("2024-05-01")); // Start af produktionen
		addAlcoholPercentage(distillate_07, 74.0, LocalDate.parse("2024-05-15")); // Midt under processen
		addAlcoholPercentage(distillate_07, 72.0, LocalDate.parse("2024-05-30")); // Færdiggørelse

		// Målinger for Speyside Gold
		addAlcoholPercentage(distillate_08, 73.0, LocalDate.parse("2024-04-15")); // Start af produktionen
		addAlcoholPercentage(distillate_08, 71.0, LocalDate.parse("2024-04-25")); // Midt under processen
		addAlcoholPercentage(distillate_08, 69.5, LocalDate.parse("2024-05-10")); // Færdiggørelse

		// Målinger for Oak Reserve
		addAlcoholPercentage(distillate_09, 74.5, LocalDate.parse("2024-03-10")); // Start af produktionen
		addAlcoholPercentage(distillate_09, 72.0, LocalDate.parse("2024-03-20")); // Midt under processen
		addAlcoholPercentage(distillate_09, 70.0, LocalDate.parse("2024-03-30")); // Færdiggørelse

		// Målinger for Golden Highland
		addAlcoholPercentage(distillate_10, 75.0, LocalDate.parse("2024-02-20")); // Start af produktionen
		addAlcoholPercentage(distillate_10, 73.5, LocalDate.parse("2024-03-01")); // Midt under processen
		addAlcoholPercentage(distillate_10, 70.5, LocalDate.parse("2024-03-15")); // Færdiggørelse

		addAlcoholPercentage(distillate_11, 74.0, LocalDate.parse("2023-05-10")); // Start af produktionen
		addAlcoholPercentage(distillate_11, 72.0, LocalDate.parse("2023-05-20")); // Midt under processen
		addAlcoholPercentage(distillate_11, 70.0, LocalDate.parse("2023-06-10")); // Færdiggørelse

		addAlcoholPercentage(distillate_12, 75.0, LocalDate.parse("2022-08-15")); // Start af produktionen
		addAlcoholPercentage(distillate_12, 73.0, LocalDate.parse("2022-08-25")); // Midt under processen
		addAlcoholPercentage(distillate_12, 71.0, LocalDate.parse("2022-09-15")); // Færdiggørelse

		addAlcoholPercentage(distillate_13, 76.0, LocalDate.parse("2020-03-01")); // Start af produktionen
		addAlcoholPercentage(distillate_13, 74.0, LocalDate.parse("2020-03-15")); // Midt under processen
		addAlcoholPercentage(distillate_13, 72.0, LocalDate.parse("2020-04-01")); // Færdiggørelse

		addAlcoholPercentage(distillate_14, 73.5, LocalDate.parse("2021-07-01")); // Start af produktionen
		addAlcoholPercentage(distillate_14, 71.5, LocalDate.parse("2021-07-15")); // Midt under processen
		addAlcoholPercentage(distillate_14, 69.5, LocalDate.parse("2021-08-01")); // Færdiggørelse

		addAlcoholPercentage(distillate_15, 74.5, LocalDate.parse("2021-09-01")); // Start af produktionen
		addAlcoholPercentage(distillate_15, 72.5, LocalDate.parse("2021-09-15")); // Midt under processen
		addAlcoholPercentage(distillate_15, 70.5, LocalDate.parse("2021-10-01")); // Færdiggørelse

		addAlcoholPercentage(distillate_16, 75.5, LocalDate.parse("2021-11-01")); // Start af produktionen
		addAlcoholPercentage(distillate_16, 73.5, LocalDate.parse("2021-11-15")); // Midt under processen
		addAlcoholPercentage(distillate_16, 71.5, LocalDate.parse("2021-12-01")); // Færdiggørelse

		addAlcoholPercentage(distillate_17, 76.5, LocalDate.parse("2022-01-01")); // Start af produktionen
		addAlcoholPercentage(distillate_17, 74.5, LocalDate.parse("2022-01-15")); // Midt under processen
		addAlcoholPercentage(distillate_17, 72.5, LocalDate.parse("2022-02-01")); // Færdiggørelse

		// Production cut information
		// Production cut information for Highland Essence
		addProductionCutInformation(
				distillate_01,
				"Cut started with heads removal at 72% ABV. Hearts were retained between 70% and 65% ABV for optimal balance. Tails were discarded below 65% ABV.",
				LocalDate.parse("2024-11-28")
		);

		// Production cut information for Islay Smoke
		addProductionCutInformation(
				distillate_02,
				"Heads removed at 73% ABV to avoid harshness. Smoky hearts selected between 70% and 62% ABV. Significant phenolic compounds detected in tails and discarded.",
				LocalDate.parse("2024-10-20")
		);

		// Production cut information for Classic Malt
		addProductionCutInformation(
				distillate_03,
				"Clean heads removal at 71% ABV. Hearts retained between 70% and 65% ABV for a smooth malt backbone. Subtle tails flavors avoided due to sweetness focus.",
				LocalDate.parse("2024-09-05")
		);

		// Production cut information for Peat Fire
		addProductionCutInformation(
				distillate_04,
				"Aggressive heads cut at 73% ABV to maintain peat integrity. Hearts maintained at 68% ABV with a rich smoky character. Tails below 60% were removed.",
				LocalDate.parse("2024-08-25")
		);

		// Production cut information for Sherry Kiss
		addProductionCutInformation(
				distillate_05,
				"Heads removed at 72% ABV. Rich, fruity hearts retained between 70% and 63% ABV. Light sweet tails (<60%) were excluded for balance.",
				LocalDate.parse("2024-07-15")
		);

		// Production cut information for Bourbon Blend
		addProductionCutInformation(
				distillate_06,
				"Heads were sharply cut at 74% ABV. Hearts retained between 72% and 65%. Mild tails were discarded under 62% ABV to match bourbon profile.",
				LocalDate.parse("2024-06-20")
		);

		// Production cut information for Loch Nectar
		addProductionCutInformation(
				distillate_07,
				"Heads excised at 73% ABV. Clean, floral hearts retained between 71% and 64% ABV for maximum balance. Tails excluded for cleaner flavor.",
				LocalDate.parse("2024-05-10")
		);

		// Production cut information for Speyside Gold
		addProductionCutInformation(
				distillate_08,
				"Heads removed at 72%. Delicate hearts preserved between 70% and 63% ABV. Tails at <60% ABV omitted for a softer profile.",
				LocalDate.parse("2024-04-20")
		);

		// Production cut information for Oak Reserve
		addProductionCutInformation(
				distillate_09,
				"Light heads removed at 71% ABV. Full-bodied hearts between 70% and 64% ABV. Subtle tails at ~60% discarded to achieve exceptional balance.",
				LocalDate.parse("2024-03-15")
		);

		// Production cut information for Golden Highland
		addProductionCutInformation(
				distillate_10,
				"Clean heads excluded at 72% ABV. Hearts retained between 70% and 65% ABV. Tails removed at <58% ABV to preserve creamy malt notes.",
				LocalDate.parse("2024-02-25")
		);

		addProductionCutInformation(
				distillate_11,
				"Heads sharply removed at 74% ABV. Rich, smoky hearts retained between 72% and 65%. Tails omitted below 60% ABV to enhance smokiness.",
				LocalDate.parse("2024-02-01")
		);

		addProductionCutInformation(
				distillate_12,
				"Heads cut at 73% ABV for a clean profile. Floral hearts retained between 71% and 64% ABV. Tails discarded below 63% ABV to preserve delicacy.",
				LocalDate.parse("2024-01-15")
		);

		addProductionCutInformation(
				distillate_13,
				"Heads excluded at 72% ABV. Hearts held between 70% and 63% ABV for a robust flavor. Tails rejected below 60% ABV for crisp winter notes.",
				LocalDate.parse("2023-12-20")
		);

		addProductionCutInformation(
				distillate_14,
				"Light heads removal at 71% ABV. Bright, grassy hearts retained between 70% and 65% ABV. Minimal tails included for a touch of sweetness.",
				LocalDate.parse("2023-11-25")
		);

		addProductionCutInformation(
				distillate_15,
				"Heads cut aggressively at 73% ABV to highlight autumnal warmth. Hearts maintained between 72% and 64% ABV. Tails omitted below 61% ABV for clarity.",
				LocalDate.parse("2023-10-15")
		);

		addProductionCutInformation(
				distillate_16,
				"Heads removed at 74% ABV. Dark, rich hearts preserved between 72% and 65% ABV. Deep tails excluded below 62% ABV to maintain a mysterious profile.",
				LocalDate.parse("2023-09-05")
		);

		addProductionCutInformation(
				distillate_17,
				"Heads excised at 72% ABV for a refined touch. Smooth hearts kept between 70% and 64% ABV. Light tails (<60%) removed for velvety balance.",
				LocalDate.parse("2023-08-20")
		);

		// Story lines
		// Storyline for Highland Essence
		addStoryToDistillate(
				distillate_01,
				"Highland Essence stems from a vision to create a quintessential Highland whisky. This whisky balances subtle sweetness and fresh malty flavors, representing the vibrant fields of golden barley from the heart of Scotland.",
				LocalDate.parse("2024-11-27")
		);

		// Storyline for Islay Smoke
		addStoryToDistillate(
				distillate_02,
				"Islay Smoke is inspired by the rugged coasts and powerful winds of Islay. Crafted with smoky, peaty notes and a touch of maritime brine, this whisky was developed to embody the spirit of Islay’s wilderness.",
				LocalDate.parse("2024-10-15")
		);

		// Storyline for Classic Malt
		addStoryToDistillate(
				distillate_03,
				"Classic Malt aims to redefine simplicity by focusing on the purity of malt. Designed to be a versatile whisky, it was crafted with the idea of bringing smooth malty tones together with a creamy, balanced finish.",
				LocalDate.parse("2024-09-01")
		);

		// Storyline for Peat Fire
		addStoryToDistillate(
				distillate_04,
				"Peat Fire was developed to highlight a bold, smoky character for connoisseurs who seek intensity. Inspired by roaring fires on a cold winter's evening, this whisky delivers smokiness with a warm, lingering finish.",
				LocalDate.parse("2024-08-20")
		);

		// Storyline for Sherry Kiss
		addStoryToDistillate(
				distillate_05,
				"Sherry Kiss is a love letter to the art of aging whisky in sherry casks. With notes of dried fruits and rich sweetness, this whisky was envisioned to create a romantic interplay between malt and oak traditions.",
				LocalDate.parse("2024-07-10")
		);

		// Storyline for Bourbon Blend
		addStoryToDistillate(
				distillate_06,
				"Bourbon Blend pays homage to the whiskey traditions of America. Combining the creamy richness of bourbon influence, this whisky seeks to capture the warm, sweet oak notes loved by bourbon enthusiasts while retaining a uniquely Scottish character.",
				LocalDate.parse("2024-06-15")
		);

		// Storyline for Loch Nectar
		addStoryToDistillate(
				distillate_07,
				"Loch Nectar draws its inspiration from the pristine waters of Scotland's lochs. This whisky was crafted with the intent of showcasing floral and delicate malty tones, creating a whisky as pure as the landscape it hails from.",
				LocalDate.parse("2024-05-01")
		);

		// Storyline for Speyside Gold
		addStoryToDistillate(
				distillate_08,
				"Speyside Gold reflects the elegance and richness of the whiskies from Speyside. With soft, fruity notes and a gentle finish, this whisky was created to celebrate the region's rich whisky heritage.",
				LocalDate.parse("2024-04-15")
		);

		// Storyline for Oak Reserve
		addStoryToDistillate(
				distillate_09,
				"Oak Reserve was developed with the idea of capturing the perfect harmony of wood and whisky. With deep, oaky undertones and a long, warm finish, it honors the craftsmanship of seasoned oak cask maturation.",
				LocalDate.parse("2024-03-10")
		);

		// Storyline for Golden Highland
		addStoryToDistillate(
				distillate_10,
				"Golden Highland is a tribute to the golden fields of barley that serve as the foundation of Scottish whisky. With creamy malt flavors and a touch of sweetness, it embodies the beauty of the Highlands.",
				LocalDate.parse("2024-02-20")
		);

		// Storyline for distillate 11
		addStoryToDistillate(
				distillate_11,
				"Distillate 11 embodies a bold exploration of smoky richness, crafted to deliver a robust and unforgettable flavor profile that lingers long on the palate.",
				LocalDate.parse("2024-02-01")
		);

		// Storyline for distillate 12
		addStoryToDistillate(
				distillate_12,
				"Distillate 12 was designed with elegance in mind, showcasing floral notes and a delicate balance that celebrates the softer side of whisky craftsmanship.",
				LocalDate.parse("2024-01-15")
		);

		// Storyline for distillate 13
		addStoryToDistillate(
				distillate_13,
				"Distillate 13 captures the essence of a winter�s night, with crisp, clean flavors and a warming finish that evokes the serenity of snow-covered landscapes.",
				LocalDate.parse("2023-12-20")
		);

		// Storyline for distillate 14
		addStoryToDistillate(
				distillate_14,
				"Distillate 14 is inspired by the fresh blooms of spring, offering a vibrant, light profile with hints of grass and subtle sweetness that heralds new beginnings.",
				LocalDate.parse("2023-11-15")
		);

		// Storyline for distillate 15
		addStoryToDistillate(
				distillate_15,
				"Distillate 15 is a tribute to traditional techniques, delivering a harmonious combination of smooth malty flavors and subtle oak undertones for a timeless taste.",
				LocalDate.parse("2023-10-10")
		);

		// Storyline for distillate 16
		addStoryToDistillate(
				distillate_16,
				"Distillate 16 was crafted to explore uncharted depths of flavor, with a bold profile that combines earthy tones and a touch of spice for an adventurous experience.",
				LocalDate.parse("2023-09-05")
		);

		// Storyline for distillate 17
		addStoryToDistillate(
				distillate_17,
				"Distillate 17 reflects the artistry of cask maturation, offering a complex interplay of dried fruits, subtle vanilla, and a rich, long-lasting finish.",
				LocalDate.parse("2023-08-01")
		);
		


		// *** Fillings ***

		// One distillate capacity fully used across casks
		Production.fillDistillateIntoCask(distillate_06, cask_01, 100, LocalDate.parse("2024-01-01"));
		Production.fillDistillateIntoCask(distillate_06, cask_02, 100, LocalDate.parse("2024-01-01"));
		Production.fillDistillateIntoCask(distillate_06, cask_03, 100, LocalDate.parse("2024-01-01"));
		Production.fillDistillateIntoCask(distillate_06, cask_04, 150, LocalDate.parse("2024-01-01"));
		Production.fillDistillateIntoCask(distillate_11, cask_08, 100, LocalDate.parse("2024-01-01"));
		Production.fillDistillateIntoCask(distillate_11, cask_09, 100, LocalDate.parse("2024-01-01"));

		// Cask holding more than two distillates and fully used
		Production.fillDistillateIntoCask(distillate_10, cask_02, 50, LocalDate.parse("2024-02-01"));
		Production.fillDistillateIntoCask(distillate_08, cask_02, 100, LocalDate.parse("2024-02-01"));
		Production.fillDistillateIntoCask(distillate_12, cask_08, 50, LocalDate.parse("2024-02-01"));
		Production.fillDistillateIntoCask(distillate_13, cask_08, 100, LocalDate.parse("2024-02-01"));

		// Cask having more than two distillates and having remaining capacity
		Production.fillDistillateIntoCask(distillate_07, cask_04, 50, LocalDate.parse("2024-03-01"));
		Production.fillDistillateIntoCask(distillate_08, cask_04, 25, LocalDate.parse("2024-04-01"));
		Production.fillDistillateIntoCask(distillate_09, cask_04, 15, LocalDate.parse("2024-05-01"));
		Production.fillDistillateIntoCask(distillate_10, cask_04, 75, LocalDate.parse("2024-06-01"));
		Production.fillDistillateIntoCask(distillate_14, cask_10, 50, LocalDate.parse("2024-03-01"));
		Production.fillDistillateIntoCask(distillate_15, cask_10, 25, LocalDate.parse("2024-04-01"));
		Production.fillDistillateIntoCask(distillate_16, cask_10, 15, LocalDate.parse("2024-05-01"));
		Production.fillDistillateIntoCask(distillate_17, cask_10, 75, LocalDate.parse("2024-06-01"));

		Production.fillDistillateIntoCask(distillate_11, cask_05, 100, LocalDate.parse("2024-07-01"));
		Production.fillDistillateIntoCask(distillate_12, cask_06, 100, LocalDate.parse("2024-08-01"));
		Production.fillDistillateIntoCask(distillate_12, cask_11, 100, LocalDate.parse("2024-07-01"));
		Production.fillDistillateIntoCask(distillate_13, cask_12, 100, LocalDate.parse("2024-08-01"));
		Production.fillDistillateIntoCask(distillate_14, cask_13, 100, LocalDate.parse("2024-09-01"));
		Production.fillDistillateIntoCask(distillate_15, cask_14, 100, LocalDate.parse("2024-10-01"));
		Production.fillDistillateIntoCask(distillate_16, cask_15, 100, LocalDate.parse("2024-11-01"));
		

		// *** Bottling ***
		Production.caskBottling(cask_04,25,LocalDate.parse("2024-12-01"));

	}

	@SuppressWarnings("unused")
	public static void loadWarehousing() {
		Warehouse warehouse = Warehousing.createWarehouse("Lager 1", "Lagervej 1");
		Warehouse warehouse2 = Warehousing.createWarehouse("Lager 2", "Lagervej 2");

		LoggerObserver logger = new LoggerObserver();

		// Register the observer
		warehouse.registerWarehousingObserver(logger);

		StorageRack rack1 = Warehousing.createStorageRack("Rack 1", 10);
		StorageRack rack2 = Warehousing.createStorageRack("Rack 2", 10);

		warehouse.addStorageRack("Rack 1", rack1);
		warehouse.addStorageRack("Rack 2", rack2);


		Supplier supplier1 = new Supplier("Supplier 1", "Address 1", "Phone 1", "Nice story");

		Ingredient ingredient1 = new Ingredient("Lars Tyndskids Grain", "Finest Grain ", 1,
				LocalDate.of(2024, 11, 27), LocalDate.of(2024, 12, 27), 1, supplier1, Unit.TONNES,
				IngredientType.GRAIN);

		rack1.addItem(1, ingredient1);
//		rack1.moveItem(ingredient1, 2, 0);

	}

	public static void loadBatchArea() {

		// =================== TASTE PROFILES =================

		ArrayList<TastingNote> notes1 = new ArrayList<>();
		tasteProfile1 = BatchArea.createNewTasteProfile("Vienna Malt",
				"\'Velvet Ember\' is a taste profile that combines the silky smoothness of honeyed vanilla with a subtle smoky warmth, finished with a whisper of spiced citrus zest for a lingering, refined complexity.",
				notes1);

		ArrayList<TastingNote> notes2 = new ArrayList<>();
		tasteProfile2 = BatchArea.createNewTasteProfile("Frosted Grove",
				"\'Frosted Grove\' delivers a crisp and refreshing blend of cool mint and green apple, accented by delicate floral undertones and a hint of piney freshness for an invigorating finish.",
				notes2);

		ArrayList<TastingNote> notes3 = new ArrayList<>();
		tasteProfile3 = BatchArea.createNewTasteProfile("Crimson Ember",
				"\'Crimson Ember\' offers a bold fusion of dark cherry and spiced cinnamon, balanced with earthy undertones of roasted cacao and a whisper of smoky oak for a rich, warming experience.",
				notes3);

		ArrayList<TastingNote> notes4 = new ArrayList<>();
		tasteProfile4 = BatchArea.createNewTasteProfile("Golden Harvest",
				"\'Golden Harvest\' combines the buttery sweetness of caramelized pears with toasted almonds, complemented by a hint of nutmeg and a smooth, creamy finish reminiscent of warm custard.",
				notes4);

		ArrayList<TastingNote> notes7 = new ArrayList<>();
		tasteProfile7 = BatchArea.createNewTasteProfile("Apple Delight",
				"\'Apple Delight\' brings crisp apple flavors with hints of vanilla, caramel, and subtle smoky undertones, creating a balanced and refreshing profile.",
				notes7);

		ArrayList<TastingNote> notes8 = new ArrayList<>();
		tasteProfile8 = BatchArea.createNewTasteProfile("Golden Delights",
				"\'Golden Delights\' features rich vanilla and caramel, with a touch of oak creating a luxurious and sweet profile.",
				notes8);

		ArrayList<TastingNote> notes9 = new ArrayList<>();
		tasteProfile9 = BatchArea.createNewTasteProfile("Smoky Delight",
				"\'Smoky Delight\' blends peaty and smoky flavors with roasted coffee for a robust and warming experience.",
				notes9);

		ArrayList<TastingNote> notes10 = new ArrayList<>();
		tasteProfile10 = BatchArea.createNewTasteProfile("Spiced Orchard",
				"\'Spiced Orchard\' highlights pear and cinnamon, with hints of raisin creating a sweet and spiced profile.",
				notes10);

		ArrayList<TastingNote> notes11 = new ArrayList<>();
		tasteProfile11 = BatchArea.createNewTasteProfile("Rich Chocolate Oak",
				"\'Rich Chocolate Oak\' offers dark chocolate and toffee flavors, complemented by oak for a deep and luxurious profile.",
				notes11);

		ArrayList<TastingNote> notes12 = new ArrayList<>();
		tasteProfile12 = BatchArea.createNewTasteProfile("Lemon Zest Edition",
				"\'Lemon Zest Edition\' combines zesty lemon with vanilla and a touch of black pepper for a bright and refreshing finish.",
				notes12);

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
		notes7.add(TastingNote.APPLE);
		notes7.add(TastingNote.VANILLA);
		notes7.add(TastingNote.CARAMEL);
		notes7.add(TastingNote.SMOKEY);
		notes8.add(TastingNote.VANILLA);
		notes8.add(TastingNote.CARAMEL);
		notes8.add(TastingNote.OAK);
		notes9.add(TastingNote.PEATY);
		notes9.add(TastingNote.SMOKEY);
		notes9.add(TastingNote.COFFEE);
		notes10.add(TastingNote.PEAR);
		notes10.add(TastingNote.CINNAMON);
		notes10.add(TastingNote.RAISIN);
		notes11.add(TastingNote.DARK_CHOCOLATE);
		notes11.add(TastingNote.OAK);
		notes11.add(TastingNote.TOFFEE);
		notes12.add(TastingNote.LEMON);
		notes12.add(TastingNote.VANILLA);
		notes12.add(TastingNote.BLACK_PEPPER);

		// =================== FORMULAE =================

		HashMap<TasteProfile, Double> blueprint1 = new HashMap<>();
		HashMap<TasteProfile, Double> blueprint2 = new HashMap<>();
		HashMap<TasteProfile, Double> blueprint3 = new HashMap<>();
		HashMap<TasteProfile, Double> blueprint4 = new HashMap<>();
		HashMap<TasteProfile, Double> blueprint7 = new HashMap<>();
		HashMap<TasteProfile, Double> blueprint8 = new HashMap<>();
		HashMap<TasteProfile, Double> blueprint9 = new HashMap<>();
		HashMap<TasteProfile, Double> blueprint10 = new HashMap<>();
		HashMap<TasteProfile, Double> blueprint11 = new HashMap<>();
		HashMap<TasteProfile, Double> blueprint12 = new HashMap<>();

		blueprint1.put(tasteProfile1, 100.0);
		blueprint2.put(tasteProfile4, 100.0);
		blueprint3.put(tasteProfile4, 10.0);
		blueprint3.put(tasteProfile2, 90.0);
		blueprint4.put(tasteProfile1, 25.0);
		blueprint4.put(tasteProfile3, 50.0);
		blueprint4.put(tasteProfile4, 25.0);
		blueprint7.put(tasteProfile7, 100.0);
		blueprint8.put(tasteProfile8, 100.0);
		blueprint9.put(tasteProfile9, 100.0);
		blueprint10.put(tasteProfile10, 100.0);
		blueprint11.put(tasteProfile11, 100.0);
		blueprint12.put(tasteProfile12, 100.0);

		Formula formula1 = BatchArea.createNewFormula("STEKKJASTAUR", blueprint1);
		Formula formula2 = BatchArea.createNewFormula("Single Malt", blueprint2);
		Formula formula3 = BatchArea.createNewFormula("Blended Deep #5", blueprint3);
		Formula formula4 = BatchArea.createNewFormula("Secret Formula", blueprint4);
		Formula formula7 = BatchArea.createNewFormula("Apple Delight", blueprint7);
		Formula formula8 = BatchArea.createNewFormula("Golden Delights", blueprint8);
		Formula formula9 = BatchArea.createNewFormula("Smoky Delight", blueprint9);
		Formula formula10 = BatchArea.createNewFormula("Spiced Orchard", blueprint10);
		Formula formula11 = BatchArea.createNewFormula("Rich Chocolate Oak", blueprint11);
		Formula formula12 = BatchArea.createNewFormula("Lemon Zest Edition", blueprint12);

		for (TastingNote tn : notes1) {
			tasteProfile1.addTastingNote(tn);
		}

		// =================== PRODUCTS =================

		Product product1 = BatchArea.createNewProduct("STEKKJASTAUR", 1000);
		Product product2 = BatchArea.createNewProduct("Glød 3.1", 500);
		Product product3 = BatchArea.createNewProduct("Muld 1.1", 1000);
		Product product4 = BatchArea.createNewProduct("New Years Special Edition", 1000);
		Product product7 = BatchArea.createNewProduct("Apple Delight", 1000);
		Product product8 = BatchArea.createNewProduct("Golden Delights", 1000);
		Product product9 = BatchArea.createNewProduct("Smoky Delight", 750);
		Product product10 = BatchArea.createNewProduct("Spiced Orchard", 1000);
		Product product11 = BatchArea.createNewProduct("Rich Chocolate Oak", 800);
		Product product12 = BatchArea.createNewProduct("Lemon Zest Edition", 1000);

		product1.defineFormula(formula1);
		product2.defineFormula(formula2);
		product3.defineFormula(formula3);
		product4.defineFormula(formula4);
		product7.defineFormula(formula7);
		product8.defineFormula(formula8);
		product9.defineFormula(formula9);
		product10.defineFormula(formula10);
		product11.defineFormula(formula11);
		product12.defineFormula(formula12);

		// =================== CASKS =================

		cask_01.setTasteProfile(tasteProfile1);
		cask_02.setTasteProfile(tasteProfile2);
		cask_03.setTasteProfile(tasteProfile3);
		cask_04.setTasteProfile(tasteProfile4);
		cask_05.setTasteProfile(tasteProfile1);
		cask_06.setTasteProfile(tasteProfile2);
		cask_07.setTasteProfile(tasteProfile9); // Smoky Delight
		cask_08.setTasteProfile(tasteProfile8); // Golden Delights
		cask_09.setTasteProfile(tasteProfile10); // Spiced Orchard
		cask_10.setTasteProfile(tasteProfile11); // Rich Chocolate Oak
		cask_11.setTasteProfile(tasteProfile12); // Lemon Zest Edition
		cask_12.setTasteProfile(tasteProfile7); // Apple Delight
		cask_13.setTasteProfile(tasteProfile3); // Crimson Ember
		cask_14.setTasteProfile(tasteProfile4); // Golden Harvest
		cask_15.setTasteProfile(tasteProfile9); // Coffee & Dark Chocolate

	}

}
