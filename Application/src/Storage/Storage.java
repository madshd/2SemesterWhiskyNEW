package Storage;

import Interfaces.StorageInterface;
import Production.Distillate;
import Warehousing.Cask;
import Warehousing.Ingredient;
import Warehousing.StorageRack;
import Warehousing.Warehouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Storage implements StorageInterface, Serializable {
    private List<Distillate> distillates = new ArrayList<>();
    private List<Cask> casks = new ArrayList<>();
    private List<Warehouse> warehouses = new ArrayList<>();
    private List<StorageRack> storageRacks = new ArrayList<>();
    private List<Ingredient> ingredients = new ArrayList<>();

    @Override
    public List<Distillate> getDistillates() {
        return new ArrayList<>(distillates);
    }

    @Override
    public void storeDistillate(Distillate distillate) {
        distillates.add(distillate);
    }

    @Override
    public void deleteDistillate(Distillate distillate) {
        distillates.remove(distillate);
    }

    @Override
    public List<Cask> getCasks() {
        return new ArrayList<>(casks);
    }

    @Override
    public void storeCask(Cask cask) {
        casks.add(cask);
    }

    @Override
    public void deleteCask(Cask cask) {
        casks.remove(cask);
    }

    @Override
    public List<Warehouse> getWarehouses() {
        return new ArrayList<>(warehouses);
    }

    @Override
    public void storeWarehouse(Warehouse warehouse) {
        warehouses.add(warehouse);
    }

    @Override
    public void deleteWarehouse(Warehouse warehouse) {
        warehouses.remove(warehouse);
    }

    @Override
    public List<StorageRack> getStorageRacks() {
        return new ArrayList<>(storageRacks);
    }

    @Override
    public void storeStorageRack(StorageRack storageRack) {
        storageRacks.add(storageRack);
    }

    @Override
    public void deleteStorageRack(StorageRack storageRack) {
        storageRacks.remove(storageRack);
    }

    @Override
    public List<Ingredient> getIngredients() {
        return new ArrayList<>(ingredients);
    }

    @Override
    public void storeIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    @Override
    public void deleteIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
    }
}
