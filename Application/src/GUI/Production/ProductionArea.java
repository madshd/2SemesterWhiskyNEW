package GUI.Production;

import Controllers.Production;
import GUI.Common.Common;
import GUI.Common.ConfirmationDialog;
import GUI.Common.ErrorWindow;
import GUI.Common.UpdateCaskCommonDialog;
import Interfaces.Item;
import Interfaces.OberverQuantitySubject;
import Interfaces.ObserverQuantityObserver;
import javafx.beans.value.ChangeListener;
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
import javafx.stage.*;
import Production.Distillate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import Warehousing.Cask;

import static Controllers.Warehousing.*;

public class ProductionArea {

	protected Rectangle2D screenBounds;
	private Stage stage;
	protected GridPane mainPane;
	private Scene scene;
	protected Distillates distillates;
	private Casks casks;
	protected FillDistillateIntoCask.DistillateElement fillDistillateElement;
	protected FillDistillateIntoCask.CasksElement fillCaskElement;
	protected FillDistillateIntoCask.InputElement fillInputElement;
	protected CreateAndUpdateDistillate.Basics distillateBasics;
	protected CreateAndUpdateDistillate.IngredientDetails ditillateIngredient;
	protected CreateAndUpdateDistillate.ProductionDetails distillateProductionDetails;
	protected CaskToCaskTransfer.CasksFrom transferCasksFrom;
	protected CaskToCaskTransfer.CasksTo transferCasksTo;
	protected CaskToCaskTransfer.InputElement transferInputElement;

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
		fillDistillateElement = new FillDistillateIntoCask.DistillateElement(this);
		fillCaskElement = new FillDistillateIntoCask.CasksElement(this);
		fillInputElement = new FillDistillateIntoCask.InputElement(this);
		distillateBasics = new CreateAndUpdateDistillate.Basics(this);
		ditillateIngredient = new CreateAndUpdateDistillate.IngredientDetails(this);
		distillateProductionDetails = new CreateAndUpdateDistillate.ProductionDetails(this);
		transferCasksFrom = new CaskToCaskTransfer.CasksFrom(this);
		transferCasksTo = new CaskToCaskTransfer.CasksTo(this);
		transferInputElement = new CaskToCaskTransfer.InputElement(this);
		initContent(mainPane);

		scene = new Scene(mainPane, screenBounds.getWidth() - 300, screenBounds.getHeight());
		scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

		stage.setResizable(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setX(300);
		stage.setY(0);
		stage.setScene(scene);
	}

	// Shared settings
	protected static int hgap = 20;
	protected static int vgap = 20;
	protected static Insets padding = new Insets(20);
	protected static boolean gridLines = false;
	protected static Border border = Common.getBorder(1, 0, 0, 0);

	public void initContent(GridPane gridPane) {
		Label headerLabel = new Label("Production Area");
		GridPane.setHalignment(headerLabel, HPos.CENTER);
		headerLabel.setId("LabelHeader");
		headerLabel.setPrefWidth(screenBounds.getWidth() - 300);
		headerLabel.setAlignment(Pos.CENTER);

		gridPane.add(headerLabel, 0, 0);
		gridPane.add(distillates, 0, 1);
		gridPane.add(casks, 0, 2);
	}

	protected class Distillates extends GridPane implements ObserverQuantityObserver {
		private final ListView<Item> lvwDistillates = new ListView<>();
		private final TextArea txaDistillateDetails = new TextArea();
		private Button btnCreateOrUpdate;

		public Distillates(ProductionArea pa) {
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
			GridPane.setHalignment(lblSubTitel, HPos.CENTER);
			lblSubTitel.setPrefWidth(areaWidth);
			lblSubTitel.setAlignment(Pos.CENTER);
			this.add(lblSubTitel, 0, 0, 4, 1);

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
			this.add(lvwDistillates, 0, 2, 2, 1);
			ChangeListener<Item> lvwDistillateListener = (ov, o, n) -> {
				updateDistillateDetails();
			};
			lvwDistillates.getSelectionModel().selectedItemProperty().addListener(lvwDistillateListener);

			// TextArea
			txaDistillateDetails.setEditable(false);
			txaDistillateDetails.setPrefHeight(lvwHeightCol0Top);
			txaDistillateDetails.setPrefWidth(areaWidth - lvwWithCol0);
			txaDistillateDetails.setWrapText(true);
			this.add(txaDistillateDetails, 2, 2, 2, 1);

			// Button panel
			HBox hBox = new HBox(20);
			int hboxBtnWidth = 250;
			String[] buttonNames = { "Create new distillate", "Fill distillate into cask" };
			String[] abbreviations = { "Create", "Fill" };

			for (int i = 0; i < buttonNames.length; i++) {
				Button newBtn = new Button(buttonNames[i]);
				hBox.getChildren().add(newBtn);
				newBtn.setUserData(abbreviations[i]);
				newBtn.setPrefWidth(hboxBtnWidth);
				newBtn.setOnAction(actionEvent -> buttionAction(newBtn));
			}

			hBox.setPrefWidth(lvwWithCol0);
			hBox.setAlignment(Pos.CENTER);
			btnCreateOrUpdate = (Button) hBox.getChildren().get(0);

			this.add(hBox, 0, 3, 2, 1);

			updateLists();
		}

