package GUI.Production;

import Controllers.Production;
import GUI.Common.Common;
import Interfaces.Filling;
import Production.Distiller;
import Production.AlcoholPercentage;
import Production.StoryLine;
import Production.ProductCutInformation;
import Warehousing.Ingredient;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import Production.Distillate;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

        private final TextField txfName = new TextField();
        private final TextField txfQuantity = new TextField();
        private final ComboBox<Distiller> cmbDistiller = new ComboBox<>();
        private final DatePicker dpStartDate = new DatePicker();
        private final DatePicker dpEndDate = new DatePicker();
        private final TextArea txaDescription = new TextArea();

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

            // Fields
            txfName.setPromptText("Name");
            this.add(txfName,0,1);
            cmbDistiller.setPromptText("Distiller");
            this.add(cmbDistiller,0,2);
            txfQuantity.setPromptText("Quantity");
            this.add(txfQuantity,0,3);

            // Date
            dpStartDate.setConverter(Common.datePickerFormat(dpStartDate));
            dpStartDate.setPromptText("Start date");
            this.add(dpStartDate,1,1);
            dpEndDate.setConverter(Common.datePickerFormat(dpEndDate));
            dpEndDate.setPromptText("End date");
            this.add(dpEndDate,1,2);

            // Text area
            txaDescription.setPromptText("" +
                    "Basic description behind the distillate, description should enforce the end Whisky story.");
            txaDescription.setPrefHeight(areaHeight * 0.1);
            txaDescription.setPrefWidth(200);
            GridPane.setHalignment(txaDescription,HPos.RIGHT);
            this.add(txaDescription,3,1,1,3);



            updateBasics(null);
        }

        public void updateBasics(Distillate distillate){
            cmbDistiller.getItems().setAll(Production.getDistillers());

            if (distillate != null){
                txfName.setText(distillate.getName());
                cmbDistiller.setValue(distillate.getDistiller());
                txfQuantity.setText(Double.toString(distillate.getQuantityStatus()));
                dpStartDate.setValue(distillate.getStartDate());
                dpEndDate.setValue(distillate.getEndDate());
                txaDescription.setText(distillate.getDescription());
            }else {
                txfName.clear();
                cmbDistiller.getItems().clear();
                cmbDistiller.getItems().setAll(Production.getDistillers());
                txfQuantity.clear();
                dpStartDate.setValue(null);
                dpEndDate.setValue(null);
                txaDescription.clear();
            }
        }
    }

    public static class IngredientDetails extends GridPane {

        private final DatePicker dpfillDate = new DatePicker();
        private final ComboBox<Ingredient> cmbIngredients = new ComboBox<>();
        private final TextField txfQuantity = new TextField();
        private final ListView<Filling> lvwIngrediantFillings = new ListView<>();
        private final TextArea txaIngrediantDetails = new TextArea();

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

            txfQuantity.setPromptText("Quantity");

            // Lists
            lvwIngrediantFillings.setEditable(false);
            lvwIngrediantFillings.setPrefHeight(areaHeight * 0.1);
            this.add(lvwIngrediantFillings,2,1);

            // Text area
            txaIngrediantDetails.setPromptText("Details on selected ingredient");
            this.add(txaIngrediantDetails,3,1);

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
                newBtn.setOnAction(actionEvent -> btnAction(newBtn));
            }

            hBox.setAlignment(Pos.CENTER);
            this.add(hBox,2,2);

            updateIngredients(null);
        }

        public void updateIngredients(Distillate distillate){
//            cmbIngredients.getItems().setAll(Warehousing.)
        }

        private void btnAction(Button button){

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
            Tab alcohol = new Tab("Alcohol %");
            Tab cutInfo = new Tab("Cut info");
            Tab story = new Tab("Story");
            tabPane.getTabs().addAll(alcohol,cutInfo,story);
            tabPane.setPrefHeight(areaHeight * 0.4);

            alcohol.setContent(lvwAlchol);
            alcohol.setClosable(false);
            cutInfo.setContent(lvwCutInfo);
            cutInfo.setClosable(false);
            story.setContent(lvwStory);
            story.setClosable(false);

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
                newBtn.setOnAction(actionEvent -> btnAction(newBtn));
            }

            hBox.setAlignment(Pos.CENTER);
            this.add(hBox,2,2);

            HBox hBoxButtom = new HBox(20);
            int hboxButtomBtnWidth = 100;
            String[] btnButtomNames = {"Cancel", "Save"};
            String[] abbreivationsButtom = {"cancel","Save"};

            for (int i = 0; i < btnButtomNames.length; i++) {
                Button newBtn = new Button(btnButtomNames[i]);
                hBoxButtom.getChildren().add(newBtn);
                newBtn.setUserData(abbreivationsButtom[i]);
                newBtn.setPrefWidth(hboxButtomBtnWidth);
                newBtn.setOnAction(actionEvent -> btnAction(newBtn));
            }

            hBoxButtom.setAlignment(Pos.BOTTOM_RIGHT);
            hBoxButtom.setBorder(pa.border);
            hBoxButtom.setPadding(pa.padding);
            this.add(hBoxButtom,0,3,4,1);

        }

        private void btnAction(Button button){

        }
    }
}
