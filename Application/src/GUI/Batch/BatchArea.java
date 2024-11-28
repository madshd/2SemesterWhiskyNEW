package GUI.Batch;

import GUI.Common.ConfirmationDialog;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class BatchArea {

	Stage stage = new Stage();

	public void show() {
		stage.setTitle("Batch Area");

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				close();
				event.consume();
			}
		});

		// Main Window
		GridPane mainPane = new GridPane();
		initContent(mainPane);

		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

		Scene scene = new Scene(mainPane, screenBounds.getWidth() - 300, screenBounds.getHeight());

		stage.setResizable(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setX(300);
		stage.setY(0);
		stage.setScene(scene);
		stage.show();
	}

	public static void initContent(GridPane gridPane) {
		Label headerLabel = new Label("Batch Area");
		headerLabel.setFont(new Font("Arial", 32));
		GridPane.setHalignment(headerLabel, HPos.CENTER);
		gridPane.add(headerLabel, 0, 0);
	}

	public void close() {
		if (stage.isShowing()) {
			ConfirmationDialog confirmationDialog = new ConfirmationDialog();
			confirmationDialog.show("Are you sure you want to close the Batch Area?", result -> {
				if (result) {
					stage.close();
					GUI.LaunchPad.Launch.enableButtons();
				}
			});
		}
	}

	public Stage getStage() {
		return stage;
	}
}
