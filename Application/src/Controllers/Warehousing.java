package Controllers;

import Enumerations.IngredientType;
import Enumerations.Unit;
import Interfaces.Item;
import Interfaces.StorageInterface;
import Production.Distillate;
import Storage.Storage;
import Warehousing.Cask;
import Warehousing.Supplier;
import Warehousing.Warehouse;
import Warehousing.StorageRack;
import Interfaces.Item;
import Warehousing.Ingredient;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;

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
		return new Supplier(name, address, description, story);
	}

	/**
	 *
	 * @param caskID
	 * @param maxQuantity
	 * @param unit
	 * @param supplier
	 * @return
	 */
	public static Cask createCask(int caskID, double maxQuantity, Unit unit, Supplier supplier) {
		Cask cask = new Cask(caskID, maxQuantity, unit, supplier);
		storage.storeCask(cask);
		return cask;
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

	/**
	 * This method retrieves a map of available storage racks within warehouses.
	 * The map's keys are Warehouse objects, and the values are StorageRack objects.
	 * The method iterates through all warehouses and their storage racks, checking
	 * if any of the storage racks contain null items.
	 * If a storage rack contains null items, it indicates that the storage rack has
	 * available space.
	 * 
	 * @return a map containing warehouses and their respective storage racks that
	 *         have available space.
	 */

	public static Map<Warehouse, StorageRack> getFreeStorage() {
		Map<Warehouse, StorageRack> freeStorage = new HashMap<>();
		for (Warehouse warehouse : storage.getWarehouses()) {
			for (StorageRack storageRack : warehouse.getRacks().values()) {
				// Check if the storage rack contains any null items, indicating free space
				if (storageRack.getList().contains(null)) {
					freeStorage.put(warehouse, storageRack);
				}
			}
		}
		return freeStorage;
	}

	public static Ingredient createIngredientAndAdd(String name, String description, int batchNo,
			LocalDate productionDate, LocalDate expirationDate,
			double quantity, Supplier supplier, Unit unit, IngredientType ingredientType,
			Warehouse warehouse, StorageRack storageRack, int atIndex) {

		Ingredient ingredient = new Ingredient(name, description, batchNo, productionDate, expirationDate, quantity,
				supplier, unit, ingredientType);

		try {
			storageRack.addItem(atIndex, ingredient);
			if (storageRack.getWarehouse() != null) {
				storageRack.getWarehouse().notifyWarehousingObserversWithDetails(
						"Ingredient added: " + ingredient.getName() + " to Rack: " + storageRack.getId() +
								", Shelf: " + atIndex + " in Warehouse: " + storageRack.getWarehouse().getName());
			}
		} catch (IllegalStateException e) {
			throw new IllegalArgumentException(
					"Failed to add ingredient to the specified storage rack: " + e.getMessage());
		}

		return ingredient;
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
		if (fromStorageRack.getWarehouse() != null) {
			fromStorageRack.getWarehouse().notifyWarehousingObserversWithDetails(
					"Item removed from Rack: " + fromStorageRack.getId() +
							", Shelf: " + fromIndex +
							" in Warehouse: " + fromStorageRack.getWarehouse().getName());
		}

		toStorageRack.addItem(toIndex, item);
		if (toStorageRack.getWarehouse() != null) {
			toStorageRack.getWarehouse().notifyWarehousingObserversWithDetails(
					"Item added to Rack: " + toStorageRack.getId() +
							", Shelf: " + toIndex +
							" in Warehouse: " + toStorageRack.getWarehouse().getName());
		}
	}

	public static List<Warehouse> getAllWarehouses() {
		return storage.getWarehouses();
	}
}
