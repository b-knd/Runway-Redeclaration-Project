package Controller.Helper;

import Controller.LoginController;
import Controller.MainController;
import Controller.UserManagerController;
import Model.Airport;
import Model.Helper.Utility;
import Model.User;
import View.Error;
import View.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class AddNewUserController implements Initializable {
    @FXML
    public Label titleLabel;
    @FXML
    public MenuButton airportMenu;
    @FXML
    public TextField nameField;
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    public Button saveButton;
    @FXML
    public Button cancelButton;
    @FXML
    public Label airportLabel;
    @FXML
    public Label airportNameLabel;

    public static User newUser = null;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final Airport[] airportSelected = new Airport[1];
        System.out.println(MainController.airports.size());
        System.out.println(MainController.managerMap.keySet());
        if(Main.getRole() == 1){
            titleLabel.setText("Set Up Manager Account");
            airportMenu.setVisible(true);
            airportLabel.setVisible(false);
            airportNameLabel.setVisible(false);
            for(Airport airport: MainController.airports){
                if(!(MainController.managerMap.containsKey(airport.getManager()))){
                    System.out.println("not in map");
                    MenuItem airportItem = new MenuItem(airport.getName());
                    airportItem.setOnAction(actionEvent -> {
                        airportSelected[0] = airport;
                        airportMenu.setText(airport.getName());
                        usernameField.setText(airport.getManager());
                        usernameField.setDisable(true);
                    });
                    airportMenu.getItems().add(airportItem);
                }
            }
            if(airportMenu.getItems().size() == 0){
                airportMenu.setText("All manager accounts have been set up");
            }
        } else{
            titleLabel.setText("Add New User");
            airportMenu.setVisible(false);
            airportLabel.setVisible(true);
            airportNameLabel.setVisible(true);
            airportNameLabel.setText(MainController.managerMap.get(Main.getUsername()).getName());
            nameField.setDisable(false);
            airportSelected[0] = MainController.managerMap.get(Main.getUsername());
        }

        saveButton.setOnAction(actionEvent -> {
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            if(name.length() == 0 || username.length() == 0 || password.length() == 0 || confirmPassword.length() == 0){
                new Error().errorPopUp("Please fill in all the fields.");
            } else{
                //check username
                if(LoginController.users.containsKey(username)){
                    new Error().errorPopUp("Username already exist, please try another username");
                    passwordField.clear();
                    confirmPasswordField.clear();
                } else if(password.length() < 6){
                    new Error().showError(passwordField, "Please ensure password length is at least 6 characters.", "");
                    confirmPasswordField.clear();
                } else if (!Utility.checkPassword(password)){
                    new Error().showError(passwordField, "Please ensure your password contains mix of symbols, lowercase and uppercase letters and numbers.", "");
                    confirmPasswordField.clear();
                } else if(!confirmPassword.equals(password)){
                    new Error().showError(confirmPasswordField, "Passwords do not match", "");
                } else{
                    try {
                        password = Utility.toHexString(Utility.getSHA(password));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    if(Main.getRole() == 1){
                        newUser = new User(username, name, password, airportSelected[0].getID(), 2);
                    } else{
                        newUser = new User(username, name, password, airportSelected[0].getID(), 3);
                    }
                    UserManagerController.helperStage.close();
                }
            }
        });
        cancelButton.setOnAction(actionEvent -> {UserManagerController.helperStage.close();});
    }



}
