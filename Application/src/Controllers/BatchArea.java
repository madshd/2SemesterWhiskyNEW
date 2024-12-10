package Controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	public static Set<Cask> searchCasks(Formula formula){
		Set<Cask> casks = new HashSet<>();
		for (Cask cask : storage.getCasks()) {
			for(TasteProfile tp : formula.getBlueprint().keySet()){
				if(cask.getTasteProfile() != null && cask.getTasteProfile().equals(tp)){
					casks.add(cask);
				}
			}
		}
		return casks;
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

	public static Batch createNewBatch(Product product, int numExpectedBottles, boolean onlyReady) {
		Batch batch = new Batch(product, numExpectedBottles);
		storage.storeBatch(batch);
		reserveQuantityInCasks(batch, onlyReady);
		return batch;
	}

	/**
	 * Reserves the required quantity in casks for the given batch.
	 *
	 * @param batch          the batch for which the quantity needs to be reserved
	 * @param onlyReadyCasks flag indicating whether to consider only ready casks
	 */
	private static void reserveQuantityInCasks(Batch batch, boolean onlyReadyCasks) {
		Map<TasteProfile, Double> productionVolume = calculateProductionVolume(batch.getNumExpectedBottles(), batch);
		Map<TasteProfile, List<Cask>> matchingCasks = getMatchingCasks(batch, productionVolume, onlyReadyCasks);

		for (TasteProfile tp : productionVolume.keySet()) {
			double requiredVolume = productionVolume.get(tp);
			List<Cask> casks = matchingCasks.get(tp);
			double remainingVolume = requiredVolume;
			Iterator<Cask> iterator = casks.iterator();

			while (iterator.hasNext() && remainingVolume > 0) {
				Cask cask = iterator.next();
				double caskVolume = cask.getLegalQuantity();
				double reservedAmount = Math.min(remainingVolume, caskVolume);

				makeCaskReservation(batch, cask, reservedAmount);
				remainingVolume -= reservedAmount;
			}
		}
	}

	private static void makeCaskReservation(Batch batch, Cask cask, double reservedAmount) {
		cask.makeReservation(batch, reservedAmount);
		batch.addReservedCask(cask, reservedAmount);
	}

	/**
	 * Produces the specified number of bottles for the given batch.
	 *
	 * @param batch               the batch to be produced
	 * @param numBottlesToProduce the number of bottles to produce
	 * @return a map of casks used and their respective volumes
	 */
	public static Map<Cask, Double> produceBatch(Batch batch, int numBottlesToProduce) {
		Map<Cask, Double> casksToUse = new HashMap<>();

		validateProductionRequest(batch, numBottlesToProduce);

		Map<TasteProfile, Double> productionVolumeTP = calculateProductionVolume(numBottlesToProduce, batch);

		for (TasteProfile tp : productionVolumeTP.keySet()) {
			processCasksForTasteProfile(batch, tp, productionVolumeTP.get(tp), batch.getReservedCasks().keySet(), casksToUse);
		}

		batch.incNumProducedBottles(numBottlesToProduce);
		finalizeBatchProduction(batch);
		return casksToUse;
	}

	/**
	 * Validates the production request for the given batch.
	 *
	 * @param batch               the batch to be validated
	 * @param numBottlesToProduce the number of bottles to produce
	 * @throws IllegalArgumentException if the number of bottles to produce exceeds
	 *                                  the maximum or expected number of bottles
	 */
	private static void validateProductionRequest(Batch batch, int numBottlesToProduce) {
		int maxNumBottles = calculateMaxNumBottles(batch.getProduct(), true, batch);
		if (numBottlesToProduce > maxNumBottles) {
			throw new IllegalArgumentException("Number of bottles to produce exceeds the maximum number of bottles.");
		}
		int totalProducedBottles = numBottlesToProduce + batch.getNumProducedBottles();
		if (totalProducedBottles > batch.getNumExpectedBottles()) {
			throw new IllegalArgumentException("Number of bottles to produce exceeds the expected number of bottles.");
		}
	}

	/**
	 * Processes casks for a specific taste profile.
	 *
	 * @param batch          the batch for which the casks are being processed
	 * @param tp             the taste profile
	 * @param requiredVolume the volume required to be processed
	 * @param set          the list of casks to be processed
	 * @param casksToUse     a map of casks used and their respective volumes
	 */
	private static void processCasksForTasteProfile(Batch batch, TasteProfile tp, double requiredVolume,
			Set<Cask> set, Map<Cask, Double> casksToUse) {
		double remainingVolume = requiredVolume;
		Iterator<Cask> iterator = set.iterator();
		while (iterator.hasNext() && remainingVolume > 0) {
			Cask cask = iterator.next();
			double reservedAmount = batch.getReservedCasks().get(cask);
			double usedAmount = Math.min(remainingVolume, reservedAmount);

			processCaskForBatch(batch, cask, usedAmount, casksToUse);
			remainingVolume -= usedAmount;
		}
	}

	private static void processCaskForBatch(Batch batch, Cask cask, double usedAmount, Map<Cask, Double> casksToUse) {
		Production.caskBottling(cask, usedAmount, LocalDate.now());
		cask.spendReservation(batch, usedAmount);
		casksToUse.put(cask, usedAmount);
		batch.addUsedCask(cask);
	}

	private static void finalizeBatchProduction(Batch batch) {
		if (batch.getNumProducedBottles() == batch.getNumExpectedBottles()) {
			batch.markProductionComplete();
		}
	}

	/**
	 * Retrieves a map of matching casks for each taste profile based on the given
	 * batch and production volume.
	 *
	 * @param batch            the batch for which matching casks are to be found
	 * @param productionVolume a map containing taste profiles and their
	 *                         corresponding production volumes
	 * @return a map where each key is a TasteProfile and the value is a list of
	 *         Casks that match the taste profile
	 */
	public static Map<TasteProfile, List<Cask>> getMatchingCasks(Batch batch,
			Map<TasteProfile, Double> productionVolume, Boolean onlyReadyCasks) {
		Map<TasteProfile, List<Cask>> matchingCasks = new HashMap<>();

		// Choose casks based on onlyReady flag
		List<Cask> casksToCheck = onlyReadyCasks ? Warehousing.getReadyCasks() : storage.getCasks();

		for (TasteProfile tp : productionVolume.keySet()) {
			matchingCasks.put(tp, new ArrayList<>());
			for (Cask cask : casksToCheck) {
				if (tp != null && tp.equals(cask.getTasteProfile())) {
					matchingCasks.get(tp).add(cask);
				}
			}
		}
		return matchingCasks;
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
	 * Calculates the maximum number of bottles that can be produced from the
	 * available casks, considering the reserved casks in the given batch if
	 * provided.
	 *
	 * @param product        The product for which the bottles are being calculated.
	 * @param onlyReadyCasks If true, only casks that are ready will be considered.
	 * @param batch          The batch containing reserved casks (optional).
	 * @return The maximum number of bottles that can be produced.
	 */
	public static int calculateMaxNumBottles(Product product, boolean onlyReadyCasks, Batch batch) {
		int maxNumBottles = Integer.MAX_VALUE;
		List<Cask> casksToCheck = onlyReadyCasks ? Warehousing.getReadyCasks() : storage.getCasks();
		double bottleSizeML = product.getBottleSize();
		double bottleSizeLITERS = UnitConverter.convertUnits(Unit.MILLILITERS, Unit.LITERS, bottleSizeML);
		HashMap<TasteProfile, Double> blueprint = product.getFormula().getBlueprint();

		for (TasteProfile tp : blueprint.keySet()) {
			double percentage = blueprint.get(tp) / 100.0;
			double totalVolumePerTP = 0;

			for (Cask cask : casksToCheck) {
				double reservedAmount = 0;
				if (batch != null) {
					// Only check for reserved casks if a batch is provided
					for (Cask reservedCask : batch.getReservedCasks().keySet()) {
						if (reservedCask.equals(cask)) {
							reservedAmount = batch.getReservedCasks().get(reservedCask);
						}
					}
				}

				// Only add volume for casks with the matching taste profile
				if (cask.getTasteProfile() != null && cask.getTasteProfile().equals(tp)) {
					totalVolumePerTP += cask.getLegalQuantity() + reservedAmount;
				}
			}

			double amountBottlesPossiblePerTP = (totalVolumePerTP / bottleSizeLITERS) * percentage;
			maxNumBottles = Math.min(maxNumBottles, (int) amountBottlesPossiblePerTP);
		}

		return maxNumBottles;
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
		double totalProductionVolumeML = batch.getProduct().getBottleSize() * numBottlesToProduce;
		double totalProductionVolumeLITER = UnitConverter.convertUnits(Unit.MILLILITERS, Unit.LITERS,totalProductionVolumeML);
		HashMap<TasteProfile, Double> blueprint = batch.getProduct().getFormula().getBlueprint();
		HashMap<TasteProfile, Double> productionVolume = new HashMap<>();
		for (TasteProfile tasteProfile : blueprint.keySet()) {
			double percentage = blueprint.get(tasteProfile) / 100.0;
			double volume = totalProductionVolumeLITER * percentage;
			productionVolume.put(tasteProfile, volume);
		}
		return productionVolume;
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
	 * Checks if the production has started for the given batch.
		* BUT reverses the logic for certain mathods in GUI that needs reversed
	 *
	 * @param b the batch to check
	 * @return true if production has started, false otherwise
	 */
	public static boolean isProductionStartedReversed(Batch b) {
		if (b.getNumProducedBottles() > 0) {
			return false;
		} else {
			return true;
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
