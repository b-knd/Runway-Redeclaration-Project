package Controller;

import Controller.Helper.EditAirportController;
import Model.Airport;
import Model.Helper.Utility;
import Model.Helper.XMLParserWriter;
import Model.LogicalRunway;
import Model.PhysicalRunway;
import Model.User;
import View.Error;
import View.*;
import View.OtherPopUp.Confirmation;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AirportManagerController implements Initializable {
    private static final int INACTIVITY_TIMEOUT = 3 * 60 * 1000; // 3 seconds in milliseconds
    private Timer inactivityTimer;

    @FXML
    private Label identityLabel;
    @FXML
    private TableView<Airport> airportTable;
    @FXML
    private TextArea airportDetails;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<Airport, String> IDCol;
    @FXML
    private TableColumn<Airport, String> nameCol;
    @FXML
    private TableColumn<Airport, String> managerCol;
    @FXML
    private MenuItem aboutProject;
    @FXML
    private MenuItem copyright;
    @FXML
    private MenuItem userGuide;
    @FXML
    private MenuItem userManager;
    @FXML
    private MenuItem backToMain;
    @FXML
    private MenuItem logoutItem;
    @FXML
    private ImageView lightModePng;
    @FXML
    private ImageView darkModePng;
    @FXML
    private Button modeButtonManager;

    public double mode = 1;
    public void stateSetter(MainController.Theme theme){
        boolean state = MainController.Theme.getState();
        System.out.println("current setter:"+state);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resetInactivityTimer();

        if(MainController.Theme.getState()){
            darkModePng.setVisible(true);
            lightModePng.setVisible(false);
            modeButtonManager.setStyle("-fx-background-color: rgb(49,73,88); -fx-background-radius:50");
        } else{
            lightModePng.setVisible(true);
            darkModePng.setVisible(false);
            modeButtonManager.setStyle("-fx-background-color: rgb(149,195,240); -fx-background-radius: 50");
        }

        airportDetails.setText("No content to display, select an airport to view airport details");
        identityLabel.setText("Logged in as "+ Main.getUsername());

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            resetInactivityTimer();
            airportTable.setItems(filterList(MainController.airports.stream().toList(), newValue));
        });

        airportTable.setEditable(false);

        IDCol.setCellValueFactory(
                new PropertyValueFactory<>("ID")
        );
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        managerCol.setCellValueFactory(
                new PropertyValueFactory<>("manager")
        );
        editColumn(IDCol);
        editColumn(nameCol);
        editColumn(managerCol);


        airportTable.setItems(MainController.airports);

        airportTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            resetInactivityTimer();
            if (newSelection != null) {
                airportDetails.setText(Utility.airportInfo(FXCollections.observableArrayList(newSelection)));
            }
        });

        airportTable.refresh();
        initShortcut();
    }

    @FXML
    public void loadCopyright(ActionEvent event){
        inactivityTimer.cancel();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/VersionNCopyright.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Version Number and Copyright Information");
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            System.out.println("Cannot access help & documentation");
        }

        resetInactivityTimer();
    }

    @FXML
    public void downloadUserGuide(ActionEvent event) throws IOException {
        inactivityTimer.cancel();
        Utility.loadAboutProject("Download User Guide", AirportManager.getStage(), "/Printer/UserGuide.pdf");
        resetInactivityTimer();
    }

    @FXML
    public void backToMain(ActionEvent event) throws Exception {
        inactivityTimer.cancel();
        AirportManager.getStage().close();
        Scene classScene = Main.getClassScene();
        if(MainController.Theme.getState()){
            classScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/MainStylesheetDark.css")).toExternalForm());
        } else{
            classScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/MainStylesheet.css")).toExternalForm());
        }

        Main.getStage().show();
    }

    @FXML
    public void goUserManager(ActionEvent event) throws Exception {
        inactivityTimer.cancel();
        AirportManager.getStage().close();
        new UserManager().start(new Stage());
    }

    @FXML
    public void handleLogout(ActionEvent event) throws Exception {

        MainController.beforeCalculation = true;
        MainController.physRunwayItem.set(null);
        MainController.airportItem.set(null);
        MainController.obstacleProperty.set(null);

        MainController.Theme.setState(false);
        inactivityTimer.cancel();
        AirportManager.getStage().close();
        new Login().start(new Stage());
    }

    @FXML
    public void loadAboutProject(ActionEvent event) throws IOException {
        inactivityTimer.cancel();
        Utility.loadAboutProject("Download Project Definition", AirportManager.getStage(), "/Printer/runwayprojectdefinition.pdf");
        resetInactivityTimer();
    }

    @FXML
    public void importAirport(ActionEvent event) throws ParserConfigurationException, TransformerException {
        inactivityTimer.cancel();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Airport XML File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );
        File selectedFile = fileChooser.showOpenDialog(AirportManager.getStage());
        if(selectedFile != null) {
            addAirport(selectedFile);
            airportTable.setItems(MainController.airports);
            airportTable.refresh();
        }
        resetInactivityTimer();
    }

    @FXML
    public void deleteAirport(ActionEvent event) throws ParserConfigurationException, TransformerException {
        inactivityTimer.cancel();
        Airport airport = airportTable.getSelectionModel().getSelectedItem();

        if(airport == null){
            new Error().errorPopUp("No airport selected. Hint: please select an airport to be deleted.");
        } else{
            boolean flag = new Confirmation().confirm("Are you sure you want to delete "+airport.getName()+" ("+airport.getID()+")?", "Warning: This action cannot be undone.\nSelected airport will be permanently deleted.");
            if(flag){
                MainController.airports.remove(airport);
                MainController.airportMap.remove(airport.getID());
                MainController.airportNames.remove(airport.getName());
                MainController.managerMap.remove(airport.getManager());
                for(User u: MainController.users.getOrDefault(airport, new ArrayList<>())){
                    LoginController.users.remove(u.getUsername());
                }
                MainController.users.remove(airport);
                LoginController.users.remove(airport.getManager());
                MainController.managers.remove(airport.getManager());
                airportTable.getSelectionModel().clearSelection();
                airportDetails.setText("No content to display, select an airport to view airport details");
                new Notification(AirportManager.getStage()).sucessNotification("Successful action", airport.getName()+" has been deleted.");
                airportTable.setItems(MainController.airports);
                airportTable.refresh();

                try {
                    XMLParserWriter.updateUserXML(FXCollections.observableArrayList(LoginController.users.values()), "src/Data/users.xml");
                } catch (ParserConfigurationException | TransformerException e) {
                    e.printStackTrace();
                }
            }
        }
        resetInactivityTimer();
    }

    @FXML
    public void changeMode(MouseEvent event){
        resetInactivityTimer();
        toggleMode();
    }

    public void toggleMode(){
        if (!MainController.Theme.getState()) {
            darkModePng.setVisible(true);
            lightModePng.setVisible(false);
            modeButtonManager.setStyle("-fx-background-color: rgb(49,73,88); -fx-background-radius:50");

            AirportManager.getClassScene().getStylesheets().remove("CSS/MainStylesheet.css");
            AirportManager.getClassScene().getStylesheets().add("CSS/MainStylesheetDark.css");


            System.out.println("Dark");
            mode = 0;
            MainController.Theme theme1 = new MainController.Theme();
            theme1.setState(true);

            System.out.println("setState: " + MainController.Theme.getState());
            stateSetter(theme1);
        }else {
            lightModePng.setVisible(true);
            darkModePng.setVisible(false);
            modeButtonManager.setStyle("-fx-background-color: rgb(149,195,240); -fx-background-radius: 50");

            AirportManager.getClassScene().getStylesheets().remove("CSS/MainStylesheetDark.css");
            AirportManager.getClassScene().getStylesheets().add("CSS/MainStylesheet.css");

            System.out.println("Light");
            mode = 1;
            MainController.Theme theme1 = new MainController.Theme();
            theme1.setState(false);

            System.out.println("setState:" + MainController.Theme.getState());
            stateSetter(theme1);

        }
    }

    @FXML
    public void editAirport(ActionEvent event) throws Exception {
        inactivityTimer.cancel();
        Airport airport = airportTable.getSelectionModel().getSelectedItem();

        if(airport == null){
            new Error().errorPopUp("No airport selected. Hint: please select an airport to edit its details.");
        } else{
            EditAirportController.setAirportSelected(airport);
            new EditAirport().start(new Stage());
            MainController.airports.remove(airport);
            MainController.airportMap.remove(airport.getID());

            if(EditAirportController.actionCancel){
                MainController.airportMap.put(airport.getID(), airport);
                MainController.airports.add(airport);
                new Notification(AirportManager.getStage()).sucessNotification("Successful revert", "Changes reverted.");
            } else{
                MainController.airportMap.put(EditAirportController.airportWithNewInfo.getID(), EditAirportController.airportWithNewInfo);
                MainController.airports.add(EditAirportController.airportWithNewInfo);
                new Notification(AirportManager.getStage()).sucessNotification("Successful action", "Airport info has been updated.");
            }
            airportTable.setItems(MainController.airports);
            airportTable.refresh();
        }
        resetInactivityTimer();
    }

    @FXML
    public void exportAirport() throws IOException, ParserConfigurationException, TransformerException {
        inactivityTimer.cancel();
        Airport airport = airportTable.getSelectionModel().getSelectedItem();

        if(airport == null){
            new Error().errorPopUp("No airport selected. Hint: please select an airport to export the details.");
        } else{
            Utility.exportAirport(AirportManager.getStage(), FXCollections.observableArrayList(airport));
        }
        resetInactivityTimer();
    }

    @FXML
    public void exportAllAirport() throws ParserConfigurationException, IOException, TransformerException {
        resetInactivityTimer();
        Utility.exportAirport(AirportManager.getStage(), MainController.airports);
    }

    public void addAirport(File selectedFile){
        inactivityTimer.cancel();
        try {
            // Create a DocumentBuilder
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Parse the XML file into a Document
            Document doc = docBuilder.parse(selectedFile);

            // Get a NodeList of all airport elements
            NodeList airportElements = doc.getElementsByTagName("airport");
            if (airportElements.getLength() == 0) {
                throw new Exception();
            } else {
                // Create a list to hold the airports
                ObservableList<Airport> airports = FXCollections.observableArrayList();

                // Loop over each airport element and create an Airport object
                for (int i = 0; i < airportElements.getLength(); i++) {
                    Node airportNode = airportElements.item(i);

                    if (airportNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element airportElement = (Element) airportNode;
                        String reference = airportElement.getElementsByTagName("ID").item(0).getTextContent();
                        // Get the airport name
                        String airportName = airportElement.getElementsByTagName("name").item(0).getTextContent();
                        // Create a list to hold the physical runways

                        StringBuilder errorMessage = new StringBuilder();
                        if (checkAirport(airportName, reference).length() > 0) {
                            errorMessage.append(checkAirport(airportName, reference));
                        }

                        ObservableList<PhysicalRunway> physicalRunways = FXCollections.observableArrayList();
                        // Get a NodeList of all physical runway elements for the current airport
                        NodeList physRunwayElements = airportElement.getElementsByTagName("physicalRunway");
                        // Loop over each physical runway element and create a PhysicalRunway object
                        for (int j = 0; j < physRunwayElements.getLength(); j++) {
                            Node physRunwayNode = physRunwayElements.item(j);

                            if (physRunwayNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element physRunwayElement = (Element) physRunwayNode;
                                // Get the physical runway name
                                String physRunwayName = physRunwayElement.getAttribute("name");
                                // Create a list to hold the logical runways
                                ObservableList<LogicalRunway> logicalRunways = FXCollections.observableArrayList();
                                // Get a NodeList of all logical runway elements for the current physical runway
                                NodeList logRunwayElements = physRunwayElement.getElementsByTagName("logicalRunway");
                                // Loop over each logical runway element and create a LogicalRunway object
                                for (int k = 0; k < logRunwayElements.getLength(); k++) {
                                    Node logRunwayNode = logRunwayElements.item(k);
                                    if (logRunwayNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element logRunwayElement = (Element) logRunwayNode;
                                        // Get the logical runway designator and dimensions
                                        String designator = logRunwayElement.getAttribute("designator");

                                        double tora = Double.parseDouble(logRunwayElement.getAttribute("tora"));
                                        double toda = Double.parseDouble(logRunwayElement.getAttribute("toda"));
                                        double asda = Double.parseDouble(logRunwayElement.getAttribute("asda"));
                                        double lda = Double.parseDouble(logRunwayElement.getAttribute("lda"));
                                        // Create a new LogicalRunway object and add it to the list of logical runways
                                        LogicalRunway logicalRunway = new LogicalRunway(designator, tora, toda, asda, lda);
                                        logicalRunways.add(logicalRunway);
                                    }
                                    // Create a new PhysicalRunway object with the logical runways and add it to the list of physical runways

                                }
                                PhysicalRunway physicalRunway = new PhysicalRunway(physRunwayName, logicalRunways);
                                physicalRunways.add(physicalRunway);
                                errorMessage.append(checkRunway(physicalRunway));
                            }
                        }

                        String manager = airportElement.getElementsByTagName("user").item(0).getTextContent();
                        if(MainController.managerMap.containsKey(manager)){
                            errorMessage.append("\nDuplicated username for airport manager, already in use");
                        }
                        Airport airport = new Airport(reference, airportName, physicalRunways, manager);

                        if (errorMessage.toString().length() > 0) {
                            new Error().showError(errorMessage.toString());
                            new Notification(AirportManager.getStage()).failNotification("Failed action", "Fail to add airport.");
                        } else {
                            boolean result = new Confirmation().confirmAddAirport(airport, Utility.airportInfo(FXCollections.observableArrayList(airport)));
                            if (result) {
                                MainController.airportMap.put(airport.getID(), airport);
                                MainController.getAirports().add(airport);
                                MainController.airportNames.add(airportName);
                                new Notification(AirportManager.getStage()).sucessNotification("Successful action", airportName + " successfully added to system.");
                            }
                        }
                    }
                }
                airports.sort(Comparator.comparing(Airport::getName));
            }
        } catch (Exception e){
            e.printStackTrace();
            new Error().showError("""
                        Invalid XML File. Please ensure the xml inputs matches the required format.
    
                        Example:\s
                        <?xml version="1.0" encoding="UTF-8"?>
                        <airports>
                         <airport>
                          <ID>LHR</ID>
                          <name>Heathrow Airport</name>
                          <physicalRunways>
                           <physicalRunway name="09L/27R">
                            <logicalRunway designator="09L" tora="3902.0" toda="3902.0" asda="3902.0" lda="3595.0" />
                            <logicalRunway designator="27R" tora="3884.0" toda="3962.0" asda="3884.0" lda="3884.0" />
                           </physicalRunway>
                           <physicalRunway name="09R/27L">
                            <logicalRunway designator="09R" tora="3660.0" toda="3660.0" asda="3660.0" lda="3353.0" />
                            <logicalRunway designator="27L" tora="3660.0" toda="3660.0" asda="3660.0" lda="3660.0" />
                           </physicalRunway>
                           <physicalRunway name="06/24">
                            <logicalRunway designator="06" tora="2734.0" toda="2734.0" asda="2734.0" lda="2734.0" />
                            <logicalRunway designator="24" tora="2734.0" toda="2900.0" asda="2800.0" lda="2500.0" />
                           </physicalRunway>
                          </physicalRunways>
                          <user>manager</user>
                         </airport>
                        </airports>
    
    
                        """);
            new Notification(AirportManager.getStage()).failNotification("Failed action", "Fail to add airport.");
        }
        resetInactivityTimer();
    }

    private ObservableList<Airport> filterList(List<Airport> list, String searchText){
        List<Airport> filteredList = new ArrayList<>();
        for (Airport airport : list){
            if(searchFindAirport(airport, searchText)) filteredList.add(airport);
        }
        airportTable.refresh();
        return FXCollections.observableList(filteredList);
    }

    private boolean searchFindAirport(Airport airport, String searchText){
        return (airport.getName().toLowerCase().contains(searchText.toLowerCase()));
    }

    private void editColumn(TableColumn<Airport, String> tableColumn){
        tableColumn.setResizable(false);
        tableColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    this.setStyle("-fx-background-color: rgb(244,244,244); -fx-alignment: CENTER;-fx-font-family: Verdana; -fx-padding: 7");
                    // Set the text of the cell to the item
                    setText(item);
                } else {
                    setText(null);
                }
            }
        });
    }

    private String checkAirport(String airportName, String airportReference){
        StringBuilder errorMessage = new StringBuilder();
        if(MainController.airportMap.containsKey(airportReference)){
            errorMessage.append("\nNAME ERROR: Duplicated airport reference (").append(airportReference).append(")");
        }
        if(MainController.airportNames.contains(airportName)) {
            errorMessage.append("\nNAME ERROR: Duplicated airport name ").append(airportName);
        }
        if(errorMessage.length() > 0){
            return "AIRPORT ERROR: "+ errorMessage;
        }
        return "";
    }

    private String checkRunway(PhysicalRunway physicalRunway){
        StringBuilder res = new StringBuilder();
        //physical runways
        StringBuilder physError = new StringBuilder();
        String name = physicalRunway.getName();
        String[] split = name.split("/");
        if(split.length != 2){
            System.out.println("splitlength");
            return "\n\nPHYSICAL RUNWAY ERROR (RUNWAY " + (physicalRunway.getName()) + ")" + "\nFORMAT ERROR: Please provide a valid physical runway with format NL/NR or NR/NL with N representing a numerical value from 01 to 36";
        } else{
            String lower = split[0];
            String higher = split[1];
            if(lower.charAt(lower.length()-1) == 'L' && higher.charAt(higher.length()-1) == 'R' || lower.charAt(lower.length()-1) == 'R' && higher.charAt(higher.length()-1) == 'L'){
                try{
                    int lowerThreshold = Integer.parseInt(lower.substring(0, lower.length()-1));
                    int higherThreshold = Integer.parseInt(higher.substring(0, higher.length()-1));
                    if(lowerThreshold > higherThreshold){
                        physError.append("\nFORMAT ERROR: Please ensure the threshold with lower value is located at the left (Ex: 09R/27L instead of 27L/09R)");
                    }
                    if(Math.abs(higherThreshold - lowerThreshold) != 18){
                        physError.append("\nFORMAT ERROR: Please ensure runways are 180 degrees away from each other");
                    }
                    StringBuilder logError = new StringBuilder();
                    if(physicalRunway.getLogicalRunways().size() != 2){
                        logError.append("\nINVALID LOGICAL RUNWAYS: Please ensure you have provided information for both runways: ").append(lower).append(" and ").append(higher);
                    } else{
                        LogicalRunway left = physicalRunway.getLogicalRunways().get(0);
                        LogicalRunway right = physicalRunway.getLogicalRunways().get(1);
                        if(left.getDesignator().equals(lower) && right.getDesignator().equals(higher)){
                            if(left.getTora() != right.getTora()){
                                logError.append("\nINVALID PARAMETERS: Unequal TORA values for logical runways ").append(lower).append(" and ").append(higher);
                            }
                            logError.append(checkLogRunway(left));
                            logError.append(checkLogRunway(right));
                        } else{
                            logError.append("\nINVALID LOGICAL RUNWAYS: Please ensure you provided logical runway in correct sequence: ").append(lower).append(" followed by ").append(higher);
                        }

                    }
                    if(logError.length() > 0){
                        physError.append("\nLOGICAL RUNWAYS ERRORS: ");
                        physError.append(logError);
                    }
                }catch (NumberFormatException e){
                    physError.append("\nFORMAT ERROR: Please provide a valid physical runway with format NL/NR or NR/NL with N representing a numerical value from 01 to 36");
                }
            } else{
                physError.append("\nFORMAT ERROR: Please provide a valid physical runway with format NL/NR or NR/NL with N representing a numerical value from 01 to 36");
            }

            if(physError.length() > 0){
                res.append("\n\nPHYSICAL RUNWAY ERROR (RUNWAY ").append(physicalRunway.getName()).append("):");
                res.append(physError);
            }

        }
        return res.toString();
    }

    public String checkLogRunway(LogicalRunway logicalRunway){
        StringBuilder res = new StringBuilder();
        if(logicalRunway.getLda() > logicalRunway.getTora()){
            res.append("\n-- LDA value should be smaller than or equals to TORA value (Invalid displaced threshold value)");
        }
        if(logicalRunway.getTora() > logicalRunway.getAsda()){
            res.append("\n-- TORA value should be smaller than or equals to ASDA value (Invalid stopway value)");
        }
        if(logicalRunway.getAsda() > logicalRunway.getToda()){
            res.append("\n-- ASDA value should be smaller than or equals to TODA value (Invalid clearway value");
        }
        if(res.length() > 0){
            return "\nPARAMETER ERROR (Logical Runway "+logicalRunway.getDesignator()+"): "+res;
        } else{
            return res.toString();
        }
    }

    public void startInactivityTimer() {
        // Schedule the timer to prompt for logout after 3 minutes of inactivity
        inactivityTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    // Prompt for logout
                    boolean flag = new Confirmation().confirm("You have been inactive for "+INACTIVITY_TIMEOUT/60000+" minutes.", "Do you want to proceed to logout?");
                    inactivityTimer.cancel();
                    if(flag){
                        Main.getStage().close();
                        try {
                            new Login().start(new Stage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    resetInactivityTimer();
                    // TODO: Add code to handle user input
                });

            }
        }, INACTIVITY_TIMEOUT);
    }

    public void resetInactivityTimer() {
        // Cancel the current timer and start a new one
        if(inactivityTimer != null){
            inactivityTimer.cancel();
        }
        inactivityTimer = new Timer();
        startInactivityTimer();
    }

    private void initShortcut(){
        KeyCombination AIRPORT_MANAGER_KEY = new KeyCodeCombination(KeyCode.A,KeyCombination.SHORTCUT_DOWN);
        KeyCombination USER_MANAGER_KEY = new KeyCodeCombination(KeyCode.U,KeyCombination.SHORTCUT_DOWN);
        KeyCombination LOGOUT_KEY = new KeyCodeCombination(KeyCode.L,KeyCombination.SHORTCUT_DOWN,KeyCombination.SHIFT_DOWN);
        KeyCombination ABOUT_PROJECT_KEY = new KeyCodeCombination(KeyCode.I,KeyCombination.SHORTCUT_DOWN);
        KeyCombination COPYRIGHT = new KeyCodeCombination(KeyCode.C,KeyCombination.SHIFT_DOWN);
        KeyCombination USERGUIDE = new KeyCodeCombination(KeyCode.G,KeyCombination.SHIFT_DOWN);

        userManager.setAccelerator(USER_MANAGER_KEY);
        aboutProject.setAccelerator(ABOUT_PROJECT_KEY);
        userGuide.setAccelerator(USERGUIDE);
        copyright.setAccelerator(COPYRIGHT);
        backToMain.setAccelerator(AIRPORT_MANAGER_KEY);
        logoutItem.setAccelerator(LOGOUT_KEY);
    }
}
