package GUI.Production;

import BatchArea.TasteProfile;
import Controllers.BatchArea;
import Controllers.Production;
import GUI.Common.Common;
import GUI.Common.ErrorWindow;
import Interfaces.Filling;
import Interfaces.Item;
import Interfaces.OberverQuantitySubject;
import Interfaces.ObserverQuantityObserver;
import Production.Distillate;
import Warehousing.Cask;
import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static Controllers.Warehousing.getCasksMinQuantity;

public abstract class FillDistillateIntoCask {

    private static void applyCommonSettings(GridPane pane, ProductionArea pa) {
        pane.setBorder(pa.border);
        pane.setPadding(pa.padding);
        pane.setHgap(pa.hgap);
        pane.setVgap(pa.vgap);
        pane.setGridLinesVisible(pa.gridLines);
    }

    public static class DistillateElement extends GridPane implements ObserverQuantityObserver{
        private final ListView<Item> lvwDistillates = new ListView<>();
        private final TextArea txaDistillateDetails = new TextArea();
        protected Distillate selectedDistillate;

        public DistillateElement(ProductionArea pa){
            // Generel settings
            applyCommonSettings(this, pa);
            double areaWidth = pa.screenBounds.getWidth() - 300 * 0.9;
            double areaHeight = pa.screenBounds.getHeight() * 0.9;

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
                updateDistillateDetails(pa);
            };
            lvwDistillates.getSelectionModel().selectedItemProperty().addListener(lvwDistillateListener);

            // TextArea
            txaDistillateDetails.setEditable(false);
            txaDistillateDetails.setPrefHeight(lvwHeightCol0Top);
            txaDistillateDetails.setPrefWidth(areaWidth - lvwWithCol0);
            txaDistillateDetails.setMouseTransparent(true);
            txaDistillateDetails.setWrapText(true);
            this.add(txaDistillateDetails, 2, 2, 2, 1);

            updateLists();
        }

        private void updateLists() {
            List<Item> distilates = new ArrayList<>(Production.getDistillates());

            distilates.forEach(item -> {
                Distillate d = (Distillate) item;
                d.addObserver(this);
            });

            lvwDistillates.getItems().setAll(distilates);

            Common.useSpecifiedListView(lvwDistillates);

        }

        private void updateDistillateDetails(ProductionArea pa) {
            selectedDistillate = (Distillate) lvwDistillates.getSelectionModel().getSelectedItem();
            pa.fillCaskElement.updatelist(pa,selectedDistillate);
            if (selectedDistillate != null) {
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
                txaDistillateDetails.clear();
            }
        }

