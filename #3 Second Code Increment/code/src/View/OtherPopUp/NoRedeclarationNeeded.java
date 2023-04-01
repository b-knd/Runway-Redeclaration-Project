package View.OtherPopUp;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class NoRedeclarationNeeded {
    public NoRedeclarationNeeded() {}

    public Alert showNoRedeclarationNeeded(){
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Information");
        infoAlert.setHeaderText("INFO");
        infoAlert.setContentText("No runway redeclaration needed\nOriginal runway parameters can be used");
        infoAlert.getDialogPane().lookup(".content.label").setStyle("-fx-font-family: Verdana; -fx-font-size: 14px; -fx-line-spacing: 5px");
        Optional<ButtonType> result = infoAlert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){
            infoAlert.close();
        }

        Button okButton = (Button) infoAlert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setOnAction(event -> {
            infoAlert.close();
        });
        return infoAlert;
    }
}
