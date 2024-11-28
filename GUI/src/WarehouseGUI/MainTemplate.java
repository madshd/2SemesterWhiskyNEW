package WarehouseGUI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainTemplate {
    private final BorderPane root;      // Hovedlayout
    private final StackPane viewContainer; // Container til views

    public MainTemplate() {
        // Opret layoutet
        root = new BorderPane();

        // Sidebar (konstant)
        VBox sidebar = new VBox();
        sidebar.setStyle("-fx-background-color: #2c3e50; -fx-padding: 10;");
        sidebar.setSpacing(10);

        // Navigation knapper
        Button btnView1 = new Button("Gå til View 1");
        Button btnView2 = new Button("Gå til View 2");

        sidebar.getChildren().addAll(btnView1, btnView2);

        // Dynamisk container (hovedområde)
        viewContainer = new StackPane();
        viewContainer.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 20;");

        // Tilføj layout til BorderPane
        root.setLeft(sidebar);
        root.setCenter(viewContainer);

        // Tilføj knap-handlers for at skifte views
        btnView1.setOnAction(e -> setView(new View1().getView()));
        btnView2.setOnAction(e -> setView(new View2().getView()));
    }

    // Metode til at opdatere det dynamiske centerområde
    public void setView(StackPane view) {
        viewContainer.getChildren().setAll(view);
    }

    // Returnér hovedlayoutet som en Scene
    public Scene getScene() {
        return new Scene(root, 800, 600);
    }
}
