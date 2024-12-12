package GUI.Warehousing;

import Controllers.Warehousing;
import GUI.Common.ConfirmationDialog;
import GUI.Common.ErrorWindow;
import GUI.Common.UpdateCaskCommonDialog;
import Interfaces.Item;
import Warehousing.Warehouse;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
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
import Warehousing.Cask;
import Warehousing.Ingredient;

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
		updateLists();
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
		storageRackCRUDButtons.getChildren().addAll(btnDeleteStorageRack, btnCreateStorageRack);
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


		// Button actions
		btnCreateIngredient.setOnAction(e -> btnCreateIngredientAction());
		btnCreateCask.setOnAction(e -> btnCreateCaskAction());

		btnDeleteWarehouse.setOnAction(e -> btnDeleteWarehouseAction());
		btnUpdateWarehouse.setOnAction(e -> updateWarehouseAction());
		btnCreateWarehouse.setOnAction(e -> createWarehouseAction());

		btnDeleteStorageRack.setOnAction(e -> btnDeleteStorageRackAction());
		btnCreateStorageRack.setOnAction(e -> createStorageRack());


		btnDeleteInventory.setOnAction(e -> deleteInventoryItem());
		btnUpdateInventory.setOnAction(e -> updateItem());


		// Nederste sektion (Warehouse Movements)
		gridPane.add(warehouseMovementsList, 0, 4, 3, 1);
		gridPane.add(warehouseMovementsLabel, 0, 3);
		warehouseMovementsList.setSelectionModel(null);


	}

	private void createStorageRack() {
		Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
		if (selectedWarehouse != null) {
			StorageRackDialog storageRackCreate = new StorageRackDialog();
			Stage dialogStage = new Stage();
			try {
				storageRackCreate.start(dialogStage);
				dialogStage.setOnHiding(event -> updateLists());
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	private void updateWarehouseAction() {
		Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
		if (selectedWarehouse != null) {
			WarehouseUpdate warehouseUpdate = new WarehouseUpdate(selectedWarehouse);
			Stage dialogStage = new Stage();
			try {
				warehouseUpdate.start(dialogStage);
				dialogStage.setOnHiding(event -> updateLists());
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	private void createWarehouseAction() {
		WarehouseCreate warehouseCreate = new WarehouseCreate();
		Stage dialogStage = new Stage();
		try {
			warehouseCreate.start(dialogStage);
			dialogStage.setOnHiding(event -> updateLists());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private void deleteInventoryItem() {
		ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.show("Are you sure you want to delete this item?", confirmed -> {
			if (confirmed) {
				try {
					Item selectedItem = inventoryList.getSelectionModel().getSelectedItem();
					StorageRack selectedStorageRack = storageRacksList.getSelectionModel().getSelectedItem();
					if (selectedItem != null && selectedItem.getRemainingQuantity() == 0) {
						Warehousing.deleteItem(selectedStorageRack, selectedItem);
						updateLists();
					} else {
						ErrorWindow errorWindow = new ErrorWindow();
						errorWindow.showError("The item isn't empty and can't be deleted.");
					}
				} catch (Exception e) {
					ErrorWindow errorWindow = new ErrorWindow();
					errorWindow.showError("An error occurred while deleting the item.");
					e.printStackTrace();
				}
			}
		});
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
		} else
		if (selectedItem != null && selectedItem instanceof Ingredient) {
			UpdateIngredientDialog updateIngredientDialog = new UpdateIngredientDialog((Ingredient) selectedItem);
			Stage dialogStage = new Stage();
			try {
				updateIngredientDialog.start(dialogStage);
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

	private void btnDeleteStorageRackAction() {
		ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.show("Are you sure you want to delete the storage rack?", confirmed -> {
			if (confirmed) {
				try {
					Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
					StorageRack selectedStorageRack = storageRacksList.getSelectionModel().getSelectedItem();
					if (selectedWarehouse != null && selectedStorageRack != null) {
						if (Warehousing.isStorageRackInUse(selectedStorageRack)) {
							ErrorWindow errorWindow = new ErrorWindow();
							errorWindow.showError("Storage rack is in use and can't be deleted.");
						} else
						Warehousing.deleteStorageRack(selectedWarehouse, selectedStorageRack);
						updateLists();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void btnDeleteWarehouseAction() {
		ConfirmationDialog confirmationDialog = new ConfirmationDialog();
		confirmationDialog.show("Are you sure you want to delete the warehouse?", confirmed -> {
			if (confirmed) {
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
		});
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

	public static void updateLists() {
		warehouseList.getItems().clear();
		storageRacksList.getItems().clear();
		inventoryList.getItems().clear();
		warehouseMovementsList.getItems().clear();

		warehouseList.getItems().addAll(Warehousing.getAllWarehouses());
//		warehouseMovementsList.getItems().setAll(warehouseList.getSelectionModel().getSelectedItem());

		if (!warehouseList.getItems().isEmpty()) {
			warehouseList.getSelectionModel().selectFirst();
		}

		warehouseList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				storageRacksList.getItems().clear();
				storageRacksList.getItems().addAll(newValue.getRacks().values());

				if (!storageRacksList.getItems().isEmpty()) {
					storageRacksList.getSelectionModel().selectFirst();
				} else {
					inventoryList.getItems().clear();
				}
			} else {
				storageRacksList.getItems().clear();
				inventoryList.getItems().clear();
			}
		});

		storageRacksList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				inventoryList.getItems().clear();
				List<Item> items = newValue.getList();
				inventoryList.getItems().addAll(Warehousing.getItemsByRack(newValue));

				if (!inventoryList.getItems().isEmpty()) {
					inventoryList.getSelectionModel().selectFirst();
				}
			} else {
				inventoryList.getItems().clear();
			}
		});

		warehouseList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		warehouseList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				updateLogListView(newValue);
			}
		});
	}

	private static void updateLogListView(Warehouse warehouse) {
		warehouseMovementsList.getItems().setAll(Warehousing.getLogsByWarehouse(warehouse));
	}

	public Stage getStage() {
		return stage;
	}
}