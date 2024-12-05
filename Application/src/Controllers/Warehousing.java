package Controllers;

import Enumerations.IngredientType;
import Enumerations.Unit;
import Interfaces.Item;
import Interfaces.StorageInterface;
import Production.Distillate;
import Warehousing.Cask;
import Warehousing.Supplier;
import Warehousing.Warehouse;
import Warehousing.StorageRack;
import Warehousing.Ingredient;
import BatchArea.TasteProfile;
import Production.FillDistillate;

import java.time.LocalDate;
import java.util.List;

import BatchArea.TasteProfile;
import javafx.scene.control.Alert;

import java.util.ArrayList;

/*
Methods that is mainly used within the Warehousing area
*/
public abstract class Warehousing {
	private static StorageInterface storage;

	public static void setStorage(StorageInterface storage) {
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
	public static Supplier createSupplier(String name, String address, String description, String story) {
		Supplier supplier = new Supplier(name, address, description, story);
		storage.storeSupplier(supplier);
		return supplier;
	}

	/**
	 *
	 * @param caskID
	 * @param maxQuantity
	 * @param unit
	 * @param supplier
	 * @return
	 */
	public static Cask createCask(int caskID, double maxQuantity, Unit unit, Supplier supplier, String caskType) {
		Cask cask = new Cask(caskID, maxQuantity, unit, supplier, caskType);
		storage.storeCask(cask);
		return cask;
	}

	public static Ingredient createIngredient(String name, String description, int batchNo, LocalDate productionDate,
			LocalDate expirationDate, double quantity, Supplier supplier, Unit unit, IngredientType ingredientType) {

		Ingredient ingredient = new Ingredient(name, description, batchNo, productionDate, expirationDate, quantity,
				supplier, unit, ingredientType);

		storage.storeIngredient(ingredient);
		return ingredient;
	}

	/**
	 *
	 * @param distillate
	 * @return
	 */
	public static List<Item> getCaskFitToDistillate(Distillate distillate) {
		// TODO
		// Only cask fit for distillates should be returned.

		List<Item> casks = new ArrayList<>();

		for (Item i : storage.getCasks()) {
			if (i.getRemainingQuantity() > 0) {
				casks.add(i);
			}

		}
		return casks;
	}

	public static Warehouse createWarehouse(String name, String address) {
		Warehouse warehouse = new Warehouse(name, address);
		storage.storeWarehouse(warehouse);
		return warehouse;
	}

	public static StorageRack createStorageRack(String rackID, int maxCapacity) {
		StorageRack storageRack = new StorageRack(rackID, maxCapacity);
		storage.storeStorageRack(storageRack);
		return storageRack;
	}

	public static Ingredient createIngredientAndAdd(
			String name,
			String description,
			int batchNo,
			LocalDate productionDate,
			LocalDate expirationDate,
			double quantity,
			Supplier supplier,
			Unit unit,
			IngredientType ingredientType,
			Warehouse warehouse,
			StorageRack storageRack) {

		Ingredient ingredient = createIngredient(name, description, batchNo, productionDate, expirationDate, quantity,
				supplier, unit, ingredientType);

		try {
			for (int i = 0; i < storageRack.getList().size(); i++) {
				if (storageRack.getList().get(i) == null) {
					storageRack.addItem(i, ingredient);
					storageRack.getWarehouse().notifyWarehousingObserversWithDetails(
							"Ingredient added: " + ingredient.getName() + " to Rack: " + storageRack.getId() +
									", Shelf: " + i + " in Warehouse: " + storageRack.getWarehouse().getName());
					return ingredient;
				}
			}
		} catch (IllegalStateException e) {
			throw new IllegalArgumentException(
					"Failed to add ingredient to the specified storage rack: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Creates a new cask and adds it to the specified storage rack.
	 * Storage rack must have an empty shelf for the cask to be added.
	 * Allocates the item to the first available shelf in the storage rack.
	 * Notifies observers of the warehouse where the cask is added.
	 * Returns the created cask if it was successfully added, or null if the cask could not be added.
	 * Throws an IllegalArgumentException if the cask could not be added to the specified storage rack.
	 */
	public static Cask createCaskAndAdd(
			int caskID,
			double maxQuantity,
			Unit unit,
			Supplier supplier,
			String caskType,
			Warehouse warehouse,
			StorageRack storageRack) {

		Cask cask = createCask(caskID, maxQuantity, Unit.LITERS, supplier, caskType);
		try {
			for (int i = 0; i < storageRack.getList().size(); i++) {
				if (storageRack.getList().get(i) == null) {
					storageRack.addItem(i, cask);
					storageRack.getWarehouse().notifyWarehousingObserversWithDetails(
							"Cask added: " + cask.getName() + " to Rack: " + storageRack.getId() +
									", Shelf: " + i + " in Warehouse: " + storageRack.getWarehouse().getName());
					return cask;
				}
			}
		} catch (IllegalStateException e) {
			throw new IllegalArgumentException(
					"Failed to add cask to the specified storage rack: " + e.getMessage());
		}
		return null;
	}


	/**
	 * Moves an item from one warehouse to another, updating the storage racks and
	 * notifying observers.
	 *
	 * @param item            the item to be moved
	 * @param fromWarehouse   the warehouse from which the item is being moved
	 * @param fromStorageRack the storage rack from which the item is being moved
	 * @param fromIndex       the index of the item in the source storage rack
	 * @param toWarehouse     the warehouse to which the item is being moved
	 * @param toStorageRack   the storage rack to which the item is being moved
	 * @param toIndex         the index in the destination storage rack where the
	 *                        item will be placed
	 * @throws IllegalArgumentException if the item is not found at the specified
	 *                                  index in the source storage rack
	 * @throws IllegalStateException    if the target shelf is not empty in the
	 *                                  destination storage rack
	 */

	public static void moveItemBetweenWarehouses(Item item, Warehouse fromWarehouse, StorageRack fromStorageRack,
												 int fromIndex, Warehouse toWarehouse, StorageRack toStorageRack, int toIndex) {
		if (item == null || fromWarehouse == null || fromStorageRack == null || toWarehouse == null || toStorageRack == null) {
			throw new IllegalArgumentException("None of the parameters can be null.");
		}
		if (fromIndex < 0 || fromIndex >= fromStorageRack.getList().size()) {
			throw new IndexOutOfBoundsException("fromIndex is out of bounds.");
		}
		if (toIndex < 0 || toIndex >= toStorageRack.getList().size()) {
			throw new IndexOutOfBoundsException("toIndex is out of bounds.");
		}

		if (fromStorageRack.getItemLocation(item) != fromIndex) {
			throw new IllegalArgumentException("Item not found at the specified index in the source storage rack.");
		}

		if (toStorageRack.getList().get(toIndex) != null) {
			throw new IllegalStateException("Target shelf is not empty in the destination storage rack.");
		}

		fromStorageRack.removeItem(item, fromIndex);
		fromWarehouse.notifyWarehousingObserversWithDetails(
				"Item removed from Rack: " + fromStorageRack.getId() +
						", Shelf: " + fromIndex +
						" in Warehouse: " + fromWarehouse.getName());

		toStorageRack.addItem(toIndex, item);
		toWarehouse.notifyWarehousingObserversWithDetails(
				"Item added to Rack: " + toStorageRack.getId() +
						", Shelf: " + toIndex +
						" in Warehouse: " + toWarehouse.getName());
	}

	/**
	 * Moves an item from one storage rack to another within the same or different
	 * warehouses, updating the storage racks and notifying observers.
	 *
	 * @param item            the item to be moved
	 * @param fromStorageRack the storage rack from which the item is being moved
	 * @param fromIndex       the index of the item in the source storage rack
	 * @param toStorageRack   the storage rack to which the item is being moved
	 * @param toIndex         the index in the destination storage rack where the
	 *                        item will be placed
	 * @throws IllegalArgumentException if the item is not found at the specified
	 *                                  index in the source storage rack
	 * @throws IllegalStateException    if the target shelf is not empty in the
	 *                                  destination storage rack
	 */

	public static void moveItemBetweenStorageRacks(Item item, StorageRack fromStorageRack, int fromIndex,
			StorageRack toStorageRack, int toIndex) {
		if (fromStorageRack.getItemLocation(item) != fromIndex) {
			throw new IllegalArgumentException("Item not found at the specified index in the source storage rack.");
		}

		if (toStorageRack.getList().get(toIndex) != null) {
			throw new IllegalStateException("Target shelf is not empty in the destination storage rack.");
		}

		fromStorageRack.removeItem(item, fromIndex);
//		if (fromStorageRack.getWarehouse() != null) {
//			fromStorageRack.getWarehouse().notifyWarehousingObserversWithDetails(
//					"Item removed from Rack: " + fromStorageRack.getId() +
//							", Shelf: " + fromIndex +
//							" in Warehouse: " + fromStorageRack.getWarehouse().getName());
//		}

		toStorageRack.addItem(toIndex, item);
//		if (toStorageRack.getWarehouse() != null) {
//			toStorageRack.getWarehouse().notifyWarehousingObserversWithDetails(
//					"Item added to Rack: " + toStorageRack.getId() +
//							", Shelf: " + toIndex +
//							" in Warehouse: " + toStorageRack.getWarehouse().getName());
//		}

		System.out.println("Item moved from " + fromStorageRack.getId() + " shelf " + fromIndex + " to "
				+ toStorageRack.getId() + " shelf " + toIndex);
	}

	public static List<Warehouse> getAllWarehouses() {
		return storage.getWarehouses();
	}

	public static List<Cask> getReadyCasks() {
		List<Cask> readyCasks = new ArrayList<>();
			for (Warehouse wh : getAllWarehouses()) {
				for (StorageRack sr : wh.getRacks().values()) {
					for (Item item : sr.getList()) {
						if (item instanceof Cask) {
							Cask cask = (Cask) item;
							if (cask.getMaturityMonths() >= 36) {
								readyCasks.add(cask);
							}
						}
					}
				}
			}
		return readyCasks;
	}

	public static double getCaskTotalReservedAmount(Cask cask) {
		return cask.getTotalReservedAmount();
	}

	public static double getLegalQuantity(Cask cask) {
		return cask.getLegalQuantity();
	}

	public static List<Supplier> getSuppliers() {
		return new ArrayList<>(storage.getSuppliers());
	}

	public static void deleteWarehouse(Warehouse selectedWarehouse) {
		if (selectedWarehouse != null) {
			if (selectedWarehouse.getRacks().isEmpty()) {
				storage.deleteWarehouse(selectedWarehouse);
			} else {
				throw new IllegalStateException("Warehouse is not empty");
			}
		}
	}

	/**
	 * Retrieves a list of all available ingredients.
	 * An ingredient is considered available if its remaining quantity is greater than zero.
	 *
	 * @return a list of available ingredients
	 */

	public static void deleteStorageRack(Warehouse selectedWarehouse, StorageRack selectedStorageRack) {
		if (selectedStorageRack != null) {
			boolean isEmpty = true;
			for (Item item : selectedStorageRack.getList()) {
				if (item != null) {
					isEmpty = false;
					break;
				}
			}
			if (isEmpty) {
				selectedWarehouse.removeStorageRack(selectedStorageRack);
				storage.deleteStorageRack(selectedStorageRack);
			}
		}
	}

	/**
	 * Retrieves a list of all available ingredients.
	 * An ingredient is considered available if its remaining quantity is greater than zero.
	 *
	 * @return a list of available ingredients
	 */

	public static List<Ingredient> getAllAvailableIngredients() {
		List<Ingredient> ingredients = new ArrayList<>();
		for (Warehouse wh : getAllWarehouses()) {
			for (StorageRack sr : wh.getRacks().values()) {
				for (Item item : sr.getList()) {
					if (item instanceof Ingredient) {
						if (item.getRemainingQuantity() > 0)
							ingredients.add((Ingredient) item);
					}
				}
			}
		}
		return ingredients;
	}

	/**
	 * Updates the taste profile of a cask and moves it to a different storage rack if necessary.
	 *
	 * @param cask The cask to be updated.
	 * @param value The new taste profile to be set for the cask.
	 * @param selectedWarehouse The warehouse where the cask is currently stored.
	 * @param selectedStorageRack The storage rack where the cask should be moved.
	 */

	public static void updateCask(Cask cask, TasteProfile value, Warehouse selectedWarehouse, StorageRack selectedStorageRack) {
		cask.setTasteProfile(value);
		if (selectedWarehouse != cask.getStorageRack().getWarehouse()) {
			if (selectedWarehouse != null && selectedStorageRack != null) {
				int fromIndex = cask.getStorageRack().getItemLocation(cask);
				int toIndex = selectedStorageRack.getFreeShelf();
				moveItemBetweenWarehouses(cask, cask.getStorageRack().getWarehouse(), cask.getStorageRack(), fromIndex, selectedWarehouse, selectedStorageRack, toIndex);
			}
				if (selectedWarehouse == cask.getStorageRack().getWarehouse() && selectedStorageRack != cask.getStorageRack()) {
					int fromIndex = cask.getStorageRack().getItemLocation(cask);
					int toIndex = selectedStorageRack.getFreeShelf();
					moveItemBetweenStorageRacks(cask, cask.getStorageRack(), fromIndex, selectedStorageRack, toIndex);
				}
			}
		}
}
