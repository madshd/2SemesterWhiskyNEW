package GUI.Warehousing;

import BatchArea.TasteProfile;
import Controllers.Warehousing;
import GUI.Common.ErrorWindow;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StorageRackDialog extends Application {
    private Label lblStorageRackId = new Label("Storage Rack ID");
    private TextField txfStorageRackId = new TextField();
    private Label lblShelves = new Label("Shelves");
    private TextField txfShelves = new TextField();
    private Button btnSave = new Button("Save");
    private Button btnCancel = new Button("Cancel");
    private Stage stage;
    private ErrorWindow errorWindow = new ErrorWindow();

    @Override
    public void start(Stage stage) {
        this.stage = stage; // Initialize the stage instance variable
        stage.show();
        // Create the main layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setHgap(10);
        grid.setVgap(10);

        VBox leftSide = new VBox(10, lblStorageRackId, txfStorageRackId, btnCancel);
        grid.add(leftSide, 0, 0);
        leftSide.setAlignment(Pos.CENTER);
        VBox rightSide = new VBox(10, lblShelves, txfShelves, btnSave);
        grid.add(rightSide, 2, 0);
        rightSide.setAlignment(Pos.CENTER);

        btnSave.setOnAction(e -> saveStorageRack());
        btnCancel.setOnAction(e -> stage.close());

        Scene scene = new Scene(grid, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Warehouse");
        stage.show();
    }

    private void saveStorageRack() {
        String storageRackId = txfStorageRackId.getText();
        String shelves = txfShelves.getText();
        if (storageRackId.isEmpty() || shelves.isEmpty()) {
            errorWindow.showError("Storage Rack ID and Shelves must be filled out.");
            return;
        }
        try {
            int shelvesCount = Integer.parseInt(shelves);
            Warehousing.createStorageRack(storageRackId, shelvesCount);
            stage.close();
        } catch (NumberFormatException e) {
            errorWindow.showError("Shelves must be a number.");
        }
    }
}
