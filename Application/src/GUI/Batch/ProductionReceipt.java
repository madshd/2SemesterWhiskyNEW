
package GUI.Batch;

import java.util.Map;
import javafx.stage.FileChooser;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import Warehousing.Cask;

public class ProductionReceipt {

	private final Stage productionReceiptStage = new Stage();
	private final TableView<Map.Entry<Cask, Double>> usedCasks = new TableView<>();
	private final Button saveButton = new Button("Save");
	private final Button okButton = new Button("OK");
	private final Label header = new Label("Casks to use for this production:");
	private Map<Cask, Double> casks;

	public ProductionReceipt() {
		productionReceiptStage.setTitle("Production Receipt");

		GridPane gridPane = new GridPane();
		productionReceiptStage.setResizable(false);

		Scene productionReceiptScene = new Scene(gridPane);
		productionReceiptStage.setScene(productionReceiptScene);
		productionReceiptStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
		productionReceiptScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

		initContent(gridPane);
	}

	public void show(Map<Cask, Double> casks) {
		this.casks = casks;
		updateContent();
		productionReceiptStage.showAndWait();
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
			usedCasks.getSelectionModel().clearSelection();
		});

		// Header
		HBox headerBox = new HBox(header);
		headerBox.setAlignment(Pos.CENTER);
		mainPane.add(headerBox, 0, 0);

		// Casks Table
		usedCasks.setPrefHeight(500);
		usedCasks.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
		usedCasks.setFocusTraversable(false);
		configureTableView();
		mainPane.add(usedCasks, 0, 1);

		Label warning = new Label(
				"Please save this information. \nThis is your production receipt that tells you which casks to use.");

		mainPane.add(warning, 0, 2);

		// Buttons
		okButton.setDisable(true);
		HBox buttonBox = new HBox(saveButton, okButton);
		mainPane.add(buttonBox, 0, 3, 1, 1);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(25);

		// Button Actions
		saveButton.setOnAction(event -> saveReceipt());
		okButton.setOnAction(event -> {
			clearFields();
			productionReceiptStage.close();
			BatchArea.updateLists();
		});
	}

	@SuppressWarnings("unchecked")
	private void configureTableView() {
		// Clear existing columns
		usedCasks.getColumns().clear();

		// Cask ID Column
		TableColumn<Map.Entry<Cask, Double>, String> caskIDColumn = new TableColumn<>("Cask ID");
		caskIDColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey().getCaskID())
		);

		// Quantity To Use Column
		TableColumn<Map.Entry<Cask, Double>, String> quantityToUseColumn = new TableColumn<>("Quantity To Use");
		quantityToUseColumn.setCellValueFactory(
				data -> new SimpleStringProperty(String.format("%.2f liter(s)", data.getValue().getValue())));

		// Add columns to the TableView
		usedCasks.getColumns().addAll(caskIDColumn, quantityToUseColumn);
	}

	public void updateContent() {
		// Convert the Map to an ObservableList of Map.Entry
		var entries = FXCollections.observableArrayList(casks.entrySet());

		// Set the data in the TableView
		usedCasks.setItems(entries);
	}

	private void saveReceipt() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Production Receipt");
		fileChooser.setInitialFileName("production_receipt.txt");
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		File file = fileChooser.showSaveDialog(productionReceiptStage);
		if (file != null) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				writer.write("Production Receipt:\n");
				writer.write("===================================\n");
				for (Map.Entry<Cask, Double> entry : usedCasks.getItems()) {
					String line = String.format("Cask ID: %s : Quantity: %.2f liter(s)%n",
							entry.getKey().getCaskID(), entry.getValue());
					writer.write(line);
				}
				writer.write("===================================\n");
				saveButton.setDisable(true);
				okButton.setDisable(false);
			} catch (IOException e) {
			}
		}
	}

	private void clearFields() {
		usedCasks.getItems().clear();
	}
}
