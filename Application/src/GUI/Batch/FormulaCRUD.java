
package GUI.Batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import BatchArea.Formula;
import BatchArea.TasteProfile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import GUI.Common.ErrorWindow;

@SuppressWarnings("unused")
public class FormulaCRUD {

	private final ErrorWindow errorWindow = new ErrorWindow();
	private final Stage formulaCrudStage = new Stage();
	private final TextField nameInput = new TextField();
	private final TextField totalPercentageInput = new TextField();
	private boolean updating = false;
	private Formula formula;
	private Button createButton = new Button("Create");
	private Label header = new Label("Create New Formula");
	private ListView<TasteProfile> tasteProfiles = new ListView<>();
	private HashMap<TasteProfile, Double> blueprint;

	public FormulaCRUD() {
		System.out.println("Initializing FormulaCRUD...");
		formulaCrudStage.setTitle("Formula Manager");

		GridPane gridPane = new GridPane();
		formulaCrudStage.setResizable(false);

		// Set the scene for the modal window
		Scene formulaManagerScene = new Scene(gridPane);
		formulaCrudStage.setScene(formulaManagerScene);
		formulaCrudStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
		formulaManagerScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

		// Initialize content
		initContent(gridPane);
		System.out.println("FormulaCRUD initialized.");
	}

	public void showFPCRUDWindow() {
		System.out.println("Showing FormulaCRUD window...");
		updateList(); // Refresh the ListView
		formulaCrudStage.showAndWait();
		System.out.println("FormulaCRUD window closed.");
	}

	public void showFPCRUDWindow(Formula formula) {
		System.out.println("Showing FormulaCRUD window for updating...");
		this.formula = formula;
		blueprint = new HashMap<>(formula.getBlueprint());
		setFields(formula); // Set fields and refresh ListView
		formulaCrudStage.showAndWait();
		System.out.println("FormulaCRUD window closed.");
	}

	public void setFields(Formula formula) {
		System.out.println("Setting fields for formula: " + formula.getFormulaName());
		createButton.setText("Update");
		header.setText("Update Existing Formula");
		updating = true;
		nameInput.setText(formula.getFormulaName());
		totalPercentageInput.setText("100.0");
		System.out.println("FORMULA BLUEEPRINT: " + formula.getBlueprint());
		System.out.println("Blueprint set: " + blueprint);

		// Update the ListView
		updateList(); // Refresh items based on the new blueprint
	}

