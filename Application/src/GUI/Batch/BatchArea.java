package GUI.Batch;

import GUI.Common.ConfirmationDialog;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
	private FormulaManager formulaManager = new FormulaManager();

	public BatchArea() {
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
		scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
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

		// For intuitive clearing of textarea focus
		gridPane.setOnMouseClicked(event -> gridPane.requestFocus());

		// Define column constraints
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(70); // Main content (Batches and Products) takes 70% of width
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(30); // Sidebar (Formulae) takes 30% of width
		gridPane.getColumnConstraints().addAll(col1, col2);

		// Define row constraints
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(5); // Heading
		RowConstraints row2 = new RowConstraints();
		row2.setPercentHeight(45); // Batches
		RowConstraints row3 = new RowConstraints();
		row3.setPercentHeight(50); // Products
		gridPane.getRowConstraints().addAll(row1, row2, row3);

		// Create individual sections
		GridPane batchSection = createBatchSection();
		GridPane productSection = createProductSection();
		GridPane formulaSection = createFormulaSection();

		batchSection.setAlignment(Pos.BOTTOM_CENTER);
		productSection.setAlignment(Pos.TOP_CENTER);
		formulaSection.setAlignment(Pos.CENTER_LEFT);

		Label lblHeader = new Label("Batch Area");
		lblHeader.setId("LabelHeader");

		// Add sections to the main GridPane
		gridPane.add(lblHeader, 0, 0, 2, 1); // Column 0, spans both columns (Header)
		gridPane.add(batchSection, 0, 1); // Column 0, Row 0 (Center column)
		gridPane.add(productSection, 0, 2); // Column 0, Row 1 (Center column)
		gridPane.add(formulaSection, 1, 1, 1, 2); // Column 1, spans both rows (Sidebar)
		GridPane.setMargin(formulaSection, new Insets(0, 0, 40, 0));
		gridPane.setGridLinesVisible(false);
		GridPane.setHalignment(lblHeader, HPos.CENTER);

	}

	private static GridPane createProductSection() {
		GridPane productGrid = new GridPane();
		productGrid.setPadding(new Insets(10));
		productGrid.setHgap(10);
		productGrid.setVgap(10);

		// Search Bar
		TextField searchBar = new TextField();
		searchBar.setPromptText("Search Products");
		searchBar.setMinWidth(200);
		searchBar.setFocusTraversable(false);

		// Products Table
		TableView<String> productsTable = new TableView<>();
		productsTable.setPlaceholder(new Label("No Products Available"));
		productsTable.setMinHeight(300);
		productsTable.setMaxHeight(200);
		productsTable.setMinWidth(700);
		productsTable.setEditable(false);
		productsTable.setFocusTraversable(false);

		// Add search functionality
		FilteredList<String> filteredData = new FilteredList<>(FXCollections.observableArrayList(), p -> true);
		productsTable.setItems(filteredData);

		searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(product -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				return product.toLowerCase().contains(lowerCaseFilter);
			});
		});

		searchBar.setOnAction(event -> {
			scene.getRoot().requestFocus(); // Return focus to the scene
		});

		// Product Buttons
		Button createProductButton = new Button("Create New Product");
		Button defineFormulaButton = new Button("Define Formula");
		Button deleteProductButton = new Button("Delete Product");
		HBox productButtons = new HBox(25, createProductButton, defineFormulaButton, deleteProductButton, searchBar);
		productButtons.setAlignment(Pos.CENTER);

		createProductButton.setFocusTraversable(false);
		defineFormulaButton.setFocusTraversable(false);
		deleteProductButton.setFocusTraversable(false);

		// Add components to Product GridPane
		productGrid.add(new Label("Products"), 0, 0); // Column 0, Row 0
		productGrid.add(productsTable, 0, 2); // Column 0, Row 2
		productGrid.add(productButtons, 0, 3); // Column 0, Row 3

		return productGrid;
	}

	private static GridPane createBatchSection() {
		GridPane batchGrid = new GridPane();
		batchGrid.setPadding(new Insets(10));
		batchGrid.setHgap(10);
		batchGrid.setVgap(10);

		// Search Bar
		TextField searchBar = new TextField();
		searchBar.setPromptText("Search Batches");
		searchBar.setMinWidth(200);
		searchBar.setFocusTraversable(false);

		// Batches Table
		TableView<String> batchesTable = new TableView<>();
		batchesTable.setPlaceholder(new Label("No Batches Available"));
		batchesTable.setMinHeight(300);
		batchesTable.setMaxHeight(200);
		batchesTable.setMinWidth(700);
		batchesTable.setEditable(false);
		batchesTable.setFocusTraversable(false);

		// Add search functionality
		FilteredList<String> filteredData = new FilteredList<>(FXCollections.observableArrayList(), p -> true);
		batchesTable.setItems(filteredData);

		searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(batch -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				return batch.toLowerCase().contains(lowerCaseFilter);
			});
		});

		searchBar.setOnAction(event -> {
			scene.getRoot().requestFocus(); // Return focus to the scene
		});

		// Batch Buttons
		Button createBatchButton = new Button("Create New Batch");
		Button filterBatchButton = new Button("Filter");
		Button deleteBatchButton = new Button("Delete Batch");
		Button produceBatchButton = new Button("PRODUCE BATCH");
		HBox batchButtons = new HBox(25, createBatchButton, filterBatchButton, deleteBatchButton, produceBatchButton,
				searchBar);
		batchButtons.setAlignment(Pos.CENTER);

		createBatchButton.setFocusTraversable(false);
		filterBatchButton.setFocusTraversable(false);
		deleteBatchButton.setFocusTraversable(false);
		produceBatchButton.setFocusTraversable(false);

		// Add components to Batch GridPane
		batchGrid.add(new Label("Batches"), 0, 0); // Column 0, Row 0
		batchGrid.add(batchesTable, 0, 1); // Column 0, Row 2
		batchGrid.add(batchButtons, 0, 2); // Column 0, Row 3
		return batchGrid;

	}

	private static GridPane createFormulaSection() {
		GridPane formulaGrid = new GridPane();
		formulaGrid.setHgap(10);
		formulaGrid.setVgap(10);
		formulaGrid.setAlignment(Pos.CENTER);

		// Formula List
		ListView<String> formulaList = new ListView<>();
		formulaList.setPlaceholder(new Label("No Formulae Available"));
		formulaList.setPrefWidth(300);
		formulaList.setMinHeight(708);
		formulaList.setFocusTraversable(false);
		formulaList.setEditable(false);

		// Open Formula Manager Button
		Button openFormulaManagerButton = new Button("Open Formula Manager");
		openFormulaManagerButton.setFocusTraversable(false);
		openFormulaManagerButton.setOnAction(e -> {
			FormulaManager formulaManager = new FormulaManager();
			formulaManager.showFormulaManagerWindow();
		});

		// Add components to Formula GridPane
		formulaGrid.add(new Label("All Formulae"), 0, 0); // Column 0, Row 0
		formulaGrid.add(formulaList, 0, 1, 1, 3); // Column 0, Row 1 (spanning 3 rows)
		formulaGrid.add(openFormulaManagerButton, 0, 4); // Column 0, Row 4
		GridPane.setHalignment(openFormulaManagerButton, javafx.geometry.HPos.CENTER);

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
