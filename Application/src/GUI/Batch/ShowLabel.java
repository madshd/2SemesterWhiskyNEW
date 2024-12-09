package GUI.Batch;

import BatchArea.Batch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class ShowLabel {

	private final Stage showLabelStage = new Stage();
	private final TextArea labelArea = new TextArea();

	public ShowLabel() {
		showLabelStage.setTitle("Batch Label");

		GridPane gridPane = new GridPane();
		showLabelStage.setResizable(false);

		Scene showLabelScene = new Scene(gridPane);
		showLabelStage.setScene(showLabelScene);
		showLabelStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
		showLabelScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

		initContent(gridPane);
	}

	public void show(Batch batch) {
		updateContent(batch);
		showLabelStage.showAndWait();
	}

	private void updateContent(Batch batch) {
		labelArea.setText(batch.getLabel());
		labelArea.setId("labelArea");
	}

	private void initContent(GridPane mainPane) {
		mainPane.setPadding(new Insets(50));
		mainPane.setHgap(10);
		mainPane.setVgap(10);
		mainPane.setAlignment(Pos.CENTER);
		mainPane.setGridLinesVisible(false);

		// For intuitive clearing of textarea focus
		mainPane.setOnMouseClicked(event -> {
			mainPane.requestFocus();
		});

		labelArea.setEditable(false);
		labelArea.setFocusTraversable(false);
		mainPane.add(labelArea, 0, 0);
		labelArea.setPrefSize(500, 300);

		Button okButton = new Button("OK");
		okButton.setDisable(false);
		HBox buttonBox = new HBox(okButton);
		mainPane.add(buttonBox, 0, 1, 1, 1);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(25);

		// Button Actions
		okButton.setOnAction(event -> {
			clearFields();
			showLabelStage.close();
		});
	}

	private void clearFields() {
		labelArea.clear();
	}
}
