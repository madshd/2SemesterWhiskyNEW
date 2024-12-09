package Controllers;

import Enumerations.IngredientType;
import Enumerations.Unit;
import Interfaces.Item;
import Interfaces.StorageInterface;
import Warehousing.Cask;
import Warehousing.Supplier;
import Warehousing.Warehouse;
import Warehousing.StorageRack;
import Warehousing.Ingredient;
import BatchArea.TasteProfile;
import Production.FillDistillate;

import java.time.LocalDate;
import java.util.List;

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
	 * Retrieves a list of all available casks.
	 * A cask is considered available if its remaining quantity is greater than zero.
	 *
	 * @return a list of available casks
	 */
	public static List<Cask> getCasksMinQuantity(Double minRemainingQuantity) {
		List<Cask> casks = new ArrayList<>();
		for (Warehouse warehouse : storage.getWarehouses()) {
			for (StorageRack storageRack : warehouse.getRacks().values()) {
				for (Item item : storageRack.getList()) {
					if (item instanceof Cask) {
						Cask cask = (Cask) item;
						if (minRemainingQuantity == null || cask.getRemainingQuantity() > minRemainingQuantity) {
							casks.add(cask);
						}
					}
				}
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
 * Creates a new ingredient and adds it to the specified storage rack.
 * The storage rack must have an empty shelf for the ingredient to be added.
 * Allocates the item to the first available shelf in the storage rack.
 * Notifies observers of the warehouse where the ingredient is added.
 * Returns the created ingredient if it was successfully added, or null if the ingredient could not be added.
 * Throws an IllegalArgumentException if the ingredient could not be added to the specified storage rack.
 *
 */
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

		for (Item item : storageRack.getList()) {
			if (item instanceof Cask && ((Cask) item).getCaskID() == caskID) {
				throw new IllegalArgumentException("A cask with the given ID already exists.");
			}
		}

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

	/**
	 * Retrieves a list of casks that are ready for use.
	 * A cask is considered ready if it has been filled for at least 3 years.
	 *
	 * @return a list of ready casks
	 */

	public static List<Cask> getReadyCasks() {
		List<Cask> readyCasks = new ArrayList<>();
		LocalDate currentDate = LocalDate.now();
		try {
			for (Warehouse wh : getAllWarehouses()) {
				for (StorageRack sr : wh.getRacks().values()) {
					for (Item item : sr.getList()) {
						if (item instanceof Cask) {
							Cask cask = (Cask) item;
							LocalDate lastFillDate = ((FillDistillate) cask.getFillingStack().getFirst()).getDate();
							if (lastFillDate.plusYears(3).isBefore(currentDate)) {
								readyCasks.add(cask);
							}
						}
					}
				}
			}
		} catch (RuntimeException e) {
			System.out.println("No ready casks found");
		}
		return readyCasks;
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

	public static List<Item> getAllAvailableIngredients() {
		List<Item> ingredients = new ArrayList<>();
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

	public static void updateIngredient(Ingredient ingredient, double quantity, Warehouse selectedWarehouse, StorageRack selectedStorageRack) {
		ingredient.setQuantity(quantity);
		if (selectedWarehouse != ingredient.getStorageRack().getWarehouse()) {
			if (selectedWarehouse != null && selectedStorageRack != null) {
				int fromIndex = ingredient.getStorageRack().getItemLocation(ingredient);
				int toIndex = selectedStorageRack.getFreeShelf();
				moveItemBetweenWarehouses(ingredient, ingredient.getStorageRack().getWarehouse(), ingredient.getStorageRack(), fromIndex, selectedWarehouse, selectedStorageRack, toIndex);
			}
		}
	}

/**
 * Retrieves a list of unused storage racks.
 * A storage rack is considered unused if it is not assigned to any warehouse.
 *
 * @return A list of unused storage racks.
 */
public static List<StorageRack> getUnusedStorageRacks() {
	List<StorageRack> unusedStorageRacks = new ArrayList<>();
	for (StorageRack sr : storage.getStorageRacks()) {
		if (sr.getWarehouse() == null) {
			unusedStorageRacks.add(sr);
		}
	}
	return unusedStorageRacks;
}

	/**
	 * Moves a storage rack to a specified warehouse.
	 *
	 * @param storageRack The storage rack to be moved.
	 * @param warehouse The warehouse to which the storage rack will be moved.
	 * @throws IllegalArgumentException if the storage rack is already in a warehouse.
	 */
	public static void moveStorageRackToWarehouse(StorageRack storageRack, Warehouse warehouse) {
		if (storageRack.getWarehouse() != null) {
			throw new IllegalArgumentException("Storage rack is already in a warehouse.");
		}
		storageRack.setWarehouse(warehouse);
		warehouse.addStorageRack(storageRack.getId(), storageRack);
	}

	/**
	 * Removes a storage rack from its current warehouse.
	 *
	 * @param storageRack The storage rack to be removed.
	 * @throws IllegalArgumentException if the storage rack is not in a warehouse.
	 */

	public static void removeStorageRackFromWarehouse(StorageRack storageRack) {
		Warehouse warehouse = storageRack.getWarehouse();
		if (warehouse == null) {
			throw new IllegalArgumentException("Storage rack is not in a warehouse.");
		}
		warehouse.removeStorageRack(storageRack);
		storageRack.setWarehouse(null);
	}

	/**
	 * Retrieves a warehouse by its name.
	 * Iterates through the list of warehouses and returns the warehouse that matches the given name.
	 * If no warehouse is found with the specified name, returns null.
	 *
	 * @param name The name of the warehouse to be retrieved.
	 * @return The warehouse with the specified name, or null if no such warehouse exists.
	 */
	public static Warehouse getWarehouseByName(String name) {
		for (Warehouse warehouse : storage.getWarehouses()) {
			if (warehouse.getName().equals(name)) {
				return warehouse;
			}
		}
		return null;
	}

	/**
	 * Updates the specified warehouse by adding the provided storage racks to it.
	 * If the warehouse is not null, each storage rack in the list is added to the warehouse.
	 *
	 * @param warehouse The warehouse to be updated.
	 * @param storageRacks The list of storage racks to be added to the warehouse.
	 */
	public static void updateWarehouse(Warehouse warehouse, List<StorageRack> storageRacks) {
		if (warehouse != null) {
			for (StorageRack rack : storageRacks) {
				warehouse.addStorageRack(rack.getId(), rack);
			}
		}
	}

	/**
	 * Deletes an item from the specified storage rack.
	 * Removes the item from the storage rack at the location where it is found.
	 * Doesn't actually delete the object from memory, as long as it is still referenced elsewhere.
	 * It should not be eligible for garbage collection as long as it is still in the storage.
	 * @param storageRack The storage rack from which the item will be removed.
	 * @param item The item to be removed from the storage rack.
	 */
	public static void deleteItem(StorageRack storageRack, Item item) {
		storageRack.removeItem(item, storageRack.getItemLocation(item));
	}

	/**
	 * Retrieves the storage rack that contains the specified item.
	 * Iterates through all storage racks and returns the one that contains the given item.
	 * If no storage rack is found containing the item, returns null.
	 *
	 * @param item The item to search for within the storage racks.
	 * @return The storage rack containing the specified item, or null if no such storage rack exists.
	 */
	public static StorageRack getStorageRackByItem(Item item) {
		for (StorageRack sr : storage.getStorageRacks()) {
			if (sr.getList().contains(item)) {
				return sr;
			}
		}
		return null;
	}

	/**
	 * Retrieves the location of the specified item within the given storage rack.
	 * Returns the index of the item within the storage rack.
	 * @param storageRack The storage rack to search within.
	 * @param item The item whose location is to be retrieved.
	 * @return The index of the item within the storage rack.
	 */
	public static int getLocationByRack(StorageRack storageRack, Item item) {
		return storageRack.getItemLocation(item);
	}
}
