package GUI.Warehousing;

import Controllers.Warehousing;
import Enumerations.IngredientType;
import Enumerations.Unit;
import GUI.Common.ConfirmationDialog;
import GUI.Common.ErrorWindow;
import Storage.Storage;
import Warehousing.Ingredient;
import Warehousing.StorageRack;
import Warehousing.Supplier;
import Warehousing.Warehouse;
import com.sun.jdi.Value;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UpdateIngredientDialog extends Application {

    private final Ingredient ingredient;
    private TextField txfName = new TextField() {{ setDisable(true); }};
    private Label lblName = new Label("Name");
    private TextField txfDescription = new TextField() {{ setDisable(true); }};
    private Label lblDescription = new Label("Description");
    private TextField txfBatchNumber = new TextField() {{ setDisable(true); }};
    private Label lblBatchNumber = new Label("Batch number");
    private TextField txfQuantity = new TextField();
    private Label lblQuantity = new Label("Add quantity");
    private DatePicker dpProductionDate = new DatePicker();
    private Label lblProductionDate = new Label("Production date");
    private DatePicker dpExpirationDate = new DatePicker();
    private Label lblExpirationDate = new Label("Expiration date");
    private ComboBox<Supplier> cbxSupplier = new ComboBox<>() {{ setDisable(true); }};
    private Label lblSupplier = new Label("Supplier");
    private ComboBox<Unit> cbxUnitType = new ComboBox<>();
    private ComboBox<IngredientType> cbxIngredientType = new ComboBox<>() {{ setDisable(true); }};
    private Label lblIngredientType = new Label("Ingredient type");
    private ListView<Warehouse> lvwWarehouse = new ListView<>();
    private ListView<StorageRack> lvwStorageRack = new ListView<>();
    private Label lblStorageRack = new Label("Storage Racks:");
    private Label lblWarehouse = new Label("Warehouses:");
    private Button btnUpdate = new Button("Update");
    private Button cancelButton = new Button("Cancel");
    private ErrorWindow errorWindow = new ErrorWindow();
    private Stage stage;

    public UpdateIngredientDialog(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public void start(Stage stage) {
        // Opret hovedlayoutet
        stage.show();
        populateFields();
        this.stage = stage;
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setHgap(10);
        grid.setVgap(10);

        VBox inputFields = new VBox(5);
        HBox quantityBox = new HBox(10, txfQuantity, cbxUnitType);

        inputFields.getChildren().addAll(lblName, txfName, lblDescription, txfDescription, lblBatchNumber, txfBatchNumber, lblProductionDate, dpProductionDate,
                lblExpirationDate, dpExpirationDate, lblSupplier, cbxSupplier, lblQuantity, quantityBox, lblIngredientType, cbxIngredientType);

        grid.add(inputFields, 0, 0);
        dpProductionDate.setDisable(true);
        dpExpirationDate.setDisable(true);
        cbxUnitType.setDisable(true);

        HBox buttonBox = new HBox(10, cancelButton, btnUpdate);

        VBox warehouseBox = new VBox(10, lblWarehouse, lvwWarehouse, lblStorageRack, lvwStorageRack, buttonBox);
        grid.add(warehouseBox, 1, 0, 1, 8);

        // Knapper
        btnUpdate.setOnAction(e -> btnUpdateAction());
        cancelButton.setOnAction(e -> stage.close());
        buttonBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(grid, 600, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setTitle("Update Ingredient");
        stage.setScene(scene);
        stage.show();
    }

    private void btnUpdateAction() {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog();
        confirmationDialog.show("Are you sure you want to update?", confirmed -> {
            if (confirmed) {
                if (!txfQuantity.getText().isEmpty() && Double.parseDouble(txfQuantity.getText()) > 0) {
                    Warehousing.updateIngredient(
                            ingredient,
                            Double.parseDouble(txfQuantity.getText().trim()),
                            lvwWarehouse.getSelectionModel().getSelectedItem(),
                            lvwStorageRack.getSelectionModel().getSelectedItem()
                    );
                    stage.close();
                } else if (txfQuantity.getText().isEmpty()) {
                    Warehousing.updateIngredient(
                            ingredient,
                            0,
                            lvwWarehouse.getSelectionModel().getSelectedItem(),
                            lvwStorageRack.getSelectionModel().getSelectedItem()
                    );
                    stage.close();
                } else {
                    errorWindow.showError("Quantity is not filled out correctly.");
                }
            }
        });
    }

    private void populateFields() {
        txfName.setText(ingredient.getName());
        txfDescription.setText(ingredient.getDescription());
        txfBatchNumber.setText(String.valueOf(ingredient.getBatchNo()));
        dpProductionDate.setValue(ingredient.getProductionDate());
        dpExpirationDate.setValue(ingredient.getExpirationDate());
        cbxSupplier.setValue(ingredient.getSupplier());
        cbxUnitType.setValue(ingredient.getUnit());
        cbxIngredientType.setValue(ingredient.getIngredientType());
        lvwWarehouse.getItems().setAll(Warehousing.getAllWarehouses());

        Warehouse associatedWarehouse = ingredient.getStorageRack().getWarehouse();
        lvwWarehouse.getSelectionModel().select(associatedWarehouse);
        lvwStorageRack.getItems().setAll(associatedWarehouse.getRacks().values());
        lvwStorageRack.getSelectionModel().select(ingredient.getStorageRack());

        lvwWarehouse.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            lvwStorageRack.getItems().clear();
            lvwStorageRack.getItems().addAll(newValue.getRacks().values());
        });
    }
}
