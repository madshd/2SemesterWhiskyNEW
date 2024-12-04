package Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import BatchArea.*;
import Enumerations.TastingNote;
import Enumerations.Unit;
import Interfaces.StorageInterface;
import Storage.Storage;
import Warehousing.Cask;
import Common.*;

public abstract class BatchArea {

	static StorageInterface storage = new Storage();
	private static Product mostRecentlyModifiedProduct;

	public static void setStorage(StorageInterface storage) {
		BatchArea.storage = storage;
	}

	// ===================== PRODUCT ========================= //

	/**
	 * Creates a new product with the specified name.
	 * ProductID is generated automatically in the Product constructor
	 *
	 * @param productName the name of the product to be created
	 * @return the newly created Product object
	 */

	public static Product createNewProduct(String productName, int bottleSize) {
		Product product = new Product(productName, bottleSize);
		storage.storeProduct(product);
		mostRecentlyModifiedProduct = product;
		return product;
	}

	public static void deleteProduct(Product product) {
		storage.deleteProduct(product);
	}

	public static Product updateProduct(String productName, int bottleSize, Product product) {
		product.setProductName(productName);
		product.setBottleSize(bottleSize);
		mostRecentlyModifiedProduct = product;
		return product;
	}

	public static Product getMostRecentlyModifiedProduct() {
		return mostRecentlyModifiedProduct;
	}

	public static void clearMostRecentlyModifiedProduct() {
		mostRecentlyModifiedProduct = null;
	}

	public static ArrayList<Product> getAllProducts() {
		return (ArrayList<Product>) storage.getAllProducts();
	}

	// ===================== FORMULA ========================= //

	public static void defineFormulaForProduct(Product product, Formula formula) {
		product.defineFormula(formula);
	}

	/**
	 * Creates a new Formula with the given formula name and blueprint.
	 *
	 * @param formulaName the name of the formula
	 * @param blueprint   a HashMap containing TasteProfile as keys and their
	 *                    respective percentages as values
	 * @return the created Formula
	 * @throws IllegalArgumentException if any percentage is not between 1 and
	 *                                  100,
	 *                                  or if the total percentage is not 100
	 */
	public static Formula createNewFormula(String formulaName, HashMap<TasteProfile, Double> blueprint) {
		Formula formula = new Formula(formulaName, blueprint);
		storage.storeFormula(formula);
		return formula;
	}

	public static Formula updateFormula(String formulaName, HashMap<TasteProfile, Double> blueprint, Formula formula) {
		formula.setFormulaName(formulaName);
		formula.setBlueprint(blueprint);
		return formula;
	}

	public static void deleteFormula(Formula selectedFormula) {
		storage.deleteFormula(selectedFormula);
	}

	public static ArrayList<Formula> getAllFormulae() {
		return (ArrayList<Formula>) storage.getFormulae();
	}

	// ===================== TASTE PROFILE =================== //

	/**
	 * Creates a new TasteProfile with the given profile ID, description, and
	 * tags.
	 *
	 * @param profileID   the ID of the taste profile (Typically just one or two
	 *                    uppercase letters.)
	 * @param description the description of the taste profile
	 * @param tags        the list of tasting notes to be added to the taste profile
	 * @return the created TasteProfile
	 * @throws IllegalArgumentException if the tags list is empty
	 */
	public static TasteProfile createNewTasteProfile(String profileID, String description,
			ArrayList<TastingNote> tags) {
		TasteProfile tasteProfile = new TasteProfile(profileID, description);
		for (TastingNote tag : tags) {
			tasteProfile.addTastingNote(tag);
		}
		storage.storeTasteProfile(tasteProfile);
		return tasteProfile;
	}

	public static TasteProfile updateTasteProfile(String profileID, String description, ArrayList<TastingNote> tags,
			TasteProfile tp) {
		tp.setProfileName(profileID);
		tp.setDescription(description);
		tp.clearTastingNotes();
		for (TastingNote tn : tags) {
			tp.addTastingNote(tn);
		}
		return tp;
	}

	/**
	 * Generates the total production volume for x amount of bottles for a given
	 * batch. I.e. how many liters of liquid is needed to producce x amount of
	 * bottles for given batch.
	 *
	 * @param numBottlesToProduce the number of bottles to produce
	 * @param batch               the batch containing the product and its formula
	 * @return a HashMap where the key is the TasteProfile and the value is the
	 *         production volume in milliliters
	 */
	public static HashMap<TasteProfile, Double> calculateProductionVolume(int numBottlesToProduce, Batch batch) {
		int totalProductionVolumeML = numBottlesToProduce *
				batch.getProduct().getBottleSize() * numBottlesToProduce;
		HashMap<TasteProfile, Double> blueprint = batch.getProduct().getFormula().getBlueprint();
		HashMap<TasteProfile, Double> productionVolume = new HashMap<>();
		for (TasteProfile tasteProfile : blueprint.keySet()) {
			double percentage = blueprint.get(tasteProfile);
			double volume = totalProductionVolumeML * percentage / 100;
			productionVolume.put(tasteProfile, volume);
		}
		return productionVolume;
	}

