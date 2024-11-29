package Storage;

import Controllers.Production;
import Controllers.Warehousing;
import Enumerations.Unit;
import Production.Distillate;
import Production.Distiller;
import Warehousing.Supplier;
import Warehousing.Cask;

import java.time.LocalDate;

public abstract class initStorage {

    public static void loadProduction(){
        Supplier sub_01 = Warehousing.createSupplier(
                "Highland Barley Co.",
                "123 Grain Street, Inverness, Scotland",
                "Supplier of premium malted barley.",
                "Highland Barley Co. has been delivering high-quality, locally grown barley to distilleries since 1975."
        );

        Supplier sub_02 = Warehousing.createSupplier(
                "Pure Spring Water Co.",
                "45 Riverbank Lane, Loch Ness, Scotland",
                "Provider of freshwater for distillation.",
                "Pure Spring Water Co. sources its water from the pristine Loch Ness, ensuring the highest purity for whisky production."
        );

        Supplier sub_03 = Warehousing.createSupplier(
                "Oak Master Barrels",
                "7 Barrel Road, Speyside, Scotland",
                "Supplier of American oak and European oak casks.",
                "Crafting barrels from sustainable sources, Oak Master Barrels has supported traditional whisky making for over a century."
        );

        Supplier sub_04 = Warehousing.createSupplier(
                "Peat & Smolder",
                "9 Rookery Path, Islay, Scotland",
                "Supplier of high-quality peat for smoking malts.",
                "Peat & Smolder specializes in Islay peat, perfect for imparting the rich, smoky profile loved by whisky connoisseurs."
        );

        Supplier sub_05 = Warehousing.createSupplier(
                "Sherry Bliss Co.",
                "28 Winery Drive, Jerez, Spain",
                "Supplier of seasoned sherry casks.",
                "Known for their rich sherry heritage, Sherry Bliss Co. provides barrels seasoned with fine Spanish sherry for finishing whisky."
        );

        Distiller distiller_01 = Production.createDistiller(
                "Ewan MacGregor",
                "EMG",
                "An expert in fine single malt production with a focus on classic Highland styles."
        );

        Distiller distiller_02 = Production.createDistiller(
                "Lachlan Stewart",
                "LS",
                "Passionate about combining traditional whisky techniques with innovative flavor profiles."
        );

        Distiller distiller_03 = Production.createDistiller(
                "Fiona Campbell",
                "FC",
                "Dedicated to perfecting peat-smoked whiskies inspired by Islay tradition."
        );

        Distiller distiller_04 = Production.createDistiller(
                "Iain MacDougall",
                "IMD",
                "Focused on crafting small batch whiskies that celebrate natural ingredients."
        );

        Distiller distiller_05 = Production.createDistiller(
                "Moira Graham",
                "MG",
                "Whisky innovator with a love for experimenting with unique cask finishes."
        );

        Distiller distiller_06 = Production.createDistiller(
                "Douglas Sinclair",
                "DS",
                "A distilling veteran known for creating rich and complex whiskies with deep character."
        );

        Distiller distiller_07 = Production.createDistiller(
                "Isla Ferguson",
                "IF",
                "Specialist in blending and balancing bold and delicate whisky flavors."
        );

        Cask cask_01 = Warehousing.createCask(1, 200, Unit.LITERS, sub_03); // Bourbon cask from Oak Master Barrels
        Cask cask_02 = Warehousing.createCask(2, 250, Unit.LITERS, sub_03); // American Oak cask from Oak Master Barrels
        Cask cask_03 = Warehousing.createCask(3, 225, Unit.LITERS, sub_05); // Sherry cask from Sherry Bliss Co.
        Cask cask_04 = Warehousing.createCask(4, 500, Unit.LITERS, sub_03); // Large European Oak cask from Oak Master Barrels
        Cask cask_05 = Warehousing.createCask(5, 300, Unit.LITERS, sub_05); // Seasoned sherry cask from Sherry Bliss Co.
        Cask cask_06 = Warehousing.createCask(6, 200, Unit.LITERS, sub_03); // Toasted Oak cask from Oak Master Barrels
        Cask cask_07 = Warehousing.createCask(7, 225, Unit.LITERS, sub_03); // Ex-Port cask from Oak Master Barrels
        Cask cask_08 = Warehousing.createCask(8, 180, Unit.LITERS, sub_03); // Peated Oak cask from Peat & Smolder
        Cask cask_09 = Warehousing.createCask(9, 300, Unit.LITERS, sub_05); // Spanish Oak cask from Sherry Bliss Co.
        Cask cask_10 = Warehousing.createCask(10, 600, Unit.LITERS, sub_03); // Large blending cask from Oak Master Barrels

        Distillate distillate_01 = Production.createDistillate(
                "Highland Essence",
                LocalDate.parse("2024-11-27"),
                LocalDate.parse("2024-12-24"),
                500,
                distiller_01,
                Unit.LITERS
        );

        Distillate distillate_02 = Production.createDistillate(
                "Islay Smoke",
                LocalDate.parse("2024-10-15"),
                LocalDate.parse("2024-11-10"),
                300,
                distiller_02,
                Unit.LITERS
        );

        Distillate distillate_03 = Production.createDistillate(
                "Classic Malt",
                LocalDate.parse("2024-09-01"),
                LocalDate.parse("2024-09-30"),
                400,
                distiller_03,
                Unit.LITERS
        );

        Distillate distillate_04 = Production.createDistillate(
                "Peat Fire",
                LocalDate.parse("2024-08-20"),
                LocalDate.parse("2024-09-15"),
                200,
                distiller_04,
                Unit.LITERS
        );

        Distillate distillate_05 = Production.createDistillate(
                "Sherry Kiss",
                LocalDate.parse("2024-07-10"),
                LocalDate.parse("2024-08-05"),
                350,
                distiller_05,
                Unit.LITERS
        );

        Distillate distillate_06 = Production.createDistillate(
                "Bourbon Blend",
                LocalDate.parse("2024-06-15"),
                LocalDate.parse("2024-07-10"),
                450,
                distiller_06,
                Unit.LITERS
        );

        Distillate distillate_07 = Production.createDistillate(
                "Loch Nectar",
                LocalDate.parse("2024-05-01"),
                LocalDate.parse("2024-05-30"),
                500,
                distiller_07,
                Unit.LITERS
        );

        Distillate distillate_08 = Production.createDistillate(
                "Speyside Gold",
                LocalDate.parse("2024-04-15"),
                LocalDate.parse("2024-05-12"),
                400,
                distiller_01,
                Unit.LITERS
        );

        Distillate distillate_09 = Production.createDistillate(
                "Oak Reserve",
                LocalDate.parse("2024-03-10"),
                LocalDate.parse("2024-04-05"),
                375,
                distiller_02,
                Unit.LITERS
        );

        Distillate distillate_10 = Production.createDistillate(
                "Golden Highland",
                LocalDate.parse("2024-02-20"),
                LocalDate.parse("2024-03-15"),
                250,
                distiller_03,
                Unit.LITERS
        );

    }

}
