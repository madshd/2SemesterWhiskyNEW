package BatchArea;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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

	public Set<TastingNote> getWeightedTastingNotes() {
		Map<TasteProfile, Double> sortedTasteProfiles = blueprint.entrySet().stream()
				.sorted(Map.Entry.<TasteProfile, Double>comparingByValue(Comparator.reverseOrder()))
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(e1, e2) -> {
							return e1;
						},
						LinkedHashMap::new));

		Set<TastingNote> weightedTastingNotes = new HashSet<>();
		for (TasteProfile tp : sortedTasteProfiles.keySet()) {
			weightedTastingNotes.addAll(tp.getTastingNotes());
		}
		return weightedTastingNotes;
	}
}
