package BatchArea;

import java.util.ArrayList;
import java.util.List;

import Enumerations.TastingNote;

public class TasteProfile {

	private String profileID;
	private String description;
	private List<TastingNote> tags = new ArrayList<>();

	/*
	 * Controller method ensures that an object of TasteProfile
	 * cannot be created without atleast 1 tag.
	 * This is not enforced in TasteProfile Constructor
	 */
	public TasteProfile(String profileID, String description) {
		this.profileID = profileID;
		this.description = description;
	}

	public void addTag(TastingNote tag) {
		tags.add(tag);
	}

	// ---------------------------GENERIC-GETTERS----------------------------//

	public String getProfileID() {
		return profileID;
	}

	public String getDescription() {
		return description;
	}

	public List<TastingNote> getTags() {
		return new ArrayList<>(tags);
	}

}
