package GUI.Warehousing;

import Controllers.Warehousing;
import GUI.Common.Common;
import GUI.Common.ConfirmationDialog;
import GUI.Common.ErrorWindow;
import GUI.Common.UpdateCaskCommonDialog;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import Warehousing.StorageRack;
import Warehousing.LoggerObserver;
import Warehousing.Cask;

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
		inventoryCRUDButtons.getChildren().addAll(btnDeleteInventory, btnUpdateInventory, btnCreateIngredient, btnCreateCask);
		inventoryCRUDButtons.setSpacing(10);

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

		Common.useSpecifiedListView(inventoryList);

		// Button actions
		btnCreateIngredient.setOnAction(e -> btnCreateIngredientAction());
		btnCreateCask.setOnAction(e -> btnCreateCaskAction());

		btnDeleteWarehouse.setOnAction(e -> btnDeleteWarehouseAction());
//		btnUpdateWarehouse.setOnAction(e ->());
//		btnCreateWarehouse.setOnAction(e ->());

		btnDeleteStorageRack.setOnAction(e -> btnDeleteStorageRackAction());
//		btnUpdateStorageRack.setOnAction(e ->());
//		btnCreateStorageRack.setOnAction(e ->());

//		btnDeleteInventory.setOnAction(e -> ());


		btnUpdateInventory.setOnAction(e -> updateItem());


		// Nederste sektion (Warehouse Movements)
		gridPane.add(warehouseMovementsList, 0, 4, 3, 1); // Spænd over alle kolonner
		gridPane.add(warehouseMovementsLabel, 0, 3);

	}

	public void updateItem() {
		Item selectedItem = inventoryList.getSelectionModel().getSelectedItem();
		if (selectedItem != null && selectedItem instanceof Cask) {
			Cask selectedCask = (Cask) selectedItem;
			UpdateCaskCommonDialog updateCaskCommonDialog = new UpdateCaskCommonDialog(selectedCask);
			Stage dialogStage = new Stage();
			try {
				updateCaskCommonDialog.start(dialogStage);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			dialogStage.setOnHiding(event -> updateLists());
		}
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
	public void btnDeleteStorageRackAction() {
		try {
			Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
			StorageRack selectedStorageRack = storageRacksList.getSelectionModel().getSelectedItem();
			if (selectedWarehouse != null && selectedStorageRack != null) {
				Warehousing.deleteStorageRack(selectedWarehouse, selectedStorageRack);
				updateLists();
			}
		} catch (Exception e) {
			ErrorWindow errorWindow = new ErrorWindow();
			errorWindow.showError("Storage rack is in use, and can't be deleted.");
			e.printStackTrace();
		}
	}

	public void btnDeleteWarehouseAction() {
		try {
			Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
			if (selectedWarehouse != null) {
				Warehousing.deleteWarehouse(selectedWarehouse);
				updateLists();
			}
		} catch (Exception e) {
			ErrorWindow errorWindow = new ErrorWindow();
			errorWindow.showError("Warehouse still has storage racks, and can't be deleted.");
			e.printStackTrace();
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