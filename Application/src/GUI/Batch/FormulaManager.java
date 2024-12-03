package GUI.Batch;

import BatchArea.Formula;
import BatchArea.TasteProfile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import GUI.Common.ErrorWindow;

@SuppressWarnings("unused")
public class FormulaManager {

	ListView<Formula> formulaList = new ListView<Formula>();
	ListView<TasteProfile> tasteList = new ListView<>();
	TasteProfileCRUD tpcrud = new TasteProfileCRUD();
	FormulaCRUD fpcrud = new FormulaCRUD();
	ErrorWindow errorWindow = new ErrorWindow();
	TextArea infoArea = new TextArea();

	public void showFormulaManagerWindow() {
		Stage formulaManagerStage = new Stage();
		formulaManagerStage.initModality(Modality.APPLICATION_MODAL);
		formulaManagerStage.setTitle("Formula Manager");

		GridPane gridPane = new GridPane();
		formulaManagerStage.setResizable(false);

		Scene formulaManagerScene = new Scene(gridPane);
		formulaManagerStage.setScene(formulaManagerScene);
		formulaManagerScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
		initContent(gridPane);

		updateLists();

		formulaManagerStage.showAndWait();
	}

	public void initContent(GridPane mainPane) {

		mainPane.setPadding(new Insets(50));
		mainPane.setHgap(10);
		mainPane.setVgap(10);
		mainPane.setAlignment(Pos.CENTER);

		mainPane.setGridLinesVisible(false);

		// For intuitive clearing of textarea focus
		mainPane.setOnMouseClicked(event -> {
			mainPane.requestFocus();
			formulaList.getSelectionModel().clearSelection();
			tasteList.getSelectionModel().clearSelection();
			infoArea.setText("Select a formula or taste profile to view its details");
		});

		GridPane formulaPane = new GridPane();
		formulaPaneInit(formulaPane);
		GridPane tastePane = new GridPane();
		tastePaneInit(tastePane);
		GridPane infoPane = new GridPane();
		infoPaneInit(infoPane);

		// Close Button
		// Button formulaManagerButton = new Button("Close");
		// formulaManagerButton.setOnAction(e -> {
		// mainPane.getScene().getWindow().hide();
		// });
		// mainPane.add(formulaManagerButton, 0, 0);

		mainPane.add(formulaPane, 0, 0);
		mainPane.add(tastePane, 0, 1);
		mainPane.add(infoPane, 1, 0, 1, 2);
		GridPane.setMargin(infoPane, new Insets(0, 0, 50, 0));
	}

	public void infoPaneInit(GridPane infoPane) {
		infoPane.setPadding(new Insets(10));
		infoPane.setHgap(10);
		infoPane.setVgap(10);
		infoPane.setAlignment(Pos.CENTER);

		infoArea.setMinHeight(610);
		infoArea.setMaxWidth(350);
		infoArea.setMinWidth(350);
		infoArea.setEditable(false);
		infoArea.setFocusTraversable(false);
		infoArea.setWrapText(true);
		infoArea.setMouseTransparent(true);

		infoPane.add(new Label("Details"), 0, 0);
		infoPane.add(infoArea, 0, 1);

		infoArea.setText("Select a formula or taste profile to view its details");
		infoArea.setId("infoArea");
	}

