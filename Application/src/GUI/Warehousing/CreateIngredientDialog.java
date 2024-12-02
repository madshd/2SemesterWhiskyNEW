package GUI.Warehousing;

import Enumerations.IngredientType;
import Enumerations.Unit;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class CreateIngredientDialog extends Application {

        @Override
        public void start(Stage primaryStage) {
        // Opret hovedlayoutet
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setPadding(new Insets(20, 20, 20, 20));
            grid.setHgap(10);
            grid.setVgap(10);

            // Tilføj komponenter til layoutet
            TextField nameField = new TextField();
            nameField.setPromptText("Name");
            grid.add(nameField, 0, 0);

            TextField descriptionField = new TextField();
            descriptionField.setPromptText("Description");
            grid.add(descriptionField, 0, 1);

            TextField batchNumberField = new TextField();
            batchNumberField.setPromptText("Batch number");
            grid.add(batchNumberField, 0, 2);

            DatePicker productionDatePicker = new DatePicker();
            productionDatePicker.setPromptText("Production date");
            grid.add(productionDatePicker, 0, 3);

            DatePicker expirationDatePicker = new DatePicker();
            expirationDatePicker.setPromptText("Expiration date");
            grid.add(expirationDatePicker, 0, 4);

            ComboBox<String> supplierComboBox = new ComboBox<>();
            supplierComboBox.setPromptText("Supplier");
            supplierComboBox.getItems().addAll("Supplier1", "Supplier2");
            grid.add(supplierComboBox, 0, 5);

            TextField quantityField = new TextField();
            quantityField.setPromptText("Quantity");
            ComboBox<String> unitTypeComboBox = new ComboBox<>();
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

            ComboBox<String> ingredientTypeComboBox = new ComboBox<>();
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
            ListView<String> warehouseListView = new ListView<>();
            warehouseListView.getItems().addAll("Warehouse1", "Warehouse2", "Warehouse3");
            warehouseListView.setPrefHeight(100);
            grid.add(warehouseLabel, 2, 0);
            grid.add(warehouseListView, 2, 1);

            Label storageRackLabel = new Label("Storage Racks:");
            ListView<String> storageRackListView = new ListView<>();
            storageRackListView.getItems().addAll("StorageRack1", "StorageRack2", "StorageRack3");
            storageRackListView.setPrefHeight(100);
            grid.add(storageRackLabel, 2, 3);
            grid.add(storageRackListView, 2, 4);

            // Knapper
            Button createButton = new Button("Create");
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
