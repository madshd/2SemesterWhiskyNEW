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

	public int calcReadyBottles() {
		int readyBottles = 0;
		for (Cask cask : batch.getReservedCasks().keySet()) {
			if (cask.getMaturityMonths() >= 36) {
				readyBottles += batch.getReservedCasks().get(cask);
			}
		}
		readyBottles -= batch.getNumProducedBottles();
		if (readyBottles < 0) {
			readyBottles = 0;
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
		expectedBottles.setMaxWidth(100);
		Label expectedLabel = new Label("Expected Bottles: ");

		// Remaining Bottles
		remainingBottles.setFocusTraversable(false);
		remainingBottles.setEditable(false);
		remainingBottles.setDisable(true);
		remainingBottles.setMaxWidth(100);
		Label remainingLabel = new Label("Remaining Bottles: ");

		// Bottles That can be produced now according to whats ready
		readyBottles.setFocusTraversable(false);
		readyBottles.setEditable(false);
		readyBottles.setDisable(true);
		readyBottles.setMaxWidth(100);
		Label readyLabel = new Label("Ready to produce now: ");

		// Bottle Number Input
		bottleNumInput.setFocusTraversable(false);
		bottleNumInput.setMaxWidth(100);
		Label bottleNumLabel = new Label("Number of Bottles to Produce: ");

		// Add all to the grid
		GridPane inputGrid = new GridPane();
		inputGrid.add(expectedLabel, 0, 0);
		inputGrid.add(expectedBottles, 1, 0);
		inputGrid.add(remainingLabel, 0, 1);
		inputGrid.add(remainingBottles, 1, 1);
		inputGrid.add(readyLabel, 0, 2);
		inputGrid.add(readyBottles, 1, 2);
		inputGrid.add(bottleNumLabel, 0, 3);
		inputGrid.add(bottleNumInput, 1, 3);
		inputGrid.setHgap(10);
		inputGrid.setVgap(10);
		inputGrid.setAlignment(Pos.CENTER);
		inputGrid.setPadding(new Insets(10));
		inputGrid.setMaxWidth(400);

		mainPane.add(inputGrid, 0, 2);
		GridPane.setHalignment(inputGrid, javafx.geometry.HPos.CENTER);

		// Buttons
		Button cancelButton = new Button("Cancel");
		HBox buttonBox = new HBox(produceButton, cancelButton);
		mainPane.add(buttonBox, 0, 3, 1, 1);
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
		caskIDColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCaskIDString()));
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
		produceBatchStage.close();

		loadingWindow.show("Generating Production Receipt...");

		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				Thread.sleep(1000);
				return null;
			}

			@Override
			protected void succeeded() {
				loadingWindow.close();
				Map<Cask, Double> usedCasks = Controllers.BatchArea.produceBatch(batch, bottleNum);
				clearFields();
				productionReceipt.show(usedCasks);
			}

			@Override
			protected void failed() {
				loadingWindow.close();
			}
		};
		new Thread(task).start();
	}

	private void clearFields() {
		bottleNumInput.clear();
	}
}
