package Controller;

import Model.Miscellaneous;
import Model.User;
import View.ErrorView;
import View.Main;
import View.MainWithLogin;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    HashMap<String, User> users = new HashMap<>();
    private String username;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button signInButton;
    @FXML
    private AnchorPane userPane;
    @FXML
    private AnchorPane passwordPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadUsers("src/Data/users.xml");
            DropShadow shadow = new DropShadow(13, Color.valueOf("#4cc9f0"));
            userPane.setEffect(shadow);
            passwordPane.setEffect(shadow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void checkUsername(ActionEvent event){
        String username = usernameField.getText();
        if(users.containsKey(username)){
            this.username = username;
        } else{
            new ErrorView().showError(usernameField, "Invalid username", "");
        }
    }

    @FXML
    public void checkPassword(ActionEvent event) throws Exception {
        String password = passwordField.getText();
        if(users.get(username).getPassword().equals(password)){
            MainWithLogin.getStage().close();
            new Main().start(new Stage());
        } else{
            new ErrorView().showError(passwordField, "Invalid password", "");
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
                String password = userElement.getElementsByTagName("password").item(0).getTextContent();
                int role = Integer.parseInt(userElement.getElementsByTagName("role").item(0).getTextContent());

                User user = new User(username, Miscellaneous.toHexString(Miscellaneous.getSHA(password)), role);
                users.put(username, user);
            }
        }
    }


}
