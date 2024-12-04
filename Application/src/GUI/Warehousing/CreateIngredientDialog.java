package GUI.Warehousing;

import Controllers.Warehousing;
import Enumerations.IngredientType;
import Enumerations.Unit;
import Interfaces.Item;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import Warehousing.Supplier;
import Warehousing.Warehouse;
import Warehousing.StorageRack;

import java.time.LocalDate;
import java.util.Map;

public class CreateIngredientDialog extends Application {
    private TextField nameField = new TextField();
    private TextField descriptionField = new TextField();
    private TextField batchNumberField = new TextField();
    private TextField quantityField = new TextField();
    private DatePicker productionDatePicker = new DatePicker();
    private DatePicker expirationDatePicker = new DatePicker();
    private ComboBox<Supplier> supplierComboBox = new ComboBox<>();
    private ComboBox<Unit> unitTypeComboBox = new ComboBox<>();
    private ComboBox<IngredientType> ingredientTypeComboBox = new ComboBox<>();
    private ListView<Warehouse> warehouseListView = new ListView<>();
    private ListView<StorageRack> storageRackListView = new ListView<>();
    private Button btnCreate = new Button("Create");
    private Stage stage;

        @Override
        public void start(Stage stage) {
        // Opret hovedlayoutet
            this.stage = stage;
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setPadding(new Insets(20, 20, 20, 20));
            grid.setHgap(10);
            grid.setVgap(10);

            // Tilføj komponenter til layoutet
            nameField.setPromptText("Name");
            grid.add(nameField, 0, 0);


            descriptionField.setPromptText("Description");
            grid.add(descriptionField, 0, 1);


            batchNumberField.setPromptText("Batch number");
            grid.add(batchNumberField, 0, 2);


            productionDatePicker.setPromptText("Production date");
            grid.add(productionDatePicker, 0, 3);


            expirationDatePicker.setPromptText("Expiration date");
            grid.add(expirationDatePicker, 0, 4);


            supplierComboBox.setPromptText("Supplier");
            grid.add(supplierComboBox, 0, 5);


            quantityField.setPromptText("Quantity");
            unitTypeComboBox.setPromptText("Unit type");


            HBox quantityBox = new HBox(10, quantityField, unitTypeComboBox);
            grid.add(quantityBox, 0, 6);


            ingredientTypeComboBox.setPromptText("Ingredient type");


            grid.add(ingredientTypeComboBox, 0, 7);

            Label warehouseLabel = new Label("Warehouses:");

            warehouseListView.setPrefHeight(100);
            grid.add(warehouseLabel, 2, 0);
            grid.add(warehouseListView, 2, 1);

            Label storageRackLabel = new Label("Storage Racks:");

            storageRackListView.setPrefHeight(100);
            grid.add(storageRackLabel, 2, 3);
            grid.add(storageRackListView, 2, 4);

            // Knapper
            btnCreate.setOnAction(e -> btnCreateAction());
            Button cancelButton = new Button("Cancel");
            HBox buttonBox = new HBox(10, cancelButton, btnCreate);
            buttonBox.setAlignment(Pos.CENTER);
            grid.add(buttonBox, 2, 8);
            // Opret og vis scene
            Scene scene = new Scene(grid, 600, 400);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setTitle("Create Ingredient");
            stage.setX(300);
            stage.setY(0);
            stage.setScene(scene);
            stage.show();
        }

        public void btnCreateAction() {
            Warehousing.createIngredientAndAdd(
                    nameField.getText(),
                    descriptionField.getText(),
                    Integer.parseInt(batchNumberField.getText()),
                    productionDatePicker.getValue(),
                    expirationDatePicker.getValue(),
                    Double.parseDouble(quantityField.getText()),
                    supplierComboBox.getValue(),
                    unitTypeComboBox.getValue(),
                    ingredientTypeComboBox.getValue(),
                    warehouseListView.getSelectionModel().getSelectedItem(),
                    storageRackListView.getSelectionModel().getSelectedItem()
            );
            stage.close();
        }

        public void updateLists() {
            unitTypeComboBox.getItems().addAll(Unit.values());
            ingredientTypeComboBox.getItems().addAll(IngredientType.values());
            supplierComboBox.getItems().addAll(Warehousing.getSuppliers());
            warehouseListView.getItems().clear();
            warehouseListView.getItems().addAll(Warehousing.getAllWarehouses());
            warehouseListView.getSelectionModel().select(0);
            warehouseListView.getSelectionModel().selectedItemProperty().addListener((observableValue, warehouse, newValue) -> {
                if (newValue != null) {
                    storageRackListView.getItems().clear();
                    for (StorageRack sr : newValue.getRacks().values()) {
                        for (Item item : sr.getList()) {
                            if (item == null) {
                                storageRackListView.getItems().add(sr);
                                break;
                            }
                        }
                    }
                }
            });
        }

        public static void main(String[] args) {
            launch(args);
        }
}
