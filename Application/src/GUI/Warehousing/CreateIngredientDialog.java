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
    TextField nameField = new TextField();
    TextField descriptionField = new TextField();
    TextField batchNumberField = new TextField();
    TextField quantityField = new TextField();
    DatePicker productionDatePicker = new DatePicker();
    DatePicker expirationDatePicker = new DatePicker();
    ComboBox<Supplier> supplierComboBox = new ComboBox<>();
    ComboBox<String> unitTypeComboBox = new ComboBox<>();
    ComboBox<String> ingredientTypeComboBox = new ComboBox<>();
    ListView<Warehouse> warehouseListView = new ListView<>();
    ListView<StorageRack> storageRackListView = new ListView<>();

        @Override
        public void start(Stage primaryStage) {
        // Opret hovedlayoutet
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
            supplierComboBox.getItems().addAll(Warehousing.getSuppliers());
            grid.add(supplierComboBox, 0, 5);


            quantityField.setPromptText("Quantity");
            unitTypeComboBox.setPromptText("Unit type");

            unitTypeComboBox.getItems().addAll(
                    Unit.LITERS.toString(),
                    Unit.MILLILITERS.toString(),
                    Unit.KILOGRAM.toString(),
                    Unit.GRAMS.toString(),
                    Unit.POUNDS.toString(),
                    Unit.PIECES.toString(),
                    Unit.CASKS.toString(),
                    Unit.PERCENT.toString(),
                    Unit.TONNES.toString()
            );

            HBox quantityBox = new HBox(10, quantityField, unitTypeComboBox);
            grid.add(quantityBox, 0, 6);


            ingredientTypeComboBox.setPromptText("Ingredient type");

            ingredientTypeComboBox.getItems().addAll(
                    IngredientType.GRAIN.toString(),
                    IngredientType.YEAST.toString(),
                    IngredientType.WATER.toString(),
                    IngredientType.ADDITIVE.toString(),
                    IngredientType.OTHER.toString()
                    );

            grid.add(ingredientTypeComboBox, 0, 7);

            Label warehouseLabel = new Label("Warehouses:");
            warehouseListView.getItems().addAll(Warehousing.getAllWarehouses());
            warehouseListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
            warehouseListView.setPrefHeight(100);
            grid.add(warehouseLabel, 2, 0);
            grid.add(warehouseListView, 2, 1);

            Label storageRackLabel = new Label("Storage Racks:");

            storageRackListView.setPrefHeight(100);
            grid.add(storageRackLabel, 2, 3);
            grid.add(storageRackListView, 2, 4);

            // Knapper
            Button createButton = new Button("Create");
            createButton.setOnAction(e -> {
                // Opret ingrediens
                String name = nameField.getText();
                String description = descriptionField.getText();
                int batchNumber = Integer.parseInt(batchNumberField.getText());
                LocalDate productionDate = productionDatePicker.getValue();
                LocalDate expirationDate = expirationDatePicker.getValue();
                Supplier supplier = (Supplier) supplierComboBox.getValue();
                double quantity = Double.parseDouble(quantityField.getText());
                Unit unitType = Unit.valueOf(unitTypeComboBox.getValue());
                IngredientType ingredientType = IngredientType.valueOf(ingredientTypeComboBox.getValue());
                StorageRack storageRack = storageRackListView.getSelectionModel().getSelectedItem();
                Warehouse warehouse = warehouseListView.getSelectionModel().getSelectedItem();

                Warehousing.createIngredientAndAdd(name, description, batchNumber, productionDate, expirationDate, quantity, supplier, unitType, ingredientType, warehouse, storageRack);
                primaryStage.close();
                WarehousingArea.updateLists();
            });
            Button cancelButton = new Button("Cancel");
            HBox buttonBox = new HBox(10, cancelButton, createButton);
            buttonBox.setAlignment(Pos.CENTER);
            grid.add(buttonBox, 2, 8);
            // Opret og vis scene
            Scene scene = new Scene(grid, 600, 400);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            primaryStage.setTitle("Create Ingredient");
            primaryStage.setX(300);
            primaryStage.setY(0);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
}
