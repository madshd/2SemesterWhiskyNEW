package GUI.Common;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ErrorWindow extends Application {

	@Override
	public void start(Stage primaryStage) {
	}

	public void showError(String message) {
		// Create an Alert of type ERROR
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null); // No header text
		alert.setContentText(message); // Set the message
		alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

		// Show the alert and wait for user to close it
		alert.showAndWait();
	}



	public static void main(String[] args) {
		launch(args);
	}
}
