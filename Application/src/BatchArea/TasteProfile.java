package BatchArea;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Enumerations.TastingNote;

public class TasteProfile implements Serializable {

	private String profileName;
	private String description;
	private List<TastingNote> tastingNotes = new ArrayList<>();

	/*
	 * Controller method ensures that an object of TasteProfile
	 * cannot be created without atleast 1 tag.
	 * This is not enforced in TasteProfile Constructor
	 */
	public TasteProfile(String profileID, String description) {
		this.profileName = profileID;
		this.description = description;
	}

	public void addTastingNote(TastingNote tn) {
		tastingNotes.add(tn);
	}

	// ---------------------------GENERIC-GETTERS----------------------------//

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void clearTastingNotes() {
		tastingNotes.clear();
	}

	public String getProfileName() {
		return profileName;
	}

	public String getDescription() {
		return description;
	}

	public List<TastingNote> getTastingNotes() {
		return new ArrayList<>(tastingNotes);
	}

	@Override
	public String toString() {
		return profileName;
	}

	public String listToString() {
		StringBuilder sb = new StringBuilder();
		sb.append("--- ").append(profileName).append(" ---\n\n").append(description);
		sb.append("\n\nTasting Notes:");
		for (TastingNote tn : tastingNotes) {
			sb.append("\n").append("- ").append(tn);
		}
		return sb.toString();
	}

}
