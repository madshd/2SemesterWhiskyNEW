package GUI.Batch;
import javafx.scene.control.ListCell;
import BatchArea.Batch;
import GUI.Common.ErrorWindow;
import Warehousing.Cask;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ProduceBatchWindow {

	private final ErrorWindow errorWindow = new ErrorWindow();
	private final Stage produceBatchStage = new Stage();
	private final ListView<Cask> reservedCasks = new ListView<>();
	private final TextField bottleNumInput = new TextField();
	private Batch batch;
	private Button produceButton = new Button("Produce Batch");
	private Label header = new Label("Reserved Casks for Batch");

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
		populateList();
		produceBatchStage.showAndWait();
	}

	public void populateList() {
		reservedCasks.getItems().clear();
		for (Cask cask : batch.getReservedCasks().keySet()) {
			reservedCasks.getItems().add(cask);
		}
		useSpecifiedListView(reservedCasks);
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

		// Casks Lists
		reservedCasks.setPrefHeight(400);
		reservedCasks.setPrefWidth(550);
		reservedCasks.setFocusTraversable(false);
		mainPane.add(reservedCasks, 0, 1);

		// bottleNumInput
		bottleNumInput.setFocusTraversable(false);
		HBox bottleSizeBox = new HBox(new Label("Number of Bottles to Produce: "), bottleNumInput);
		bottleSizeBox.setAlignment(Pos.CENTER_LEFT);
		bottleSizeBox.setSpacing(55);
		mainPane.add(bottleSizeBox, 0, 2);
		bottleNumInput.setMaxWidth(100);

		// Buttons
		Button cancelButton = new Button("Cancel");
		HBox buttonBox = new HBox(produceButton, cancelButton);
		mainPane.add(buttonBox, 0, 3, 1, 1);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(25);

		// Button Actions
		produceButton.setOnAction(event -> {
			int bottleNum = Integer.parseInt(bottleNumInput.getText());
			create( bottleNum);
		});

		cancelButton.setOnAction(event -> {
			clearFields();
			produceBatchStage.close();
		});
	}


	private <T> void useSpecifiedListView(ListView<T> listView) {
		listView.setCellFactory(lv -> new ListCell<>() {
			@Override
			protected void updateItem(T item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(((Cask) item).getListInfoReservation(batch));
				}
			}
		});
	}

	// Create a new Product
	private void create(int bottleNum) {
		clearFields();
		produceBatchStage.close();
	}

	// Clear all fields
	private void clearFields() {

		bottleNumInput.clear();
	}
}