		private void buttionAction(Button button) {
			switch ((String) button.getUserData()){
				case "Fill" -> {
					mainPane.getChildren().clear();
					openFillIntoCask();
				}
				case "Create" -> {
					mainPane.getChildren().clear();
					openCreateUpdateDistillate();
				}
			}

		}

		private void updateLists() {
			List<Item> distilates = new ArrayList<>(Production.getDistillates());
			distilates.forEach(d -> d.addObserver(this));
			lvwDistillates.getItems().setAll(distilates);

			Common.useSpecifiedListView(lvwDistillates);
		}

		private void updateDistillateDetails() {
			Distillate selectedDistillate = (Distillate) lvwDistillates.getSelectionModel().getSelectedItem();
			casks.updatelist(selectedDistillate);
			distillateBasics.updateBasics(selectedDistillate);
			ditillateIngredient.updateIngredients(selectedDistillate);
			distillateProductionDetails.updateProductionDetails(selectedDistillate);

			if (selectedDistillate != null) {
				btnCreateOrUpdate.setText("Update selected distillate");
				String infoText = String.format("""
						*****\t Distillate Created By \t*****
						Distiller: %s
						Story: %s

						*****\t Distillate Details \t*****
						New Make ID: %s
						Production Start Date: %s
						Productino End Date: %s
						Description: %s
						""",
						selectedDistillate.getDistiller().toString(),
						selectedDistillate.getDistiller().getStory(),
						selectedDistillate.getNewMakeID(), selectedDistillate.getStartDate().toString(),
						selectedDistillate.getEndDate().toString(),
						selectedDistillate.getDescription());
				txaDistillateDetails.setText(infoText);
			} else {
				btnCreateOrUpdate.setText("Create new distillate");
				txaDistillateDetails.clear();
			}
		}

		public void setWindowSetings(Distillate distillate){
			updateLists();
			lvwDistillates.getSelectionModel().select(distillate);
			updateDistillateDetails();
		}

		private void openFillIntoCask() {
			Label headerLabel = new Label("Fill distillate into cask");
			GridPane.setHalignment(headerLabel, HPos.CENTER);
			headerLabel.setId("LabelHeader");
			headerLabel.setPrefWidth(screenBounds.getWidth() - 300);
			headerLabel.setAlignment(Pos.CENTER);
			mainPane.add(headerLabel, 0, 0);
			mainPane.add(fillDistillateElement,0,1);
			mainPane.add(fillCaskElement,0,2);
			mainPane.add(fillInputElement,0,3);
		}

		private void openCreateUpdateDistillate(){
			Label headerLabel = new Label("Create or update distillate");
			GridPane.setHalignment(headerLabel, HPos.CENTER);
			headerLabel.setId("LabelHeader");
			headerLabel.setPrefWidth(screenBounds.getWidth() - 300);
			headerLabel.setAlignment(Pos.CENTER);
			Distillate selectedDistillate = (Distillate) lvwDistillates.getSelectionModel().getSelectedItem();
			distillateBasics.updateBasics(selectedDistillate);
			ditillateIngredient.updateIngredients(selectedDistillate);
			distillateProductionDetails.updateProductionDetails(selectedDistillate);

			mainPane.add(headerLabel, 0, 0);
			mainPane.add(distillateBasics,0,1);
			mainPane.add(ditillateIngredient,0,2);
			mainPane.add(distillateProductionDetails,0,3);
		}

		@Override
		public void update(OberverQuantitySubject o) {
			updateLists();
			updateDistillateDetails();
		}
	}

	private class Casks extends GridPane implements ObserverQuantityObserver {
		private final ListView<Item> lvwCasks = new ListView<>();
		private final TextArea txaCaskDetails = new TextArea();
		private final UpdateCaskCommonDialog updateCaskCommonDialog = new UpdateCaskCommonDialog(null);
		private final ErrorWindow errorWindow = new ErrorWindow();
		private final Stage updateCaskStage = new Stage();

