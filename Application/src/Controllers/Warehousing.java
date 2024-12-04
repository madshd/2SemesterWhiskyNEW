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

import java.time.LocalDate;
import java.util.List;

import BatchArea.TasteProfile;
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

	public static Cask createCaskAndAdd(
			int caskID,
			double maxQuantity,
			Unit unit,
			Supplier supplier,
			String caskType,
			Warehouse warehouse,
			StorageRack storageRack,
			int atIndex) {

		Cask cask = createCask(caskID, maxQuantity, Unit.LITERS, supplier, caskType);
		try {
			if (storageRack.getWarehouse() != null) {
				if (storageRack.getList().get(atIndex) == null)
					storageRack.addItem(atIndex, cask);
						storageRack.getWarehouse().notifyWarehousingObserversWithDetails(
							"Cask added: " + cask.getName() + " to Rack: " + storageRack.getId() +
									", Shelf: " + atIndex + " in Warehouse: " + storageRack.getWarehouse().getName());
			}
		} catch (IllegalStateException e) {
			throw new IllegalArgumentException(
					"Storage rack is not used in a warehouse, or shelf is already in use: " + e.getMessage());
		}
		return cask;
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

	/**
	 * Retrieves a list of casks that are ready based on their fill date.
	 * A cask is considered ready if it has been filled for at least 3 years.
	 *
	 * @return a list of casks that are ready
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
							LocalDate lastFillDate = cask.getFillingStack().getFirst().getDate();
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

	public static void setTasteProfile(Cask cask, TasteProfile tasteProfile) {
		cask.setTasteProfile(tasteProfile);
	}

}
