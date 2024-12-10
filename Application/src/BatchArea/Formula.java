package BatchArea;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import Enumerations.TastingNote;

public class Formula implements Serializable {

	private String formulaName;
	private HashMap<TasteProfile, Double> blueprint = new HashMap<>();

	public Formula(String formulaName, Map<TasteProfile, Double> blueprint) {
		this.formulaName = formulaName;
		this.blueprint = new HashMap<>(blueprint);
	}

	// ---------------------------GENERIC-GETTERS----------------------------//

	public String getFormulaName() {
		return formulaName;
	}

	public HashMap<TasteProfile, Double> getBlueprint() {
		return new HashMap<>(blueprint);
	}

	public String toString() {
		return formulaName;
	}

	public String listToString() {
		StringBuilder sb = new StringBuilder();
		sb.append("--- ").append(formulaName).append(" ---\n\n");
		sb.append("Blueprint:\n\n");
		sb.append(String.format("%-20s | %10s\n", "Taste Profile", "Percentage"));
		sb.append(String.format("%-20s | %10s\n", "--------------------", "----------"));
		for (TasteProfile tp : blueprint.keySet()) {
			// Ensure blueprint.get(tp) returns an integer
			sb.append(String.format("%-20s | %10d%%\n", tp, blueprint.get(tp).intValue()));
		}
		return sb.toString();
	}

	public void setFormulaName(String formulaName) {
		this.formulaName = formulaName;
	}

	public void setBlueprint(HashMap<TasteProfile, Double> blueprint) {
		this.blueprint = blueprint;
	}

	/**
	 * Retrieves a set of weighted tasting notes based on the taste profiles and
	 * their percentage quantities in the Formulas blueprint.
	 * The taste profiles are sorted in descending order of their weights before
	 * extracting the tasting notes.
	 *
	 * @return a set of weighted tasting notes
	 */
	public Set<TastingNote> getWeightedTastingNotes() {
		List<Map.Entry<TasteProfile, Double>> entries = new ArrayList<>(blueprint.entrySet());

		entries.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

		Map<TasteProfile, Double> sortedTasteProfiles = new LinkedHashMap<>();
		for (Map.Entry<TasteProfile, Double> entry : entries) {
			sortedTasteProfiles.put(entry.getKey(), entry.getValue());
		}

		Set<TastingNote> weightedTastingNotes = new HashSet<>();
		for (TasteProfile tp : sortedTasteProfiles.keySet()) {
			weightedTastingNotes.addAll(tp.getTastingNotes());
		}

		return weightedTastingNotes;
	}

}
