package BatchArea;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import org.checkerframework.checker.units.qual.s;

import Enumerations.Unit;

public class Product implements Serializable {
	private String productName;
	private String productID;
	private Formula formula;
	private Unit bottleUnits = Unit.MILLILITERS;
	private int bottleSize;
	private final ArrayList<Batch> batches = new ArrayList<Batch>();

	public Product(String productName, int bottleSize) {
		this.productName = productName;
		this.bottleSize = bottleSize;
		String productID = generateProductID(productName);
		this.productID = productID;
	}

	public void defineFormula(Formula formula) {
		this.formula = formula;
	}

	public void addBatch(Batch batch) {
		batches.add(batch);
	}

	public void removeBatch(Batch batch) {
		batches.remove(batch);
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setBottleSize(int bottleSize) {
		this.bottleSize = bottleSize;
	}

	/**
	 * Generates a unique product ID based on the product name.
	 * The ID consists of the initials of the product name followed by a unique
	 * identifier.
	 *
	 * @param productName the name of the product
	 * @return a unique product ID in uppercase
	 */
	public static String generateProductID(String productName) {
		StringBuilder initials = new StringBuilder();
		for (String word : productName.split("\\s+")) {
			if (!word.isEmpty()) {
				initials.append(word.charAt(0));
			}
		}
		String uniqueID = UUID.randomUUID().toString().substring(0, 3);
		String productID = initials.toString() + "_" + uniqueID;
		return productID.toUpperCase();
	}

	// ---------------------------GENERIC-GETTERS----------------------------//

	@Override
	public String toString() {
		return productName;
	}

	public String getListInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append(productName);
		sb.append(" - ");
		sb.append(productID);
		sb.append(" - ");
		sb.append(bottleSize);
		sb.append(" ");
		sb.append(bottleUnits);
		sb.append(" - ");
		if (formula == null) {
			sb.append("No formula defined");
		} else {
			sb.append(formula);
		}
		return sb.toString();
	}

	public String getProductName() {
		return productName;
	}

	public String getProductID() {
		return productID;
	}

	public Unit getBottleUnits() {
		return bottleUnits;
	}

	public int getBottleSize() {
		return bottleSize;
	}

	public Formula getFormula() {
		return formula;
	}

	public ArrayList<Batch> getBatches() {
		return new ArrayList<>(batches);
	}
}
