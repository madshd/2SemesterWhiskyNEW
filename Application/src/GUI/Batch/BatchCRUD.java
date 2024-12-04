package GUI.Batch;

import BatchArea.Batch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import GUI.Common.ErrorWindow;
import BatchArea.Product;
import Controllers.BatchArea;

@SuppressWarnings("unused")
public class BatchCRUD {

	private final ErrorWindow errorWindow = new ErrorWindow();
	private final Stage batchCrudStage = new Stage();
	private Batch batch;
	private Button createButton = new Button("Create");
	private TextField batchID = new TextField();
	private TextField numMaxBottles = new TextField();
	private TextField numExpectedBottles = new TextField();
	private Product product;
	int maxBottles = 0;

	public BatchCRUD() {
		batchCrudStage.setTitle("Create Batch");

		GridPane gridPane = new GridPane();
		batchCrudStage.setResizable(false);

		// Set the scene for the modal window
		Scene batchCrudScene = new Scene(gridPane);
		batchCrudStage.setScene(batchCrudScene);
		batchCrudStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
		batchCrudScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

		initContent(gridPane);
	}

	public void show(Product product) {
		updateContent(product);
		batchCrudStage.showAndWait();
	}

	public void updateContent(Product product) {
		maxBottles = Controllers.BatchArea.calculateMaxNumBottles(product);
		batchID.setText(Batch.getBatchIDglobalCount() + "");
		numMaxBottles.setText(maxBottles + "");
		this.product = product;
	}

	// Initialize the content of the window
	private void initContent(GridPane mainPane) {
		// Main GridPane setup
		mainPane.setPadding(new Insets(50));
		mainPane.setHgap(10);
		mainPane.setVgap(10);
		mainPane.setAlignment(Pos.CENTER);
		mainPane.setGridLinesVisible(false);

		// For intuitive clearing of textarea focus
		mainPane.setOnMouseClicked(event -> mainPane.requestFocus());

		// Batch ID
		batchID.setFocusTraversable(false);
		batchID.setEditable(false);
		batchID.setMouseTransparent(true);
		batchID.setMaxWidth(30);
		batchID.setText(String.valueOf(Batch.getBatchIDglobalCount()));
		batchID.setDisable(true);

		// Max number bottles possible
		numMaxBottles.setPromptText(maxBottles + "");
		numMaxBottles.setFocusTraversable(false);
		numMaxBottles.setMaxWidth(100);
		numMaxBottles.setDisable(true);

		// Number of Expected Bottles
		numExpectedBottles.setFocusTraversable(false);
		numExpectedBottles.setMaxWidth(100);

		mainPane.add(new Label("Batch ID: "), 0, 0);
		mainPane.add(batchID, 1, 0);
		mainPane.add(new Label("Max Number of Bottles: "), 0, 1);
		mainPane.add(numMaxBottles, 1, 1);
		mainPane.add(new Label("Number of Bottles to produce: "), 0, 2);
		mainPane.add(numExpectedBottles, 1, 2);

		// Buttons
		Button cancelButton = new Button("Cancel");
		HBox buttonBox = new HBox(createButton, cancelButton);
		mainPane.add(buttonBox, 0, 4, 2, 1);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(25);

		// Button Actions
		createButton.setOnAction(event -> {
			create(numExpectedBottles.getText());
		});

		cancelButton.setOnAction(event -> {
			clearFields();
			batchCrudStage.close();
		});
	}

	// Create a new Batch
	private void create(String bottleSize) {
		int bottleSizeInt = bottleSize.isEmpty() ? 0 : Integer.parseInt(bottleSize);
		if (bottleSizeInt <= 0) {
			errorWindow.showError("Please enter a valid Bottle Size.");
			return;
		}
		if (numExpectedBottles.getText().isEmpty()) {
			errorWindow.showError("Please input number of bottles to produce.");
			return;
		}
		// check if the number of bottles to produce is a valid integer
		try {
			int numBottles = Integer.parseInt(numExpectedBottles.getText());
			if (numBottles <= 0) {
				errorWindow.showError("Please input a valid number of bottles to produce.");
				return;
			}
			if (numBottles > Integer.parseInt(numMaxBottles.getText())) {
				errorWindow.showError(
						"Number of bottles to produce exceeds the maximum number of bottles possible to produce given the current warehouse status.");
				return;
			}
		} catch (NumberFormatException e) {
			errorWindow.showError("Please input a valid number of bottles to produce.");
			return;
		}
		batch = Controllers.BatchArea.createNewBatch(product, bottleSizeInt);
		clearFields();
		batchCrudStage.close();
	}

	// Clear all fields
	private void clearFields() {
		product = null;
		numExpectedBottles.clear();
	}
}
