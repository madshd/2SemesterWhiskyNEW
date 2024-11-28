package WarehouseGUI;

import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;

public class View2 {
    private final StackPane view;

    public View2() {
        view = new StackPane();
        view.setStyle("-fx-background-color: #3498db; -fx-border-color: black; -fx-border-width: 2;");
        Label label = new Label("Dette er View 2");
        label.setStyle("-fx-font-size: 20; -fx-text-fill: white;");
        view.getChildren().add(label);
    }

    public StackPane getView() {
        return view;
    }
}
