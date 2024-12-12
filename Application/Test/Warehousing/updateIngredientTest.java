package Warehousing;

import Controllers.Warehousing;
import Enumerations.IngredientType;
import Enumerations.Unit;
import Storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class updateIngredientTest {
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

    @DisplayName("Update ingredient with positive quantity")
    @Test
    void updateIngredientWithPositiveQuantity() {
        ingredient = Warehousing.createIngredientAndAdd("Barley", "Barley description", 1, LocalDate.now(), LocalDate.now().plusDays(1), 1,
                supplier1, Unit.KILOGRAM, IngredientType.GRAIN, warehouse, storageRack);
        Warehousing.updateIngredient(ingredient, 5.0, warehouse, storageRack);

        assertEquals(5, ingredient.getRemainingQuantity());
    }

    @DisplayName("Update ingredient with zero quantity and move to different warehouse")
    @Test
    void updateIngredientWithZeroQuantityAndMoveToDifferentWarehouse() {
        Ingredient ingredient = new Ingredient("Barley", "Barley description", 1, LocalDate.now(), LocalDate.now().plusDays(1), 1, supplier1, Unit.KILOGRAM, IngredientType.GRAIN);
        Warehouse warehouse1 = new Warehouse("Warehouse1", "Address1");
        Warehouse warehouse2 = new Warehouse("Warehouse2", "Address2");
        StorageRack storageRack1 = new StorageRack("Rack1", 10);
        StorageRack storageRack2 = new StorageRack("Rack2", 10);
        warehouse1.addStorageRack(storageRack1.getId(), storageRack1);
        warehouse2.addStorageRack(storageRack2.getId(), storageRack2);
        storageRack1.addItem(0, ingredient);

        Warehousing.updateIngredient(ingredient, 0, warehouse2, storageRack2);

        assertEquals(0, ingredient.getRemainingQuantity());
        assertEquals(storageRack2, ingredient.getStorageRack());
    }

    @DisplayName("Update ingredient with negative quantity throws exception")
    @Test
    void updateIngredientWithNegativeQuantityThrowsException() {
        Ingredient ingredient = new Ingredient("Barley", "Barley description", 1, LocalDate.now(), LocalDate.now().plusDays(1), 1, supplier1, Unit.KILOGRAM, IngredientType.GRAIN);
        Warehouse warehouse = new Warehouse("Warehouse1", "Address1");
        StorageRack storageRack = new StorageRack("Rack1", 10);
        warehouse.addStorageRack(storageRack.getId(), storageRack);
        storageRack.addItem(0, ingredient);

        assertThrows(IllegalArgumentException.class, () -> {
            Warehousing.updateIngredient(ingredient, -1, warehouse, storageRack);
        });
    }

    @DisplayName("Update ingredient with zero quantity and same warehouse")
    @Test
    void updateIngredientWithZeroQuantityAndSameWarehouse() {
        Ingredient ingredient = new Ingredient("Barley", "Barley description", 1, LocalDate.now(), LocalDate.now().plusDays(1), 1, supplier1, Unit.KILOGRAM, IngredientType.GRAIN);
        Warehouse warehouse = new Warehouse("Warehouse1", "Address1");
        StorageRack storageRack = new StorageRack("Rack1", 10);
        warehouse.addStorageRack(storageRack.getId(), storageRack);
        storageRack.addItem(0, ingredient);

        Warehousing.updateIngredient(ingredient, 0, warehouse, storageRack);

        assertEquals(0, ingredient.getRemainingQuantity());
        assertEquals(storageRack, ingredient.getStorageRack());
    }
}