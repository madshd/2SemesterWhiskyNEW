package GUI.Warehousing;

import Controllers.Warehousing;
import Enumerations.Unit;
import GUI.Common.ConfirmationDialog;
import GUI.Common.ErrorWindow;
import Interfaces.Item;
import Warehousing.Supplier;
import Warehousing.Warehouse;
import Warehousing.StorageRack;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class CreateCaskDialog extends Application {
    private Label lblCaskId = new Label("Cask ID");
    private Label lblMaxQuantity = new Label("Max quantity");
    private Label lblCaskType = new Label("Cask type");
    private Label lblSupplier = new Label("Supplier");
    private TextField txfCaskId = new TextField();
    private TextField txfMaxQuantity = new TextField();
    private TextField txfCaskType = new TextField();
    private Label lblWarehouses = new Label("Warehouses");
    private Label lblStorageRacks = new Label("Storage racks");
    private ComboBox<Supplier> cbxSupplier = new ComboBox<>();
    private ListView<Warehouse> lvwWarehouses = new ListView<>();
    private ListView<StorageRack> lvwStorageRacks = new ListView<>();
    private Button btnCreate = new Button("Create");
    private Button btnCancel = new Button("Cancel");
    private ErrorWindow errorWindow = new ErrorWindow();
    private Stage stage;

    @Override
    public void start(Stage stage) {
        updateLists();
        this.stage = stage;
        // Opret hovedlayoutet
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setHgap(10);
        grid.setVgap(10);

        // Brug HBox til at gruppere label og TextField
        VBox inputFields = new VBox(5);
        inputFields.getChildren().addAll(lblCaskId, txfCaskId, lblMaxQuantity, txfMaxQuantity, lblCaskType, txfCaskType, lblSupplier, cbxSupplier);
        grid.add(inputFields, 0, 0);
        VBox rightBox = new VBox(10, lblWarehouses, lvwWarehouses, lblStorageRacks, lvwStorageRacks);
        grid.add(rightBox, 1, 0);

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(btnCancel, btnCreate);
        rightBox.getChildren().add(buttons);
        buttons.setAlignment(Pos.CENTER);

        btnCreate.setOnAction(e -> btnCreateAction());
        btnCancel.setOnAction(e -> stage.close());

        // Opret en scene og tilføj layoutet
        Scene scene = new Scene(grid, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setTitle("Create cask");
        stage.setScene(scene);
        stage.show();
    }

private void btnCreateAction() {
    ConfirmationDialog confirmationDialog = new ConfirmationDialog();
    confirmationDialog.show("Are you sure you want to create this cask?", confirmed -> {
        if (confirmed) {
            if (isFormValid()) {
                Warehousing.createCaskAndAdd(
                        Integer.parseInt(txfCaskId.getText()),
                        Double.parseDouble(txfMaxQuantity.getText()),
                        Unit.LITERS,
                        cbxSupplier.getValue(),
                        txfCaskType.getText(),
                        lvwWarehouses.getSelectionModel().getSelectedItem(),
                        lvwStorageRacks.getSelectionModel().getSelectedItem());
                stage.close();
            } else {
                errorWindow.showError("Please fill out all fields correctly.");
            }
        }
    });
}

/**
 * Validates the form fields to ensure all required fields are filled out.
 *
 * @return true if all required fields are filled out correctly, false otherwise.
 */
private boolean isFormValid() {
    return !txfCaskId.getText().isEmpty() && // Check if Cask ID field is not empty
            !txfMaxQuantity.getText().isEmpty() && // Check if Max Quantity field is not empty
            !txfCaskType.getText().isEmpty() && // Check if Cask Type field is not empty
            cbxSupplier.getValue() != null && // Check if a Supplier is selected
            lvwWarehouses.getSelectionModel().getSelectedItem() != null && // Check if a Warehouse is selected
            lvwStorageRacks.getSelectionModel().getSelectedItem() != null; // Check if a Storage Rack is selected
}

    private void updateLists() {
        cbxSupplier.getItems().addAll(Warehousing.getSuppliers());

        lvwWarehouses.getItems().clear();
        lvwWarehouses.getItems().addAll(Warehousing.getAllWarehouses());
        lvwWarehouses.getSelectionModel().selectedItemProperty().addListener((observableValue, warehouse, newValue) -> {
            if (newValue != null) {
                lvwStorageRacks.getItems().setAll(newValue.getRacks().values());
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
