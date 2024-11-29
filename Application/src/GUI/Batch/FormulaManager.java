package GUI.Batch;

import java.util.ArrayList;

import BatchArea.Formula;
import BatchArea.TasteProfile;
import Enumerations.TastingNote;
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

public class FormulaManager {

	ListView<Formula> formulaList = new ListView<Formula>();
	ListView<TasteProfile> tasteList = new ListView<>();

	TextArea infoArea = new TextArea();

	public void showFormulaManagerWindow() {
		// Create the Formula Manager modal window
		Stage formulaManagerStage = new Stage();
		formulaManagerStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with main window
		formulaManagerStage.setTitle("Formula Manager");

		GridPane gridPane = new GridPane();
		formulaManagerStage.setResizable(false);

		// Set the scene for the modal window
		Scene formulaManagerScene = new Scene(gridPane);
		formulaManagerStage.setScene(formulaManagerScene);
		formulaManagerScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
		initContent(gridPane);

		// Show the modal and wait for it to be closed
		formulaManagerStage.showAndWait();
	}

	@SuppressWarnings("unused")
	public void initContent(GridPane mainPane) {

		// Main GridPane setup
		mainPane.setPadding(new Insets(50));
		mainPane.setHgap(10);
		mainPane.setVgap(10);
		mainPane.setAlignment(Pos.CENTER);

		mainPane.setGridLinesVisible(false);

		// For intuitive clearing of textarea focus
		mainPane.setOnMouseClicked(event -> mainPane.requestFocus());

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
	}

	public void infoPaneInit(GridPane infoPane) {
		infoPane.setPadding(new Insets(10));
		infoPane.setHgap(10);
		infoPane.setVgap(10);
		infoPane.setAlignment(Pos.CENTER);

		infoArea.setMinHeight(620);
		infoArea.setMaxWidth(300);
		infoArea.setMinWidth(300);
		infoArea.setEditable(false);
		infoArea.setFocusTraversable(false);
		infoArea.setWrapText(true);
		infoArea.setMouseTransparent(true);

		infoPane.add(infoArea, 0, 0);

		infoArea.setText("Select a formula or taste profile to view its details");
	}

	public void formulaPaneInit(GridPane formulaPane) {
		formulaPane.setPadding(new Insets(10));
		formulaPane.setHgap(10);
		formulaPane.setVgap(10);
		formulaPane.setAlignment(Pos.CENTER);
		int width = 400;
		int height = 250;

		formulaList.setPlaceholder(new Label("No formulas found"));
		formulaList.setMinSize(width, height);
		formulaList.setMaxSize(width, height);

		HBox buttonBox = new HBox();
		Button newFormulaButton = new Button("Create New");
		Button editFormulaButton = new Button("Edit");
		Button deleteFormulaButton = new Button("Delete");
		buttonBox.getChildren().addAll(newFormulaButton, editFormulaButton, deleteFormulaButton);
		buttonBox.setSpacing(25);
		buttonBox.setAlignment(Pos.CENTER);

		formulaPane.add(formulaList, 0, 0);
		formulaPane.add(buttonBox, 0, 1);
	}

	@SuppressWarnings("unused")
	public void tastePaneInit(GridPane tastePane) {
		tastePane.setPadding(new Insets(10));
		tastePane.setHgap(10);
		tastePane.setVgap(10);
		tastePane.setAlignment(Pos.CENTER);
		int width = 400;
		int height = 250;

		tasteList.setPlaceholder(new Label("No TasteProfiles found"));
		tasteList.setMinSize(width, height);
		tasteList.setMaxSize(width, height);

		HBox buttonBox = new HBox();
		Button newTasteButton = new Button("Create New");
		Button editTasteButton = new Button("Edit");
		Button deleteTasteButton = new Button("Delete");
		buttonBox.getChildren().addAll(newTasteButton, editTasteButton, deleteTasteButton);
		buttonBox.setSpacing(25);
		buttonBox.setAlignment(Pos.CENTER);

		tastePane.add(tasteList, 0, 0);
		tastePane.add(buttonBox, 0, 1);

		newTasteButton.setOnAction(e -> {
			// Create a new TasteProfile
			// TODO: Actually input the real data
			ArrayList<TastingNote> tags = new ArrayList<>();
			tags.add(TastingNote.APPLE);
			Controllers.BatchArea.createNewTasteProfile("ID", "Description", tags);

			updateLists();

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
