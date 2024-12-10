package GUI.Production;

import Controllers.Production;
import GUI.Common.Common;
import GUI.Common.ErrorWindow;
import Interfaces.Filling;
import Interfaces.Item;
import Interfaces.OberverQuantitySubject;
import Interfaces.ObserverQuantityObserver;
import Warehousing.Cask;
import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static Controllers.Warehousing.getCasksMinQuantity;

public abstract class CaskToCaskTransfer {

    private static void applyCommonSettings(GridPane pane, ProductionArea pa) {
        pane.setBorder(pa.border);
        pane.setPadding(pa.padding);
        pane.setHgap(pa.hgap);
        pane.setVgap(pa.vgap);
        pane.setGridLinesVisible(pa.gridLines);
    }



    public static class CasksFrom extends GridPane implements ObserverQuantityObserver {
        private final ListView<Item> lvwCasks = new ListView<>();
        private final ListView<Filling > lvwCaskFillings = new ListView();
        private final ProductionArea pa;


        public CasksFrom(ProductionArea pa){
            // Generel settings
            applyCommonSettings(this, pa);
            double areaWidth = pa.screenBounds.getWidth() - 300 * 0.9;
            double areaHeight = pa.screenBounds.getHeight() * 0.9;
            this.pa = pa;

            // Subtitel
            Label lblSubTitel = new Label("Filling from cask");
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
            this.add(lvwCaskFillings, 2, 2, 2, 1);

            updatelist(pa);
        }

        public void updatelist(ProductionArea pa) {
            List<Item> casks = new ArrayList<>(getCasksMinQuantity(null));

            casks.forEach(cask -> cask.addObserver(this));

            lvwCasks.getItems().setAll(casks);

            Common.useSpecifiedListView(lvwCasks);
        }

        private void updateFillingList(ProductionArea pa) {
            Cask selectedCask = (Cask) lvwCasks.getSelectionModel().getSelectedItem();
            pa.transferCasksTo.updatelist(pa,selectedCask);

            if (selectedCask != null) {
                lvwCaskFillings.getItems().setAll(selectedCask.getFillingStack());
            } else {
                lvwCaskFillings.getItems().clear();
            }
        }

        @Override
        public void update(OberverQuantitySubject o) {
            updatelist(pa);
            updateFillingList(pa);
        }
    }

    public static class CasksTo extends GridPane{
        private final ListView<Item> lvwCasks = new ListView<>();
        private final ListView<Filling > lvwCaskFillings = new ListView();
        private Cask caskFrom;

        public CasksTo(ProductionArea pa){
            // Generel settings
            applyCommonSettings(this, pa);
            double areaWidth = pa.screenBounds.getWidth() - 300 * 0.9;
            double areaHeight = pa.screenBounds.getHeight() * 0.9;

            // Subtitel
            Label lblSubTitel = new Label("Filling into cask");
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
            this.add(lvwCaskFillings, 2, 2, 2, 1);

        }

        public void updatelist(ProductionArea pa, Item cask) {
            caskFrom = (Cask) cask;
            if (cask != null) {
                List<Item> casks = new ArrayList<>(getCasksMinQuantity(0.0));
                casks.remove(cask);

                Iterator<Item> itemIterator = casks.iterator();

                while (itemIterator.hasNext()){
                    Cask c = (Cask) itemIterator.next();
                    if (c.getTotalReservedAmount() > 0){
                        itemIterator.remove();
                    }
                }

                lvwCasks.getItems().setAll(casks);

                Common.useSpecifiedListView(lvwCasks);
            } else {
                lvwCasks.getItems().clear();
            }
        }

        private void updateFillingList(ProductionArea pa) {
            Cask selectedCask = (Cask) lvwCasks.getSelectionModel().getSelectedItem();
            pa.transferInputElement.calculateFilling(caskFrom,selectedCask);

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
        private final Label lblAttentionReserved = new Label("");
        private Button btnAddFillment = new Button("Add filling to cask");
        private double maxLittersToFill = 0;
        private Cask selectedCaskFrom;
        private Cask selectedCaskTo;
        private ErrorWindow errorWindow = new ErrorWindow();

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
            this.add(lblEnterLiters, 0, 2);
            Label lblDateForFilling = new Label("Date for filling");
            this.add(lblDateForFilling, 0, 3);

            this.add(lblInfoMaxLiters,2,2);
            GridPane.setHalignment(lblAttentionReserved, HPos.LEFT);
            lblAttentionReserved.setAlignment(Pos.BASELINE_LEFT);
            this.add(lblAttentionReserved,0,1,2,1);
            
            // Buttons
            this.add(btnAddFillment, 0, 4,2,1);
            btnAddFillment.setOnAction(actionEvent -> fillDistillateIntoCask(pa));

            Button btnClose = new Button("Close");
            GridPane.setHalignment(btnClose, HPos.RIGHT);
            btnClose.setOnAction(actionEvent -> close(pa));
            this.add(btnClose, 3, 4);

            //Input fields
            this.add(txfInputLiters,1,2);
            dpDateForFilling.setConverter(Common.datePickerFormat(dpDateForFilling));
            this.add(dpDateForFilling,1,3);

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

            try {
                Production.caskToCaskTransfer(selectedCaskFrom,selectedCaskTo,liters,date);
            }catch (IllegalArgumentException e){
                errorWindow.showError(e.getMessage());
            }

        }

        protected void calculateFilling(Cask caskFrom, Cask caskTo){
            if (caskFrom != null && caskTo != null){
                selectedCaskFrom = caskFrom;
                selectedCaskTo = caskTo;
                txfInputLiters.setDisable(false);
                dpDateForFilling.setDisable(false);
                btnAddFillment.setDisable(false);
                double remainingCaskFrom = caskFrom.getLegalQuantity();
                double remainingCaskTo = caskTo.getLegalQuantity();


                lblAttentionReserved.setText(String.format("""
                         Quantity reservation: Cask: %s reserved quantity: %,.2f
                        """,caskFrom.getName(),caskFrom.getTotalReservedAmount()));


                if (remainingCaskFrom > remainingCaskTo){
                    maxLittersToFill = remainingCaskTo;
                    lblInfoMaxLiters.setText(String.format("Max %,-4.2f liters", maxLittersToFill));
                }else {
                    maxLittersToFill = remainingCaskFrom;
                    lblInfoMaxLiters.setText(String.format("Max %,-4.2f liters", maxLittersToFill));
                }
            }else {
                txfInputLiters.setDisable(true);
                dpDateForFilling.setDisable(true);
                btnAddFillment.setDisable(true);
                lblInfoMaxLiters.setText("Please selecet a 'From Cask' and a 'Into Cask.");
                lblAttentionReserved.setText("Quantity reservation:");
            }
        }

        private void close(ProductionArea pa){
            pa.mainPane.getChildren().clear();
            pa.initContent(pa.mainPane);
        }
    }
}
