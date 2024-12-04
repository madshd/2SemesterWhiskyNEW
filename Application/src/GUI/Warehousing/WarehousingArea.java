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
	private static ListView<StorageRack> storageRacksList = new ListView<>();
	private static ListView<Item> inventoryList = new ListView<>();
	private static ListView<String> warehouseMovementsList = new ListView<>();

	public WarehousingArea() {
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
		updateLists();
		Label headerLabel = new Label("Warehousing Area");
		headerLabel.setFont(new Font("Arial", 32));
		GridPane.setHalignment(headerLabel, HPos.CENTER);
		gridPane.add(headerLabel, 0, 0);


		// Laver lister
		warehouseList.getItems().addAll(Warehousing.getAllWarehouses());

		warehouseMovementsList.getItems().addAll("Item One", "Item Two", "Item Three");
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

		// Knappesektioner
		HBox warehouseButtons = new HBox(10, new Button("Delete"), new Button("Update"), new Button("Create"));
		HBox storageRackButtons = new HBox(10, new Button("Delete"), new Button("Update"), new Button("Create"));
		HBox inventoryButtons = new HBox(10, new Button("Delete"), new Button("Update"), new Button("Create"));

		HBox createButtons = new HBox();
		Button createIngredient = new Button("Create Ingredient");
		Button createProduct = new Button("Create Cask");

		// Layoutkonfiguration
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		// Venstre sektion (Warehouses)
		Label warehouseLabel = new Label("Warehouses");
		VBox warehouseSection = new VBox(10, warehouseList, warehouseButtons);
		gridPane.add(warehouseSection, 0, 1);

		// Midt sektion (Storage Racks)
		Label storageRacksLabel = new Label("Storage Racks");
		VBox storageRacksSection = new VBox(10, storageRacksList, storageRackButtons);
		gridPane.add(storageRacksSection, 1, 1);

		// Højre sektion (Inventory)
		Label inventoryLabel = new Label("Inventory");
		VBox inventorySection = new VBox(10, inventoryList, inventoryButtons, createButtons);
		inventorySection.setAlignment(Pos.CENTER);
		gridPane.add(inventorySection, 2, 1);

		createButtons.getChildren().addAll(createIngredient, createProduct);
		createButtons.setSpacing(10);


		createIngredient.setOnAction(e -> {
			CreateIngredientDialog createIngredientDialog = new CreateIngredientDialog();
			Stage dialogStage = new Stage();
			createIngredientDialog.start(dialogStage);
			dialogStage.setOnHiding(event -> updateLists());
		});

		// Nederste sektion (Warehouse Movements)
		Label warehouseMovementsLabel = new Label("Warehouse Movements");
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

	public static void updateLists() {
		inventoryList.getItems().clear();
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

		storageRacksList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				inventoryList.getItems().clear();
				inventoryList.getItems().addAll(newValue.getList());
			}
		});

		warehouseList.getSelectionModel().selectedItemProperty().addListener(observable -> {
			warehouseMovementsList.getItems().clear();
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
		});
	}

	public Stage getStage() {
		return stage;
	}
}