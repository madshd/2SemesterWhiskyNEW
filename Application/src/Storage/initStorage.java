package Storage;

import Controllers.Production;
import Controllers.Warehousing;
import Enumerations.Unit;
import Production.Distillate;
import Production.Distiller;
import Production.Supplier;
import Warehousing.Cask;

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

}
