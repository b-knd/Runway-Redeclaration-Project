package View.ErrorPopUp;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.util.Optional;

public class InvalidStripEnd {
    public InvalidStripEnd() {}

    public void showError(TextField stripEndField){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error Message");
        errorAlert.setHeaderText("ERROR");
        errorAlert.setContentText("Invalid input for strip end \nHint: please input a numerical value within this range 0-100");
        errorAlert.getDialogPane().lookup(".content.label").setStyle("-fx-font-family: Verdana; -fx-font-size: 14px; -fx-text-fill: red; -fx-line-spacing: 5px");
        Optional<ButtonType> result = errorAlert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){
            stripEndField.setText("60");
            errorAlert.close();
        }

        Button okButton = (Button) errorAlert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setOnAction(event -> {
            stripEndField.setText("60");
            errorAlert.close();
        });
    }
}
