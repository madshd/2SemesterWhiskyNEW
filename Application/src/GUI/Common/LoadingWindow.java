package GUI.Common;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoadingWindow {

    private final Stage loadingStage;
    private Label messageLabel;

    public LoadingWindow() {
        // Initialize the stage
        loadingStage = new Stage();
        loadingStage.setTitle("Loading");
        loadingStage.initModality(Modality.APPLICATION_MODAL);
        loadingStage.setResizable(false);

        // Set up the layout
        StackPane root = new StackPane();
        root.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        messageLabel = new Label();
        messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        root.getChildren().add(messageLabel);
        StackPane.setAlignment(messageLabel, Pos.CENTER);

        // Set up the scene
        Scene scene = new Scene(root, 300, 100);
        loadingStage.setScene(scene);
    }

    public void show(String message) {
        // Set the message dynamically
        messageLabel.setText(message);
        // Show the loading window
            loadingStage.show();
    }

    public void close() {
		loadingStage.close();
    }
}
