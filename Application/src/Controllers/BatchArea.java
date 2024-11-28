package Controllers;

import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import BatchArea.*;
import Enumerations.TastingNote;
import Warehousing.Cask;

public class BatchArea {

	/**
	 * Creates a new product with the specified name.
	 * ProductID is generated automatically in the Product constructor
	 *
	 * @param productName the name of the product to be created
	 * @return the newly created Product object
	 */

	public static Product createNewProduct(String productName, int bottleSize) {
		Product product = new Product(productName, bottleSize);
		// TODO: save this object in storage
		return product;
	}

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
	 * @throws IllegalArgumentException if any percentage is not between 1 and 100,
	 *                                  or if the total percentage is not 100
	 */
	public static Formula createNewFormula(String formulaName, HashMap<TasteProfile, Integer> blueprint) {
		int totalPercantage = 0;
		for (Integer percentage : blueprint.values()) {
			if (percentage < 1 || percentage > 100) {
				throw new IllegalArgumentException("Percentage must be between 1 and 100.");
			}
			totalPercantage += percentage;
		}
		if (totalPercantage != 100) {
			throw new IllegalArgumentException("The total percentage of the taste profiles must be 100.");
		}
		Formula formula = new Formula(formulaName, blueprint);
		// TODO: Save this object in storage
		return formula;
	}

	/**
	 * Creates a new TasteProfile with the given profile ID, description, and tags.
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
		if (tags.isEmpty()) {
			throw new IllegalArgumentException("A TasteProfile must have at least one tag.");
		} else {
			TasteProfile tasteProfile = new TasteProfile(profileID, description);
			for (TastingNote tag : tags) {
				tasteProfile.addTag(tag);
			}
			// TODO: Save this object in storage
			return tasteProfile;
		}
	}

	public Batch createNewBatch(Product product, int numExpectedBottles) {
		// TODO: Implement checks for max num of possible bottles according to quantity
		// of ingredients for recipe
		Batch batch = new Batch(product, numExpectedBottles);
		// TODO: Save this object in storage
		return batch;
	}

	/**
	 * Generates the production volume for each taste profile in the batch based on
	 * the amount of bottles to be produced.
	 *
	 * @param numBottlesToProduce the number of bottles to produce
	 * @param batch               the batch containing the product and its formula
	 * @return a HashMap where the key is the TasteProfile and the value is the
	 *         production volume in milliliters
	 */
	public static HashMap<TasteProfile, Integer> generateProductionVolume(int numBottlesToProduce, Batch batch) {
		int totalProductionVolumeML = numBottlesToProduce * batch.getProduct().getBottleSize() * numBottlesToProduce;
		HashMap<TasteProfile, Integer> blueprint = batch.getProduct().getFormula().getBlueprint();
		HashMap<TasteProfile, Integer> productionVolume = new HashMap<>();
		for (TasteProfile tasteProfile : blueprint.keySet()) {
			int percentage = blueprint.get(tasteProfile);
			int volume = totalProductionVolumeML * percentage / 100;
			productionVolume.put(tasteProfile, volume);
		}
		return productionVolume;
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
	 * @throws IllegalArgumentException if the number of bottles to produce exceeds
	 *                                  expected number of bottles for full batch
	 */
	public static void producePartOfBatch(Batch batch, int numBottlesToProduce) {
		if (batch.isProductionComplete()) {
			throw new IllegalStateException("This batch has already been produced.");
		}
		if (numBottlesToProduce > batch.getNumExpectedBottles() - batch.getNumProducedBottlesPreSpill()) {
			throw new IllegalArgumentException("You cannot produce more bottles than expected for this batch.");
		}

		HashMap<TasteProfile, Integer> productionVolume = generateProductionVolume(numBottlesToProduce, batch);

		// Iterates over all reserved casks and pulls liquid from them if needed for
		// production volume
//		for (TasteProfile tasteProfile : productionVolume.keySet()) {
//			for (Cask cask : batch.getReservedCasks().keySet()) {
//				// Checks if TasteProfile has already been fulfilled
//				if (productionVolume.get(tasteProfile) == 0)
//					continue;
//
//				if (cask.getTasteProfile().equals(tasteProfile)) {
//
//					// TODO: Ensure proper conversion of units here
//					int volume = productionVolume.get(tasteProfile);
//					int caskVolume = (int) cask.getRemainingQuantity();
//
//					if (volume >= caskVolume) {
//
//						// remove full volume from cask
//						cask.setRemainingQuantity(0);
//						// remove cask from reservedCasks
//						batch.removeCaskFromReserved(cask);
//						// remove volume from productionVolume
//						productionVolume.put(tasteProfile, volume - caskVolume);
//
//					} else { // if not full cask needed to fulfill volume needed
//
//						// cask stays in reservedCasks, reserved volume is decreased
//						cask.setReservedQuantity(caskVolume - volume);
//						// remove volume from productionVolume i.e. set to 0
//						productionVolume.put(tasteProfile, 0);
//
//					}
//				}
//			}
//		}

		batch.incNumProducedBottlesPreSpill(numBottlesToProduce);
		if (batch.getNumProducedBottlesPreSpill() == batch.getNumExpectedBottles()) {
			batch.markProductionComplete();
		}
	}

	/**
	 * Searches for casks that match the given formula's taste profiles.
	 *
	 * @param formula the formula to match against the casks' taste profiles
	 * @return a list of casks that match the given formula's taste profiles
	 */
//	public ArrayList<Cask> searchCasksForFormula(Formula formula) {
//		Set<TasteProfile> formulaTasteProfiles = formula.getBlueprint().keySet();
//		ArrayList<Cask> casksThatMatchFormula = new ArrayList<>();
//
//		for (Cask cask : Warehousing.getReadyCasks()) {
//			if (formulaTasteProfiles.contains(cask.getTasteProfile())) {
//				casksThatMatchFormula.add(cask);
//			}
//		}
//		return casksThatMatchFormula;
//	}
}
