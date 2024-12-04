package GUI.Warehousing;

import Controllers.Warehousing;
import GUI.Common.ConfirmationDialog;
import Interfaces.Item;
import Storage.Storage;
import Warehousing.Warehouse;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import Warehousing.StorageRack;
import Warehousing.LoggerObserver;

import java.util.ArrayList;
import java.util.List;

public class WarehousingArea {

	private Rectangle2D screenBounds;
	private Stage stage;
	private GridPane mainPane;
	private Scene scene;

	private static ListView<Warehouse> warehouseList = new ListView<>();
	private Button btnDeleteWarehouse = new Button("Delete");
	private Button btnUpdateWarehouse = new Button("Update");
	private Button btnCreateWarehouse = new Button("Create");

	private static ListView<StorageRack> storageRacksList = new ListView<>();
	private Button btnDeleteStorageRack = new Button("Delete");
	private Button btnUpdateStorageRack = new Button("Update");
	private Button btnCreateStorageRack = new Button("Create");

	private static ListView<Item> inventoryList = new ListView<>();
	private Button btnDeleteInventory = new Button("Delete");
	private Button btnUpdateInventory = new Button("Update");
	private Button btnCreateInventory = new Button("Create");

	private Button btnCreateIngredient = new Button("Create Ingredient");
	private Button btnCreateCask = new Button("Create Cask");

	private static ListView<String> warehouseMovementsList = new ListView<>();

	private Label headerLabel = new Label("Warehousing Area");
	private Label warehouseLabel = new Label("Warehouses");
	private Label storageRacksLabel = new Label("Storage Racks");
	private Label inventoryLabel = new Label("Inventory");
	private Label warehouseMovementsLabel = new Label("Warehouse Movements");

	public WarehousingArea() {
		updateLists();
		stage = new Stage();
		stage.setTitle("Warehousing Area");
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				close();
				event.consume();
			}
		});
		screenBounds = Screen.getPrimary().getVisualBounds();
		mainPane = new GridPane();
		mainPane.setAlignment(Pos.CENTER);
