package Controllers;

import Enumerations.Unit;
import Interfaces.StorageInterface;
import Storage.Storage;
import Production.Distiller;
import Production.Distillate;
import Warehousing.Supplier;
import Warehousing.Warehouse;
import Warehousing.StorageRack;
import Warehousing.Cask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static Controllers.Warehousing.createCaskAndAdd;
import static Controllers.Production.fillDistillateIntoCask;

class ProductionTest {
    private Distiller distiller_01;
    private Supplier supplier;
    private Distillate distillate;
    private Warehouse warehouse;
    private StorageRack storageRack;
    private Cask cask;


    @BeforeEach
    void setUp() {
        StorageInterface storage = new Storage();
        Controllers.Production.setStorage(storage);
        Controllers.Warehousing.setStorage(storage);
        warehouse = Warehousing.createWarehouse("Sherlock Whisky", "Baker Street 221 B");
        storageRack = Warehousing.createStorageRack("221 B", 100);
        warehouse.addStorageRack("221 B", storageRack);

        distiller_01 = Production.createDistiller(
                "Ewan MacGregor",
                "EMG",
                "An expert in fine single malt production with a focus on classic Highland styles.");

        supplier = Warehousing.createSupplier(
                "Oak Master Barrels",
                "7 Barrel Road, Speyside, Scotland",
                "Supplier of American oak and European oak casks.",
                "Crafting barrels from sustainable sources, Oak Master Barrels has supported traditional whisky making for over a century.");

        distillate = Production.createDistillate(
                "Highland Essence",
                LocalDate.parse("2024-11-27"),
                LocalDate.parse("2024-12-24"),
                500,
                distiller_01,
                Unit.LITERS);

        cask = createCaskAndAdd(1, 200, Unit.LITERS, supplier, "Bourbon Cask", warehouse, storageRack);
    }

    @Test
    @DisplayName("TC1: For høj Quantity")
    void fillDistillateIntoCaskTC1() {
        Exception e1 = assertThrows(IllegalArgumentException.class,
        () -> Production.fillDistillateIntoCask(distillate,cask,250,LocalDate.parse("2024-12-24")));
        assertEquals(e1.getMessage(),"Provided quantity does not fit into this cask.");


    }

    @Test
    @DisplayName("TC2:For høj Quantity ")
    void fillDistillateIntoCaskTC2(){
        Exception e2 = assertThrows(IllegalArgumentException.class,
                () -> Production.fillDistillateIntoCask(distillate,cask,600,LocalDate.parse("2024-12-24")));
        assertEquals(e2.getMessage(),"Provided quantity does not fit this distillate.");
    }

    @Test
    @DisplayName("TC3: Valid Quantity")
    void fillDistillateIntoCaskTC3(){
        Production.fillDistillateIntoCask(distillate,cask,150,LocalDate.parse("2024-12-24"));

        assertEquals(cask.getRemainingQuantity(),50);
        assertEquals(distillate.getRemainingQuantity(),350);
    }
}