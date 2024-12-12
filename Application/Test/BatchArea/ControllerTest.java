package BatchArea;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Warehousing.*;
import Production.*;

public class ControllerTest {

	private Interfaces.StorageInterface storage;

	private TasteProfile tasteProfile1;
	private Map<TasteProfile, Double> bluePrint;
	private Formula formula;
	private Product product;
	private Supplier supplier;
	private Warehouse warehouse;
	private StorageRack storageRack;
	private Cask cask;
	private Distiller distiller;
	private Distillate distillateReady;
	private Distillate distillateNotReady;

	@BeforeEach
	public void setUp() {
		// reset storage
		storage = new Storage.Storage();
		Controllers.Production.setStorage(storage);
		Controllers.Warehousing.setStorage(storage);
		Controllers.BatchArea.setStorage(storage);

		// Initialize shared objects
		tasteProfile1 = new TasteProfile("tasteProfile1", "Tastes like success.");
		bluePrint = new HashMap<>();
		bluePrint.put(tasteProfile1, 100.0);
		formula = new Formula("formula1", bluePrint);

		product = new Product("product", 1000);
		product.defineFormula(formula);

		supplier = new Supplier("supplier", "address", "description", "story");
		warehouse = Controllers.Warehousing.createWarehouse("warehouse", "address");
		storageRack = Controllers.Warehousing.createStorageRack("storageRack", 1000);
		Controllers.Warehousing.moveStorageRackToWarehouse(storageRack, warehouse);

		cask = Controllers.Warehousing.createCaskAndAdd(1, 500, Enumerations.Unit.LITERS, supplier, "OAK", warehouse,
				storageRack);
		distiller = Controllers.Production.createDistiller("distiller", "description", "story");

		distillateReady = Controllers.Production.createDistillate("distillateReady", LocalDate.of(2020, 12, 1),
				LocalDate.of(2020, 12, 5), 500, distiller, Enumerations.Unit.LITERS);
		distillateNotReady = Controllers.Production.createDistillate("distillateNotReady", LocalDate.of(2020, 12, 1),
				LocalDate.now(), 500, distiller, Enumerations.Unit.LITERS);

		cask.setTasteProfile(tasteProfile1);
	}

	@Test
	@DisplayName("TC1: Valid input and onlyReady = true")
	public void testCreateNewBatch_ValidInput_OnlyReadyTrue_CreatesBatch() {
		Controllers.Production.fillDistillateIntoCask(distillateReady, cask, 100, LocalDate.now());

		int numExpectedBottles = 100;
		boolean onlyReady = true;

		Batch batch = Controllers.BatchArea.createNewBatch(product, numExpectedBottles, onlyReady);
		assertNotNull(batch);

		assertTrue(storage.getAllBatches().size() == 1);
		assertTrue(storage.getAllBatches().get(0).getProduct().equals(product));
		assertTrue(batch.getReservedCasks().size() == 1);
		assertTrue(batch.getReservedCasks().get(cask) == 100);
	}

	@Test
	@DisplayName("TC2: Valid input and onlyReady = false")
	public void testCreateNewBatch_ValidInput_OnlyReadyFalse_CreatesBatch() {
		Controllers.Production.fillDistillateIntoCask(distillateReady, cask, 100, LocalDate.now());
		Controllers.Production.fillDistillateIntoCask(distillateNotReady, cask, 100, LocalDate.now());

		int numExpectedBottles = 200;
		boolean onlyReady = false;

		Batch batch = Controllers.BatchArea.createNewBatch(product, numExpectedBottles, onlyReady);
		assertNotNull(batch);

		assertTrue(storage.getAllBatches().size() == 1);
		assertTrue(storage.getAllBatches().get(0).getProduct().equals(product));
		assertTrue(batch.getReservedCasks().size() == 1);
		assertTrue(batch.getReservedCasks().get(cask) == 200);
	}

	@Test
	@DisplayName("TC3: numExpectedBottle = 0")
	public void testCreateNewBatch_NumExpectedBottlesZero_ThrowsException() {
		Controllers.Production.fillDistillateIntoCask(distillateReady, cask, 100, LocalDate.now());
		Controllers.Production.fillDistillateIntoCask(distillateNotReady, cask, 100, LocalDate.now());

		int numExpectedBottles = 0;
		boolean onlyReady = true;

		assertThrows(IllegalArgumentException.class, () -> {
			Controllers.BatchArea.createNewBatch(product, numExpectedBottles, onlyReady);
		});

		assertTrue(storage.getAllBatches().isEmpty());
		assertTrue(cask.getTotalReservedQuantity() == 0);
	}

	@Test
	@DisplayName("TC4: numExpectedBottle = negative")
	public void testCreateNewBatch_NumExpectedBottlesNegative_ThrowsException() {
		Controllers.Production.fillDistillateIntoCask(distillateReady, cask, 100, LocalDate.now());
		Controllers.Production.fillDistillateIntoCask(distillateNotReady, cask, 100, LocalDate.now());

		int numExpectedBottles = -8;
		boolean onlyReady = true;

		assertThrows(IllegalArgumentException.class, () -> {
			Controllers.BatchArea.createNewBatch(product, numExpectedBottles, onlyReady);
		});

		assertTrue(storage.getAllBatches().isEmpty());
		assertTrue(cask.getTotalReservedQuantity() == 0);
	}

	@Test
	@DisplayName("TC5: numExpectedBottle exceeds max and onlyReady = true")
	public void testCreateNewBatch_NumExpectedBottlesExceedsMax_OnlyReadyTrue_ThrowsException() {
		Controllers.Production.fillDistillateIntoCask(distillateReady, cask, 100, LocalDate.now());

		int numExpectedBottles = 200;
		boolean onlyReady = true;

		assertThrows(IllegalArgumentException.class, () -> {
			Controllers.BatchArea.createNewBatch(product, numExpectedBottles, onlyReady);
		});

		assertTrue(storage.getAllBatches().isEmpty());
		assertTrue(cask.getTotalReservedQuantity() == 0);
	}

	@Test
	@DisplayName("TC6: numExpectedBottle exceeds max and onlyReady = false")
	public void testCreateNewBatch_NumExpectedBottlesExceedsMaxOnlyReadyFalse_ThrowsException() {
		Controllers.Production.fillDistillateIntoCask(distillateReady, cask, 100, LocalDate.now());
		Controllers.Production.fillDistillateIntoCask(distillateNotReady, cask, 100, LocalDate.now());

		int numExpectedBottles = 250;
		boolean onlyReady = false;

		assertThrows(IllegalArgumentException.class, () -> {
			Controllers.BatchArea.createNewBatch(product, numExpectedBottles, onlyReady);
		});

		assertTrue(storage.getAllBatches().isEmpty());
		assertTrue(cask.getTotalReservedQuantity() == 0);
	}
}
