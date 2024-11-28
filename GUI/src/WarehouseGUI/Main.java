package WarehouseGUI;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Brug MainTemplate som applikationens layout
        MainTemplate mainTemplate = new MainTemplate();

        // Vis scenen
        primaryStage.setScene(mainTemplate.getScene());
        primaryStage.setTitle("JavaFX Template med Sidebar");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
