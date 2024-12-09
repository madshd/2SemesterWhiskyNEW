
package GUI.Batch;

import java.util.ArrayList;
import java.util.Map;

import BatchArea.Formula;
import BatchArea.Product;
import javafx.stage.FileChooser;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import Warehousing.Cask;

@SuppressWarnings("unused")
public class SearchCaskWindow {

	private final Stage searchCaskStage = new Stage();
	private final TableView<Cask> casksTable = new TableView<>();
	private final ArrayList<Cask> casksFound = new ArrayList<>();
	private final Button okButton = new Button("OK");
	private final Label header = new Label("Casks that fit selected product or formula");

	public SearchCaskWindow() {
		searchCaskStage.setTitle("Cask Search");

		GridPane gridPane = new GridPane();
		searchCaskStage.setResizable(false);

		Scene caskSearchScene = new Scene(gridPane);
		searchCaskStage.setScene(caskSearchScene);
		searchCaskStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
		caskSearchScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

		initContent(gridPane);
	}

	public void show(Formula formula, Product product) {
		if(formula != null) {
		searchCasks(formula);
		} else {
			searchCasks(product.getFormula());
		}
		updateContent();
		searchCaskStage.showAndWait();
	}

	private void searchCasks(Formula formula) {
		for(Cask cask : Controllers.BatchArea.searchCasks(formula)) {
			casksFound.add(cask);
		}
	}

	private void initContent(GridPane mainPane) {
		mainPane.setPadding(new Insets(50));
		mainPane.setHgap(10);
		mainPane.setVgap(10);
		mainPane.setAlignment(Pos.CENTER);
		mainPane.setGridLinesVisible(false);

		// For intuitive clearing of textarea focus
		mainPane.setOnMouseClicked(event -> {
			mainPane.requestFocus();
			casksTable.getSelectionModel().clearSelection();
		});

		// Header
		HBox headerBox = new HBox(header);
		headerBox.setAlignment(Pos.CENTER);
		mainPane.add(headerBox, 0, 0);

		// Casks Table
		casksTable.setPrefHeight(500);
		casksTable.setPrefWidth(800);
		casksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
		casksTable.setFocusTraversable(false);
		configureTableView();
		mainPane.add(casksTable, 0, 1);

		// Buttons
		okButton.setDisable(false);
		okButton.setOnAction(e -> {
			searchCaskStage.close();
		});
		HBox buttonBox = new HBox(okButton);
		mainPane.add(buttonBox, 0, 3, 1, 1);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(25);

		// Handles closing the window if user tries to bypass saving the receipt to file
		// all views and data will update as normally.
		// this is not the goal, merely a safeguard to prevent data loss.
		searchCaskStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				clearFields();
				BatchArea.updateLists();
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void configureTableView() {
		// Clear existing columns
		casksTable.getColumns().clear();

		// Cask ID Column
		TableColumn<Cask, String> caskIDColumn = new TableColumn<>("Cask ID");
		caskIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCaskIDString()));

		// Cask Type Column
		TableColumn<Cask, String> caskTypeColumn = new TableColumn<>("Cask Type");
		caskTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCaskType()));

		// Cask Volume Column
		TableColumn<Cask, String> caskVolumeColumn = new TableColumn<>("Current Volume");
		caskVolumeColumn.setCellValueFactory(
				cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getQuantityStatus())));

		// Cask Volume Column
		TableColumn<Cask, String> caskLegalVolumeColumn = new TableColumn<>("Legal Volume");
		caskVolumeColumn.setCellValueFactory(
				cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getLegalQuantity())));

		// Add columns to the TableView
		casksTable.getColumns().addAll(caskIDColumn, caskTypeColumn, caskVolumeColumn, caskLegalVolumeColumn);
	}

	public void updateContent() { 
		// Convert the list of casks to an ObservableList
		var observableCasks = FXCollections.observableArrayList(casksFound);

		// Set the items in the TableView
		casksTable.setItems(observableCasks);
	}

	private void clearFields() {
		casksTable.getItems().clear();
	}
}