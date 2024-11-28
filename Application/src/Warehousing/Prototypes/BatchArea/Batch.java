package Warehousing.Prototypes.BatchArea;

import Warehousing.Prototypes.Warehousing.Cask;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Batch {

	// TODO: Decide how batchID is generated and if auto increment
	private final String batchID;
	private final Product product;
	private final LocalDate creationDate;

	private int numExpectedBottles;
	private int producedBottles;

	private boolean productionComplete;

	private final Map<Cask, Integer> reservedCasks = new HashMap<>();

	public Batch(String batchID, Product product, int numExpectedBottles) {
		this.batchID = batchID;
		this.product = product;
		this.creationDate = LocalDate.now();
		this.numExpectedBottles = numExpectedBottles;
	}

	/**
	 * This marks the production of the batch as complete and sets the number of
	 * actual produced bottles
	 * 
	 * @param producedBottles
	 */
	public void markProductionComplete(int producedBottles) {
		this.productionComplete = true;
		this.producedBottles = producedBottles;
	}

	// ---------------------------GENERIC-GETTERS----------------------------//

	public String getBatchID() {
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

	public int getProducedBottles() {
		return producedBottles;
	}

	public boolean isProductionComplete() {
		return productionComplete;
	}

}
