package Controllers;

import Interfaces.Item;
import Warehousing.Cask;
import Warehousing.Ingredient;
import Warehousing.Prototypes.StorageRack2dArray;
import Warehousing.StorageRack;
import Warehousing.Warehouse;

public class Warehousing {
        /*
    Methods that is mainly used within the prodtion area
     */
    public static Warehouse createNewWarehouse(String warehouseName, String warehouseAddress) {
//        TODO
        return null;
    }

    public static Ingredient createNewIngredient(String ingredientName, String ingredientID) {
//        TODO
        return null;
    }

    public static Cask createNewCask(String caskName, String caskID) {
//        TODO
        return null;
    }

    public static void storeIngredient(Ingredient ingredient, Warehouse warehouse, StorageRack rack) {
//        TODO
    }

    public static void storeIngredient(Ingredient ingredient, Warehouse warehouse, StorageRack2dArray rack) {
//        TODO
    }

    public static void MoveItemBetweenRacks(StorageRack sourceRack, StorageRack destinationRack, Item item) {
//        TODO
        
    }
    public static void MoveItemBetweenWarehouses(Warehouse sourceWarehouse, Warehouse destinationWarehouse, Item item) {
//        TODO
    }

}
