package Controller;

import Model.*;
import View.ErrorView;
import View.Main;
import View.OtherPopUp.NoRedeclarationNeeded;
import View.OtherPopUp.ResetConfirmation;
import com.gluonhq.charm.glisten.control.ToggleButtonGroup;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static boolean needRedeclare = true;

    //fxml elements
    @FXML
    private MenuButton airportMenu;
    @FXML
    private MenuButton physicalRunwayMenu;
    @FXML
    private MenuButton obstacleMenu;
    @FXML
    private Label obstacleHeightLabel;
    @FXML
    private Label obstacleWidthLabel;
    @FXML
    private TextField obstacleHeightField;
    @FXML
    private TextField obstacleWidthField;
    @FXML
    private TextField distanceThresholdTextField;
    @FXML
    private Button performCalculationButton;
    @FXML
    private TextField clDistTextField;
    @FXML
    private Button oldToraInfo;
    @FXML
    private Button oldTodaInfo;
    @FXML
    private Button oldAsdaInfo;
    @FXML
    private Button oldLdaInfo;
    @FXML
    private Label oldToraInfoLabel;
    @FXML
    private Label oldTodaInfoLabel;
    @FXML
    private Label oldAsdaInfoLabel;
    @FXML
    private Label oldLdaInfoLabel;
    @FXML
    private TextField stripEndTextField;
    @FXML
    private TextField blastProtectionField;
    @FXML
    private TextField resaTextField;
    @FXML
    private Label stripEndInfoLabel;
    @FXML
    private Label blastProtectionInfoLabel;
    @FXML
    private Label resaInfoLabel;
    @FXML
    private Button stripEndInfo;
    @FXML
    private Button blastProtectionInfo;
    @FXML
    private Button resaInfo;
    @FXML
    private Button calculationBreakdown;
    @FXML
    public Tab topViewTab;
    @FXML
    private Tab sideViewTab;
    @FXML
    private Tab simultaneousViewTab;
    @FXML
    private ToggleButtonGroup lrButtonGroup;
    @FXML
    private RadioButton leftDirButton;
    @FXML
    private Label notificationLabel;

    //table
    @FXML
    private TableView<Parameter> leftTableView;
    @FXML
    private TableView<Parameter> rightTableView;
    @FXML
    private TableColumn<Parameter, String> parColumn1;
    @FXML
    private TableColumn<Parameter, String> originalCol1;
    @FXML
    private TableColumn<Parameter, String> revisedCol1;
    @FXML
    private TableColumn<Parameter, String> parColumn2;
    @FXML
    private TableColumn<Parameter, String> originalCol2;
    @FXML
    private TableColumn<Parameter, String> revisedCol2;

    //property to be used in Visualisation classes
    public static ObjectProperty<PhysicalRunway> physRunwayItem = new SimpleObjectProperty<>();
    public static ObjectProperty<Airport> airportItem = new SimpleObjectProperty();
    public static ObjectProperty<Obstacle> obstacleProperty = new SimpleObjectProperty<>();
    public static DoubleProperty disFromThreshold = new SimpleDoubleProperty();
    public static DoubleProperty disFromCentre = new SimpleDoubleProperty();
    public static StringProperty dirFromCentre = new SimpleStringProperty();
    public static DoubleProperty valueChanged = new SimpleDoubleProperty();
    public static DoubleProperty obstacleHeight = new SimpleDoubleProperty();
    public static DoubleProperty obstacleWidth = new SimpleDoubleProperty();


    //list of airports and obstacles from files
    ObservableList<Airport> airports = FXCollections.observableArrayList();
    ObservableList<Obstacle> obstacles = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadInfos();
        try {
            topViewTab.setContent(FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/TopView.fxml"))));
            sideViewTab.setContent(FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/SideView.fxml"))));
            simultaneousViewTab.setContent(FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/SimultaneousView.fxml"))));
            loadAirports("src/Data/airports.xml");
            addAirportEvent();
            loadObstacles("src/Data/obstacles.xml");
            addObstacleEvent();

            if(Main.isReset()){
                notificationLabel.setText("Status: Options Reset\t " + getDateTimeNow());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //getters
    public ObservableList<Obstacle> getObstacles(){return this.obstacles;}
    public ObservableList<Airport> getAirports(){return this.airports;}
    public static PhysicalRunway getPhysRunwaySelected() {return physRunwayItem.get();}
    public static boolean needRedeclare(){return needRedeclare;}
    public static Obstacle getObstacleSelected() {return obstacleProperty.get();}
    public MenuButton getAirportMenu() {return this.airportMenu;}

    //event handlers
    @FXML
    public void loadAboutProject(ActionEvent event){
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/SEG-Group-1-2023/ProjectRelatedInformation/blob/main/runwayprojectdefinition.pdf"));
        } catch (IOException | URISyntaxException ignored) {}
    }
    @FXML
    public void handleReset(ActionEvent event) throws IOException {
        boolean flag = new ResetConfirmation().confirmReset();
        Main.setReset(true);
        if(flag) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = Main.getStage();
            stage.setTitle("SEG Runway Project");
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    public void checkDistFromThreshold(ActionEvent event){
        try {
            double distFromThreshold = Double.parseDouble(distanceThresholdTextField.getText().trim());
            getObstacleSelected().setDistFThreshold(distFromThreshold);
            disFromThreshold.set(distFromThreshold);
        } catch (NumberFormatException exception) {
            //display error message
            new ErrorView().showError(distanceThresholdTextField, "Invalid input for distance from threshold\nHint: please input a numerical value", "0");
        }
    }

    @FXML
    public void checkDistFromCentreLine(ActionEvent event){
        try{
            double distFromCentreLine = Double.parseDouble(clDistTextField.getText().trim());
            if(distFromCentreLine < 0){throw new NumberFormatException();}
            getObstacleSelected().setDistFCent(distFromCentreLine);
            disFromCentre.set(distFromCentreLine);
        } catch (NumberFormatException exception){
            new ErrorView().showError(clDistTextField, "Invalid input for distance from centre line\nHint: please input a numerical value greater or equal to 0", "0");
        }
    }

    @FXML
    public void checkObstacleHeight(ActionEvent event){
        try{
            double height = Double.parseDouble(obstacleHeightField.getText().trim());
            if(height <= 0){throw new NumberFormatException();}
            getObstacleSelected().setHeight(height);
            obstacleHeight.set(height);
        } catch (NumberFormatException e){
            new ErrorView().showError(obstacleHeightField, "Invalid obstacle height, please input a numerical value greater than 0", "10");
        }
    }

    @FXML
    public void checkObstacleWidth(ActionEvent event){
        try{
            double width = Double.parseDouble(obstacleWidthField.getText().trim());
            if(width <= 0){throw new NumberFormatException();}
            getObstacleSelected().setWidth(width);
            obstacleWidth.set(width);
        } catch (NumberFormatException e){
            new ErrorView().showError(obstacleWidthField, "Invalid obstacle width, please input a numerical value greater than 0", "10");
        }
    }

    @FXML
    public void performCalculation(ActionEvent event){
        checkDistFromThreshold(new ActionEvent());
        checkDistFromCentreLine(new ActionEvent());
        checkObstacleHeight(new ActionEvent());
        checkObstacleWidth(new ActionEvent());
        setRESA(new ActionEvent());
        setStripEnd(new ActionEvent());
        setBlastProtection(new ActionEvent());
        if(Calculator.needRedeclare(getObstacleSelected(), getPhysRunwaySelected().getLogicalRunways().get(0))){
            needRedeclare = true;
            Calculator.performCalc(getObstacleSelected(), getPhysRunwaySelected());
            showTable(false);
        } else{
            needRedeclare = false;
            showTable(false);
            new NoRedeclarationNeeded().showNoRedeclarationNeeded();
        }
        calculationBreakdown.setDisable(false);
        valueChanged.set(valueChanged.doubleValue() == 1? 0: 1);
        notificationLabel.setText("Status: Calculation performed\t " + getDateTimeNow());

    }

    public String getDateTimeNow(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return  dtf.format(now);
    }

    @FXML
    public void setStripEnd(ActionEvent event){
        try{
            double stripEnd = Double.parseDouble(stripEndTextField.getText().trim());
            if(stripEnd < 0 || stripEnd > 100){throw new NumberFormatException();}
            PhysicalRunway.setStripEnd(stripEnd);
        } catch (NumberFormatException e){
            new ErrorView().showError(stripEndTextField, "Invalid input for strip end \nHint: please input a numerical value within this range 0-100", "60");
        }
    }

    @FXML
    public void setBlastProtection(ActionEvent event){
        try{
            double blastProtection = Double.parseDouble(blastProtectionField.getText().trim());
            if(blastProtection < 300 || blastProtection > 500){
                throw new NumberFormatException();
            }
            PhysicalRunway.setBlastProtection(blastProtection);
        } catch (NumberFormatException e){
            new ErrorView().showError(blastProtectionField, "Invalid input for blast protection\nHint: please input a numerical value within this range: 300-500 (for safety purpose)", "300");
        }
    }

    @FXML
    public void setRESA(ActionEvent event){
        try{
            double resa = Double.parseDouble(resaTextField.getText().trim());
            if(resa < 240 || resa > 500){
                throw new NumberFormatException();
            }
            PhysicalRunway.setResa(resa);
        } catch (NumberFormatException e){
            new ErrorView().showError(resaTextField, "Invalid input for RESA\nHint: please input a numerical value within this range 240-500 (for safety purpose)", "240");
        }
    }

    @FXML
    public void showCalculationBreakdown(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CalculationBreakdown.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setTitle("Calculation Breakdown");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    public void setLeftRightDirection(ActionEvent event){
        if(leftDirButton.isSelected()){
            getObstacleSelected().setDirFromCentre("L");
            dirFromCentre.set("L");
        } else{
            getObstacleSelected().setDirFromCentre("R");
            dirFromCentre.set("R");
        }
    }

    //method to load information such as documentation and description of parameters
    public void loadInfos() {
        oldToraInfo.setOnMouseEntered(mouseEvent -> oldToraInfoLabel.setVisible(true));
        oldToraInfo.setOnMouseExited(mouseEvent -> oldToraInfoLabel.setVisible(false));
        oldTodaInfo.setOnMouseEntered(mouseEvent -> oldTodaInfoLabel.setVisible(true));
        oldTodaInfo.setOnMouseExited(mouseEvent -> oldTodaInfoLabel.setVisible(false));
        oldAsdaInfo.setOnMouseEntered(mouseEvent -> oldAsdaInfoLabel.setVisible(true));
        oldAsdaInfo.setOnMouseExited(mouseEvent -> oldAsdaInfoLabel.setVisible(false));
        oldLdaInfo.setOnMouseEntered(mouseEvent -> oldLdaInfoLabel.setVisible(true));
        oldLdaInfo.setOnMouseExited(mouseEvent -> oldLdaInfoLabel.setVisible(false));
        stripEndInfo.setOnMouseEntered(mouseEvent -> stripEndInfoLabel.setVisible(true));
        stripEndInfo.setOnMouseExited(mouseEvent -> stripEndInfoLabel.setVisible(false));
        blastProtectionInfo.setOnMouseEntered(mouseEvent -> blastProtectionInfoLabel.setVisible(true));
        blastProtectionInfo.setOnMouseExited(mouseEvent -> blastProtectionInfoLabel.setVisible(false));
        resaInfo.setOnMouseEntered(mouseEvent -> resaInfoLabel.setVisible(true));
        resaInfo.setOnMouseExited(mouseEvent -> resaInfoLabel.setVisible(false));
    }

    //this function read from a xml file and instantiate list of airports available
    public void loadAirports(String file) throws Exception {
        // Create a DocumentBuilder
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // Parse the XML file into a Document
        Document doc = docBuilder.parse(file);

        // Get a NodeList of all airport elements
        NodeList airportElements = doc.getElementsByTagName("airport");

        // Create a list to hold the airports
        ObservableList<Airport> airports = FXCollections.observableArrayList();

        // Loop over each airport element and create an Airport object
        for (int i = 0; i < airportElements.getLength(); i++) {
            Node airportNode = airportElements.item(i);

            if (airportNode.getNodeType() == Node.ELEMENT_NODE) {
                Element airportElement = (Element) airportNode;
                // Get the airport name
                String airportName = airportElement.getElementsByTagName("name").item(0).getTextContent();
                // Create a list to hold the physical runways
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
                        }
                        // Create a new PhysicalRunway object with the logical runways and add it to the list of physical runways
                        PhysicalRunway physicalRunway = new PhysicalRunway(physRunwayName, logicalRunways);
                        physicalRunways.add(physicalRunway);
                    }
                }
                // Create a new Airport object with the physical runways and add it to the list of airports
                Airport airport = new Airport(airportName, physicalRunways);
                getAirports().add(airport);
            }
        }
        airports.sort(Comparator.comparing(Airport::getName));
    }

    public void addAirportEvent() {
        for(Airport airport: getAirports()){
            MenuItem airportMenuItem = new MenuItem(airport.getName());
            airportMenuItem.setStyle("-fx-font-family: Verdana; -fx-font-size: 16px");
            airportMenuItem.setOnAction(e -> {
                airportItem.set(airport);
                physicalRunwayMenu.getItems().clear();
                getAirportMenu().setText(airport.getName());
                physicalRunwayMenu.setText("Select Physical Runway");
                physicalRunwayMenu.setDisable(false);
                performCalculationButton.setDisable(true);
                obstaclesEditing(true);
                for(PhysicalRunway runway: airport.getPhysicalRunways()){
                    MenuItem runwayMenuItem = new MenuItem(runway.getName());
                    runwayMenuItem.setOnAction(f -> {
                        oldToraInfo.setVisible(true);
                        oldTodaInfo.setVisible(true);
                        oldLdaInfo.setVisible(true);
                        oldAsdaInfo.setVisible(true);
                        physRunwayItem.set(runway);
                        physicalRunwayMenu.setText(runway.getName());
                        stripEndTextField.setText(String.valueOf(PhysicalRunway.getStripEnd()));
                        blastProtectionField.setText(String.valueOf(PhysicalRunway.getBlastProtection()));
                        resaTextField.setText(String.valueOf(PhysicalRunway.getResa()));
                        stripEndTextField.setDisable(false);
                        blastProtectionField.setDisable(false);
                        resaTextField.setDisable(false);
                        obstaclesEditing(false);
                        showTable(true);
                    });
                    runwayMenuItem.setStyle("-fx-font-family: Verdana; -fx-font-size: 16px");
                    physicalRunwayMenu.getItems().add(runwayMenuItem);
                }
            });
            getAirportMenu().getItems().add(airportMenuItem);
        }
    }

    public void showTable(boolean beforeCalc){
        ObservableList<Parameter> data1 = FXCollections.observableArrayList();
        LogicalRunway logRunway1 = getPhysRunwaySelected().getLogicalRunways().get(0);
        ObservableList<Parameter> data2 = FXCollections.observableArrayList();
        LogicalRunway logRunway2 = getPhysRunwaySelected().getLogicalRunways().get(1);
        //display original values
        // Define the columns of the table
        parColumn1.setCellValueFactory(new PropertyValueFactory<>("name"));
        parColumn1.setText(logRunway1.getDesignator());
        originalCol1.setCellValueFactory(new PropertyValueFactory<>("originalValue"));
        revisedCol1.setCellValueFactory(new PropertyValueFactory<>("newValue"));
        editColumn(parColumn1);
        editColumn(originalCol1);
        editColumn(revisedCol1);

        parColumn2.setCellValueFactory(new PropertyValueFactory<>("name"));
        parColumn2.setText(logRunway2.getDesignator());
        originalCol2.setCellValueFactory(new PropertyValueFactory<>("originalValue"));
        revisedCol2.setCellValueFactory(new PropertyValueFactory<>("newValue"));
        editColumn(parColumn2);
        editColumn(originalCol2);
        editColumn(revisedCol2);

        if(beforeCalc){
            data1.add(new Parameter("TORA (m)", String.valueOf(logRunway1.getTora()), "-"));
            data1.add(new Parameter("TODA (m)", String.valueOf(logRunway1.getToda()), "-"));
            data1.add(new Parameter("ASDA (m)", String.valueOf(logRunway1.getAsda()), "-"));
            data1.add(new Parameter("LDA (m)", String.valueOf(logRunway1.getLda()), "-"));

            data2.add(new Parameter("TORA (m)", String.valueOf(logRunway2.getTora()), "-"));
            data2.add(new Parameter("TODA (m)", String.valueOf(logRunway2.getToda()), "-"));
            data2.add(new Parameter("ASDA (m)", String.valueOf(logRunway2.getAsda()), "-"));
            data2.add(new Parameter("LDA (m)", String.valueOf(logRunway2.getLda()), "-"));
        } else{
            data1.add(new Parameter("TORA (m)", String.valueOf(logRunway1.getTora()), String.valueOf(needRedeclare? logRunway1.getNewTora(): logRunway1.getTora())));
            data1.add(new Parameter("TODA (m)", String.valueOf(logRunway1.getToda()), String.valueOf(needRedeclare? logRunway1.getNewToda(): logRunway1.getToda())));
            data1.add(new Parameter("ASDA (m)", String.valueOf(logRunway1.getAsda()), String.valueOf(needRedeclare? logRunway1.getNewAsda(): logRunway1.getAsda())));
            data1.add(new Parameter("LDA (m)", String.valueOf(logRunway1.getLda()), String.valueOf(needRedeclare? logRunway1.getNewLda(): logRunway1.getLda())));

            data2.add(new Parameter("TORA (m)", String.valueOf(logRunway2.getTora()), String.valueOf(needRedeclare? logRunway2.getNewTora(): logRunway1.getTora())));
            data2.add(new Parameter("TODA (m)", String.valueOf(logRunway2.getToda()), String.valueOf(needRedeclare? logRunway2.getNewToda(): logRunway1.getToda())));
            data2.add(new Parameter("ASDA (m)", String.valueOf(logRunway2.getAsda()), String.valueOf(needRedeclare? logRunway2.getNewAsda(): logRunway1.getAsda())));
            data2.add(new Parameter("LDA (m)", String.valueOf(logRunway2.getLda()), String.valueOf(needRedeclare? logRunway2.getNewLda(): logRunway1.getLda())));
        }

        leftTableView.setItems(data1);
        rightTableView.setItems(data2);
    }

    private void editColumn(TableColumn<Parameter, String> tableColumn){
        tableColumn.setResizable(false);
        tableColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    this.setStyle("-fx-background-color: rgb(244,244,244); -fx-alignment: CENTER;-fx-font-family: Verdana; -fx-padding: 7 0 0 0");
                    // Set the text of the cell to the item
                    setText(item);
                } else {
                    setText(null);
                }
            }
        });
    }

    //functions to load obstacles from xml files and displayed in the menu selection
    public void loadObstacles(String file) throws IOException, ParserConfigurationException, SAXException {
        // Create a DocumentBuilder
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        // Parse the XML file into a Document
        Document doc = docBuilder.parse(file);
        // Get a NodeList of all obstacle elements
        NodeList obstacleElements = doc.getElementsByTagName("obstacle");
        // Create a list to hold the obstacles
        ObservableList<Obstacle> obstacles = FXCollections.observableArrayList();
        // Loop over each obstacle element and create an Obstacle object
        for (int i = 0; i < obstacleElements.getLength(); i++) {
            Node obstacleNode = obstacleElements.item(i);
            if (obstacleNode.getNodeType() == Node.ELEMENT_NODE) {
                Element obstacleElement = (Element) obstacleNode;
                // Get the obstacle name, height and width
                String obstacleName = obstacleElement.getElementsByTagName("name").item(0).getTextContent();
                double height = Double.parseDouble(obstacleElement.getElementsByTagName("height").item(0).getTextContent());
                double width = Double.parseDouble(obstacleElement.getElementsByTagName("width").item(0).getTextContent());
                // Create a new Obstacle object with the physical runways and add it to the list of airports
                Obstacle obstacle = new Obstacle(obstacleName, height, width, 0, 0);
                getObstacles().add(obstacle);
            }
        }
        obstacles.sort(Comparator.comparing(Obstacle::getName));
    }

    public void addObstacleEvent(){
        for(Obstacle obstacle: getObstacles()){
            MenuItem obstacleMenuItem = new MenuItem(obstacle.getName());
            obstacleMenuItem.setStyle("-fx-font-family: Verdana; -fx-font-size: 16px");
            obstacleMenuItem.setOnAction(e -> {
                obstacleHeightField.setText(""+obstacle.getHeight());
                obstacleWidthField.setText(""+obstacle.getWidth());
                performCalculationButton.setDisable(false);
                distanceThresholdTextField.setDisable(false);
                lrButtonGroup.setDisable(false);
                clDistTextField.setDisable(false);
                obstacleProperty.set(obstacle);
                obstacleMenu.setText(obstacle.getName());
                if(obstacle.getName().equals("Customisable Obstacle")){
                    obstacleHeightField.setEditable(true);
                    obstacleWidthField.setEditable(true);
                } else{
                    obstacleHeightField.setEditable(false);
                    obstacleWidthField.setEditable(false);
                }
                obstacleProperty.set(obstacle);
                checkDistFromThreshold(new ActionEvent());
                checkDistFromCentreLine(new ActionEvent());
                checkObstacleWidth(new ActionEvent());
                checkObstacleHeight(new ActionEvent());
            });
            obstacleMenu.getItems().add(obstacleMenuItem);
        }
    }

    public void obstaclesEditing(boolean notAllowed){
        obstacleMenu.setDisable(notAllowed);
        if(MainController.getObstacleSelected() != null){
            lrButtonGroup.setDisable(notAllowed);
            distanceThresholdTextField.setDisable(notAllowed);
            clDistTextField.setDisable(notAllowed);
            performCalculationButton.setDisable(notAllowed);
        }
    }


}
