package Controller;

import Model.Helper.Utility;
import Model.User;
import View.Error;
import View.Login;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public static HashMap<String, User> users = new HashMap<>();
    private String username = null;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private AnchorPane userPane;
    @FXML
    private AnchorPane passwordPane;
    @FXML
    private Button loginButton;
    @FXML
    private Label forgotPasswordLink;

    public static Stage resetPasswordStage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadUsers("src/Data/users.xml");
            DropShadow shadow = new DropShadow(13, Color.valueOf("#4cc9f0"));
            userPane.setEffect(shadow);
            passwordPane.setEffect(shadow);
            usernameField.setOnKeyPressed(keyEvent -> {
                if(new KeyCodeCombination(KeyCode.ENTER).match(keyEvent)){
                    check();
                }
            });
            passwordField.setOnKeyPressed(keyEvent -> {
                if (new KeyCodeCombination(KeyCode.ENTER).match(keyEvent)) {
                    check();
                }
            });
            loginButton.setOnMouseClicked(actionEvent -> check());

            forgotPasswordLink.setOnMouseClicked(mouseEvent -> {
                try {
                    forgotPassword();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(MainController.airports+" from login");
    }

    @FXML
    public void forgotPassword() throws IOException {
        resetPasswordStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ResetPassword.fxml"));

        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/MainStylesheet.css")).toExternalForm());

        resetPasswordStage.setTitle("Forgot password");
        resetPasswordStage.setScene(scene);
        resetPasswordStage.setResizable(false);
        resetPasswordStage.initModality(Modality.APPLICATION_MODAL);
        resetPasswordStage.show();
    }


    public void check(){
        try{
            String password = passwordField.getText().trim();
            String name = usernameField.getText().trim();
            if(name.length() > 0 && password.length() > 0 && users.containsKey(name)){
                if(users.get(name).getPassword().equals(Utility.toHexString(Utility.getSHA(password)))) {
                    Login.getStage().close();
                    new View.Main(users.get(name)).start(new Stage());
                } else{
                    new Error().showError(passwordField, "Invalid password", "");
                }
            } else if (name.length() <= 0){
                new Error().showError(usernameField, "Please enter a valid username", "");
            } else if (!users.containsKey(name)){
                new Error().showError(usernameField,"Username does not exist, please check again" , name);
            } else{
                new Error().showError(passwordField,"Please enter a valid password" , "");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //have the xml ready before writing this writer
    //this function read from a xml file and instantiate list of airports available
    public void loadUsers(String file) throws Exception {
        // Create a DocumentBuilder
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // Parse the XML file into a Document
        Document doc = docBuilder.parse(file);

        // Get a NodeList of all airport elements
        NodeList userElements = doc.getElementsByTagName("user");

        // Loop over each airport element and create an Airport object
        for (int i = 0; i < userElements.getLength(); i++) {
            Node userNode = userElements.item(i);

            if (userNode.getNodeType() == Node.ELEMENT_NODE) {
                Element userElement = (Element) userNode;

                //get username
                String username = userElement.getElementsByTagName("username").item(0).getTextContent();
                String name = userElement.getElementsByTagName("name").item(0).getTextContent();
                String password = userElement.getElementsByTagName("password").item(0).getTextContent();
                String airport = userElement.getElementsByTagName("airportID").item(0).getTextContent();
                int role = Integer.parseInt(userElement.getElementsByTagName("role").item(0).getTextContent());

                User user = new User(username, name, password, airport, role);
                users.put(username, user);
            }
        }
    }
}
