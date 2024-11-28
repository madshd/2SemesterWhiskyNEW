package BatchArea;

import Enumerations.TastingNote;

import java.util.ArrayList;
import java.util.List;

public class TasteProfile {

	private String profileID;
	private String description;
	private List<TastingNote> tags = new ArrayList<>();

	/*
	 * TODO:
	 * Controller method needs to ensure that an object of TasteProfile
	 * cannot be created without atleast 1 tag.
	 * This is not enforced in TasteProfile Constructor
	 */
	public TasteProfile(String profileID, String description) {
		this.profileID = profileID;
		this.description = description;
	}

	/**
	 * Adds a tag to the TasteProfile
	 * 
	 * @param tag
	 */

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
