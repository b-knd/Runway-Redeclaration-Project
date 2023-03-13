package View;

import Controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        MainController controller = new MainController();
        loader.setController(controller);
        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setTitle("SEG Runway Project");
        stage.setScene(scene);
        stage.show();
    }
}
