package GUI.Batch;

import java.util.Map;

import BatchArea.Batch;
import GUI.Common.ErrorWindow;
import GUI.Common.LoadingWindow;
import Warehousing.Cask;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class ProduceBatchWindow {

	private final ErrorWindow errorWindow = new ErrorWindow();
	private final Stage produceBatchStage = new Stage();
	private final TableView<Cask> reservedCasks = new TableView<>();
	private final TextField bottleNumInput = new TextField();
	private final TextField expectedBottles = new TextField();
	private final TextField remainingBottles = new TextField();
	private final TextField readyBottles = new TextField();
	private Batch batch;
	private final ProductionReceipt productionReceipt = new ProductionReceipt();
	private Button produceButton = new Button("Produce Batch");
	private Label header = new Label("Reserved Casks for Batch");
	private LoadingWindow loadingWindow = new LoadingWindow();

	public ProduceBatchWindow() {
		produceBatchStage.setTitle("Produce Batch");

		GridPane gridPane = new GridPane();
		produceBatchStage.setResizable(false);

		Scene produceBatchScene = new Scene(gridPane);
		produceBatchStage.setScene(produceBatchScene);
		produceBatchStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
		produceBatchScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

		initContent(gridPane);
	}

	public void show(Batch batch) {
		this.batch = batch;
		updateContent();
		produceBatchStage.showAndWait();
	}

	public void updateContent() {
		expectedBottles.setText(String.valueOf(batch.getNumExpectedBottles()));
		remainingBottles.setText(String.valueOf(batch.getNumRemainingBottles()));
		ObservableList<Cask> caskList = FXCollections.observableArrayList(batch.getReservedCasks().keySet());
		reservedCasks.setItems(caskList);
		readyBottles.setText(Integer.toString(calcReadyBottles()));
	}

	public int calcReadyBottles(){
		int readyBottles = 0;
		for (Cask cask : batch.getReservedCasks().keySet()) {
			if (cask.getMaturityMonths() > 36) {
			readyBottles += batch.getReservedCasks().get(cask);
			}
		}
		return readyBottles;
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
			reservedCasks.getSelectionModel().clearSelection();
		});

		// Header
		HBox headerBox = new HBox(header);
		headerBox.setAlignment(Pos.CENTER);
		mainPane.add(headerBox, 0, 0);

		// Casks Table
		reservedCasks.setPrefHeight(500);
		reservedCasks.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
		reservedCasks.setFocusTraversable(false);
		configureTableView();
		mainPane.add(reservedCasks, 0, 1);

		// Expected Bottles
		expectedBottles.setFocusTraversable(false);
		expectedBottles.setEditable(false);
		expectedBottles.setDisable(true);
		HBox expectedBox = new HBox(new Label("Total Batch Size: "), expectedBottles);
		expectedBox.setAlignment(Pos.CENTER);
		expectedBox.setSpacing(65);
		mainPane.add(expectedBox, 0, 2);
		expectedBottles.setMaxWidth(100);

		// Remaining Bottles
		remainingBottles.setFocusTraversable(false);
		remainingBottles.setEditable(false);
		remainingBottles.setDisable(true);
		HBox remainingBox = new HBox(new Label("Remaining Bottles: "), remainingBottles);
		remainingBox.setAlignment(Pos.CENTER);
		remainingBox.setSpacing(90);
		mainPane.add(remainingBox, 0, 3);
		remainingBottles.setMaxWidth(100);

		// Bottles That can be produced now according to whats ready
		readyBottles.setFocusTraversable(false);
		readyBottles.setEditable(false);
		readyBottles.setDisable(true);
		HBox readyBox = new HBox(new Label("Ready to produce now: "), readyBottles);
		readyBox.setAlignment(Pos.CENTER);
		readyBox.setSpacing(90);
		mainPane.add(readyBox, 0, 4);
		readyBottles.setMaxWidth(100);

		// Bottle Number Input
		bottleNumInput.setFocusTraversable(false);
		HBox bottleSizeBox = new HBox(new Label("Number of Bottles to Produce: "), bottleNumInput);
		bottleSizeBox.setAlignment(Pos.CENTER);
		bottleSizeBox.setSpacing(25);
		mainPane.add(bottleSizeBox, 0, 5);
		bottleNumInput.setMaxWidth(100);

		// Buttons
		Button cancelButton = new Button("Cancel");
		HBox buttonBox = new HBox(produceButton, cancelButton);
		mainPane.add(buttonBox, 0, 6, 1, 1);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(25);

		// Button Actions
		produceButton.setOnAction(event -> {
			int bottleNum = Integer.parseInt(bottleNumInput.getText());
			produce(bottleNum);
		});

		cancelButton.setOnAction(event -> {
			clearFields();
			produceBatchStage.close();
		});
	}

	@SuppressWarnings("unchecked")
	private void configureTableView() {
		// Cask ID Column
		TableColumn<Cask, String> caskIDColumn = new TableColumn<>("Cask ID");
		caskIDColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCaskID()));
		caskIDColumn.setMinWidth(100);

		// Taste Profile Column
		TableColumn<Cask, String> tasteProfileColumn = new TableColumn<>("Taste Profile");
		tasteProfileColumn.setCellValueFactory(
				data -> new SimpleStringProperty(data.getValue().getTasteProfile().getProfileName()));
		tasteProfileColumn.setMinWidth(200);

		// Quantity Reserved Column
		TableColumn<Cask, String> quantityReservedColumn = new TableColumn<>("Quantity Reserved");
		quantityReservedColumn.setCellValueFactory(
				data -> new SimpleStringProperty(batch.getReservedCasks().get(data.getValue()) + " Liters"));
		quantityReservedColumn.setMinWidth(150);

		// Ready In Column
		TableColumn<Cask, String> readyInColumn = new TableColumn<>("Ready In");
		readyInColumn.setCellValueFactory(data -> {
			int maturityMonths = data.getValue().getMaturityMonths();
			String readyIn = maturityMonths < 36 ? (36 - maturityMonths) + " months" : "Now";
			return new SimpleStringProperty(readyIn);
		});
		readyInColumn.setMinWidth(150);

		reservedCasks.getColumns().addAll(caskIDColumn, tasteProfileColumn, quantityReservedColumn, readyInColumn);
	}

	private void produce(int bottleNum) {
		// Close the produceBatchStage first
		produceBatchStage.close();

		// Show the loading window
		loadingWindow.show("Generating Production Receipt...");

		// Use a Task to run the delay in the background thread
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				// Simulate a long-running operation (5 seconds)
				Thread.sleep(1000);
				return null;
			}

			@Override
			protected void succeeded() {
				// Close the loading window and show the production receipt
				loadingWindow.close();

				// Produce batch and get the used casks
				Map<Cask, Double> usedCasks = Controllers.BatchArea.produceBatch(batch, bottleNum,
						batch.isOnlyReadyCasks());

				// Clear fields and show the production receipt window
				clearFields();
				productionReceipt.show(usedCasks);
			}

			@Override
			protected void failed() {
				// Handle failure (optional)
				loadingWindow.close();
				System.err.println("Error occurred while generating the production receipt.");
			}
		};

		// Run the task in a background thread
		new Thread(task).start();
	}

	// Clear all fields
	private void clearFields() {
		bottleNumInput.clear();
	}
}
