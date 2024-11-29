package GUI.Batch;

import GUI.Common.ConfirmationDialog;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class BatchArea {

	private Rectangle2D screenBounds;
	private Stage stage;
	private GridPane mainPane;
	private static Scene scene;

	public void initGlobalSettings() {
		stage = new Stage();
		stage.setTitle("Batch Area");
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				close();
				event.consume();
			}
		});
		screenBounds = Screen.getPrimary().getVisualBounds();
		mainPane = new GridPane();
		mainPane.setAlignment(Pos.CENTER);
		initContent(mainPane);
		scene = new Scene(mainPane, screenBounds.getWidth() - 300, screenBounds.getHeight());
		stage.setResizable(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setX(300);
		stage.setY(0);
		stage.setScene(scene);
	}

	public void show() {
		stage.show();
	}

	public static void initContent(GridPane gridPane) {
		// Main GridPane setup
		gridPane.setPadding(new Insets(10));
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setAlignment(Pos.CENTER);

		// Define column constraints
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(70); // Main content (Batches and Products) takes 70% of width
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(30); // Sidebar (Formulae) takes 30% of width
		gridPane.getColumnConstraints().addAll(col1, col2);

		// Define row constraints
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(50); // Batches take 50% of the height
		RowConstraints row2 = new RowConstraints();
		row2.setPercentHeight(50); // Products take 50% of the height
		gridPane.getRowConstraints().addAll(row1, row2);

		// Create individual sections
		GridPane batchSection = createBatchSection();
		GridPane productSection = createProductSection();
		GridPane formulaSection = createFormulaSection();

		// Add sections to the main GridPane
		gridPane.add(batchSection, 0, 0); // Column 0, Row 0 (Center column)
		gridPane.add(productSection, 0, 1); // Column 0, Row 1 (Center column)
		gridPane.add(formulaSection, 1, 0, 1, 2); // Column 1, spans both rows (Sidebar)
	}

	private static GridPane createProductSection() {
		GridPane productGrid = new GridPane();
		productGrid.setPadding(new Insets(10));
		productGrid.setHgap(10);
		productGrid.setVgap(10);

		// Products Table
		TableView<String> productsTable = new TableView<>();
		productsTable.setPlaceholder(new Label("No Products Available"));
		productsTable.setMinHeight(200);
		productsTable.setMaxHeight(200);
		productsTable.setPrefWidth(600);

		// Product Buttons
		Button createProductButton = new Button("Create New Product");
		Button defineFormulaButton = new Button("Define Formula");
		Button deleteProductButton = new Button("Delete Product");
		HBox productButtons = new HBox(10, createProductButton, defineFormulaButton, deleteProductButton);
		productButtons.setAlignment(Pos.CENTER);

		// Add components to Product GridPane
		productGrid.add(new Label("Products"), 0, 0); // Column 0, Row 0
		productGrid.add(productsTable, 0, 1); // Column 0, Row 1
		productGrid.add(productButtons, 0, 2); // Column 0, Row 2

		return productGrid;
	}

	private static GridPane createBatchSection() {
		GridPane batchGrid = new GridPane();
		batchGrid.setPadding(new Insets(10));
		batchGrid.setHgap(10);
		batchGrid.setVgap(10);

		// Batches Table
		TableView<String> batchesTable = new TableView<>();
		batchesTable.setPlaceholder(new Label("No Batches Available"));
		batchesTable.setMinHeight(200);
		batchesTable.setMaxHeight(200);
		batchesTable.setPrefWidth(600);

		// Batch Buttons
		Button createBatchButton = new Button("Create New Batch");
		Button filterBatchButton = new Button("Filter");
		Button deleteBatchButton = new Button("Delete Batch");
		HBox batchButtons = new HBox(10, createBatchButton, filterBatchButton, deleteBatchButton);
		batchButtons.setAlignment(Pos.CENTER);

		// Produce Batch Button
		Button produceBatchButton = new Button("PRODUCE BATCH");
		produceBatchButton.setStyle("-fx-background-color: lightgreen; -fx-font-weight: bold;");

		// Add components to Batch GridPane
		batchGrid.add(new Label("Batches"), 0, 0); // Column 0, Row 0
		batchGrid.add(batchesTable, 0, 1); // Column 0, Row 1
		batchGrid.add(batchButtons, 0, 2); // Column 0, Row 2
		batchGrid.add(produceBatchButton, 0, 3); // Column 0, Row 3

		return batchGrid;
	}

	private static GridPane createFormulaSection() {
		GridPane formulaGrid = new GridPane();
		formulaGrid.setPadding(new Insets(10));
		formulaGrid.setHgap(10);
		formulaGrid.setVgap(10);

		// Formula List
		ListView<String> formulaList = new ListView<>();
		formulaList.setPrefWidth(200);

		// Open Formula Manager Button
		Button openFormulaManagerButton = new Button("Open Formula Manager");

		// Add components to Formula GridPane
		formulaGrid.add(new Label("All Formulae"), 0, 0); // Column 0, Row 0
		formulaGrid.add(formulaList, 0, 1, 1, 3); // Column 0, Row 1 (spanning 3 rows)
		formulaGrid.add(openFormulaManagerButton, 0, 4); // Column 0, Row 4
		GridPane.setHalignment(openFormulaManagerButton, javafx.geometry.HPos.RIGHT);

		return formulaGrid;
	}

	public void close() {
		if (stage.isShowing()) {
			ConfirmationDialog confirmationDialog = new ConfirmationDialog();
			confirmationDialog.show("Are you sure you want to close the Batch Area?", result -> {
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