	private void initContent(GridPane mainPane) {
		System.out.println("Initializing content...");
		mainPane.setPadding(new Insets(50));
		mainPane.setHgap(10);
		mainPane.setVgap(10);
		mainPane.setAlignment(Pos.CENTER);
		mainPane.setGridLinesVisible(false);

		// For intuitive clearing of textarea focus
		mainPane.setFocusTraversable(true);
		mainPane.setOnMouseClicked(event -> {
			tasteProfiles.getSelectionModel().clearSelection();
			mainPane.requestFocus();
		});

		// Header
		HBox headerBox = new HBox(header);
		headerBox.setAlignment(Pos.CENTER);
		mainPane.add(headerBox, 0, 0);

		// Formula Name
		nameInput.setPromptText("Name");
		nameInput.setFocusTraversable(false);
		mainPane.add(nameInput, 0, 1);
		nameInput.setOnMouseClicked(event -> {
			tasteProfiles.getSelectionModel().clearSelection();
		});

		// TasteProfile Label
		Label label = new Label("Define Taste Profile %-split");
		HBox labelBox1 = new HBox(label);
		labelBox1.setAlignment(Pos.CENTER);
		mainPane.add(labelBox1, 0, 2);

		tasteProfiles.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
		tasteProfiles.setMaxWidth(350);
		tasteProfiles.setMinWidth(350);
		tasteProfiles.setMinHeight(400);
		tasteProfiles.setMaxHeight(400);
		mainPane.add(tasteProfiles, 0, 3, 1, 2);

		// For TasteProfile ListView
		tasteProfiles.setCellFactory(param -> new ListCell<TasteProfile>() {
			@Override
			protected void updateItem(TasteProfile item, boolean empty) {
				super.updateItem(item, empty);

				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.getProfileName());
					TextField percentageField = createPercentageField(item);
					setGraphic(percentageField);

					// Set the field value from the blueprint map
					Double percentage = blueprint.get(item);
					if (percentage != null) {
						percentageField.setText(String.valueOf(percentage));
						percentageField
								.setStyle(percentage < 0 ? "-fx-border-color: red;" : "-fx-border-color: #d4a373;");
					}
				}
			}

			private TextField createPercentageField(TasteProfile item) {
				TextField percentageField = new TextField();
				percentageField.setPromptText("0");
				percentageField.setMaxWidth(75);
				percentageField.setFocusTraversable(true);

				// Add a listener to handle percentage input changes
				percentageField.textProperty().addListener((observable, oldValue, newValue) -> {
					handlePercentageInputChange(item, percentageField, newValue);
				});

				return percentageField;
			}

			private void handlePercentageInputChange(TasteProfile item, TextField percentageField, String newValue) {
				try {
					double percentage;
					System.out.println("New value for " + item.getProfileName() + ": " + newValue);
					if (newValue.isEmpty()) {
						System.out.println("New value is empty.");
						percentage = 0;
						percentageField.setText("0");
						return;
					}
					percentage = Double.parseDouble(newValue);
					System.out.println("Parsed percentage: " + percentage);
					if (percentage < 0) {
						System.out.println("Percentage is less than 0.");
						percentageField.setStyle("-fx-border-color: red;");
						return;
					}
					blueprint.put(item, percentage); // Update blueprint
					percentageField.setStyle("-fx-border-color: #d4a373;");
					updateTotalPercentage();
				} catch (NumberFormatException e) {
					System.out.println("NumberFormatException: " + e.getMessage());
					percentageField.setStyle("-fx-border-color: red;");
				}
			}
		});

		GridPane.setValignment(tasteProfiles, VPos.TOP);

		// Total Percentage
		Label totalLabel = new Label("Total Percentage");
		totalPercentageInput.setEditable(false);
		totalPercentageInput.setPromptText("Total Percentage");
		totalPercentageInput.setFocusTraversable(false);
		totalPercentageInput.setMaxWidth(75);
		HBox totalPercentageBox = new HBox(totalLabel, totalPercentageInput);
		mainPane.add(totalPercentageBox, 0, 5);
		totalPercentageBox.setAlignment(Pos.CENTER_LEFT);
		totalPercentageBox.setSpacing(25);
		totalPercentageInput.setMouseTransparent(true);

		// Buttons
		Button cancelButton = new Button("Cancel");
		HBox buttonBox = new HBox(createButton, cancelButton);
		mainPane.add(buttonBox, 0, 6, 1, 1);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(25);

		// Button Actions
		createButton.setOnAction(event -> {
			String name = nameInput.getText();
			System.out.println("Create button clicked with name: " + name);
			create(name);
		});

		cancelButton.setOnAction(event -> {
			System.out.println("Cancel button clicked.");
			clearFields();
			formulaCrudStage.close();
		});

		System.out.println("Content initialized.");
	}

	// Create a new Formula
	private void create(String name) {
		System.out.println("Create method called with name: " + name);
		if (name == null || name.isEmpty()) {
			System.out.println("Name is null or empty.");
			errorWindow.showError("Name cannot be empty");
			return;
		}
		if (totalPercentageInput.getText().isEmpty() || Double.parseDouble(totalPercentageInput.getText()) != 100.0) {
			System.out.println("Total percentage is not 100.0 or is empty.");
			errorWindow.showError("Total percentage must be 100.0");
			return;
		}

		List<TasteProfile> toRemove = new ArrayList<>();
		blueprint.keySet().forEach(tp -> {
			if (blueprint.get(tp) <= 0) {
				System.out.println("Marking TasteProfile for removal: " + tp);
				toRemove.add(tp);
			}
		});
		toRemove.forEach(tp -> {
			System.out.println("Removing TasteProfile: " + tp);
			blueprint.remove(tp);
		});

		// Create or update the formula with selected TasteProfiles and their
		// percentages
		if (updating) {
			System.out.println("Updating existing formula.");
			System.out.println("Blueprint: " + blueprint);
			Controllers.BatchArea.updateFormula(name, blueprint, formula);
			System.out.println("Updated formula blueprint: " + formula.getBlueprint());
		} else {
			System.out.println("Creating new formula.");
			Controllers.BatchArea.createNewFormula(name, blueprint);
		}
		clearFields();
		System.out.println("Fields cleared.");
		formulaCrudStage.close();
		System.out.println("Formula CRUD stage closed.");
	}

	// Calculate the total percentage
	private void updateTotalPercentage() {
		System.out.println("Updating total percentage...");
		double total = 0;
		for (Double value : blueprint.values()) {
			total += value;
		}
		totalPercentageInput.setText(String.valueOf(total));
		if (total != 100.0) {
			System.out.println("Total percentage is not 100.0: " + total);
			totalPercentageInput.setStyle("-fx-border-color: red;");
		} else {
			System.out.println("Total percentage is 100.0");
			totalPercentageInput.setStyle("-fx-border-color: black;");
		}
	}

	private void updateList() {
		System.out.println("Updating taste profiles list...");
		List<TasteProfile> profiles = Controllers.BatchArea.getAllTasteProfiles();

		// Re-populate the ListView
		tasteProfiles.getItems().clear();
		tasteProfiles.getItems().addAll(profiles);

		// Manually refresh each item's percentage value in the blueprint
		tasteProfiles.setCellFactory(param -> new ListCell<>() {
			@Override
			protected void updateItem(TasteProfile item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					setText(item.getProfileName());
					TextField percentageField = createPercentageField(item);
					setGraphic(percentageField);

					// Set the field value from the blueprint map
					Double percentage = blueprint.get(item);
					if (percentage != null) {
						percentageField.setText(String.valueOf(percentage));
						percentageField
								.setStyle(percentage < 0 ? "-fx-border-color: red;" : "-fx-border-color: #d4a373;");
					}
				}
			}

			private TextField createPercentageField(TasteProfile item) {
				TextField percentageField = new TextField();
				percentageField.setPromptText("0");
				percentageField.setMaxWidth(75);
				percentageField.setFocusTraversable(true);

				// Add a listener to handle percentage input changes
				percentageField.textProperty().addListener((observable, oldValue, newValue) -> {
					handlePercentageInputChange(item, percentageField, newValue);
				});

				return percentageField;
			}

			private void handlePercentageInputChange(TasteProfile item, TextField percentageField, String newValue) {
				try {
					double percentage;
					if (newValue.isEmpty()) {
						percentage = 0;
						percentageField.setText("0");
						return;
					}
					percentage = Double.parseDouble(newValue);
					if (percentage < 0) {
						percentageField.setStyle("-fx-border-color: red;");
						return;
					}
					blueprint.put(item, percentage); // Update blueprint
					percentageField.setStyle("-fx-border-color: #d4a373;");
					updateTotalPercentage();
				} catch (NumberFormatException e) {
					percentageField.setStyle("-fx-border-color: red;");
				}
			}
		});
		System.out.println("Taste profiles list updated: " + profiles);
	}

	private void clearFields() {
		System.out.println("Clearing fields...");
		nameInput.clear();
		totalPercentageInput.clear();
		tasteProfiles.refresh();
		updating = false;
		System.out.println("Fields cleared.");
	}
}
