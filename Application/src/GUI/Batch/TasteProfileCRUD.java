package GUI.Batch;
import java.util.ArrayList;

import BatchArea.TasteProfile;
import Enumerations.TastingNote;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import GUI.Common.ErrorWindow;

@SuppressWarnings("unused")
public class TasteProfileCRUD {

	private final ErrorWindow errorWindow = new ErrorWindow();
	private final Stage tasteProfileCrudStage = new Stage();
	private final TextField nameInput = new TextField();
	private final TextArea descriptionInput = new TextArea();
	private final ListView<TastingNote> tastingNotes = new ListView<>();
	private boolean updating = false;
	private TasteProfile tp;
	Button createButton = new Button("Create");
	Label header = new Label("Create New Taste Profile");

	public TasteProfileCRUD() {
		tasteProfileCrudStage.setTitle("Formula Manager");

		GridPane gridPane = new GridPane();
		tasteProfileCrudStage.setResizable(false);

		Scene formulaManagerScene = new Scene(gridPane);
		tasteProfileCrudStage.setScene(formulaManagerScene);
		tasteProfileCrudStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
		formulaManagerScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

		initContent(gridPane);
	}

	public void showTPCRUDWindow() {
		tasteProfileCrudStage.showAndWait();
	}

	// IF UPDATING
	public void showTPCRUDWindow(TasteProfile tp) {
		this.tp = tp;
		setFields(tp);
		tasteProfileCrudStage.showAndWait();
	}

	public void setFields(TasteProfile tp) {
		createButton.setText("Update");
		header.setText("Update Existing Taste Profile");
		updating = true;
		nameInput.setText(tp.getProfileName());
		descriptionInput.setText(tp.getDescription());
		for (TastingNote note : tp.getTastingNotes()) {
			tastingNotes.getSelectionModel().select(note);
		}
	}

	private void initContent(GridPane mainPane) {
		mainPane.setPadding(new Insets(50));
		mainPane.setHgap(10);
		mainPane.setVgap(10);
		mainPane.setAlignment(Pos.CENTER);
		mainPane.setGridLinesVisible(false);

		// For intuitive clearing of textarea focus
		mainPane.setOnMouseClicked(event -> mainPane.requestFocus());

		// Header
		HBox headerBox = new HBox(header);
		headerBox.setAlignment(Pos.CENTER);
		mainPane.add(headerBox, 0, 0);

		// Taste Profile Name
		nameInput.setPromptText("Name");
		nameInput.setFocusTraversable(false);
		mainPane.add(nameInput, 0, 1);

		// Description
		descriptionInput.setPromptText("Description");
		descriptionInput.setWrapText(true);
		descriptionInput.setFocusTraversable(false);
		descriptionInput.setMaxWidth(250);
		descriptionInput.setMinHeight(300);
		mainPane.add(descriptionInput, 0, 2);

		// Tasting Notes
		for (TastingNote note : TastingNote.values()) {
			tastingNotes.getItems().add(note);
		}
		tastingNotes.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
		tastingNotes.setMaxWidth(250);
		tastingNotes.setMinWidth(250);
		tastingNotes.setMinHeight(350);
		tastingNotes.setMaxHeight(350);
		mainPane.add(tastingNotes, 1, 1, 1, 2);
		GridPane.setValignment(tastingNotes, VPos.TOP);

		// Tasting Notes Labels
		Label label = new Label("Select Tasting Notes");
		HBox labelBox1 = new HBox(label);
		labelBox1.setAlignment(Pos.CENTER);
		mainPane.add(labelBox1, 1, 0);

		// Tasting notes selection helper text
		Label label2 = new Label("Select multiple by holding Ctrl/Cmd");
		HBox labelBox2 = new HBox(label2);
		mainPane.add(labelBox2, 1, 3);
		labelBox2.setAlignment(Pos.CENTER_RIGHT);

		// Buttons
		Button cancelButton = new Button("Cancel");
		HBox buttonBox = new HBox(createButton, cancelButton);
		mainPane.add(buttonBox, 0, 3, 1, 1);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(25);

		// Button Actions
		createButton.setOnAction(event -> {
			String name = nameInput.getText();
			String description = descriptionInput.getText();
			ArrayList<TastingNote> tags = new ArrayList<>(tastingNotes.getSelectionModel().getSelectedItems());
			create(name, description, tags);
		});

		cancelButton.setOnAction(event -> {
			clearFields();
			tasteProfileCrudStage.close();
		});
	}

	// Create a new Taste Profile
	private void create(String name, String description, ArrayList<TastingNote> tags) {
		if (name == null || name.isEmpty()) {
			errorWindow.showError("Name cannot be empty");
			return;
		}
		if (description == null || description.isEmpty()) {
			errorWindow.showError("Description cannot be empty");
			return;
		}
		if (tags == null || tags.isEmpty()) {
			errorWindow.showError("A Taste Profile must contain at least one Tasting Note");
			return;
		}
		if (updating) {
			Controllers.BatchArea.updateTasteProfile(name, description, tags, tp);
		} else {
			Controllers.BatchArea.createNewTasteProfile(name, description, tags);
		}
		clearFields();
		tasteProfileCrudStage.close();
	}

	// Clear all fields
	private void clearFields() {
		tp = null;
		updating = false;
		createButton.setText("Create");
		header.setText("Create New Taste Profile");
		nameInput.clear();
		descriptionInput.clear();
		tastingNotes.getSelectionModel().clearSelection();
	}
}
