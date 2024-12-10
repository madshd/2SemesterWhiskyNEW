package GUI.Warehousing;

import Controllers.Warehousing;
import Enumerations.IngredientType;
import Enumerations.Unit;
import GUI.Common.ConfirmationDialog;
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

    private TextField txfName = new TextField();
    private Label lblName = new Label("Name");
    private TextField txfDescription = new TextField();
    private Label lblDescription = new Label("Description");
    private TextField txfBatchNumber = new TextField();
    private Label lblBatchNumber = new Label("Batch number");
    private TextField txfQuantity = new TextField();
    private Label lblQuantity = new Label("Add quantity");
    private DatePicker dpProductionDate = new DatePicker();
    private Label lblProductionDate = new Label("Production date");
    private DatePicker dpExpirationDate = new DatePicker();
    private Label lblExpirationDate = new Label("Expiration date");
    private ComboBox<Supplier> cbxSupplier = new ComboBox<>();
    private Label lblSupplier = new Label("Supplier");
    private ComboBox<Unit> cbxUnitType = new ComboBox<>();
    private Label lblUnitType = new Label("Unit type");
    private ComboBox<IngredientType> cbxIngredientType = new ComboBox<>();
    private Label lblIngredientType = new Label("Ingredient type");
    private ListView<Warehouse> lvwWarehouse = new ListView<>();
    private ListView<StorageRack> lvwStorageRack = new ListView<>();
    private Label lblStorageRack = new Label("Storage Racks:");
    private Label lblWarehouse = new Label("Warehouses:");
    private Button btnUpdate = new Button("Update");
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
        HBox quantityBox = new HBox(10, txfQuantity, cbxUnitType);

        inputFields.getChildren().addAll(lblName, txfName, lblDescription, txfDescription, lblBatchNumber, txfBatchNumber, lblProductionDate, dpProductionDate,
                lblExpirationDate, dpExpirationDate, lblSupplier, cbxSupplier, lblQuantity, quantityBox, lblIngredientType, cbxIngredientType);

        // Tilføj komponenter til layoutet
        grid.add(inputFields, 0, 0);

        HBox buttonBox = new HBox(10, cancelButton, btnUpdate);

        VBox warehouseBox = new VBox(10, lblWarehouse, lvwWarehouse, lblStorageRack, lvwStorageRack, buttonBox);
        grid.add(warehouseBox, 1, 0, 1, 8);

        // Knapper
        btnUpdate.setOnAction(e -> btnCreateAction());
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
        ConfirmationDialog confirmationDialog = new ConfirmationDialog();
        confirmationDialog.show("Are you sure you want to create this ingredient?", confirmed -> {
            if (confirmed) {
                if (isFormValid()) {
                    Warehousing.createIngredientAndAdd(
                            txfName.getText(),
                            txfDescription.getText(),
                            Integer.parseInt(txfBatchNumber.getText()),
                            dpProductionDate.getValue(),
                            dpExpirationDate.getValue(),
                            Double.parseDouble(txfQuantity.getText()),
                            cbxSupplier.getValue(),
                            cbxUnitType.getValue(),
                            cbxIngredientType.getValue(),
                            lvwWarehouse.getSelectionModel().getSelectedItem(),
                            lvwStorageRack.getSelectionModel().getSelectedItem());
                    stage.close();
                } else {
                    errorWindow.showError("Please fill out all fields correctly.");
                }
            }
        });
    }

    public void updateLists() {
        cbxUnitType.getItems().addAll(Unit.values());
        cbxIngredientType.getItems().addAll(IngredientType.values());
        cbxSupplier.getItems().addAll(Warehousing.getSuppliers());
        lvwWarehouse.getItems().clear();
        lvwWarehouse.getItems().addAll(Warehousing.getAllWarehouses());
        lvwWarehouse.getSelectionModel().selectedItemProperty().addListener((observableValue, warehouse, newValue) -> {
            if (newValue != null) {
                lvwStorageRack.getItems().clear();
                for (StorageRack sr : newValue.getRacks().values()) {
                    for (Item item : sr.getList()) {
                        if (item == null) {
                            lvwStorageRack.getItems().add(sr);
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
        return !txfName.getText().isEmpty() &&
                !txfDescription.getText().isEmpty() &&
                !txfBatchNumber.getText().isEmpty() &&
                dpProductionDate.getValue() != null &&
                dpExpirationDate.getValue() != null &&
                !txfQuantity.getText().isEmpty() &&
                cbxSupplier.getValue() != null &&
                cbxUnitType.getValue() != null &&
                cbxIngredientType.getValue() != null &&
                lvwWarehouse.getSelectionModel().getSelectedItem() != null &&
                lvwStorageRack.getSelectionModel().getSelectedItem() != null;
    }
}