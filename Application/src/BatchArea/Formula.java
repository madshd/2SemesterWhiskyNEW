package BatchArea;

import java.util.HashMap;
import java.util.Map;

public class Formula {

	private String formulaName;
	private Map<TasteProfile, Integer> blueprint = new HashMap<>();

	/*
	 * TODO:
	 * Controller method needs to ensure that an object of Formula
	 * cannot be created with less than 1 TasteProfile.
	 * This is not enforced in Formula Constructor
	 */
	public Formula(String formulaName) {
		this.formulaName = formulaName;
	}

	/**
	 * Adds a TasteProfile to the blueprint of the formula
	 * 
	 * @param tasteProfile
	 * @param percentage   must be Integer between 1 and 100
	 */

	public void addTasteProfileToBlueprint(TasteProfile tasteProfile, int percentage) {
		blueprint.put(tasteProfile, percentage);
	}

	// ---------------------------GENERIC-GETTERS----------------------------//

	public String getFormulaName() {
		return formulaName;
	}

	public Map<TasteProfile, Integer> getBlueprint() {
		return new HashMap<>(blueprint);
	}
}
