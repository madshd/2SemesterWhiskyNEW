package BatchArea;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Enumerations.TastingNote;
import Warehousing.Cask;

public class Batch implements Serializable {

	private static int batchIDglobalCount = 1;
	private final int batchID;
	private final Product product;
	private final LocalDate creationDate;
	private LocalDate completionDate;
	private int numExpectedBottles;
	private int numProducedBottles;
	private String labelSimple = null;
	private String labelFull = null;
	private final List<Cask> usedCasks = new ArrayList<>();

	private boolean productionComplete;

	private final Map<Cask, Double> reservedCasks = new HashMap<>();

	public Batch(Product product, int numExpectedBottles) {
		this.batchID = batchIDglobalCount++;
		this.product = product;
		this.creationDate = LocalDate.now();
		this.numExpectedBottles = numExpectedBottles;
	}

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

	public void addReservedCask(Cask cask, double quantity) {
		this.reservedCasks.put(cask, quantity);
	}

	public void generateLabel() {
		generateLabelSimple();
		generateLabelFull();
	}

	public void generateLabelFull() {
		StringBuilder sb = new StringBuilder();
		sb.append(product.getProductName());
		sb.append("\n");
		sb.append("Bottled: " + completionDate);
		sb.append("\n");
		sb.append("Batch Size: " + numProducedBottles + " bottles");
		sb.append("\n");
		sb.append("Tasting Notes: " + getWeightedTastingNotes() + "\n");
		sb.append("-------------------------\n");
		sb.append("Casks used in the production of this batch: \n");
		for (Cask cask : usedCasks) {
			sb.append("\n    *** Cask ID: " + cask.getCaskID() + " ***");
			sb.append("\n");
			sb.append("Cask type: " + cask.getCaskType());
			sb.append("\n");
			sb.append("Cask story: " + Controllers.Production.getCaskStory(cask, completionDate, false));
		}
		labelFull = sb.toString();
	}

	public void generateLabelSimple() {
		StringBuilder sb = new StringBuilder();
		sb.append(product.getProductName());
		sb.append("\n");
		sb.append("Bottled: " + completionDate);
		sb.append("\n");
		sb.append("Batch Size: " + numProducedBottles + " bottles");
		sb.append("\n");
		sb.append("Tasting Notes: " + getWeightedTastingNotes() + "\n");
		sb.append("-------------------------\n");
		sb.append("Casks used in the production of this batch: \n");
		for (Cask cask : usedCasks) {
			sb.append("\n    *** Cask ID: " + cask.getCaskID() + " ***");
			sb.append("\n");
			sb.append("Cask type: " + cask.getCaskType());
			sb.append("\n");
			sb.append("Cask story: " + Controllers.Production.getCaskStory(cask, completionDate, true));
		}
		labelSimple = sb.toString();
	}

	/**
	 * Retrieves the top weighted tasting notes for the product.
	 *
	 * @return A string containing the top weighted tasting notes separated by
	 *         spaces.
	 */
	public String getWeightedTastingNotes() {
		int numNotes = 3;
		StringBuilder sb = new StringBuilder();
		List<TastingNote> tastingNotesSortedByPercentage = new ArrayList<>(
				product.getFormula().getWeightedTastingNotes());


		for (int i = 0; i < numNotes && i < tastingNotesSortedByPercentage.size(); i++) {
			sb.append(tastingNotesSortedByPercentage.get(i));

			// add a comma if there are more tasting notes to add
			if (i < numNotes - 1 && i < tastingNotesSortedByPercentage.size() - 1) {
				sb.append(", ");
			}
		}

		String result = sb.toString();
		return result;
	}

	// ---------------------------GENERIC-GETTERS----------------------------//

	public String getListInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("Batch ID: " + batchID);
		sb.append(" - ");
		sb.append("Product: ");
		sb.append(product);
		sb.append(" - ");
		sb.append("Creation Date: ");
		sb.append(creationDate);
		sb.append(" - ");
		if (!productionComplete) {
			sb.append("Expected Bottles: ");
			sb.append(numExpectedBottles);
		} else {
			sb.append("PRODUCTION COMPLETE - ");
			sb.append("Produced Bottles: ");
			sb.append(numProducedBottles);
		}
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
		return labelSimple != null;
	}

	public LocalDate getCompletionDate() {
		return completionDate;
	}

	public Map<Cask, Double> getReservedCasks() {
		return new HashMap<>(reservedCasks);
	}

	public static int getBatchIDglobalCount() {
		return batchIDglobalCount;
	}

	public String getLabel(Boolean isSimpleStory) {
		if (isSimpleStory) {
			return labelSimple;
		} else {
			return labelFull;
		}
	}

	public int getNumRemainingBottles() {
		return numExpectedBottles - numProducedBottles;
	}

	public void addUsedCask(Cask cask) {
		usedCasks.add(cask);
	}

}