	public void formulaPaneInit(GridPane formulaPane) {
		formulaPane.setPadding(new Insets(10));
		formulaPane.setHgap(10);
		formulaPane.setVgap(10);
		formulaPane.setAlignment(Pos.CENTER);
		int width = 300;
		int height = 250;

		formulaList.setPlaceholder(new Label("No formulae found"));
		formulaList.setMinSize(width, height);
		formulaList.setMaxSize(width, height);

		formulaList.setOnMouseClicked(e -> {
			Formula selectedFormula = formulaList.getSelectionModel().getSelectedItem();
			if (selectedFormula != null) {
				infoArea.setText(selectedFormula.listToString());
				tasteList.getSelectionModel().clearSelection();
			}
		});

		HBox buttonBox = new HBox();
		Button newFormulaButton = new Button("Create New");
		Button editFormulaButton = new Button("Edit");
		editFormulaButton.setDisable(true);
		Button deleteFormulaButton = new Button("Delete");
		deleteFormulaButton.setDisable(true);
		buttonBox.getChildren().addAll(newFormulaButton, editFormulaButton, deleteFormulaButton);
		buttonBox.setSpacing(25);
		buttonBox.setAlignment(Pos.CENTER);

		formulaList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			editFormulaButton.setDisable(newValue == null); // Disable if no item is selected
			deleteFormulaButton.setDisable(newValue == null); // Disable if no item is selected
		});

		newFormulaButton.setOnAction(e -> {
			formulaList.getSelectionModel().clearSelection();
			fpcrud.showFPCRUDWindow();
			updateLists();
		});

		editFormulaButton.setOnAction(e -> {
			Formula selectedFormula = formulaList.getSelectionModel().getSelectedItem();
			if (selectedFormula != null) {
				fpcrud.showFPCRUDWindow(selectedFormula);
				updateLists();
			}
		});

		deleteFormulaButton.setOnAction(e -> {
			Formula selectedFormula = formulaList.getSelectionModel().getSelectedItem();
			if (selectedFormula != null) {
				Controllers.BatchArea.deleteFormula(selectedFormula);
				updateLists();
			}
		});

		formulaPane.add(new Label("All Formulae"), 0, 0);
		formulaPane.add(formulaList, 0, 1);
		formulaPane.add(buttonBox, 0, 2);
	}

	public void tastePaneInit(GridPane tastePane) {
		tastePane.setPadding(new Insets(10));
		tastePane.setHgap(10);
		tastePane.setVgap(10);
		tastePane.setAlignment(Pos.CENTER);
		int width = 300;
		int height = 250;

		tasteList.setPlaceholder(new Label("No TasteProfiles found"));
		tasteList.setMinSize(width, height);
		tasteList.setMaxSize(width, height);

		tasteList.setOnMouseClicked(e -> {
			TasteProfile selectedTaste = tasteList.getSelectionModel().getSelectedItem();
			if (selectedTaste != null) {
				infoArea.setText(selectedTaste.listToString());
				formulaList.getSelectionModel().clearSelection();
			}
		});

		HBox buttonBox = new HBox();
		Button newTasteButton = new Button("Create New");
		Button editTasteButton = new Button("Edit");
		editTasteButton.setDisable(true);
		Button deleteTasteButton = new Button("Delete");
		deleteTasteButton.setDisable(true);
		buttonBox.getChildren().addAll(newTasteButton, editTasteButton, deleteTasteButton);
		buttonBox.setSpacing(25);
		buttonBox.setAlignment(Pos.CENTER);

		tastePane.add(new Label("All Taste Profiles"), 0, 0);
		tastePane.add(tasteList, 0, 1);
		tastePane.add(buttonBox, 0, 2);

		// Add a listener to the ListView selection property
		tasteList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			// If an item is selected, enable the button, otherwise disable it
			editTasteButton.setDisable(newValue == null); // Disable if no item is selected
			deleteTasteButton.setDisable(newValue == null); // Disable if no item is selected
		});

		newTasteButton.setOnAction(e -> {
			tasteList.getSelectionModel().clearSelection();
			tpcrud.showTPCRUDWindow();
			updateLists();
		});

		editTasteButton.setOnAction(e -> {
			TasteProfile selectedTaste = tasteList.getSelectionModel().getSelectedItem();
			if (selectedTaste != null) {
				tpcrud.showTPCRUDWindow(selectedTaste);
				updateLists();
			}
		});

		deleteTasteButton.setOnAction(e -> {
			TasteProfile selectedTaste = tasteList.getSelectionModel().getSelectedItem();
			if (selectedTaste != null) {
				if (Controllers.BatchArea.deleteTasteProfile(selectedTaste)) {
					updateLists();
				} else {
					errorWindow.showError(
						"This Taste Profile is currently used in atleast 1 Formula. \n It cannot be deleted.");
				}
			}
		});
	}

	public void updateLists() {
		tasteList.getItems().clear();
		tasteList.getItems().addAll(Controllers.BatchArea.getAllTasteProfiles());
		formulaList.getItems().clear();
		formulaList.getItems().addAll(Controllers.BatchArea.getAllFormulae());
		infoArea.setText("Select a formula or taste profile to view its details");
	}
}
