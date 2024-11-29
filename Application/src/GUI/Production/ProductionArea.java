package GUI.Production;

import GUI.Common.ConfirmationDialog;
import Interfaces.Item;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class ProductionArea {

	private Rectangle2D screenBounds;
	private Stage stage;
	private GridPane mainPane;
	private Scene scene;

	public ProductionArea() {
		stage = new Stage();
		stage.setTitle("Production Area");

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				close();
				event.consume();
			}
		});

		screenBounds = Screen.getPrimary().getVisualBounds();

		mainPane = new GridPane();
		initContent(mainPane);

		scene = new Scene(mainPane, screenBounds.getWidth() - 300, screenBounds.getHeight());

		stage.setResizable(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setX(300);
		stage.setY(0);
		stage.setScene(scene);
	}

	private final ListView<Item> lvwDistillates = new ListView<>();
	private final ListView<Item> lvwCasks = new ListView<>();

	public void initContent(GridPane gridPane) {
		Label headerLabel = new Label("Production Area");
		headerLabel.setFont(new Font("Arial", 32));
		GridPane.setHalignment(headerLabel, HPos.CENTER);
		gridPane.add(headerLabel, 0, 0);
	}

	private class Distillates extends GridPane{

	}

	public void show() {
		stage.show();
	}

	public void close() {
		if (stage.isShowing()) {
			ConfirmationDialog confirmationDialog = new ConfirmationDialog();
			confirmationDialog.show("Are you sure you want to close the Production Area?", result -> {
				if (result) {
					stage.close();
					GUI.LaunchPad.Launch.enableAllButtons();
				}
			});
		}
	}

	public Stage getStage() {
		return stage;
	}
}
