package View.OtherPopUp;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Optional;

public class ResetConfirmation {
    public ResetConfirmation() {}

    public boolean confirmReset(){
        boolean flag = false;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to reset the system?");
        alert.setContentText("Warning: This action cannot be undone.\nAll inputs and selections will be cleared.");
        alert.getDialogPane().lookup(".content.label").setStyle("-fx-font-family: Verdana; -fx-font-size: 14px; -fx-text-fill: red; -fx-line-spacing: 5px");

// Create the DialogPane explicitly
        DialogPane dialogPane = alert.getDialogPane();

// Add an event filter to the dialog's scene
        dialogPane.getScene().addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ENTER) {
                e.consume(); // Cancel the Enter key event
            }
        });

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            alert.close();
            flag = true;
        } else if(result.isPresent() && result.get() == ButtonType.CANCEL){
            alert.close();
        }

        return flag;
    }
}
