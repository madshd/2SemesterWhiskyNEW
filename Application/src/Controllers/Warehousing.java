package Controllers;

import Enumerations.Unit;
import Interfaces.Item;
import Interfaces.StorageInterface;
import Storage.Storage;
import Warehousing.Cask;
import Warehousing.Supplier;
import Production.*;

import java.util.ArrayList;
import java.util.List;

/*
Methods that is mainly used within the Warehousing area
*/
public abstract class Warehousing {
    private static StorageInterface storage;

    public static void setStorage(StorageInterface storage){
        Warehousing.storage = storage;
    }

    /**
     *
     * @param name
     * @param address
     * @param description
     * @param story
     * @return
     */
    public static Supplier createSupplier(String name, String address, String description, String story){
        return new Supplier(name,address,description,story);
    }

    /**
     *
     * @param caskID
     * @param maxQuantity
     * @param unit
     * @param supplier
     * @return
     */
    public static Cask createCask(int caskID, double maxQuantity, Unit unit, Supplier supplier){
        Cask cask = new Cask(caskID,maxQuantity,unit,supplier);
        storage.storeCask(cask);
        return cask;
    }

    /**
     *
     * @param distillate
     * @return
     */
    public static List<Item> getCaskFitToDistillate(Distillate distillate){
        //TODO
        // Only cask fit for distillates should be returned.

        List<Item> casks = new ArrayList<>();

        for (Item i : storage.getCasks()){
            if (i.getRemainingQuantity() > 0){
                casks.add(i);
            }
        }

        return casks;
    }
}
