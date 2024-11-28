package Controllers;

import Enumerations.Unit;
import Interfaces.StorageInterface;
import Warehousing.Cask;
import Warehousing.Supplier;
import Production.*;

/*
Methods that is mainly used within the Warehousing area
*/
public abstract class Warehousing {
    private static StorageInterface storage;

    public static void setStorage(StorageInterface storage){
        Warehousing.storage = storage;
    }

    public static Supplier createSupplier(String name, String address, String description, String story){
        return new Supplier(name,address,description,story);
    }

    public static Cask createCask(int caskID, double maxQuantity, Unit unit, Supplier supplier){
        Cask cask = new Cask(caskID,maxQuantity,unit,supplier);
        storage.storeCask(cask);
        return cask;
    }
}
