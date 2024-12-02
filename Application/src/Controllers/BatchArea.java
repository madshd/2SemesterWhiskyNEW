package Controllers;

import java.util.ArrayList;
import java.util.HashMap;

import BatchArea.*;
import Enumerations.TastingNote;
import Interfaces.StorageInterface;
import Storage.Storage;

public abstract class BatchArea {

	static StorageInterface storage = new Storage();

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
		return product;
	}

	public static Product updateProduct(String productName, int bottleSize, Product product) {
		product.setProductName(productName);
		product.setBottleSize(bottleSize);
		return product;
	}

	public static ArrayList<Product> getAllProducts() {
		return (ArrayList<Product>) storage.getAllProducts();
	}

	public static void defineFormulaForProduct(Product product, Formula formula) {
		product.defineFormula(formula);
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

	public Batch createNewBatch(Product product, int numExpectedBottles) {
		// TODO: Implement checks for max num of possible bottles according to quantity
		// of ingredients for recipe
		Batch batch = new Batch(product, numExpectedBottles);
		// TODO: Save this object in storage
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

	/**
	 * Produces a part of the batch by generating the production volume for each
	 * taste profile and pulling liquid from the reserved casks as needed.
	 * If the batch has been fully produced, it will be marked as production
	 * complete
	 *
	 * @param batch               the batch to produce
	 * @param numBottlesToProduce the number of bottles to produce
	 * @throws IllegalStateException    if the batch has already been produced
	 * @throws IllegalArgumentException if the number of bottles to produce
	 *                                  exceeds
	 *                                  expected number of bottles for full batch
	 */
	// public static void producePartOfBatch(Batch batch, int numBottlesToProduce) {
	// if (batch.isProductionComplete()) {
	// throw new IllegalStateException("This batch has already been produced.");
	// }
	// if (numBottlesToProduce > batch.getNumExpectedBottles() -
	// batch.getNumProducedBottlesPreSpill()) {
	// throw new IllegalArgumentException("You cannot produce more bottles than
	// expected for this batch.");
	// }
	//
	// HashMap<TasteProfile, Integer> productionVolume =
	// generateProductionVolume(numBottlesToProduce, batch);
	//
	// // Iterates over all reserved casks and pulls liquid from them if needed for
	// // production volume
	// for (TasteProfile tasteProfile : productionVolume.keySet()) {
	// for (Cask cask : batch.getReservedCasks().keySet()) {
	// // Checks if TasteProfile has already been fulfilled
	// if (productionVolume.get(tasteProfile) == 0)
	// continue;
	//
	// if (cask.getTasteProfile().equals(tasteProfile)) {
	//
	// // TODO: Ensure proper conversion of units here
	// int volume = productionVolume.get(tasteProfile);
	// int caskVolume = (int) cask.getRemainingQuantity();
	//
	// if (volume >= caskVolume) {
	//
	// // remove full volume from cask
	// cask.setRemainingQuantity(0);
	// // remove cask from reservedCasks
	// batch.removeCaskFromReserved(cask);
	// // remove volume from productionVolume
	// productionVolume.put(tasteProfile, volume - caskVolume);
	//
	// } else { // if not full cask needed to fulfill volume needed
	//
	// // cask stays in reservedCasks, reserved volume is decreased
	// cask.setReservedQuantity(caskVolume - volume);
	// // remove volume from productionVolume i.e. set to 0
	// productionVolume.put(tasteProfile, 0);
	//
	// }
	// }
	// }
	// }
	//
	// batch.incNumProducedBottlesPreSpill(numBottlesToProduce);
	// if (batch.getNumProducedBottlesPreSpill() == batch.getNumExpectedBottles()) {
	// batch.markProductionComplete();
	// }
	// }

	// /**
	// * Searches for casks that match the given formula's taste profiles.
	// *
	// * @param formula the formula to match against the casks' taste profiles
	// * @return a list of casks that match the given formula's taste profiles
	// */
	// public ArrayList<Cask> searchCasksForFormula(Formula formula) {
	// Set<TasteProfile> formulaTasteProfiles = formula.getBlueprint().keySet();
	// ArrayList<Cask> casksThatMatchFormula = new ArrayList<>();
	//
	// for (Cask cask : Warehousing.getReadyCasks()) {
	// if (formulaTasteProfiles.contains(cask.getTasteProfile())) {
	// casksThatMatchFormula.add(cask);
	// }
	// }
	// return casksThatMatchFormula;
	// }
}
