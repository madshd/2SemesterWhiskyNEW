package Controllers;

import Enumerations.Unit;
import Interfaces.Item;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static Controllers.Warehousing.createCaskAndAdd;

class ProductionTest {
    private Distiller distiller_01;
    private Supplier supplier;
    private Distillate distillate_1;
    private Distillate distillate_2;
    private Distillate distillate_3;
    private Warehouse warehouse;
    private StorageRack storageRack;
    private Cask cask_1;
    private Cask cask_2;
    private Cask cask_3;


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

        distillate_1 = Production.createDistillate(
                "Highland Essence",
                LocalDate.parse("2024-11-27"),
                LocalDate.parse("2024-12-24"),
                500,
                distiller_01,
                Unit.LITERS);

        distillate_2 = Production.createDistillate(
                "Islay Smoke",
                LocalDate.parse("2024-10-15"),
                LocalDate.parse("2024-11-10"),
                300,
                distiller_01,
                Unit.LITERS);

        distillate_3 = Production.createDistillate(
                "Classic Malt",
                LocalDate.parse("2024-09-01"),
                LocalDate.parse("2024-09-30"),
                400,
                distiller_01,
                Unit.LITERS);

        cask_1 = createCaskAndAdd(1, 200, Unit.LITERS, supplier, "Bourbon Cask", warehouse, storageRack);
        cask_2 = createCaskAndAdd(2, 250, Unit.LITERS, supplier, "American Oak Cask", warehouse, storageRack);
        cask_3 = createCaskAndAdd(3, 575, Unit.LITERS, supplier, "Sherry Cask", warehouse, storageRack);
    }

    @Test
    @DisplayName("TC1: For høj Quantity")
    void fillDistillateIntoCaskTC1() {
        Exception e = assertThrows(IllegalArgumentException.class,
        () -> Production.fillDistillateIntoCask(distillate_1, cask_1,250,LocalDate.parse("2024-12-24")));
        assertEquals("Provided quantity does not fit into this cask.",e.getMessage());


    }

    @Test
    @DisplayName("TC2:For høj Quantity ")
    void fillDistillateIntoCaskTC2(){
        Exception e = assertThrows(IllegalArgumentException.class,
                () -> Production.fillDistillateIntoCask(distillate_1, cask_1,600,LocalDate.parse("2024-12-24")));
        assertEquals("Provided quantity does not fit this distillate.", e.getMessage());
    }

    @Test
    @DisplayName("TC3: Valid Quantity")
    void fillDistillateIntoCaskTC3(){
        Production.fillDistillateIntoCask(distillate_1, cask_1,150,LocalDate.parse("2024-12-24"));

        assertEquals(cask_1.getRemainingQuantity(),50);
        assertEquals(distillate_1.getRemainingQuantity(),350);
    }

    @Test
    @DisplayName("TC4: For høj Quantity")
    void caskToCaskTranferTC4(){
        Production.fillDistillateIntoCask(distillate_1,cask_1,200, LocalDate.parse("2024-12-24"));
        Production.fillDistillateIntoCask(distillate_2,cask_2,100, LocalDate.parse("2024-12-24"));
        Production.fillDistillateIntoCask(distillate_3,cask_3,400, LocalDate.parse("2024-12-24"));

        Exception e = assertThrows(IllegalArgumentException.class,
                () -> Production.caskToCaskTransfer(cask_1,cask_3,200,LocalDate.parse("2024-12-24")));
        assertEquals("Quantity exceeds the capacity of 'To Cask': 3",e.getMessage());
    }

    @Test
    @DisplayName("TC5:For høj Quantity ")
    void caskToCaskTranferTC5(){
        Production.fillDistillateIntoCask(distillate_1,cask_1,200, LocalDate.parse("2024-12-24"));
        Production.fillDistillateIntoCask(distillate_2,cask_2,100, LocalDate.parse("2024-12-24"));
        Production.fillDistillateIntoCask(distillate_3,cask_3,400, LocalDate.parse("2024-12-24"));

        Exception e = assertThrows(IllegalArgumentException.class,
                () -> Production.caskToCaskTransfer(cask_2,cask_3,150,LocalDate.parse("2024-12-24")));
        assertEquals("Quantity exceeds what remaining in 'From Cask': 2",e.getMessage());
    }

    @Test
    @DisplayName("TC6: Flyt fra cask 1 til 2")
    void caskToCaskTranferTC6(){
        Production.fillDistillateIntoCask(distillate_1,cask_1,200, LocalDate.parse("2024-12-24"));
        Production.fillDistillateIntoCask(distillate_2,cask_2,100, LocalDate.parse("2024-12-24"));
        Production.fillDistillateIntoCask(distillate_3,cask_3,400, LocalDate.parse("2024-12-24"));

        Production.caskToCaskTransfer(cask_1,cask_2,50,LocalDate.parse("2024-12-24"));
        assertEquals(150,cask_1.getQuantityStatus());
        assertEquals(150,cask_2.getQuantityStatus());

        List<String> list = new ArrayList<>();
        Set<Item> caskList = cask_2.getCasksAddedByTransfer(cask_2.getLifeCycle());
        caskList.forEach(c -> list.add(c.getName()));

        Collections.sort(list);
        String sortedString = list.toString();

        assertEquals("[1, 2]",sortedString);
    }

    @Test
    @DisplayName("TC7: Flyt fra cask 2 til 3")
    void caskToCaskTranferTC7(){
        Production.fillDistillateIntoCask(distillate_1,cask_1,200, LocalDate.parse("2024-12-24"));
        Production.fillDistillateIntoCask(distillate_2,cask_2,100, LocalDate.parse("2024-12-24"));
        Production.fillDistillateIntoCask(distillate_3,cask_3,400, LocalDate.parse("2024-12-24"));

        Production.caskToCaskTransfer(cask_1,cask_2,50,LocalDate.parse("2024-12-24"));
        Production.caskToCaskTransfer(cask_2,cask_3,50,LocalDate.parse("2024-12-24"));
        assertEquals(100,cask_2.getQuantityStatus());
        assertEquals(450,cask_3.getQuantityStatus());

        List<String> list = new ArrayList<>();
        Set<Item> caskList = cask_3.getCasksAddedByTransfer(cask_3.getLifeCycle());
        caskList.forEach(c -> list.add(c.getName()));

        Collections.sort(list);
        String sortedString = list.toString();

        assertEquals("[1, 2, 3]",sortedString);
    }
}