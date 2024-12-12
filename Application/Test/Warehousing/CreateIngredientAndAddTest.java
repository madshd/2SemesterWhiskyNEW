package Warehousing;

import BatchArea.ControllerTest;
import Controllers.Warehousing;
import Enumerations.IngredientType;
import Enumerations.Unit;
import Interfaces.StorageInterface;
import Storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CreateIngredientAndAddTest {
    private Interfaces.StorageInterface storage;

    Warehouse warehouse;
    StorageRack storageRack;
    Supplier supplier1;
    Ingredient ingredient;

    @BeforeEach
    void setUp() {
        storage = new Storage();
        Controllers.Production.setStorage(storage);
        Controllers.Warehousing.setStorage(storage);
        Controllers.BatchArea.setStorage(storage);

        warehouse = Warehousing.createWarehouse("warehouse1", "address1");
        storageRack = Warehousing.createStorageRack("storageRack1", 5);
        supplier1 = Warehousing.createSupplier("supplier1", "address1", "test", "test");
        Warehousing.moveStorageRackToWarehouse(storageRack, warehouse);
    }

    @Test
    @DisplayName("Create ingredient and add successfully")
    void createIngredientAndAddSuccessfully() {
        ingredient = Warehousing.createIngredientAndAdd("Barley", "Barley description", 1, LocalDate.now(), LocalDate.now().plusDays(1), 1,
                supplier1, Unit.KILOGRAM, IngredientType.GRAIN, warehouse, storageRack);
        assertNotNull(ingredient);
        assertTrue(storageRack.getList().contains(ingredient));
    }

    @Test
    @DisplayName("Create ingredient and add to full rack")
    void createIngredientAndAddToFullRack() {
        for (int i = 0; i < storageRack.getShelves(); i++) {
            storageRack.addItem(storageRack.getFreeShelf(), new Ingredient("Ingredient" + i, "Description", 1, LocalDate.now(), LocalDate.now().plusDays(1), 1,
                    supplier1, Unit.KILOGRAM, IngredientType.GRAIN));
        }
        assertThrows(RuntimeException.class, () -> {
            ingredient = Warehousing.createIngredientAndAdd("Barley", "Barley description", 1, LocalDate.now(), LocalDate.now().plusDays(1), 1,
                    supplier1, Unit.KILOGRAM, IngredientType.GRAIN, warehouse, storageRack);
        });
        System.out.println(assertThrows(RuntimeException.class, () -> Warehousing.getStorageRackByItem(ingredient)));
        assertFalse(storageRack.getList().contains(ingredient));
        System.out.println(storageRack.getList());
    }

    @Test
    @DisplayName("Create ingredient and add to null warehouse")
    void createIngredientAndAddToNullWarehouse() {
        assertThrows(IllegalArgumentException.class, () -> {
            Warehousing.createIngredientAndAdd("Barley", "Barley description", 1, LocalDate.now(), LocalDate.now().plusDays(1), 1,
                    supplier1, Unit.KILOGRAM, IngredientType.GRAIN, null, storageRack);
        });
        System.out.println(storageRack.getList());
    }

    @Test
    void createIngredientAndAddToNullStorageRack() {
        assertThrows(IllegalArgumentException.class, () -> {
            Warehousing.createIngredientAndAdd("Barley", "Barley description", 1, LocalDate.now(), LocalDate.now().plusDays(1), 1,
                    supplier1, Unit.KILOGRAM, IngredientType.GRAIN, warehouse, null);
        });

        System.out.println(        assertThrows(IllegalArgumentException.class, () -> {
            Warehousing.createIngredientAndAdd("Barley", "Barley description", 1, LocalDate.now(), LocalDate.now().plusDays(1), 1,
                    supplier1, Unit.KILOGRAM, IngredientType.GRAIN, warehouse, null);
        }));
    }

    @Test
    void createIngredientAndAddWithInvalidDate() {
        assertThrows(IllegalArgumentException.class, () -> {
            Warehousing.createIngredientAndAdd("Barley", "Barley description", 1, LocalDate.now().plusDays(1), LocalDate.now(), 1,
                    supplier1, Unit.KILOGRAM, IngredientType.GRAIN, warehouse, storageRack);
        });
        System.out.println(        assertThrows(IllegalArgumentException.class, () -> {
            Warehousing.createIngredientAndAdd("Barley", "Barley description", 1, LocalDate.now().plusDays(1), LocalDate.now(), 1,
                    supplier1, Unit.KILOGRAM, IngredientType.GRAIN, warehouse, storageRack);
        }));
    }
}