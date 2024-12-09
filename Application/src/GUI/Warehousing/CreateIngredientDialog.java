package GUI.Warehousing;

import Controllers.Warehousing;
import Enumerations.IngredientType;
import Enumerations.Unit;
import GUI.Common.ErrorWindow;
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
    private Label nameLabel = new Label("Name");
    private TextField descriptionField = new TextField();
    private Label descriptionLabel = new Label("Description");
    private TextField batchNumberField = new TextField();
    private Label batchNumberLabel = new Label("Batch number");
    private TextField quantityField = new TextField();
    private Label quantityLabel = new Label("Quantity");
    private DatePicker productionDatePicker = new DatePicker();
    private Label productionDateLabel = new Label("Production date");
    private DatePicker expirationDatePicker = new DatePicker();
    private Label expirationDateLabel = new Label("Expiration date");
    private ComboBox<Supplier> supplierComboBox = new ComboBox<>();
    private Label supplierLabel = new Label("Supplier");
    private ComboBox<Unit> unitTypeComboBox = new ComboBox<>();
    private Label unitTypeLabel = new Label("Unit type");
    private ComboBox<IngredientType> ingredientTypeComboBox = new ComboBox<>();
    private Label ingredientTypeLabel = new Label("Ingredient type");
    private ListView<Warehouse> warehouseListView = new ListView<>();
    private ListView<StorageRack> storageRackListView = new ListView<>();
    private Label storageRackLabel = new Label("Storage Racks:");
    private Label warehouseLabel = new Label("Warehouses:");
    private Button btnCreate = new Button("Create");
    private Button cancelButton = new Button("Cancel");
    private ErrorWindow errorWindow = new ErrorWindow();
    private Stage stage;

    @Override
    public void start(Stage stage) {
        // Opret hovedlayoutet
        stage.show();
        updateLists();
        this.stage = stage;
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setHgap(10);
        grid.setVgap(10);

        VBox inputFields = new VBox(5); // Smaller spacing between Label and TextField
        HBox quantityBox = new HBox(10, quantityField, unitTypeComboBox);

        inputFields.getChildren().addAll(nameLabel, nameField, descriptionLabel, descriptionField, batchNumberLabel, batchNumberField, productionDateLabel, productionDatePicker,
                expirationDateLabel, expirationDatePicker, supplierLabel, supplierComboBox, quantityLabel, quantityBox, ingredientTypeLabel, ingredientTypeComboBox);

        // Tilføj komponenter til layoutet
        grid.add(inputFields, 0, 0);

        HBox buttonBox = new HBox(10, cancelButton, btnCreate);

        VBox warehouseBox = new VBox(10, warehouseLabel, warehouseListView, storageRackLabel, storageRackListView, buttonBox);
        grid.add(warehouseBox, 1, 0, 1, 8);

        // Knapper
        btnCreate.setOnAction(e -> btnCreateAction());
        cancelButton.setOnAction(e -> stage.close());
        buttonBox.setAlignment(Pos.CENTER);

        // Opret og vis scene

        Scene scene = new Scene(grid, 600, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setTitle("Create Ingredient");
        stage.setScene(scene);
        stage.show();
    }

    public void btnCreateAction() {
        if (isFormValid()) {
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
        } else {
            errorWindow.showError("Please fill out all fields correctly.");
        }
    }

    public void updateLists() {
        unitTypeComboBox.getItems().addAll(Unit.values());
        ingredientTypeComboBox.getItems().addAll(IngredientType.values());
        supplierComboBox.getItems().addAll(Warehousing.getSuppliers());
        warehouseListView.getItems().clear();
        warehouseListView.getItems().addAll(Warehousing.getAllWarehouses());
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

    private boolean isFormValid() {
        return !nameField.getText().isEmpty() &&
                !descriptionField.getText().isEmpty() &&
                !batchNumberField.getText().isEmpty() &&
                productionDatePicker.getValue() != null &&
                expirationDatePicker.getValue() != null &&
                !quantityField.getText().isEmpty() &&
                supplierComboBox.getValue() != null &&
                unitTypeComboBox.getValue() != null &&
                ingredientTypeComboBox.getValue() != null &&
                warehouseListView.getSelectionModel().getSelectedItem() != null &&
                storageRackListView.getSelectionModel().getSelectedItem() != null;
    }
}