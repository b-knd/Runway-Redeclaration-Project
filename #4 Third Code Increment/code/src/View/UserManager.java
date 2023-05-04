package View;

import Controller.AirportManagerController;
import Controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class UserManager extends Application {
    private static Stage classStage;
    private static String username;
    private static Scene classScene;

    //default constructor
    public UserManager(){}

    public UserManager(String username) {
        UserManager.username = username;
    }

    public static String getUsername() {
        return username;
    }

    public static Stage getStage(){
        return classStage;
    }

    public static Scene getClassScene(){
        return classScene;
    }

    @Override
    public void start(Stage stage) throws Exception {
        classStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/UserManager.fxml"));
        Parent root = loader.load();
        loader.setController(new AirportManagerController());

        classScene = new Scene(root);
        if(MainController.Theme.getState()){
            classScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/MainStylesheetDark.css")).toExternalForm());
        } else{
            classScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/MainStylesheet.css")).toExternalForm());
        }

        stage.setTitle("SEG Runway Project");
        stage.setScene(classScene);
        stage.setResizable(false);
        stage.show();
    }
}
