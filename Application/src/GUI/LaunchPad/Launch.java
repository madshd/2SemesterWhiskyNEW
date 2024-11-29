package GUI.LaunchPad;

import java.util.ArrayList;

import GUI.Batch.BatchArea;
import GUI.Production.ProductionArea;
import GUI.Warehousing.WarehousingArea;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Launch extends Application {

	private static ProductionArea productionArea;
	private static WarehousingArea warehousingArea;
	private static BatchArea batchArea;
	private static ArrayList<Button> buttons = new ArrayList<>();
	private static Stage primaryStage;
	private static Rectangle2D screenBounds;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		initAreaWindows();
		screenBounds = Screen.getPrimary().getVisualBounds();

		Launch.primaryStage = primaryStage;

		primaryStage.setTitle("LabelTales");
		GridPane dashboardPane = new GridPane();

		initContent(dashboardPane);

		Scene scene = new Scene(dashboardPane, 300, screenBounds.getHeight());

		primaryStage.setScene(scene);
		primaryStage.setX(0);
		primaryStage.setY(0);
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.UNDECORATED);

		primaryStage.setOnCloseRequest(event -> {
			closeProgram();
			event.consume();
		});

		primaryStage.show();
	}

	public static void initContent(GridPane dashboardPane) {
		// =================== TOP PANE ===================
		GridPane topPane = new GridPane();

		Label heading = new Label("LabelTales");
		heading.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

		HBox buttonBox = new HBox(10);
		buttonBox.setAlignment(Pos.CENTER);

		Button warehouseButton = new Button("Warehouse");
		Button productionButton = new Button("Production");
		Button batchAreaButton = new Button("BatchArea");

		setButtonAction(warehouseButton, "Warehouse");
		setButtonAction(productionButton, "Production");
		setButtonAction(batchAreaButton, "BatchArea");

		buttons.add(warehouseButton);
		buttons.add(productionButton);
		buttons.add(batchAreaButton);

		for (Button button : buttons) {
			button.setFocusTraversable(false);
		}

		buttonBox.getChildren().addAll(warehouseButton, productionButton, batchAreaButton);

		topPane.add(heading, 0, 0);
		topPane.add(buttonBox, 0, 1);
		topPane.setAlignment(Pos.CENTER);
		GridPane.setHalignment(heading, HPos.CENTER);

		topPane.setVgap(50);
		topPane.setHgap(10);
		topPane.setPrefSize(300, 200);

		// =================== MIDDLE PANE 1 ===================
		GridPane midPane1 = new GridPane();
		midPane1.setAlignment(Pos.CENTER);
		midPane1.add(new Label("Dynamic Info Pane 1"), 0, 0);
		midPane1.setVgap(10);
		midPane1.setHgap(10);
		midPane1.setPrefSize(300, 200);

		// =================== MIDDLE PANE 2 ===================
		GridPane midPane2 = new GridPane();
		midPane2.setAlignment(Pos.CENTER);
		midPane2.add(new Label("Dynamic Info Pane 2"), 0, 0);
		midPane2.setVgap(10);
		midPane2.setHgap(10);
		midPane2.setPrefSize(300, 200);

		// =================== MIDDLE PANE 3 ===================
		GridPane midPane3 = new GridPane();
		midPane3.setAlignment(Pos.CENTER);
		midPane3.add(new Label("Dynamic Info Pane 3"), 0, 0);
		midPane3.setVgap(10);
		midPane3.setHgap(10);
		midPane3.setPrefSize(300, 200);

		// =================== BOTTOM PANE ===================
		GridPane bottomPane = new GridPane();
		Button quitButton = new Button("Quit");
		quitButton.setFocusTraversable(false);
		quitButton.setOnAction(e -> closeProgram());

		bottomPane.add(quitButton, 0, 0);
		bottomPane.setAlignment(Pos.CENTER);
		bottomPane.setVgap(10);
		bottomPane.setHgap(10);
		bottomPane.setPrefSize(300, 50);
		GridPane.setValignment(quitButton, VPos.BOTTOM);

		// =================== OUTER GRIDPANE ===================
		dashboardPane.setHgap(10);
		dashboardPane.setVgap(10);
		dashboardPane.setPadding(new Insets(10, 10, 10, 10));
		dashboardPane.add(topPane, 0, 0);
		dashboardPane.add(midPane1, 0, 1);
		dashboardPane.add(midPane2, 0, 2);
		dashboardPane.add(midPane3, 0, 3);
		dashboardPane.add(bottomPane, 0, 4);
		dashboardPane.setAlignment(Pos.CENTER);
		dashboardPane.setGridLinesVisible(false);
	}

	private static void flipButtons(Button button) {
		for (Button b : buttons) {
			b.setDisable(false);
		}
		button.setDisable(true);
	}

	private static void setButtonAction(Button button, String area) {
		button.setOnAction(e -> {
			switch (area) {
				case "Warehouse" -> {
					productionArea.close();
					batchArea.close();
					if (!productionArea.getStage().isShowing() && !batchArea.getStage().isShowing()) {
						warehousingArea.show();
						flipButtons(button);
					}

				}
				case "Production" -> {
					warehousingArea.close();
					batchArea.close();
					if (!warehousingArea.getStage().isShowing() && !batchArea.getStage().isShowing()) {
						productionArea.show();
						flipButtons(button);
					}
				}
				case "BatchArea" -> {
					productionArea.close();
					warehousingArea.close();
					if (!productionArea.getStage().isShowing() && !warehousingArea.getStage().isShowing()) {
						batchArea.show();
						flipButtons(button);
					}
				}
			}
		});
	}

	public static void enableAllButtons() {
		for (Button button : buttons) {
			button.setDisable(false);
		}
	}

	private static void closeProgram() {
		if (warehousingArea.getStage().isShowing()) {
			warehousingArea.close();
			if (!warehousingArea.getStage().isShowing()) {
				Launch.primaryStage.close();
			}
		} else if (productionArea.getStage().isShowing()) {
			productionArea.close();
			if (!productionArea.getStage().isShowing()) {
				Launch.primaryStage.close();
			}
		} else if (batchArea.getStage().isShowing()) {
			batchArea.close();
			if (!batchArea.getStage().isShowing()) {
				Launch.primaryStage.close();
			}
		} else {
			Launch.primaryStage.close();
		}
	}

	private static void initAreaWindows() {
		productionArea = new ProductionArea();
		warehousingArea = new WarehousingArea();
		batchArea = new BatchArea();

		productionArea.initGlobalSettings();
		warehousingArea.initGlobalSettings();
		batchArea.initGlobalSettings();
	}
}
