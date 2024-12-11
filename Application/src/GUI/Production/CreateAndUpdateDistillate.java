package GUI.Production;

import Controllers.Production;
import Controllers.Warehousing;
import Enumerations.Unit;
import GUI.Common.Common;
import GUI.Common.ErrorWindow;
import Interfaces.Item;
import Production.Distiller;
import Production.AlcoholPercentage;
import Production.StoryLine;
import Production.ProductCutInformation;
import Warehousing.Ingredient;
import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import Production.Distillate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public abstract class CreateAndUpdateDistillate {
    private static void applyCommonSettings(GridPane pane, ProductionArea pa) {
        pane.setBorder(pa.border);
        pane.setPadding(pa.padding);
        pane.setHgap(pa.hgap);
        pane.setVgap(pa.vgap);
        pane.setGridLinesVisible(pa.gridLines);
    }

    public static class Basics extends GridPane {

        private Label lblNewMakeID = new Label();
        private final TextField txfName = new TextField();
        private final TextField txfQuantity = new TextField();
        private final ComboBox<Distiller> cmbDistiller = new ComboBox<>();
        private final DatePicker dpStartDate = new DatePicker();
        private final DatePicker dpEndDate = new DatePicker();
        private final TextArea txaDescription = new TextArea();
        private final ErrorWindow errorWindow = new ErrorWindow();
        private Distillate distillate;

        @SuppressWarnings("unchecked")
		public Basics(ProductionArea pa){
            // Generel settings
            applyCommonSettings(this, pa);
            double areaWidth = pa.screenBounds.getWidth() - 300 * 0.9;
            double areaHeight = pa.screenBounds.getHeight() * 0.9;

            // Set columns width
            ColumnConstraints col0 = new ColumnConstraints();
            col0.setPrefWidth(areaWidth * 0.15);
            ColumnConstraints col01 = new ColumnConstraints();
            col01.setPrefWidth(areaWidth * 0.15);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPrefWidth(areaWidth * 0.4);
            ColumnConstraints col3 = new ColumnConstraints();
            col3.setPrefWidth(areaWidth * 0.3);
            this.getColumnConstraints().setAll(col01,col01,col2,col3);

            // Subtitel
            Label lblSubTitel = new Label("Basics");
            lblSubTitel.setId("LabelSubtitle");
            GridPane.setHalignment(lblSubTitel, HPos.CENTER);
            lblSubTitel.setPrefWidth(areaWidth);
            lblSubTitel.setAlignment(Pos.CENTER);
            this.add(lblSubTitel, 0, 0, 4, 1);

            // Label
            this.add(lblNewMakeID,0,1,2,1);

            // Fields
            txfName.setPromptText("Name");
            this.add(txfName,0,2);
            cmbDistiller.setPromptText("Distiller");
            this.add(cmbDistiller,0,3);
            txfQuantity.setPromptText("Quantity");
            this.add(txfQuantity,0,4);

            // Date
            dpStartDate.setConverter(Common.datePickerFormat(dpStartDate));
            dpStartDate.setPromptText("Production start date");
            dpStartDate.setMaxWidth(areaWidth);
            this.add(dpStartDate,1,2);
            dpEndDate.setConverter(Common.datePickerFormat(dpEndDate));
            dpEndDate.setPromptText("Production end date");
            dpEndDate.setMaxWidth(areaWidth);
            this.add(dpEndDate,1,3);

            // Text area
            txaDescription.setPromptText("" +
                    "Basic description behind the distillate, description should enforce the end Whisky story.");
            txaDescription.setPrefHeight(areaHeight * 0.1);
            txaDescription.setPrefWidth(200);
            txaDescription.setWrapText(true);
            GridPane.setHalignment(txaDescription,HPos.RIGHT);
            this.add(txaDescription,3,1,1,3);

            // Button
            Button btnSave = new Button("Save");
            btnSave.setMaxWidth(areaWidth);
            btnSave.setOnAction(actionEvent -> storeChange(pa,distillate));
            this.add(btnSave, 1, 4);

            updateBasics(null);
        }

        public void updateBasics(Distillate distillate){
            cmbDistiller.getItems().setAll(Production.getDistillers());
            this.distillate = distillate;

            if (distillate != null){
                lblNewMakeID.setText("New Make ID: " + distillate.getNewMakeID());
                txfName.setText(distillate.getName());
                cmbDistiller.setValue(distillate.getDistiller());
                txfQuantity.setText(Double.toString(distillate.getQuantity()));
                dpStartDate.setValue(distillate.getStartDate());
                dpEndDate.setValue(distillate.getEndDate());
                txaDescription.setText(distillate.getDescription().trim());
            }else {
                lblNewMakeID.setText("Please input basic info and click save.");
                txfName.clear();
                cmbDistiller.getItems().clear();
                cmbDistiller.getItems().setAll(Production.getDistillers());
                txfQuantity.clear();
                dpStartDate.setValue(null);
                dpEndDate.setValue(null);
                txaDescription.clear();
            }
        }

        protected boolean storeChange(ProductionArea pa, Distillate distillate){
            Distillate tmp = this.distillate;

            if (tmp != null){
                try {
                    Double.parseDouble(txfQuantity.getText().trim());
                }catch (NumberFormatException e){
                    errorWindow.showError("Value for quantity is not valid");
                    return false;
                }

                if (dpStartDate == null || dpEndDate == null){
                    errorWindow.showError("Dates are not valid.");
                    return false;
                }

                double quantity = Double.parseDouble(txfQuantity.getText().trim());
                if (quantity < distillate.getQuantityStatus()){
                    errorWindow.showError("Quantity must be higher or equal to what has been filled into cask(s)");
                    return false;
                }

                String name = txfName.getText().trim();
                Distiller distiller = cmbDistiller.getValue();
                String description = txaDescription.getText().trim();

                Controllers.Production.distillateUpdateBasicInfo(tmp,name,distiller,quantity,
                        dpStartDate.getValue(),dpEndDate.getValue(),description);

                pa.distillates.setWindowSetings(tmp);
                return true;
            }else {
                try {
                    Double.parseDouble(txfQuantity.getText().trim());
                }catch (NumberFormatException e){
                    errorWindow.showError("Value for quantity is not valid");
                    return false;
                }

                if (dpStartDate == null || dpEndDate == null){
                    errorWindow.showError("Dates are not valid.");
                    return false;
                }

                double quantity = Double.parseDouble(txfQuantity.getText().trim());

                if (quantity <= 0){
                    errorWindow.showError("Quantity must be greater than zero.");
                    return false;
                }

                String name = txfName.getText().trim();
                Distiller distiller = cmbDistiller.getValue();
                String description = txaDescription.getText().trim();

                if (distiller == null){
                    errorWindow.showError("Please select a distiller.");
                    return false;
                }

                Distillate newDistillate = Production.createDistillate(name,dpStartDate.getValue(),dpEndDate.getValue(),quantity,distiller, Unit.LITERS);
                Production.addDescriotionToDistillate(newDistillate,description);
                pa.distillates.setWindowSetings(newDistillate);
                return true;
            }

        }
    }

    public static class IngredientDetails extends GridPane {

        private final DatePicker dpfillDate = new DatePicker();
        private final ComboBox<Item> cmbIngredients = new ComboBox<>();
        private final TextField txfQuantity = new TextField();
        private final ListView<Item> lvwIngrediants = new ListView<>();
        private final TextArea txaIngrediantDetails = new TextArea();
        private final TextArea txaNewIngrediantDetail = new TextArea();
        private Distillate distillate;
        private final ErrorWindow errorWindow = new ErrorWindow();

        @SuppressWarnings("unchecked")
		public IngredientDetails(ProductionArea pa){
            // Generel settings
            applyCommonSettings(this, pa);
            double areaWidth = pa.screenBounds.getWidth() - 300 * 0.9;
            double areaHeight = pa.screenBounds.getHeight() * 0.9;

            // Set columns width
            ColumnConstraints col0 = new ColumnConstraints();
            col0.setPrefWidth(areaWidth * 0.15);
            ColumnConstraints col01 = new ColumnConstraints();
            col01.setPrefWidth(areaWidth * 0.15);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPrefWidth(areaWidth * 0.4);
            ColumnConstraints col3 = new ColumnConstraints();
            col3.setPrefWidth(areaWidth * 0.3);
            this.getColumnConstraints().setAll(col01,col01,col2,col3);

            // Subtitel
            Label lblSubTitel = new Label("Ingredients");
            lblSubTitel.setId("LabelSubtitle");
            GridPane.setHalignment(lblSubTitel, HPos.CENTER);
            lblSubTitel.setPrefWidth(areaWidth);
            lblSubTitel.setAlignment(Pos.CENTER);
            this.add(lblSubTitel, 0, 0, 4, 1);

            // Fields
            dpfillDate.setConverter(Common.datePickerFormat(dpfillDate));
            dpfillDate.setPromptText("Date for filling");
            dpfillDate.setPrefWidth(areaWidth * 0.15);

            cmbIngredients.setPromptText("Ingredients");
            cmbIngredients.setPrefWidth(areaWidth * 0.15);
            cmbIngredients.setOnAction(actionEvent -> {
                if (cmbIngredients.getSelectionModel().getSelectedItem() != null){
                    txaNewIngrediantDetail.setText(
                            ((Ingredient) cmbIngredients.getSelectionModel().getSelectedItem()).getDescription());
                }
            });

            txfQuantity.setPromptText("Quantity");

            // Lists
            lvwIngrediants.setEditable(false);
            lvwIngrediants.setPrefHeight(areaHeight * 0.1);
            ChangeListener<Item> lvwIngrediantListener = (ov, o, n) -> {
                updateIngrediantDetails();
            };
            lvwIngrediants.getSelectionModel().selectedItemProperty().addListener(lvwIngrediantListener);
            this.add(lvwIngrediants,2,1);

            // Text area
            txaIngrediantDetails.setPromptText("Details on selected allready added ingredient");
            txaIngrediantDetails.setEditable(false);
            txaIngrediantDetails.setWrapText(true);
            txaIngrediantDetails.setMouseTransparent(true);
            this.add(txaIngrediantDetails,3,1);

            txaNewIngrediantDetail.setPromptText("Detaills on selected new ingredient.");
            txaNewIngrediantDetail.setEditable(false);
            txaNewIngrediantDetail.setWrapText(true);
            txaNewIngrediantDetail.setMouseTransparent(true);
            this.add(txaNewIngrediantDetail,1,1);

            VBox vBox = new VBox(20);
            double vBoxWidth = areaWidth * 0.15;
            vBox.getChildren().setAll(List.of(dpfillDate,cmbIngredients,txfQuantity));
            this.add(vBox,0,1);

            // Button panel
            HBox hBox = new HBox(20);
            int hboxBtnWidth = 250;
            String[] btnNames = {"Add registraion", "Remove selected"};
            String[] abbreivations = {"add","remove"};

            for (int i = 0; i < btnNames.length; i++) {
                Button newBtn = new Button(btnNames[i]);
                hBox.getChildren().add(newBtn);
                newBtn.setUserData(abbreivations[i]);
                newBtn.setPrefWidth(hboxBtnWidth);
                newBtn.setOnAction(actionEvent -> btnAction(pa,newBtn));
            }

            hBox.setAlignment(Pos.CENTER);
            this.add(hBox,2,2);

            updateIngredients(null);
        }

        public void updateIngredients(Distillate distillate){
            cmbIngredients.getItems().setAll(Warehousing.getAllAvailableIngredients());
            this.distillate = distillate;


            if (distillate != null){
                activateElements(true);
                lvwIngrediants.getItems().setAll(distillate.getIngredientsInDistillate());
                Common.useSpecifiedListView(lvwIngrediants);
            }else {
                activateElements(false);
            }
        }

        private void updateIngrediantDetails(){
            Item item = lvwIngrediants.getSelectionModel().getSelectedItem();

            if (item != null && distillate != null){
                txaIngrediantDetails.setText(distillate.getInfoOnIngredientUsed(((Ingredient) item)));
            }else {
                txaIngrediantDetails.clear();
            }
        }

        private void btnAction(ProductionArea pa, Button button){
            if (distillate == null){
                errorWindow.showError("Please input basic info and click save.");
                return;
            }
            switch ((String) button.getUserData()){
                case "add" -> {
                    addFillIngredient(pa);
                }
                case "remove" -> {
                    removeIngredientFromDistillate(pa);
                }
            }
        }

        private void addFillIngredient(ProductionArea pa){
            Distillate tmp = this.distillate;
            if (dpfillDate == null){
                errorWindow.showError("Please set a date");
                return;
            }

            try {
                Double.parseDouble(txfQuantity.getText().trim());
            }catch (NumberFormatException e){
                errorWindow.showError("Quantity is not set correctly.");
                return;
            }

            double quantity = Double.parseDouble(txfQuantity.getText().trim());
            if (quantity <= 0){
                errorWindow.showError("Quantity must be greater than zero.");
                return;
            }
            Ingredient ingredient = ((Ingredient) cmbIngredients.getSelectionModel().getSelectedItem());
            Production.addIngredientToDistillate(distillate,ingredient,quantity,dpfillDate.getValue());
            txaNewIngrediantDetail.clear();
            pa.distillates.setWindowSetings(tmp);
        }

        private void removeIngredientFromDistillate(ProductionArea pa){
            Distillate tmp = this.distillate;
            Ingredient ingredient = (Ingredient) lvwIngrediants.getSelectionModel().getSelectedItem();

            if (ingredient != null && tmp != null){
                Production.removeIngredientFromDistillate(tmp,ingredient);
                txaNewIngrediantDetail.clear();
                pa.distillates.setWindowSetings(tmp);
            }
        }

        private void activateElements(boolean active){
                dpfillDate.setDisable(!active);
                cmbIngredients.setDisable(!active);
                txfQuantity.setDisable(!active);
                txaNewIngrediantDetail.setDisable(!active);
                lvwIngrediants.setDisable(!active);
                txaIngrediantDetails.setDisable(!active);
        }
    }

    public static class ProductionDetails extends GridPane {

        private final DatePicker dpDate = new DatePicker();
        private final TextField txfValue = new TextField();
        private final TextArea txaText = new TextArea();
        private final TabPane tabPane = new TabPane();
        private final ListView<AlcoholPercentage> lvwAlchol = new ListView<>();
        private final ListView<ProductCutInformation> lvwCutInfo = new ListView<>();
        private final ListView<StoryLine> lvwStory = new ListView<>();
        private final Tab alcohol = new Tab("Alcohol %");
        private final Tab cutInfo = new Tab("Cut info");
        private final Tab story = new Tab("Story");
        private Distillate distillate;
        private final ErrorWindow errorWindow = new ErrorWindow();

        @SuppressWarnings("unchecked")
		public ProductionDetails(ProductionArea pa){
            // Generel settings
            applyCommonSettings(this, pa);
            double areaWidth = pa.screenBounds.getWidth() - 300 * 0.9;
            double areaHeight = pa.screenBounds.getHeight() * 0.9;

            // Set columns width
            ColumnConstraints col0 = new ColumnConstraints();
            col0.setPrefWidth(areaWidth * 0.15);
            ColumnConstraints col01 = new ColumnConstraints();
            col01.setPrefWidth(areaWidth * 0.15);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPrefWidth(areaWidth * 0.4);
            ColumnConstraints col3 = new ColumnConstraints();
            col3.setPrefWidth(areaWidth * 0.3);
            this.getColumnConstraints().setAll(col01,col01,col2,col3);

            // Subtitel
            Label lblSubTitel = new Label("Production details");
            lblSubTitel.setId("LabelSubtitle");
            GridPane.setHalignment(lblSubTitel, HPos.CENTER);
            lblSubTitel.setPrefWidth(areaWidth);
            lblSubTitel.setAlignment(Pos.CENTER);
            this.add(lblSubTitel, 0, 0, 4, 1);

            // Left panel
            VBox vBox = new VBox(20);

            dpDate.setConverter(Common.datePickerFormat(dpDate));
            dpDate.setPromptText("From date");
            dpDate.setPrefWidth(areaWidth * 0.15);

            txfValue.setPromptText("Percentage");
            txfValue.setPrefWidth(areaWidth * 0.15);
            txfValue.setMaxWidth(areaWidth * 0.135);

            txaText.setPromptText("Text input for story line og 'cut information'.");
            txaText.setPrefWidth(areaWidth * 0.15);
            txaText.setPrefHeight(areaHeight * 0.30);

            vBox.getChildren().setAll(dpDate,txfValue,txaText);
            this.add(vBox,0,1,2,1);

            // Tabpane
            tabPane.getTabs().addAll(alcohol,cutInfo,story);
            tabPane.setPrefHeight(areaHeight * 0.4);

            alcohol.setContent(lvwAlchol);
            alcohol.setClosable(false);
            alcohol.setId("1");
            alcohol.setOnSelectionChanged(event -> updateOnTabShift("1"));
            cutInfo.setContent(lvwCutInfo);
            cutInfo.setClosable(false);
            cutInfo.setId("2");
            cutInfo.setOnSelectionChanged(event -> updateOnTabShift("2"));
            story.setContent(lvwStory);
            story.setClosable(false);
            story.setId("3");
            story.setOnSelectionChanged(event -> updateOnTabShift("3"));
            tabPane.getSelectionModel().selectFirst();
            updateOnTabShift("1");

            this.add(tabPane,2,1,2,1);

            // Button panel
            HBox hBox = new HBox(20);
            int hboxBtnWidth = 250;
            String[] btnNames = {"Add registraion", "Remove selected"};
            String[] abbreivations = {"add","remove"};

            for (int i = 0; i < btnNames.length; i++) {
                Button newBtn = new Button(btnNames[i]);
                hBox.getChildren().add(newBtn);
                newBtn.setUserData(abbreivations[i]);
                newBtn.setPrefWidth(hboxBtnWidth);
                newBtn.setOnAction(actionEvent -> btnAction(pa, newBtn));
            }

            hBox.setAlignment(Pos.CENTER);
            this.add(hBox,2,2);

            HBox hBoxButtom = new HBox(20);
            int hboxButtomBtnWidth = 100;
            String[] btnButtomNames = {"Close"};
            String[] abbreivationsButtom = {"close"};

            for (int i = 0; i < btnButtomNames.length; i++) {
                Button newBtn = new Button(btnButtomNames[i]);
                hBoxButtom.getChildren().add(newBtn);
                newBtn.setUserData(abbreivationsButtom[i]);
                newBtn.setPrefWidth(hboxButtomBtnWidth);
                newBtn.setOnAction(actionEvent -> btnAction(pa,newBtn));
            }

            hBoxButtom.setAlignment(Pos.BOTTOM_RIGHT);
            hBoxButtom.setBorder(pa.border);
            hBoxButtom.setPadding(pa.padding);
            this.add(hBoxButtom,0,3,4,1);

        }

        public void updateProductionDetails(Distillate distillate){
            dpDate.setValue(null);
            txfValue.clear();
            txaText.clear();
            this.distillate = distillate;
            if (distillate != null){
                activateElements(true);
                lvwAlchol.getItems().setAll(distillate.getAlcoholPercentages());
                lvwCutInfo.getItems().setAll(distillate.getProductCutInformations());
                lvwStory.getItems().setAll(distillate.getStoryLines());
            }else {
                activateElements(false);
                lvwAlchol.getItems().clear();
                lvwCutInfo.getItems().clear();
                lvwStory.getItems().clear();
            }
        }

        private void activateElements(boolean active){
            dpDate.setDisable(!active);
            txfValue.setDisable(!active);
            txaText.setDisable(!active);
            tabPane.setDisable(!active);
            tabPane.getTabs().forEach(tab -> {
                if (tab.getContent() != null) {
                    tab.getContent().setDisable(!active);
                }
            });
        }

        private void btnAction(ProductionArea pa, Button button){
            switch ((String) button.getUserData()){
                case "close" -> close(pa);
                case "add" -> {
                    if (distillate == null){
                        errorWindow.showError("Please input basic info and click save.");
                        return;
                    }
                    addElemnt(pa);
                }
                case "remove" -> {
                    if (distillate == null){
                        errorWindow.showError("Please input basic info and click save.");
                        return;
                    }
                    removeElement(pa);
                }
            }
        }

        private void addElemnt(ProductionArea pa){
            String tabID = tabPane.getSelectionModel().getSelectedItem().getId();
            LocalDate date = dpDate.getValue();
            String value = txfValue.getText().trim();
            String text = txaText.getText().trim();
            Distillate tmp = this.distillate;

            if (distillate == null){
                errorWindow.showError("Please ensure basic info on distallate has been saved.");
                return;
            }

            if (date != null){
                switch (tabID){
                    case "1" -> {
                        try {
                            Double.parseDouble(value);
                        }catch (NumberFormatException e){
                            errorWindow.showError("Provied procentage is not a valid number.");
                            return;
                        }
                        double percentage = Double.parseDouble(value);
                        if (percentage <= 0){
                            errorWindow.showError("Percentage must be greater than zero.");
                            return;
                        }

                        Production.addAlcoholPercentage(tmp,percentage,date);
                        pa.distillates.setWindowSetings(tmp);
                    }
                    case "2" -> {
                        Production.addProductionCutInformation(tmp,text,date);
                        pa.distillates.setWindowSetings(tmp);
                    }
                    case "3" -> {
                        Production.addStoryToDistillate(tmp,text,date);
                        pa.distillates.setWindowSetings(tmp);
                    }
                }
            }else {
                errorWindow.showError("Please select a date.");
            }
        }

        private void removeElement(ProductionArea pa){
            String tabID = tabPane.getSelectionModel().getSelectedItem().getId();
            Distillate tmp = this.distillate;

            if (tmp != null){
                switch (tabID){
                    case "1" -> {
                        AlcoholPercentage alcoholPercentage = lvwAlchol.getSelectionModel().getSelectedItem();
                        if (alcoholPercentage != null){
                            Production.removeAlcholPercentage(tmp,alcoholPercentage);
                            pa.distillates.setWindowSetings(tmp);
                        }
                    }
                    case "2" -> {
                        ProductCutInformation productCutInformation = lvwCutInfo.getSelectionModel().getSelectedItem();
                        if (productCutInformation != null){
                            Production.removeProductionCutInformation(tmp,productCutInformation);
                            pa.distillates.setWindowSetings(tmp);
                        }
                    }
                    case "3" -> {
                        StoryLine storyLine = lvwStory.getSelectionModel().getSelectedItem();
                        if (storyLine != null){
                            Production.removeStoryFromDistillate(tmp,storyLine);
                            pa.distillates.setWindowSetings(tmp);
                        }
                    }
                }
            }
        }

        private void updateOnTabShift(String tabID){
            switch (tabID){
                case "1" ->{
                    txaText.setDisable(true);
                    txfValue.setDisable(false);
                }
                case "2" -> {
                    txaText.setDisable(false);
                    txfValue.setDisable(true);
                }
                case "3" -> {
                    txaText.setDisable(false);
                    txfValue.setDisable(true);
                }
            }
        }

        private void close(ProductionArea pa){
            pa.mainPane.getChildren().clear();
            pa.initContent(pa.mainPane);
        }
        
    }
}
