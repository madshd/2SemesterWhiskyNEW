package GUI.Common;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class ConfirmationDialog {

	/**
	 * Displays a modal confirmation dialog with a custom message.
	 *
	 * @param message  The message to display in the dialog.
	 * @param onResult A callback function to handle the user's choice (true for
	 *                 Yes/Accept, false for No/Decline).
	 */
	public void show(String message, Consumer<Boolean> onResult) {
		Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.setTitle("Confirmation");

		// Message label
		Label messageLabel = new Label(message);

		// Yes/Accept button
		Button yesButton = new Button("Yes");
		yesButton.setOnAction(e -> {
			onResult.accept(true); // Return true for Yes/Accept
			dialog.close();
		});

		// No/Decline button
		Button noButton = new Button("No");
		noButton.setOnAction(e -> {
			onResult.accept(false); // Return false for No/Decline
			dialog.close();
		});

		// Layout
		HBox buttonLayout = new HBox(10, yesButton, noButton);
		VBox layout = new VBox(10, messageLabel, buttonLayout);
		layout.setStyle("-fx-padding: 10; -fx-alignment: center;");
		buttonLayout.setStyle("-fx-alignment: center;");

		// Scene and stage
		Scene scene = new Scene(layout, 350, 150);
		dialog.setScene(scene);
		dialog.showAndWait(); // Show the dialog and wait for it to close
	}
}
