package Warehousing.Prototypes.Controllers;

import Warehousing.Prototypes.BatchArea.Batch;
import Warehousing.Prototypes.BatchArea.Formula;
import Warehousing.Prototypes.BatchArea.Product;
import Warehousing.Prototypes.Warehousing.Cask;

import java.util.List;

public class BatchArea {
	/*
	 * Methods that is mainly used within the product area
	 */

	public static Product createNewProduct(String productName, String productID) {
		// TODO: Implement this method
		return null;
	}

	public static Formula createNewFormula(String formulaName) {
		// TODO: Implement this method
		return null;
	}

	public static void tapBottlesForBatch(int numBottles, Batch batch) {
		// TODO: Implement this method
	}

	public List<Cask> searchCaskForFormula(Formula formula) {
		// TODO: Implement this method
        return List.of();
    }

	public Batch createNewBatch(Product product, int numExpectedBottles) {
		// TODO: Implement this method
		return null;
	}
}
