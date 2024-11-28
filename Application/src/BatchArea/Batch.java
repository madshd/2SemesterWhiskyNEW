
package BatchArea;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import Warehousing.Cask;

public class Batch {

	private static int batchIDglobalCount = 0;
	private final int batchID;
	private final Product product;
	private final LocalDate creationDate;
	private LocalDate completionDate;

	private int numExpectedBottles;
	private int numProducedBottlesPreSpill;
	private int numProducedBottlesPostSpill;

	private boolean productionComplete;

	private final Map<Cask, Integer> reservedCasks = new HashMap<>();

	public Batch(Product product, int numExpectedBottles) {
		this.batchID = batchIDglobalCount++;
		this.product = product;
		this.creationDate = LocalDate.now();
		this.numExpectedBottles = numExpectedBottles;
	}

	/**
	 * This marks the production of the batch as complete and sets the number of
	 * actual produced bottles and the completion date.
	 * 
	 * @param producedBottles
	 */
	public void markProductionComplete() {
		this.productionComplete = true;
		this.completionDate = LocalDate.now();
	}

	public void incNumProducedBottlesPreSpill(int producedBottles) {
		this.numProducedBottlesPreSpill += producedBottles;
	}

	public void incNumProducedBottlesPostSpill(int producedBottles) {
		this.numProducedBottlesPostSpill += producedBottles;
	}

	public void removeCaskFromReserved(Cask cask) {
		this.reservedCasks.remove(cask);
	}

	// ---------------------------GENERIC-GETTERS----------------------------//

	public int getBatchID() {
		return batchID;
	}

	public Product getProduct() {
		return product;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public int getNumExpectedBottles() {
		return numExpectedBottles;
	}

	public int getNumProducedBottlesPreSpill() {
		return numProducedBottlesPreSpill;
	}

	public boolean isProductionComplete() {
		return productionComplete;
	}

	public LocalDate getCompletionDate() {
		return completionDate;
	}

	public Map<Cask, Integer> getReservedCasks() {
		return new HashMap<>(reservedCasks);
	}
}
