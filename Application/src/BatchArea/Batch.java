
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
	private int numProducedBottles;
	private String label = null;

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

	public void incNumProducedBottles(int producedBottles) {
		this.numProducedBottles += producedBottles;
	}

	public void removeCaskFromReserved(Cask cask) {
		this.reservedCasks.remove(cask);
	}

	public void generateLabel() {
		StringBuilder sb = new StringBuilder();
		sb.append(product.getProductName());
		sb.append(" - ");
		sb.append(creationDate);
		sb.append(" - Batch ID: ");
		sb.append(batchID);
		sb.append(" - Bottle X of ");
		sb.append(numProducedBottles);
		label = sb.toString();
	}
	// ---------------------------GENERIC-GETTERS----------------------------//


	public String getListInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("Batch ID: ");
		sb.append(" - ");
		sb.append("Product: ");
		sb.append(product);
		sb.append(" - ");
		sb.append("Creation Date: ");
		sb.append(creationDate);
		sb.append(" - ");
		sb.append("Expected Bottles: ");
		sb.append(numExpectedBottles);
		return sb.toString();
	}

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

	public int getNumProducedBottles() {
		return numProducedBottles;
	}

	public boolean isProductionComplete() {
		return productionComplete;
	}

	public boolean isLabelGenerated() {
		return label != null;
	}

	public LocalDate getCompletionDate() {
		return completionDate;
	}

	public Map<Cask, Integer> getReservedCasks() {
		return new HashMap<>(reservedCasks);
	}

	public static int getBatchIDglobalCount() {
		int count = batchIDglobalCount;
		return count + 1;
	}

	public String getLabel() {
		return label;
	}
}
