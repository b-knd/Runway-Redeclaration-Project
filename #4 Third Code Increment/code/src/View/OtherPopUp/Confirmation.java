package View.OtherPopUp;

import Model.Airport;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Optional;

public class Confirmation {
    public Confirmation() {}

    public boolean confirm(String header, String message){
        boolean flag = false;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(header);
        alert.setContentText(message);
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

    private EventHandler<KeyEvent> enterFilter = e -> {
        if (e.getCode() == KeyCode.ENTER) {
            e.consume(); // Cancel the Enter key event
        }
    };

    public boolean confirmAddAirport(Airport airport, String info) {
        boolean flag = false;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Confirmation: add airport " + airport.getName() + " (" + airport.getID() + ") to the system?");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(800, 400);

        // Create a label with the error message
        Label label = new Label(info);
        label.setLineSpacing(5);
        label.setWrapText(true);

        // Add the label to the scroll pane
        scrollPane.setContent(label);
        alert.getDialogPane().setContent(scrollPane);
        label.setStyle("-fx-font-family: Verdana; -fx-font-size: 14px; -fx-text-fill: black; -fx-line-spacing: 10px");

        // Create the DialogPane explicitly
        DialogPane dialogPane = alert.getDialogPane();

        // Add an event filter to the dialog's root node
//        dialogPane.getScene().getRoot().addEventFilter(KeyEvent.KEY_PRESSED, enterFilter);
        dialogPane.getScene().addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ENTER) {
                e.consume(); // Cancel the Enter key event
            }
        });

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("OK pressed");
            alert.close();
            flag = true;
        } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
            alert.close();
        }

        return flag;
    }

}
