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
	private static Product mostRecentlyModifiedProduc;

	public static void setStorage(StorageInterface storage) {
		BatchArea.storage = storage;
	}

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
		mostRecentlyModifiedProduc = product;
		return product;
	}

	public static void deleteProduct(Product product) {
		storage.deleteProduct(product);
	}

	public static Product updateProduct(String productName, int bottleSize, Product product) {
		product.setProductName(productName);
		product.setBottleSize(bottleSize);
		mostRecentlyModifiedProduc = product;
		return product;
	}

	public static Product getMostRecentlyModifiedProduct() {
		return mostRecentlyModifiedProduc;
	}

	public static void defineFormulaForProduct(Product product, Formula formula) {
		product.defineFormula(formula);
	}

	public static ArrayList<Product> getAllProducts() {
		return (ArrayList<Product>) storage.getAllProducts();
	}

	public static ArrayList<TasteProfile> getAllTasteProfiles() {
		return (ArrayList<TasteProfile>) storage.getTasteProfiles();
	}

	public static ArrayList<Formula> getAllFormulae() {
		return (ArrayList<Formula>) storage.getFormulae();
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
	public static Formula createNewFormula(String formulaName, HashMap<TasteProfile, Integer> blueprint) {
		Formula formula = new Formula(formulaName, blueprint);
		storage.storeFormula(formula);
		return formula;
	}

	public static Formula updateFormula(String formulaName, HashMap<TasteProfile, Integer> blueprint, Formula formula) {
		formula.setFormulaName(formulaName);
		formula.setBlueprint(blueprint);
		return formula;
	}

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

	public static Batch createNewBatch(Product product, int numExpectedBottles) {
		Batch batch = new Batch(product, numExpectedBottles);
		storage.storeBatch(batch);
		return batch;
	}

	public static ArrayList<Batch> getAllBatches() {
		return (ArrayList<Batch>) storage.getAllBatches();
	}

	/**
	 * Generates the production volume for each taste profile in the batch based
	 * on
	 * the amount of bottles to be produced.
	 *
	 * @param numBottlesToProduce the number of bottles to produce
	 * @param batch               the batch containing the product and its formula
	 * @return a HashMap where the key is the TasteProfile and the value is the
	 *         production volume in milliliters
	 */
	public static HashMap<TasteProfile, Integer> generateProductionVolume(int numBottlesToProduce, Batch batch) {
		int totalProductionVolumeML = numBottlesToProduce *
				batch.getProduct().getBottleSize() * numBottlesToProduce;
		HashMap<TasteProfile, Integer> blueprint = batch.getProduct().getFormula().getBlueprint();
		HashMap<TasteProfile, Integer> productionVolume = new HashMap<>();
		for (TasteProfile tasteProfile : blueprint.keySet()) {
			int percentage = blueprint.get(tasteProfile);
			int volume = totalProductionVolumeML * percentage / 100;
			productionVolume.put(tasteProfile, volume);
		}
		return productionVolume;
	}

	public static void deleteFormula(Formula selectedFormula) {
		storage.deleteFormula(selectedFormula);
	}

	public static void deleteBatch(Batch selectedBatch) {
		storage.deleteBatch(selectedBatch);
	}

	public static void clearMostRecentlyModifiedProduct() {
		mostRecentlyModifiedProduc = null;
	}

	/**
	 * Calculates the maximum number of bottles that can be produced for a given
	 * product.
	 *
	 * @param product the product for which the maximum number of bottles is to be
	 *                calculated
	 * @return the maximum number of bottles that can be produced
	 */
	public static int calcMaxNumBottles(Product product) {
		int maxNumBottles = Integer.MAX_VALUE;
		double bottleSizeML = product.getBottleSize();
		double bottleSizeLITERS = UnitConverter.convertUnits(Unit.MILLILITERS, Unit.LITERS, bottleSizeML);

		HashMap<TasteProfile, Integer> blueprint = product.getFormula().getBlueprint();

		for (TasteProfile tp : blueprint.keySet()) {
			double percentage = blueprint.get(tp) / 100.0;
			double totalVolumePerTP = 0;
			for (Cask cask : storage.getCasks()) {
				if (cask.getTasteProfile().equals(tp)) {
					totalVolumePerTP += cask.getFakeQuantity();
				}
			}
			double amountBottlesPossiblePerTP = (totalVolumePerTP / bottleSizeLITERS) * percentage;
			maxNumBottles = Math.min(maxNumBottles, (int) amountBottlesPossiblePerTP);
		}
		return maxNumBottles;
	}

}
