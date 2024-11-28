package Warehousing;

import Enumerations.IngredientType;
import Enumerations.Unit;
import Production.Supplier;

import java.time.LocalDate;

public class WarehousingTestApp {
    public static void main(String[] args) {

        Warehouse warehouse = new Warehouse("Warehouse 1", "Address 1");
        Warehouse warehouse2 = new Warehouse("Warehouse 2", "Address 2");

        StorageRack storageRack1 = new StorageRack("Rack 1", 3);
        StorageRack storageRack2 = new StorageRack("Rack 2", 2);
        StorageRack storageRack3 = new StorageRack("Rack 3", 1);

        warehouse.addStorageRack("Rack 1", storageRack1);
        warehouse.addStorageRack("Rack 2", storageRack2);
        warehouse.addStorageRack("Rack 3", storageRack3);


        Supplier supplier1 = new Supplier("Supplier 1", "Address 1", "Phone 1", "Nice story");

        Ingredient ingredient1 = new Ingredient("Ingredient 1", "Cool ingredient", 1,
                LocalDate.of(2024,11,27), LocalDate.of(2024,12,27), 10, supplier1, Unit.PIECES, IngredientType.OTHER);
    
        storageRack1.addItem(2, ingredient1);


//        StorageRack
        System.out.println("Warehouse: " + warehouse.getName());
        System.out.println("Address: " + warehouse.getAddress());
        System.out.println("Racks: ");
        for (String key : warehouse.getRacks().keySet()) {
            System.out.println("  " + key);
            System.out.println("  " + warehouse.getRacks().get(key).getItems());
        }
        storageRack1.moveItem(ingredient1, 2, 0);
        System.out.println("Racks: ");
        for (String key : warehouse.getRacks().keySet()) {
            System.out.println("  " + key);
            System.out.println("  " + warehouse.getRacks().get(key).getItems());
        }
    }
}
