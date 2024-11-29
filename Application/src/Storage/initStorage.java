package Storage;

import Controllers.Production;
import Controllers.Warehousing;
import Enumerations.IngredientType;
import Enumerations.Unit;
import Production.Distillate;
import Production.Distiller;
import Warehousing.Supplier;
import Warehousing.Cask;
import Warehousing.Warehouse;
import Warehousing.StorageRack;
import Warehousing.Ingredient;

import java.time.LocalDate;

public abstract class initStorage {

    public static void loadProduction(){
        Supplier testSupplier = Warehousing.createSupplier("Knud", "Knudvej 1", "Kan levere alt!",
                "Passion for god Whisky");

        Distiller distiller = Production.createDistiller("Per", "PP","Den bedste!");

        Distillate d1 = Production.createDistillate("Jul 24", LocalDate.parse("2024-11-27"),
                LocalDate.parse("2024-12-24"),100,distiller, Unit.LITERS);

        Cask cask = Warehousing.createCask(1,50,Unit.LITERS,testSupplier);

        System.out.println("*** Distillate ****");
        System.out.println(d1.getListInfo());
        System.out.println("*** Cask ****");
        System.out.println(cask.getListInfo());
        System.out.println("*** Cask Filling Stack****");
        System.out.println(cask.getFillingTextLines());

        Production.fillDistillateIntoCask(d1,cask,10,LocalDate.parse("2024-11-28"));

        System.out.println("*** Distillate ****");
        System.out.println(d1.getListInfo());
        System.out.println("*** Cask ****");
        System.out.println(cask.getListInfo());
        System.out.println("*** Cask Filling Stack****");
        System.out.println(cask.getFillingTextLines());

        System.out.println();

        Production.fillDistillateIntoCask(d1,cask,20,LocalDate.parse("2024-11-28"));

        System.out.println("*** Distillate ****");
        System.out.println(d1.getListInfo());
        System.out.println("*** Cask ****");
        System.out.println(cask.getListInfo());
        System.out.println("*** Cask Filling Stack****");
        System.out.println(cask.getFillingTextLines());

        Production.fillDistillateIntoCask(d1,cask,5,LocalDate.parse("2024-11-28"));

        System.out.println("*** Distillate ****");
        System.out.println(d1.getListInfo());
        System.out.println("*** Cask ****");
        System.out.println(cask.getListInfo());
        System.out.println("*** Cask Filling Stack****");
        System.out.println(cask.getFillingTextLines());

        Production.fillDistillateIntoCask(d1,cask,20,LocalDate.parse("2024-11-28"));

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

        StorageRack rack1 = Warehousing.createStorageRack("Rack 1", 100);
        StorageRack rack2 = Warehousing.createStorageRack("Rack 2", 100);
        StorageRack rack3 = Warehousing.createStorageRack("Rack 3", 100);
        StorageRack rack4 = Warehousing.createStorageRack("Rack 4", 100);

        warehouse.addStorageRack("W1R1",rack1);
        warehouse.addStorageRack("W1R2", rack2);

        Supplier supplier1 = new Supplier("Supplier 1", "Address 1", "Phone 1", "Nice story");

        Ingredient ingredient1 = new Ingredient("Lars Tyndskids Grain", "Finest Grain ", 1,
                LocalDate.of(2024,11,27), LocalDate.of(2024,12,27), 1, supplier1, Unit.TONNES, IngredientType.GRAIN);

        rack1.addItem(2, ingredient1);

    }

}