		public Casks(ProductionArea pa) {
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
			GridPane.setHalignment(lblSubTitel, HPos.CENTER);
			lblSubTitel.setPrefWidth(areaWidth);
			lblSubTitel.setAlignment(Pos.CENTER);
			this.add(lblSubTitel, 0, 0, 4, 1);

			// Labels
			Label lblList = new Label("Avaliable Casks");
			this.add(lblList, 0, 1);
			Label lblDetails = new Label("Cask detials");
			this.add(lblDetails, 2, 1);

			// Lists
			double lvwWithCol0 = areaWidth * 0.6;
			double lvwHeightCol0Top = areaHeight * 0.45;

			lvwCasks.setEditable(false);
			lvwCasks.setPrefWidth(lvwWithCol0);
			lvwCasks.setPrefHeight(lvwHeightCol0Top);
			this.add(lvwCasks, 0, 2, 2, 1);
			ChangeListener<Item> lvwCasksListener = (ov, o, n) -> {
				updateFillingList();
			};
			lvwCasks.getSelectionModel().selectedItemProperty().addListener(lvwCasksListener);

			txaCaskDetails.setEditable(false);
			txaCaskDetails.setPrefHeight(lvwHeightCol0Top);
			txaCaskDetails.setPrefWidth(areaWidth - lvwWithCol0);
			txaCaskDetails.setWrapText(true);
			this.add(txaCaskDetails, 2, 2, 2, 1);

			// Button panel
			HBox hBox = new HBox(20);
			int hboxBtnWidth = 250;
			String[] buttonNames = { "Update cask details", "Cask to cask transfer" };
			String[] abbreviations = { "update", "transfer" };

			for (int i = 0; i < buttonNames.length; i++) {
				Button newBtn = new Button(buttonNames[i]);
				hBox.getChildren().add(newBtn);
				newBtn.setUserData(abbreviations[i]);
				newBtn.setPrefWidth(hboxBtnWidth);
				newBtn.setOnAction(actionEvent -> buttionAction(newBtn));
			}

			hBox.setPrefWidth(lvwWithCol0);
			hBox.setAlignment(Pos.CENTER);
			this.add(hBox, 0, 3, 2, 1);

			// Close button
			Button btnClose = new Button("Close");
			GridPane.setHalignment(btnClose, HPos.RIGHT);
			btnClose.setOnAction(actionEvent -> close());
			this.add(btnClose, 3, 3);

			// Prepare update cask dialogue
			updateCaskStage.initModality(Modality.APPLICATION_MODAL);


		}

		private void buttionAction(Button button) {
			switch ((String) button.getUserData()){
				case "transfer" -> {
					mainPane.getChildren().clear();
					openCaskToCaskTransfer();
				}
				case "update" -> openUpdateCask();
			}
		}

		private void openCaskToCaskTransfer() {
			Label headerLabel = new Label("Cask To Cask Transfer");
			GridPane.setHalignment(headerLabel, HPos.CENTER);
			headerLabel.setId("LabelHeader");
			headerLabel.setPrefWidth(screenBounds.getWidth() - 300);
			headerLabel.setAlignment(Pos.CENTER);
			mainPane.add(headerLabel, 0, 0);
			mainPane.add(transferCasksFrom,0,1);
			mainPane.add(transferCasksTo,0,2);
			mainPane.add(transferInputElement,0,3);
		}

		private void openUpdateCask(){
			Item item = lvwCasks.getSelectionModel().getSelectedItem();

			if (item != null){
				Cask cask = (Cask) item;

				updateCaskCommonDialog.setCask(cask);
                try {
                    updateCaskCommonDialog.start(updateCaskStage);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to open 'Update cask details' window");
                }
            }else {
				errorWindow.showError("Please select a cask.");
			}
		}

		public void updatelist(Distillate distillate) {
			if (distillate != null) {
				List<Item> casks = new ArrayList<>(getCasksMinQuantity(0.0));
				lvwCasks.getItems().setAll(casks);

				casks.forEach(cask -> {
					cask.addObserver(this);
				});

				Common.useSpecifiedListView(lvwCasks);
			} else {
				lvwCasks.getItems().clear();
			}
		}

		private void updateFillingList() {
			Cask selectedCask = (Cask) lvwCasks.getSelectionModel().getSelectedItem();

			if (selectedCask != null) {
				txaCaskDetails.setText(selectedCask.getDetails());
			} else {
				txaCaskDetails.clear();
			}
		}

		@Override
		public void update(OberverQuantitySubject o) {
			updatelist(null);
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
