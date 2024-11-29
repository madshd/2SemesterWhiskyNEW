package GUI.Production;

import Controllers.Production;
import GUI.Common.Common;
import GUI.Common.ConfirmationDialog;
import Interfaces.Item;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.List;

public class ProductionArea {

	private Rectangle2D screenBounds;
	private Stage stage;
	private GridPane mainPane;
	private Scene scene;
	private Distillates distillates;
	private Casks casks;

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
		distillates = new Distillates(this);
		casks = new Casks(this);
		initContent(mainPane);

		scene = new Scene(mainPane, screenBounds.getWidth() - 300, screenBounds.getHeight());
		scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

		stage.setResizable(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setX(300);
		stage.setY(0);
		stage.setScene(scene);
	}


	public void initContent(GridPane gridPane) {
		Label headerLabel = new Label("Production Area");
//		headerLabel.setFont(new Font("Arial", 32));
		GridPane.setHalignment(headerLabel, HPos.CENTER);
		headerLabel.setId("LabelHeader");
		headerLabel.setPrefWidth(screenBounds.getWidth() - 300);
		headerLabel.setAlignment(Pos.CENTER);

		gridPane.add(headerLabel, 0, 0);
		gridPane.add(distillates,0,1);
		gridPane.add(casks,0,2);
	}

	// Shared settings
	private static int hgap = 20;
	private static int vgap = 20;
	private static Insets padding = new Insets(20);
	private static boolean gridLines = false;
	private static Border border = Common.getBorder(1,0,0,0);

	private class Distillates extends GridPane{
		private final ListView<Item> lvwDistillates = new ListView<>();
		private final TextArea txaDistillateDetails = new TextArea();

		public Distillates(ProductionArea pa){
			// Generel settings
			this.setBorder(border);
			this.setPadding(padding);
			this.setHgap(hgap);
			this.setVgap(vgap);
			this.setGridLinesVisible(gridLines);
			double areaWidth = screenBounds.getWidth() - 300 * 0.9;
			double areaHeight = screenBounds.getHeight() * 0.9;

			// Subtitel
			Label lblSubTitel = new Label("Distillates");
			lblSubTitel.setId("LabelSubtitle");
			GridPane.setHalignment(lblSubTitel,HPos.CENTER);
			lblSubTitel.setPrefWidth(areaWidth);
			lblSubTitel.setAlignment(Pos.CENTER);
			this.add(lblSubTitel, 0, 0,4,1);

			// Labels
			Label lblList = new Label("Avaliable distillates");
			this.add(lblList, 0, 1);
			Label lblDetails = new Label("Detailed info on selected distillate");
			this.add(lblDetails, 2, 1);

			// Lists
			double lvwWithCol0 = areaWidth * 0.6;
			double lvwHeightCol0Top = areaHeight * 0.3;

			lvwDistillates.setEditable(false);
			lvwDistillates.setPrefWidth(lvwWithCol0);
			lvwDistillates.setPrefHeight(lvwHeightCol0Top);
			this.add(lvwDistillates,0,2,2,1);


			// TextArea
			txaDistillateDetails.setEditable(false);
			txaDistillateDetails.setPrefHeight(lvwHeightCol0Top);
			txaDistillateDetails.setPrefWidth(areaWidth - lvwWithCol0);
			this.add(txaDistillateDetails,2,2,2,1);

			// Button panel
			HBox hBox = new HBox(20);
			int hboxBtnWidth = 250;
			String[] buttonNames = {"Create or update distillate", "Fill distillate into cask"};
			String [] abbreviations = {"create","Fill"};

			for (int i = 0; i < buttonNames.length; i++) {
				Button newBtn = new Button(buttonNames[i]);
				hBox.getChildren().add(newBtn);
				newBtn.setUserData(abbreviations[i]);
				newBtn.setPrefWidth(hboxBtnWidth);
				newBtn.setOnAction(actionEvent -> buttionAction(newBtn));
			}

			hBox.setPrefWidth(lvwWithCol0);
			hBox.setAlignment(Pos.CENTER);
			this.add(hBox,0,3,2,1);

			updateList();
		}

		private void buttionAction(Button button){

		}

		private void updateList(){
			List<Item> distilates = new ArrayList<>(Production.getDistillates());
			lvwDistillates.getItems().setAll(distilates);

			// We need to show specified text in the list aka different from toSting.
			lvwDistillates.setCellFactory(lv -> new ListCell<>() {
				@Override
				protected void updateItem(Item item, boolean empty) {
					super.updateItem(item, empty);

					if (empty || item == null) {
						setText(null); // Hand empty cells
					} else {
						// Add new info text.
						setText(item.getListInfo());
					}
				}
			});
		}
	}

	private class Casks extends GridPane{
		private final ListView<Item> lvwCasks = new ListView<>();
		private final ListView<Item> txaCaskFillings = new ListView<>();

		public Casks(ProductionArea pa){
			// Generel settings
			this.setBorder(border);
			this.setPadding(padding);
			this.setHgap(hgap);
			this.setVgap(vgap);
			this.setGridLinesVisible(gridLines);
			double areaWidth = screenBounds.getWidth() - 300 * 0.9;
			double areaHeight = screenBounds.getHeight() * 0.9;

			// Subtitel
			Label lblSubTitel = new Label("Casks");
			lblSubTitel.setId("LabelSubtitle");
			GridPane.setHalignment(lblSubTitel,HPos.CENTER);
			lblSubTitel.setPrefWidth(areaWidth);
			lblSubTitel.setAlignment(Pos.CENTER);
			this.add(lblSubTitel, 0, 0,4,1);

			// Labels
			Label lblList = new Label("Avaliable Casks");
			this.add(lblList, 0, 1);
			Label lblDetails = new Label("Fillings in selected cask");
			this.add(lblDetails, 2, 1);

			// Lists
			double lvwWithCol0 = areaWidth * 0.6;
			double lvwHeightCol0Top = areaHeight * 0.3;

			lvwCasks.setEditable(false);
			lvwCasks.setPrefWidth(lvwWithCol0);
			lvwCasks.setPrefHeight(lvwHeightCol0Top);
			this.add(lvwCasks,0,2,2,1);

			txaCaskFillings.setEditable(false);
			txaCaskFillings.setPrefHeight(lvwHeightCol0Top);
			txaCaskFillings.setPrefWidth(areaWidth - lvwWithCol0);
			this.add(txaCaskFillings,2,2,2,1);

			// Button panel
			HBox hBox = new HBox(20);
			int hboxBtnWidth = 250;
			String[] buttonNames = {"Update cask details", "Cask to cask transfer"};
			String [] abbreviations = {"update","transfer"};

			for (int i = 0; i < buttonNames.length; i++) {
				Button newBtn = new Button(buttonNames[i]);
				hBox.getChildren().add(newBtn);
				newBtn.setUserData(abbreviations[i]);
				newBtn.setPrefWidth(hboxBtnWidth);
				newBtn.setOnAction(actionEvent -> buttionAction(newBtn));
			}

			hBox.setPrefWidth(lvwWithCol0);
			hBox.setAlignment(Pos.CENTER);
			this.add(hBox,0,3,2,1);

			// Close button
			Button btnClose = new Button("Close");
			GridPane.setHalignment(btnClose,HPos.RIGHT);
			btnClose.setOnAction(actionEvent -> close());
			this.add(btnClose, 3, 3);

		}

		private void buttionAction(Button button){

		}

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
