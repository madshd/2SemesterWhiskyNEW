package GUI.Batch;

import java.util.HashMap;

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
	private HashMap<TasteProfile, Double> blueprint = new HashMap<>();

	public FormulaCRUD() {
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
	}

	public void showFPCRUDWindow() {
		// Show the modal and wait for it to be closed
		updateList();
		formulaCrudStage.showAndWait();
	}

	// IF UPDATING
	public void showFPCRUDWindow(Formula formula) {
		updateList();
		this.formula = formula;
		setFields(formula);
		formulaCrudStage.showAndWait();
	}

	public void setFields(Formula formula) {
		createButton.setText("Update");
		header.setText("Update Existing Formula");
		updating = true;
		nameInput.setText(formula.getFormulaName());
		blueprint.clear(); // Clear before repopulating
		formula.getBlueprint().forEach((tp, percentage) -> blueprint.put(tp, percentage));
		tasteProfiles.refresh();
	}

	private void initContent(GridPane mainPane) {
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
		tasteProfiles.setMaxWidth(250);
		tasteProfiles.setMinWidth(250);
		tasteProfiles.setMinHeight(350);
		tasteProfiles.setMaxHeight(350);
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
				}
			}

			private TextField createPercentageField(TasteProfile item) {
				TextField percentageField = new TextField();
				percentageField.setPromptText("Enter Percentage");
				percentageField.setMaxWidth(75);
				percentageField.setFocusTraversable(true);

				// Initialize with the value from the blueprint map
				percentageField.setText(blueprint.getOrDefault(item, (double) 0).toString());

				// Add a listener to handle percentage input changes
				percentageField.textProperty().addListener((observable, oldValue, newValue) -> {
					handlePercentageInputChange(item, percentageField, newValue);
				});

				// If the field is left empty, reset the value to 0
				percentageField.focusedProperty().addListener((observable, oldValue, newValue) -> {
					if (!newValue && percentageField.getText().isEmpty()) {
						resetPercentageToZero(item, percentageField);
					}
				});

				return percentageField;
			}

			private void handlePercentageInputChange(TasteProfile item, TextField percentageField, String newValue) {
				try {
					if (newValue.isEmpty())
						return;
					int percentage = Integer.parseInt(newValue);
					if (percentage < 0) {
						percentageField.setStyle("-fx-border-color: red;");
						return;
					}
					// Valid input
					percentageField.setStyle("-fx-border-color: #d4a373;");
					blueprint.put(item, (double) percentage);
					updateTotalPercentage();
				} catch (NumberFormatException e) {
					percentageField.setStyle("-fx-border-color: red;");
				}
			}

			private void resetPercentageToZero(TasteProfile item, TextField percentageField) {
				percentageField.setText("0");
				blueprint.put(item, (double) 0);
				updateTotalPercentage();
			}
		});

		GridPane.setValignment(tasteProfiles, VPos.TOP);

		// Total Percentage
		Label totalLabel = new Label("Total Percentage");
		totalPercentageInput.setEditable(false);
		totalPercentageInput.setPromptText("Total Percentage");
		totalPercentageInput.setFocusTraversable(false);
		totalPercentageInput.setMaxWidth(50);
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
			create(name);
		});

		cancelButton.setOnAction(event -> {
			clearFields();
			formulaCrudStage.close();
		});
	}

	// Create a new Formula
	private void create(String name) {
		if (name == null || name.isEmpty()) {
			errorWindow.showError("Name cannot be empty");
			return;
		}
		if (totalPercentageInput.getText().isEmpty() || Double.parseDouble(totalPercentageInput.getText()) != 100) {
			errorWindow.showError("Total percentage must be 100");
			return;
		}
		// Create or update the formula with selected TasteProfiles and their
		// percentages
		if (updating) {
			Controllers.BatchArea.updateFormula(name, blueprint, formula);
		} else {
			Controllers.BatchArea.createNewFormula(name, blueprint);
		}
		clearFields();
		formulaCrudStage.close();
	}

	// Calculate the total percentage
	private void updateTotalPercentage() {
		double total = 0;
		for (Double value : blueprint.values()) {
			total += value;
		}
		totalPercentageInput.setText(String.valueOf(total));
		if (total != 100) {
			totalPercentageInput.setStyle("-fx-border-color: red;");
		} else {
			totalPercentageInput.setStyle("-fx-border-color: #d4a373;");
		}
	}

	// Clear all percentage inputs in the ListView tasteProfiles
	private void clearPercentageInputs() {
		for (TasteProfile tp : tasteProfiles.getItems()) {
			blueprint.put(tp, (double) 0);
		}
		tasteProfiles.refresh();
		updateTotalPercentage();
	}

	public void updateList() {
		blueprint.clear();
		clearPercentageInputs();
		tasteProfiles.getItems().clear();
		tasteProfiles.getItems().addAll(Controllers.BatchArea.getAllTasteProfiles());
	}

	// Clear all fields
	private void clearFields() {
		formula = null;
		updating = false;
		createButton.setText("Create");
		header.setText("Create New Formula");
		nameInput.clear();
		totalPercentageInput.clear();
		blueprint.clear();
		clearPercentageInputs();
	}
}