	public static boolean deleteTasteProfile(TasteProfile tp) {
		for (Formula formula : storage.getFormulae()) {
			if (formula.getBlueprint().containsKey(tp)) {
				return false;
			}
		}
		storage.deleteTasteProfile(tp);
		return true;
	}

	public static void setTasteProfileName(String profileName, TasteProfile tp) {
		tp.setProfileName(profileName);
	}

	public static ArrayList<TasteProfile> getAllTasteProfiles() {
		return (ArrayList<TasteProfile>) storage.getTasteProfiles();
	}

	// ====================== BATCH ========================= //

	public static Batch createNewBatch(Product product, int numExpectedBottles) {
		Batch batch = new Batch(product, numExpectedBottles);
		storage.storeBatch(batch);
		reserveQuantityFromCasks(batch);
		return batch;
	}

	private static void reserveQuantityFromCasks(Batch batch) {
		HashMap<TasteProfile, Double> productionVolume = calculateProductionVolume(batch.getNumExpectedBottles(), batch);
		//TODO:
		// ---for each tasteprofile---
		//run through all casks matching 
		//-> reserve maximum possible amount from each cask until production volume for tp is fullfilled (decrement PV as we go)
		//-> put this batch under Casks reservedBatches with the reservedAmount
		//-> put the cask and the reserved volume under batch.reservedCasks
	}

	public static ArrayList<Batch> getAllBatches() {
		return (ArrayList<Batch>) storage.getAllBatches();
	}

	public static void deleteBatch(Batch selectedBatch) {
		storage.deleteBatch(selectedBatch);
	}

	/**
	 * Marks the production as complete for the given batch.
	 *
	 * @param batch the batch to mark as complete
	 */
	public static void setBatchProductionComplete(Batch batch) {
		batch.markProductionComplete();
	}

	// =================== CALCULATIONS ===================== //

	/**
	 * Calculates the maximum number of bottles that can be produced for a given
	 * product.
	 *
	 * @param product the product for which the maximum number of bottles is to be
	 *                calculated
	 * @return the maximum number of bottles that can be produced
	 */
	public static int calculateMaxNumBottles(Product product) {
		int maxNumBottles = Integer.MAX_VALUE;
		double bottleSizeML = product.getBottleSize();
		double bottleSizeLITERS = UnitConverter.convertUnits(Unit.MILLILITERS, Unit.LITERS, bottleSizeML);
		HashMap<TasteProfile, Double> blueprint = product.getFormula().getBlueprint();
		for (TasteProfile tp : blueprint.keySet()) {
			double percentage = blueprint.get(tp) / 100.0;
			double totalVolumePerTP = 0;
			for (Cask cask : storage.getCasks()) {
				if (cask.getTasteProfile() != null && cask.getTasteProfile().equals(tp)) {
					totalVolumePerTP += cask.getQuantityStatus();
				}
			}
			double amountBottlesPossiblePerTP = (totalVolumePerTP / bottleSizeLITERS) * percentage;
			maxNumBottles = Math.min(maxNumBottles, (int) amountBottlesPossiblePerTP);
		}
		return maxNumBottles;
	}

	// ===================== LABELS ========================= //
	/**
	 * Generates a label for the given batch if the production is complete.
	 *
	 * @param batch the batch to generate a label for
	 * @return true if the label was generated, false otherwise
	 */
	public static boolean generateLabelForBatch(Batch batch) {
		if (batch.getCompletionDate() == null) {
			return false;
		} else {
			batch.generateLabel();
			return true;
		}
	}

	/**
	 * Retrieves the label for the given batch.
	 *
	 * @param batch the batch to retrieve the label for
	 * @return the label if it has been generated, otherwise a message indicating
	 *         the label has not been generated
	 */
	public static String getLabelForBatch(Batch batch) {
		String label = batch.getLabel();
		if (label != null) {
			return label;
		} else {
			return "Label not generated yet.";
		}
	}

	// ================= "IS"-VALIDATIONS =================== //

	/**
	 * Checks if a given product is used in any batch.
	 *
	 * @param p the product to check
	 * @return true if the product is used in any batch, false otherwise
	 */
	public static boolean isProductUsedInBatch(Product p) {
		for (Batch batch : storage.getAllBatches()) {
			if (batch.getProduct().equals(p)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the production has started for the given batch.
	 *
	 * @param b the batch to check
	 * @return true if production has started, false otherwise
	 */
	public static boolean isProductionStarted(Batch b) {
		if (b.getNumProducedBottles() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if the production is complete for the given batch.
	 *
	 * @param b the batch to check
	 * @return true if production is complete, false otherwise
	 */
	public static boolean isProductionComplete(Batch b) {
		return b.isProductionComplete();
	}

	/**
	 * Checks if a label has been generated for the given batch.
	 *
	 * @param batch the batch to check
	 * @return true if a label has been generated, false otherwise
	 */
	public static boolean isLabelGenerate(Batch batch) {
		return batch.isLabelGenerated();
	}

}
