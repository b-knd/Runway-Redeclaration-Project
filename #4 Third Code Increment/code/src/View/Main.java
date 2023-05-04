package View;

import Controller.MainController;
import Model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;


public class Main extends Application {
    private static Scene classScene;
    private static Stage classStage;
    private static boolean status;
    private static User user;

    public Main(){}

    public Main(User user) {
        Main.user = user;
    }

    public static String getAirportID(){
        return user.getAirportID();
    }

    public static int getRole(){
        return user.getRole();
    }

    public static String getName(){
        return user.getName();
    }

    public static String getUsername() {
        return user.getUsername();
    }

    public static Stage getStage(){
        return classStage;
    }

    public static boolean isReset(){
        return status;
    }

    public static void setReset(boolean reset){
        status = reset;

    }

    public static Scene getClassScene(){
        return classScene;
    }

    public static void setClassScene(Scene scene){
        classScene = scene;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Main.classStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Main.fxml"));

        Parent root = loader.load();

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
