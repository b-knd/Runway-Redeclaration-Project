package View;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.util.Objects;

public class Notification {
    private static Stage owner;
    public Notification(Stage owner) {
        Notification.owner = owner;
    }

    public void sucessNotification(String title, String message){

        Notifications res = Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_LEFT)
                .owner(owner);

        Scene scene = owner.getScene();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/OtherStylesheet.css")).toExternalForm());
        res.getStyleClass().add("notification");

        res.showInformation();

    }

    public void failNotification(String title, String message){

        Notifications res = Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_LEFT)
                .owner(owner);

        Scene scene = owner.getScene();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/OtherStylesheet.css")).toExternalForm());
        res.getStyleClass().add("notification");

        res.showWarning();
    }
}
