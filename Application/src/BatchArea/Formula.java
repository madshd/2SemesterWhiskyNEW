package BatchArea;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Formula implements Serializable {

	private String formulaName;
	private HashMap<TasteProfile, Integer> blueprint = new HashMap<>();

	public Formula(String formulaName, Map<TasteProfile, Integer> blueprint) {
		this.formulaName = formulaName;
	}

	// ---------------------------GENERIC-GETTERS----------------------------//

	public String getFormulaName() {
		return formulaName;
	}

	public HashMap<TasteProfile, Integer> getBlueprint() {
		return new HashMap<>(blueprint);
	}
}
