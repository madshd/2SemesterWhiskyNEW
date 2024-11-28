package Warehousing.Prototypes.BatchArea;

import java.util.ArrayList;

public class Product {
	private String productName;
	private String productID;
	private Formula formula;
	private final ArrayList<Batch> batches = new ArrayList<Batch>();

	public Product(String productName, String productID) {
		this.productName = productName;
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

	// ---------------------------GENERIC-GETTERS----------------------------//

	public String getProductName() {
		return productName;
	}

	public String getProductID() {
		return productID;
	}

	public Formula getFormula() {
		return formula;
	}

	public ArrayList<Batch> getBatches() {
		return new ArrayList<>(batches);
	}
}
