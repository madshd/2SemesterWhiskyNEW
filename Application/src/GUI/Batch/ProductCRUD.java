
package GUI.Batch;

import BatchArea.Product;
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

public class ProductCRUD {

	private final ErrorWindow errorWindow = new ErrorWindow();
	private final Stage productCrudStage = new Stage();
	private final TextField nameInput = new TextField();
	private final TextField bottleSizeInput = new TextField();
	private boolean updating = false;
	private Product product;
	private Button createButton = new Button("Create");
	private Label header = new Label("Create New Product");

	public ProductCRUD() {
		productCrudStage.setTitle("Formula Manager");

		GridPane gridPane = new GridPane();
		productCrudStage.setResizable(false);

		// Set the scene for the modal window
		Scene productCrudScene = new Scene(gridPane);
		productCrudStage.setScene(productCrudScene);
		productCrudStage.initModality(javafx.stage.Modality.APPLICATION_MODAL); // Block interaction with main
																				// window
		productCrudScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

		// Initialize content
		initContent(gridPane);
	}

	public void show() {
		// Show the modal and wait for it to be closed
		productCrudStage.showAndWait();
	}

	// IF UPDATING
	public void show(Product product) {
		this.product = product;
		setFields(product);
		productCrudStage.showAndWait();
	}

	public void setFields(Product product) {
		createButton.setText("Update");
		header.setText("Update Existing Product");
		updating = true;
		nameInput.setText(product.getProductName());
	}

	// Initialize the content of the window
	@SuppressWarnings("unused")
	private void initContent(GridPane mainPane) {
		// Main GridPane setup
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

		// Product Name
		nameInput.setFocusTraversable(false);
		HBox nameBox = new HBox(new Label("Name: "), nameInput);
		nameBox.setAlignment(Pos.CENTER);
		mainPane.add(nameBox, 0, 1);
		nameInput.setMinWidth(200);
		nameBox.setSpacing(10);

		// BottleSize
		bottleSizeInput.setFocusTraversable(false);
		HBox bottleSizeBox = new HBox(new Label("Bottle Size (ML): "), bottleSizeInput);
		bottleSizeBox.setAlignment(Pos.CENTER_LEFT);
		bottleSizeBox.setSpacing(55);
		mainPane.add(bottleSizeBox, 0, 2);
		bottleSizeInput.setMaxWidth(100);

		// Buttons
		Button cancelButton = new Button("Cancel");
		HBox buttonBox = new HBox(createButton, cancelButton);
		mainPane.add(buttonBox, 0, 3, 1, 1);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(25);

		// Button Actions
		createButton.setOnAction(event -> {
			String name = nameInput.getText();
			int bottleSize = Integer.parseInt(bottleSizeInput.getText());
			create(name, bottleSize);
		});

		cancelButton.setOnAction(event -> {
			clearFields();
			productCrudStage.close();
		});
	}

	// Create a new Taste Profile
	private void create(String name, int bottleSize) {
		if (name == null || name.isEmpty()) {
			errorWindow.showError("Name cannot be empty");
			return;
		}
		if (updating) {
			Controllers.BatchArea.updateProduct(name, bottleSize, product);

		} else {
			Controllers.BatchArea.createNewProduct(name, bottleSize);
		}
		clearFields();
		productCrudStage.close();
	}

	// Clear all fields
	private void clearFields() {
		product = null;
		updating = false;
		createButton.setText("Create");
		header.setText("Create New Taste Profile");
		nameInput.clear();
		bottleSizeInput.clear();
	}
}
