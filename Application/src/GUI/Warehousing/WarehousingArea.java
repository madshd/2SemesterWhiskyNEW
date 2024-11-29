package GUI.Warehousing;

import Controllers.Warehousing;
import GUI.Common.ConfirmationDialog;
import Interfaces.Item;
import Storage.Storage;
import Warehousing.Warehouse;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
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

public class WarehousingArea {

	private Rectangle2D screenBounds;
	private Stage stage;
	private GridPane mainPane;
	private Scene scene;

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
		initContent(mainPane);
		scene = new Scene(mainPane, screenBounds.getWidth() - 300, screenBounds.getHeight());
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
		gridPane.add(headerLabel, 0, 0, 3, 1); // Headeren spænder over 3 kolonner

		// Laver lister
		ListView<Warehouse> warehouseList = new ListView<>();
		warehouseList.getItems().addAll(Warehousing.getAllWarehouses());

		ListView<StorageRack> storageRacksList = new ListView<>();
		warehouseList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				storageRacksList.getItems().clear();
				storageRacksList.getItems().addAll(newValue.getRacks().values());
			}
		});

		ListView<Item> inventoryList = new ListView<>();
		warehouseList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				storageRacksList.getItems().clear();
				storageRacksList.getItems().addAll(newValue.getRacks().values());
				inventoryList.getItems().clear();
			}
		});

		storageRacksList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				inventoryList.getItems().clear();
				inventoryList.getItems().addAll(newValue.getList());
			}
		});

		ListView<String> warehouseMovementsList = new ListView<>();
		warehouseMovementsList.getItems().addAll("Item One", "Item Two", "Item Three");

		// Knappesektioner
		HBox warehouseButtons = new HBox(10, new Button("Delete"), new Button("Update"), new Button("Create"));
		HBox storageRackButtons = new HBox(10, new Button("Delete"), new Button("Update"), new Button("Create"));
		HBox inventoryButtons = new HBox(10, new Button("Delete"), new Button("Update"), new Button("Create"));
		HBox createButtons = new HBox(10, new Button("Create Ingredient"), new Button("Create Cask"));

		// Layoutkonfiguration
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		// Venstre sektion (Warehouses)
		VBox warehouseSection = new VBox(10, warehouseList, warehouseButtons);
		gridPane.add(warehouseSection, 0, 1);

		// Midt sektion (Storage Racks)
		VBox storageRacksSection = new VBox(10, storageRacksList, storageRackButtons);
		gridPane.add(storageRacksSection, 1, 1);

		// Højre sektion (Inventory)
		VBox inventorySection = new VBox(10, inventoryList, inventoryButtons, createButtons);
		gridPane.add(inventorySection, 2, 1);

		// Nederste sektion (Warehouse Movements)
		gridPane.add(warehouseMovementsList, 0, 2, 3, 1); // Spænd over alle kolonner
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

	public Stage getStage() {
		return stage;
	}
}