        @Override
        public void update(OberverQuantitySubject o) {
            updateLists();
        }
    }

    public static class CasksElement extends GridPane{
        private  final ListView<Item> lvwCasks = new ListView<>();
        private  final ListView<Filling > lvwCaskFillings = new ListView<>();

        public CasksElement(ProductionArea pa){
            // Generel settings
            applyCommonSettings(this, pa);
            double areaWidth = pa.screenBounds.getWidth() - 300 * 0.9;
            double areaHeight = pa.screenBounds.getHeight() * 0.9;

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
            Label lblDetails = new Label("Fillings in selected cask");
            this.add(lblDetails, 2, 1);

            // Lists
            double lvwWithCol0 = areaWidth * 0.6;
            double lvwHeightCol0Top = areaHeight * 0.3;

            lvwCasks.setEditable(false);
            lvwCasks.setPrefWidth(lvwWithCol0);
            lvwCasks.setPrefHeight(lvwHeightCol0Top);
            this.add(lvwCasks, 0, 2, 2, 1);
            ChangeListener<Item> lvwCasksListener = (ov, o, n) -> {
                updateFillingList(pa);
            };
            lvwCasks.getSelectionModel().selectedItemProperty().addListener(lvwCasksListener);

            lvwCaskFillings.setEditable(false);
            lvwCaskFillings.setPrefHeight(lvwHeightCol0Top);
            lvwCaskFillings.setPrefWidth(areaWidth - lvwWithCol0);
            lvwCaskFillings.setMouseTransparent(true);
            this.add(lvwCaskFillings, 2, 2, 2, 1);

        }

        public void updatelist(ProductionArea pa, Distillate distillate) {
            pa.fillInputElement.calculateFilling(distillate,(Cask) lvwCasks.getSelectionModel().getSelectedItem());
            if (distillate != null) {
                List<Item> casks = new ArrayList<>(getCasksMinQuantity(0.0));
                lvwCasks.getItems().setAll(casks);

                Common.useSpecifiedListView(lvwCasks);
            } else {
                lvwCasks.getItems().clear();
            }
        }

        private void updateFillingList(ProductionArea pa) {
            Cask selectedCask = (Cask) lvwCasks.getSelectionModel().getSelectedItem();
            pa.fillInputElement.calculateFilling(pa.fillDistillateElement.selectedDistillate,selectedCask);

            if (selectedCask != null) {
                lvwCaskFillings.getItems().setAll(selectedCask.getFillingStack());
            } else {
                lvwCaskFillings.getItems().clear();
            }
        }
    }

    public static class InputElement extends GridPane{
        private final TextField txfInputLiters = new TextField();
        private final DatePicker dpDateForFilling = new DatePicker();
        private final Label lblInfoMaxLiters = new Label();
        private Button btnAddFillment = new Button("Add filling to cask");
        private double maxLittersToFill = 0;
        private Cask selectedCask;
        private Distillate selectedDistillate;
        private ErrorWindow errorWindow = new ErrorWindow();
        private final ComboBox<TasteProfile> cmbTasteProfile = new ComboBox<>();

        @SuppressWarnings("unchecked")
		public InputElement(ProductionArea pa){
            // Generel settings
            applyCommonSettings(this, pa);
            double areaWidth = pa.screenBounds.getWidth() - 300 * 0.9;
            double areaHeight = pa.screenBounds.getHeight() * 0.9;

            // Subtitel
            Label lblSubTitel = new Label("Input");
            lblSubTitel.setId("LabelSubtitle");
            GridPane.setHalignment(lblSubTitel, HPos.CENTER);
            lblSubTitel.setPrefWidth(areaWidth);
            lblSubTitel.setAlignment(Pos.CENTER);
            this.add(lblSubTitel, 0, 0, 4, 1);
            
            // Labels
            Label lblEnterLiters = new Label("Enter liters");
            this.add(lblEnterLiters, 0, 1);
            Label lblDateForFilling = new Label("Date for filling");
            this.add(lblDateForFilling, 0, 2);

            this.add(lblInfoMaxLiters,2,1);

            this.add(btnAddFillment, 0, 3,2,1);
            btnAddFillment.setOnAction(actionEvent -> fillDistillateIntoCask(pa));

            Button btnClose = new Button("Close");
            GridPane.setHalignment(btnClose, HPos.RIGHT);
            btnClose.setOnAction(actionEvent -> close(pa));
            this.add(btnClose, 3, 3);

            //Input fields
            this.add(txfInputLiters,1,1);
            dpDateForFilling.setConverter(Common.datePickerFormat(dpDateForFilling));
            this.add(dpDateForFilling,1,2);

            cmbTasteProfile.setPromptText("Select taste profile");
            this.add(cmbTasteProfile,2,2);

            calculateFilling(null,null);

        }

        private void fillDistillateIntoCask(ProductionArea pa){
            try {
                Double.parseDouble(txfInputLiters.getText().trim());
            }catch (Exception e){
                errorWindow.showError("Please insert a valid number");
                return;
            }
            LocalDate date = dpDateForFilling.getValue();

            if (date == null){
                errorWindow.showError("Please select a date");
                return;
            }

            double liters = Double.parseDouble(txfInputLiters.getText().trim());

            if (liters <= 0){
                errorWindow.showError("Liters must be greater than zero.");
                return;
            }

            if (cmbTasteProfile.getSelectionModel().getSelectedItem() == null){
                errorWindow.showError("Please select a taste profile.");
                return;
            }

            try {
                Controllers.Production.fillDistillateIntoCask(selectedDistillate,selectedCask,liters,date);
                selectedCask.setTasteProfile(cmbTasteProfile.getValue());
                pa.fillCaskElement.updateFillingList(pa);
            }catch (IllegalArgumentException e){
                errorWindow.showError(e.getMessage());
            }

        }

        protected void calculateFilling(Distillate distillate, Cask cask){
            if (distillate != null && cask != null){
                selectedCask = cask;
                selectedDistillate = distillate;
                txfInputLiters.setDisable(false);
                dpDateForFilling.setDisable(false);
                btnAddFillment.setDisable(false);
                cmbTasteProfile.setDisable(false);
                double remainingDistillate = distillate.getRemainingQuantity();
                double remainingCaskQuantity = cask.getRemainingQuantity();

                cmbTasteProfile.getItems().setAll(BatchArea.getAllTasteProfiles());
                cmbTasteProfile.getSelectionModel().select(selectedCask.getTasteProfile());

                if (remainingDistillate > remainingCaskQuantity){
                    maxLittersToFill = remainingCaskQuantity;
                    lblInfoMaxLiters.setText(String.format("Max %,-4.2f liters", maxLittersToFill));
                }else {
                    maxLittersToFill = remainingDistillate;
                    lblInfoMaxLiters.setText(String.format("Max %,-4.2f liters", maxLittersToFill));
                }
            }else {
                txfInputLiters.setDisable(true);
                dpDateForFilling.setDisable(true);
                btnAddFillment.setDisable(true);
                lblInfoMaxLiters.setText("Please selecet a distillate and a cask.");
                cmbTasteProfile.setDisable(true);
            }
        }

        private void close(ProductionArea pa){
            pa.mainPane.getChildren().clear();
            pa.initContent(pa.mainPane);
        }
    }
}
