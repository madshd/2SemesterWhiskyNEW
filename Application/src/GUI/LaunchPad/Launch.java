package GUI.LaunchPad;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import GUI.Batch.BatchArea;
import GUI.Production.ProductionArea;
import GUI.Warehousing.WarehousingArea;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
		scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.setX(0);
		primaryStage.setY(0);
		primaryStage.setResizable(false);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setAlwaysOnTop(true);
		primaryStage.setOnCloseRequest(event -> {
			closeProgram();
			event.consume();
		});

		primaryStage.show();
	}

	@SuppressWarnings("unused")
	public static void initContent(GridPane dashboardPane) {
		// =================== TOP PANE ===================
		GridPane topPane = new GridPane();

		Image image = new Image(Launch.class.getResource("/logo.png").toExternalForm());

		ImageView imageView = new ImageView(image);

		imageView.setFitHeight(150);

		imageView.setPreserveRatio(true);

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

		topPane.add(imageView, 0, 1);
		topPane.add(buttonBox, 0, 2);
		topPane.setAlignment(Pos.CENTER);
		GridPane.setHalignment(imageView, HPos.CENTER);

		topPane.setVgap(25);
		topPane.setHgap(10);
		topPane.setPrefSize(300, 200);

		Separator sep1 = new Separator();
		sep1.setOrientation(Orientation.HORIZONTAL);
		sep1.setPrefWidth(300);

		topPane.add(sep1, 0, 3);

		// =================== INFO PANE 1 ===================
		GridPane infoPane1 = new GridPane();
		infoPane1.setAlignment(Pos.CENTER);
		infoPane1.setVgap(10);
		infoPane1.setHgap(10);
		infoPane1.setPrefSize(300, 200);

		// Create a Label to display the time
		Label timeLabel = new Label();
		timeLabel.setId("timeLabel");

		// Create a DateTimeFormatter to format the time
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		// Create a Timeline to update the time every second
		Timeline timeline = new Timeline(
				new KeyFrame(Duration.seconds(0), event -> {
					// Get the current time and format it
					LocalTime currentTime = LocalTime.now();
					timeLabel.setText(currentTime.format(timeFormatter));
				}),
				new KeyFrame(Duration.seconds(1)));
		timeline.setCycleCount(Timeline.INDEFINITE); // Run indefinitely
		timeline.play(); // Start the timeline

		infoPane1.add(new Label("Dynamic Info Pane 1"), 0, 0);

		infoPane1.add(timeLabel, 0, 1);
		GridPane.setHalignment(timeLabel, HPos.CENTER);

		// =================== INFO PANE 2 ===================
		GridPane infoPane2 = new GridPane();
		infoPane2.setAlignment(Pos.CENTER);
		infoPane2.add(new Label("Dynamic Info Pane 2"), 0, 0);
		infoPane2.setVgap(10);
		infoPane2.setHgap(10);
		infoPane2.setPrefSize(300, 200);

		// =================== INFO PANE 3 ===================
		GridPane infoPane3 = new GridPane();
		infoPane3.setAlignment(Pos.CENTER);
		infoPane3.add(new Label("Dynamic Info Pane 3"), 0, 0);
		infoPane3.setVgap(10);
		infoPane3.setHgap(10);
		infoPane3.setPrefSize(300, 200);

		// =================== BOTTOM PANE ===================
		Separator sep2 = new Separator();
		sep2.setOrientation(Orientation.HORIZONTAL);
		sep2.setPrefWidth(300);

		GridPane bottomPane = new GridPane();
		Button quitButton = new Button("  Quit  ");
		quitButton.setFocusTraversable(false);
		quitButton.setOnAction(e -> closeProgram());

		bottomPane.add(sep2, 0, 0);
		bottomPane.add(quitButton, 0, 1);
		bottomPane.setAlignment(Pos.CENTER);
		bottomPane.setVgap(25);
		bottomPane.setHgap(10);
		bottomPane.setPrefSize(300, 50);
		GridPane.setHalignment(quitButton, HPos.CENTER);
		GridPane.setValignment(sep2, VPos.TOP);
		GridPane.setValignment(quitButton, VPos.BOTTOM);

		// =================== OUTER GRIDPANE ===================
		dashboardPane.setHgap(10);
		dashboardPane.setVgap(10);
		dashboardPane.setPadding(new Insets(10, 10, 10, 10));
		dashboardPane.add(topPane, 0, 0);
		dashboardPane.add(infoPane1, 0, 1);
		dashboardPane.add(infoPane2, 0, 2);
		dashboardPane.add(infoPane3, 0, 3);
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

	@SuppressWarnings("unused")
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
	}
}
