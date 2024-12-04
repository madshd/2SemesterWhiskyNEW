package GUI.Warehousing;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CreateCaskDialog extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Opret hovedlayoutet
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setHgap(10);
        grid.setVgap(10);
        // Tilføj komponenter til layoutet

        // Opret en scene og tilføj layoutet
        Scene scene = new Scene(grid, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setTitle("Create Ingredient");
        stage.setX(300);
        stage.setY(0);
        stage.setScene(scene);
        stage.show();

    }
}
