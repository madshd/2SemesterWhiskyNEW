package GUI.Warehousing;

import Controllers.Warehousing;
import Warehousing.Warehouse;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Warehousing.StorageRack;

public class WarehouseCreate extends Application {
    private Label lblName = new Label("Name");
    private TextField txfName = new TextField();
    private Label lblAddress = new Label("Address");
    private TextField txfAddress = new TextField();
    private Label lblStorageRacks = new Label("Storage racks");
    private ListView<StorageRack> lvwStorageRacks = new ListView<>();
    private Label lblUnusedStorageRacks = new Label("Unused storage racks");
    private ListView<StorageRack> lvwUnusedStorageRacks = new ListView<>();
    private Button btnRight = new Button(">");
    private Button btnLeft = new Button("<");
    private Button btnSave = new Button("Save");
    private Button btnCancel = new Button("Cancel");
    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.show();
        initialize();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setHgap(10);
        grid.setVgap(10);

        HBox inputFields = new HBox(5);
        VBox nameFields = new VBox(5);
        nameFields.getChildren().addAll(lblName, txfName);

        VBox addressFields = new VBox(5);
        addressFields.getChildren().addAll(lblAddress, txfAddress);

        inputFields.getChildren().addAll(nameFields, addressFields);
        grid.add(inputFields, 0, 0);

        VBox leftSide = new VBox(10, lblStorageRacks, lvwStorageRacks, btnCancel);
        grid.add(leftSide, 0, 1);
        leftSide.setAlignment(Pos.CENTER);

        VBox rightSide = new VBox(10, lblUnusedStorageRacks, lvwUnusedStorageRacks, btnSave);
        grid.add(rightSide, 2, 1);
        rightSide.setAlignment(Pos.CENTER);

        VBox buttons = new VBox(10, btnRight, btnLeft);
        grid.add(buttons, 1, 1);
        buttons.setAlignment(Pos.CENTER);

        Scene scene = new Scene(grid, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Warehouse");
        stage.show();
    }

    private void initialize() {
        btnRight.setOnAction(e -> moveStorageRackToUnused());
        btnLeft.setOnAction(e -> moveStorageRackToWarehouse());
        btnSave.setOnAction(e -> saveWarehouse());
        btnCancel.setOnAction(e -> stage.close());
        lvwUnusedStorageRacks.getItems().addAll(Warehousing.getUnusedStorageRacks());
    }

    private void moveStorageRackToUnused() {
        StorageRack selectedRack = lvwStorageRacks.getSelectionModel().getSelectedItem();
        if (selectedRack != null) {
            lvwStorageRacks.getItems().remove(selectedRack);
            lvwUnusedStorageRacks.getItems().add(selectedRack);
        }
    }

    private void moveStorageRackToWarehouse() {
        StorageRack selectedRack = lvwUnusedStorageRacks.getSelectionModel().getSelectedItem();
        if (selectedRack != null) {
            lvwUnusedStorageRacks.getItems().remove(selectedRack);
            lvwStorageRacks.getItems().add(selectedRack);
        }
    }

    private void saveWarehouse() {
        String name = txfName.getText();
        String address = txfAddress.getText();
        if (name.isEmpty() || address.isEmpty()) {
            showAlert("Error", "Name and address cannot be empty.");
            return;
        }
        Warehouse warehouse = Warehousing.createWarehouse(name, address);
        for (StorageRack rack : lvwStorageRacks.getItems()) {
            warehouse.addStorageRack(rack.getId(), rack);
        }

        stage.close();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