//		Grid lines visibility
		mainPane.setGridLinesVisible(false);
		initContent(mainPane);
		scene = new Scene(mainPane, screenBounds.getWidth() - 300, screenBounds.getHeight());
		scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
		stage.setResizable(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setX(300);
		stage.setY(0);
		stage.setScene(scene);
	}

	public void show() {
		stage.show();
	}

	public void initContent(GridPane gridPane) {
		Label headerLabel = new Label("Warehousing Area");
		headerLabel.setFont(new Font("Arial", 32));
		GridPane.setHalignment(headerLabel, HPos.CENTER);
		gridPane.add(headerLabel, 1, 0);
		// Layoutkonfiguration
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		// Knappesektioner

		HBox warehouseCRUDButtons = new HBox();
		warehouseCRUDButtons.getChildren().addAll(btnDeleteWarehouse, btnUpdateWarehouse, btnCreateWarehouse);
		warehouseCRUDButtons.setSpacing(10);

		HBox storageRackCRUDButtons = new HBox();
		storageRackCRUDButtons.getChildren().addAll(btnDeleteStorageRack, btnUpdateStorageRack, btnCreateStorageRack);
		storageRackCRUDButtons.setSpacing(10);

		HBox inventoryCRUDButtons = new HBox();
		inventoryCRUDButtons.getChildren().addAll(btnDeleteInventory, btnUpdateInventory, btnCreateInventory);
		inventoryCRUDButtons.setSpacing(10);

		HBox createButtons = new HBox();
		createButtons.getChildren().addAll(btnCreateIngredient, btnCreateCask);
		createButtons.setSpacing(10);


		// Venstre sektion (Warehouses)
		VBox warehouseSection = new VBox(10, warehouseLabel, warehouseList, warehouseCRUDButtons);
		warehouseSection.setAlignment(Pos.CENTER);
		warehouseCRUDButtons.setAlignment(Pos.CENTER);
		gridPane.add(warehouseSection, 0, 1);

		// Midt sektion (Storage Racks)
		VBox storageRacksSection = new VBox(10, storageRacksLabel, storageRacksList, storageRackCRUDButtons);
		storageRacksSection.setAlignment(Pos.CENTER);
		storageRackCRUDButtons.setAlignment(Pos.CENTER);
		gridPane.add(storageRacksSection, 1, 1);

		// Højre sektion (Inventory)
		VBox inventorySection = new VBox(10, inventoryLabel, inventoryList, inventoryCRUDButtons);
		inventorySection.setAlignment(Pos.CENTER);
		inventoryCRUDButtons.setAlignment(Pos.CENTER);
		gridPane.add(inventorySection, 2, 1);

		// Bund sektion (Create)
		HBox createSection = new HBox(10, createButtons);
		createSection.setAlignment(Pos.CENTER);
		createButtons.setAlignment(Pos.CENTER);
		gridPane.add(createSection, 1, 2);
		createButtons.setSpacing(10);

		// Button actions
		btnCreateIngredient.setOnAction(e -> btnCreateIngredientAction());
		btnCreateCask.setOnAction(e -> btnCreateCaskAction());

		btnDeleteWarehouse.setOnAction(e -> btnDeleteWarehouseAction());
//		btnUpdateWarehouse.setOnAction(e ->());
//		btnCreateWarehouse.setOnAction(e ->());

		btnDeleteStorageRack.setOnAction(e -> btnDeleteStorageRackAction());

		// Nederste sektion (Warehouse Movements)
		gridPane.add(warehouseMovementsList, 0, 4, 3, 1); // Spænd over alle kolonner
		gridPane.add(warehouseMovementsLabel, 0, 3);
	}


	public void close() {
		if (stage.isShowing()) {
			ConfirmationDialog confirmationDialog = new ConfirmationDialog();
			confirmationDialog.show("Are you sure you want to close the Warehousing Area?", result -> {
				if (result) {
					stage.close();
					GUI.LaunchPad.Launch.enableAllButtons();
				}
			});
		}
	}
	private void btnDeleteStorageRackAction() {
		StorageRack selectedStorageRack = storageRacksList.getSelectionModel().getSelectedItem();
		if (selectedStorageRack != null) {
			Warehousing.deleteStorageRack(selectedStorageRack);
			updateLists();
		}
	}

	public void btnDeleteWarehouseAction() {
		Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
		if (selectedWarehouse != null) {
			Warehousing.deleteWarehouse(selectedWarehouse);
			updateLists();
		}
	}

	public void btnCreateCaskAction() {
		CreateCaskDialog createCaskDialog = new CreateCaskDialog();
		Stage dialogStage = new Stage();
		try {
			createCaskDialog.start(dialogStage);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		dialogStage.setOnHiding(event -> updateLists());
	}

	public void btnCreateIngredientAction() {
		CreateIngredientDialog createIngredientDialog = new CreateIngredientDialog();
		Stage dialogStage = new Stage();
		createIngredientDialog.start(dialogStage);
		dialogStage.setOnHiding(event -> updateLists());
	}

	private static void updateLists() {
		// Clear existing items
		warehouseList.getItems().clear();
		storageRacksList.getItems().clear();
		inventoryList.getItems().clear();
		warehouseMovementsList.getItems().clear();

		// Populate warehouse list
		warehouseList.getItems().addAll(Warehousing.getAllWarehouses());

		// Add listeners to update storage racks and inventory based on selections
		warehouseList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				storageRacksList.getItems().clear();
				storageRacksList.getItems().addAll(newValue.getRacks().values());
			}
		});

		storageRacksList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				inventoryList.getItems().clear();
				inventoryList.getItems().addAll(newValue.getList());
			}
		});

		// Populate warehouse movements list
		for (Warehouse warehouse : Warehousing.getAllWarehouses()) {
			if (!warehouse.getWarehousingObservers().isEmpty()) {
				for (Object observer : warehouse.getWarehousingObservers()) {
					if (observer instanceof LoggerObserver) {
						for (String log : ((LoggerObserver) observer).getLogs()) {
							warehouseMovementsList.getItems().add(log);
						}
					}
				}
			}
		}
	}

	public Stage getStage() {
		return stage;
	}
}