package GUI.Common;

import BatchArea.TasteProfile;
import Controllers.BatchArea;
import Controllers.Warehousing;
import Warehousing.Cask;
import Warehousing.StorageRack;
import Warehousing.Supplier;
import Warehousing.Warehouse;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UpdateCaskCommonDialog extends Application {
    private Label lblCaskId = new Label("Cask ID");
    private Label lblMaxQuantity = new Label("Max quantity");
    private Label lblCaskType = new Label("Cask type");
    private Label lblSupplier = new Label("Supplier");
    private TextField txfCaskId = new TextField() {{ setDisable(true); }};
    private TextField txfMaxQuantity = new TextField() {{ setDisable(true); }};
    private TextField txfCaskType = new TextField() {{ setDisable(true); }};
    private Label lblWarehouses = new Label("Warehouses");
    private Label lblStorageRacks = new Label("Storage racks");
    private Label lblTasteProfile = new Label("Taste profile");
    private ComboBox<TasteProfile> cbxTasteProfile = new ComboBox<>();
    private TextField txfSupplier = new TextField() {{ setDisable(true); }};
    private ListView<Warehouse> lvwWarehouses = new ListView<>();
    private ListView<StorageRack> lvwStorageRacks = new ListView<>();
    private Button btnUpdate = new Button("Update");
    private Button btnCancel = new Button("Cancel");
    private Stage stage;
    private Cask cask;

    public UpdateCaskCommonDialog(Cask cask) {
        this.cask = cask;
    }

    @Override
    public void start(Stage stage) {
        populateFields();
        this.stage = stage;
        // Create the main layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setHgap(10);
        grid.setVgap(10);

        // Use VBox to group labels and text fields
        VBox inputFields = new VBox(5); // Smaller spacing between Label and TextField
        inputFields.getChildren().addAll(lblCaskId, txfCaskId, lblMaxQuantity, txfMaxQuantity, lblCaskType, txfCaskType, lblSupplier, txfSupplier, lblTasteProfile, cbxTasteProfile);
        grid.add(inputFields, 0, 0); // "Cask ID" and text field in one line
        VBox rightBox = new VBox(10, lblWarehouses, lvwWarehouses, lblStorageRacks, lvwStorageRacks);
        grid.add(rightBox, 1, 0);

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(btnCancel, btnUpdate);
        rightBox.getChildren().add(buttons);

        txfCaskId.setEditable(false);
        txfMaxQuantity.setEditable(false);
        txfCaskType.setEditable(false);
        txfSupplier.setEditable(false);

        // Set up the action of the buttons
        btnUpdate.setOnAction(e -> btnUpdateAction());
        btnCancel.setOnAction(e -> stage.close());
        // Set up the scene and show the stage
        Scene scene = new Scene(grid, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Update Cask");
        stage.show();
    }

    public void populateFields() {
        if (cask != null) {
            txfCaskId.setText(String.valueOf(cask.getCaskID()));
            txfMaxQuantity.setText(String.valueOf(cask.getMaxQuantity()));
            txfCaskType.setText(cask.getCaskType());
            txfSupplier.setText(cask.getSupplier().getName());
            if (cask.getTasteProfile() != null) {
                cbxTasteProfile.setValue(cask.getTasteProfile());
            }
            cbxTasteProfile.getItems().addAll(BatchArea.getAllTasteProfiles());
            lvwWarehouses.getItems().addAll(Warehousing.getAllWarehouses());

            // Select the warehouse and storage rack associated with the cask
            Warehouse associatedWarehouse = cask.getStorageRack().getWarehouse();
            lvwWarehouses.getSelectionModel().select(associatedWarehouse);
            lvwStorageRacks.getItems().setAll(associatedWarehouse.getRacks().values());
            lvwStorageRacks.getSelectionModel().select(cask.getStorageRack());

            lvwWarehouses.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                lvwStorageRacks.getItems().clear();
                lvwStorageRacks.getItems().setAll(newValue.getRacks().values());
            });
        }
    }

public void btnUpdateAction() {
    ConfirmationDialog confirmationDialog = new ConfirmationDialog();
    confirmationDialog.show("Are you sure you want to update?", confirmed -> {
        if (confirmed) {
            if (isFormValid()) {
                Warehousing.updateCask(
                        cask,
                        cbxTasteProfile.getValue(),
                        lvwWarehouses.getSelectionModel().getSelectedItem(),
                        lvwStorageRacks.getSelectionModel().getSelectedItem()
                );
                stage.close();
            } else {
                new ErrorWindow().showError("Please fill out all fields correctly.");
            }
        }
    });
}

    private boolean isFormValid() {
        return !txfCaskId.getText().isEmpty() &&
                !txfMaxQuantity.getText().isEmpty() &&
                !txfCaskType.getText().isEmpty() &&
                !txfSupplier.getText().isEmpty() &&
                cbxTasteProfile.getValue() != null &&
                lvwWarehouses.getSelectionModel().getSelectedItem() != null &&
                lvwStorageRacks.getSelectionModel().getSelectedItem() != null;
    }

    public void setCask(Cask cask) {
        this.cask = cask;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
