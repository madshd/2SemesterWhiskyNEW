package Interfaces;

import Production.Distillate;
import Warehousing.Cask;
import Warehousing.Ingredient;
import Warehousing.StorageRack;
import Warehousing.Warehouse;

import java.util.List;

import BatchArea.Formula;
import BatchArea.Product;
import BatchArea.TasteProfile;

public interface StorageInterface {
	public List<Distillate> getDistillates();

	public void storeDistillate(Distillate distillate);

	public void deleteDistillate(Distillate distillate);

	public List<Cask> getCasks();

	public void storeCask(Cask cask);

	public void deleteCask(Cask cask);

	public List<Warehouse> getWarehouses();

	public void storeWarehouse(Warehouse warehouse);

	public void deleteWarehouse(Warehouse warehouse);

	public List<StorageRack> getStorageRacks();

	public void storeStorageRack(StorageRack storageRack);

	public void deleteStorageRack(StorageRack storageRack);

	public List<Ingredient> getIngredients();

	public void storeIngredient(Ingredient ingredient);

	public void deleteIngredient(Ingredient ingredient);

	public List<Product> getProducts();

	public void storeProduct(Product product);

	public void deleteProduct(Product product);

	public List<Formula> getFormulae();

	public void storeFormula(Formula formula);

	public void deleteFormula(Formula formula);

	public List<TasteProfile> getTasteProfiles();

	public void storeTasteProfile(TasteProfile tasteProfile);

	public void deleteTasteProfile(TasteProfile tasteProfile);

}
