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

import BatchArea.Formula;
import BatchArea.Product;
import BatchArea.TasteProfile;

public class Storage implements StorageInterface, Serializable {
	private List<Distillate> distillates = new ArrayList<>();
	private List<Cask> casks = new ArrayList<>();
	private List<Warehouse> warehouses = new ArrayList<>();
	private List<StorageRack> storageRacks = new ArrayList<>();
	private List<Ingredient> ingredients = new ArrayList<>();
	private List<Product> products = new ArrayList<>();
	private List<Formula> formulae = new ArrayList<>();
	private List<TasteProfile> tasteProfiles = new ArrayList<>();

	@Override
	public List<Product> getProducts() {
		return new ArrayList<>(products);
	}

	@Override
	public void storeProduct(Product product) {
		products.add(product);
	}

	@Override
	public void deleteProduct(Product product) {
		products.remove(product);
	}

	@Override
	public List<Formula> getFormulae() {
		return new ArrayList<>(formulae);
	}

	@Override
	public void storeFormula(Formula formula) {
		formulae.add(formula);
	}

	@Override
	public void deleteFormula(Formula formula) {
		formulae.remove(formula);
	}

	@Override
	public List<TasteProfile> getTasteProfiles() {
		return new ArrayList<>(tasteProfiles);
	}

	@Override
	public void storeTasteProfile(TasteProfile tasteProfile) {
		System.out.println("TasteProfile stored in storage");
		tasteProfiles.add(tasteProfile);
	}

	@Override
	public void deleteTasteProfile(TasteProfile tasteProfile) {
		tasteProfiles.remove(tasteProfile);
	}

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
