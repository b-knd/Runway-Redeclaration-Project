package View;

import javafx.scene.control.*;
import javafx.stage.Modality;

import java.util.Optional;

public class Error {
    public Error() {}

    public void showError(TextField field, String message, String resetValue){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error Message");
        errorAlert.setHeaderText("ERROR");
        errorAlert.setContentText(message);
        errorAlert.getDialogPane().lookup(".content.label").setStyle("-fx-font-family: Verdana; -fx-font-size: 14px; -fx-text-fill: red; -fx-line-spacing: 10px");
        errorAlert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = errorAlert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){
            field.setText(resetValue);
            errorAlert.close();
            field.positionCaret(field.getText().length());
        }

        Button okButton = (Button) errorAlert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setOnAction(event1 -> {
            field.setText(resetValue);
            errorAlert.close();
            field.positionCaret(field.getText().length());
        });
    }

    public void errorPopUp(String message){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error Message");
        errorAlert.setHeaderText("ERROR");
        errorAlert.setContentText(message);
        errorAlert.getDialogPane().lookup(".content.label").setStyle("-fx-font-family: Verdana; -fx-font-size: 14px; -fx-text-fill: red; -fx-line-spacing: 10px");
        errorAlert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = errorAlert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){
            errorAlert.close();
        }

        Button okButton = (Button) errorAlert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setOnAction(event1 -> {
            errorAlert.close();
        });
    }

    public void showError(String message){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error Message");
        errorAlert.setHeaderText("ERROR");
        errorAlert.initModality(Modality.APPLICATION_MODAL);
        // Create a scroll pane for the content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(800, 400);

// Create a label with the error message
        Label label = new Label(message);
        label.setLineSpacing(5);
        label.setWrapText(true);

// Add the label to the scroll pane
        scrollPane.setContent(label);
        errorAlert.getDialogPane().setContent(scrollPane);
        label.setStyle("-fx-font-family: Verdana; -fx-font-size: 14px; -fx-text-fill: red; -fx-line-spacing: 10px");
        Optional<ButtonType> result = errorAlert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){
            errorAlert.close();
        }

        Button okButton = (Button) errorAlert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setOnAction(event1 -> {
            errorAlert.close();
        });
    }
}
