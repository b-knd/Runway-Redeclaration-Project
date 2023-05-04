package Controller.Helper;

import Controller.LoginController;
import Model.Helper.Utility;
import Model.Helper.XMLParserWriter;
import Model.User;
import View.Error;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class ResetPasswordController implements Initializable {
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void handleCancel(ActionEvent event){
        LoginController.resetPasswordStage.close();
    }

    @FXML
    public void check(ActionEvent event) throws NoSuchAlgorithmException {
        String name = nameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if(name.length() == 0 || password.length() == 0 || confirmPassword.length() == 0){
            new Error().errorPopUp("Please fill in all the field");
        } else if(!LoginController.users.containsKey(name)){
            new Error().errorPopUp("Username does not exist");
        } else if(password.length() < 6){
            new Error().showError(passwordField, "Please ensure password length is at least 6 characters.", "");
            confirmPasswordField.clear();
        } else if (!Utility.checkPassword(password)){
            new Error().showError(passwordField, "Please ensure your password contains mix of symbols, lowercase and uppercase letters and numbers.", "");
            confirmPasswordField.clear();
        } else if(!confirmPassword.equals(password)){
            new Error().showError(confirmPasswordField, "Passwords do not match", "");
        } else{
            User user = LoginController.users.get(name);
            user.setPassword(Utility.toHexString(Utility.getSHA(password)));
            LoginController.users.put(name, user);
            try {
                XMLParserWriter.updateUserXML(FXCollections.observableArrayList(LoginController.users.values()), "src/Data/users.xml");
            } catch (ParserConfigurationException | TransformerException e) {
                e.printStackTrace();
            }
        }
        LoginController.resetPasswordStage.close();
    }
}
