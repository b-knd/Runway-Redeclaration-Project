package Controller;

import Model.*;
import Model.Helper.PDFGenerator;
import Model.Helper.Utility;
import Model.Helper.XMLParserWriter;
import View.Error;
import View.*;
import View.OtherPopUp.Confirmation;
import View.OtherPopUp.NoRedeclarationNeeded;
import com.gluonhq.charm.glisten.control.ToggleButtonGroup;
import com.itextpdf.text.DocumentException;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    private static final int INACTIVITY_TIMEOUT = 3 * 60* 1000; // 3 seconds in milliseconds
    @FXML
    private MenuItem RotateRCmd;
    @FXML
    private MenuItem RotateLCmd;
    @FXML
    private MenuItem upCmd;
    @FXML
    private MenuItem downCmd;
    @FXML
    private MenuItem leftCmd;
    @FXML
    private MenuItem rightCmd;
    @FXML
    private MenuItem ZoomInCmd;
    @FXML
    private MenuItem ZoomOutCmd;
    @FXML
    private MenuItem regenerationReport;
    @FXML
    private MenuItem regenerationReport1;
    @FXML
    private MenuItem regenerationReport2;
    @FXML
    private MenuItem aboutProject;
    @FXML
    private MenuItem copyright;
    @FXML
    private MenuItem userGuide;
    @FXML
    private MenuItem tabCmd;
    @FXML
    private MenuItem notiHistoryCmd;
    @FXML
    private MenuItem calcBDCmd;
    @FXML
    private MenuItem resetCmd;
    @FXML
    private MenuItem resetViewCmd;
    private Timer inactivityTimer;

    private static boolean needRedeclare = true;
    @FXML
    private Button notificationButton;

    //notification
    @FXML
    private Pane notiPane;
    @FXML
    private ScrollPane notiScrollPane;
    @FXML
    private VBox notiVBox;


    //fxml elements
    @FXML
    private MenuButton airportMenu;
    @FXML
    private MenuButton physicalRunwayMenu;
    @FXML
    private MenuButton obstacleMenu;
    @FXML
    private MenuItem logoutItem;
    @FXML
    private MenuItem airportManager;
    @FXML
    private MenuItem userManager;
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
    @FXML
    private TabPane visualPane;

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
    @FXML
    private Button logoutButton;
    @FXML
    private Button resetButton;
    @FXML
    private Label identityLabel;
    @FXML
    private Menu navigatingMenu;
    @FXML
    private Menu exportMenu;
    @FXML
    private Label airportNameLabel;
    @FXML
    private MenuItem generateReport;
    @FXML
    private MenuItem exportAirport;
    @FXML
    private MenuItem defaultTheme;
    @FXML
    private MenuItem rgTheme;
    @FXML
    private MenuItem byTheme;

    @FXML
    private ImageView lightModePng;
    @FXML
    private ImageView darkModePng;

    @FXML
    private Button modeButtonManager;

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
    public static IntegerProperty themeProperty = new SimpleIntegerProperty();

    //list of airports and obstacles from files
    public static HashMap<String, Airport> airportMap = new HashMap<>();
    public static ObservableList<Airport> airports = FXCollections.observableArrayList();
    //    public static MapProperty<String, Airport> airports = new SimpleMapProperty<>(FXCollections.observableMap(map));
    public static ObservableList<Obstacle> obstacles = FXCollections.observableArrayList();
    public static ObservableList<String> airportNames = FXCollections.observableArrayList();
    public static HashMap<String, Airport> managerMap = new HashMap<>();
    public static HashMap<Airport, ArrayList<User>> users = new HashMap<>();
    public static HashMap<String, User> managers = new HashMap<>();

    //Controllers
    private TopViewController topViewController;
    private SideViewController sideViewController;
    public double mode = 1;
    public static class Theme{
        private static boolean state = false;

        public boolean isState() {
            return state;
        }

        public static boolean getState(){
            return state;
        }
        public static void setState(boolean state){
            Theme.state = state;
        }

    }


    public void stateSetter(Theme theme){
        boolean state = theme.getState();
        System.out.println("current setter:"+state);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(MainController.Theme.getState()){
            darkModePng.setVisible(true);
            lightModePng.setVisible(false);
            modeButtonManager.setStyle("-fx-background-color: rgb(49,73,88); -fx-background-radius:50");
        } else{
            lightModePng.setVisible(true);
            darkModePng.setVisible(false);
            modeButtonManager.setStyle("-fx-background-color: rgb(149,195,240); -fx-background-radius: 50");
        }

        beforeCalculation = true;
        resetInactivityTimer();
        obstacleProperty = new SimpleObjectProperty<>();
        DropShadow shadow = new DropShadow(2, Color.valueOf("#212f45"));
        leftTableView.setEffect(shadow);
        rightTableView.setEffect(shadow);

        // or if you only want to disable horizontal scrolling
        leftTableView.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });
        rightTableView.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getDeltaX() != 0) {
                event.consume();
            }
        });

        loadInfos();
        try {
            //create instances of the controller so that we can direct access their field when needed
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/TopView.fxml"));
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/FXML/SideView.fxml"));
            Parent root = loader.load();
            Parent root1 = loader1.load();
            topViewController = loader.getController();
            sideViewController = loader1.getController();
            topViewTab.setContent(root);
            sideViewTab.setContent(root1);
            simultaneousViewTab.setContent(FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/FXML/SimultaneousView.fxml"))));

            exportMenu.setVisible(true);



            loadAirports("src/Data/airports.xml");
            System.out.println(airports);
            addAirportEvent();
            loadObstacles("src/Data/obstacles.xml");
            addObstacleEvent();
            loadUsers();

            if(Main.getRole() == 1){
                userManager.setVisible(true);
                airportManager.setVisible(true);
                exportAirport.setVisible(false);
                airportNameLabel.setVisible(false);
                airportMenu.setVisible(true);
                // disable
                userManager.setDisable(false);
                airportManager.setDisable(false);
                exportAirport.setDisable(true);
                airportMenu.setDisable(false);
            } else if(Main.getRole() == 2){
                airportManager.setVisible(false);
                userManager.setVisible(true);
                airportMenu.setVisible(false);
                airportNameLabel.setVisible(true);
                airportNameLabel.setText(airportMap.get(Main.getAirportID()).getName());
                airportItem.set(airportMap.get(Main.getAirportID()));
                physicalRunwayMenu.setDisable(false);
                //disable
                airportManager.setDisable(true);
                userManager.setDisable(false);
                airportMenu.setDisable(true);
            } else{
                navigatingMenu.setVisible(false);
                airportMenu.setVisible(false);
                airportNameLabel.setVisible(true);
                airportNameLabel.setText(airportMap.get(Main.getAirportID()).getName());
                airportItem.set(airportMap.get(Main.getAirportID()));
                physicalRunwayMenu.setDisable(false);
                //disable
                navigatingMenu.setDisable(true);
                airportMenu.setDisable(true);
                airportNameLabel.setDisable(false);
            }

            identityLabel.setText("Logged in as "+ Main.getUsername());

            airportManager.setOnAction(actionEvent -> {
                addNotificationLabel("Status: Go to Airport Manager");
                inactivityTimer.cancel();
                try {
                    Main.getStage().close();
                    new AirportManager(AirportManager.getUsername()).start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            if(Main.isReset()){
                addNotificationLabel("Status: Options Reset");
            }

            generateReport.setDisable(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        initializeNotification(getNotiPane(),getNotiScrollPane(),getNotiVBox());
        topViewController.initializeCompass();

        airports.addListener(listener);
        calcBDCmd.setDisable(true);
        initShortcut();
    }

    public ListChangeListener<Airport> listener = new ListChangeListener<Airport>() {
        @Override
        public void onChanged(Change<? extends Airport> c) {
            Airport temp = airportItem.get();
            try {
                XMLParserWriter.writeToFile(FXCollections.observableArrayList(MainController.airports.stream().toList()), "src/Data/airports.xml");
                loadAirports("src/Data/airports.xml");
            } catch (Exception e) {
                e.printStackTrace();
            }
            addAirportEvent();
            System.out.println("Listener detected airport list changes");
            if(temp != null){
                boolean flag = false;
                while (c.next()) {
                    if (c.wasRemoved() && c.getRemoved().contains(temp)) {
                        flag = true;
                        airportMenu.setText("Select an Airport");
                        physicalRunwayMenu.setText("Select Physical Runway");
                        //create instances of the controller so that we can direct access their field when needed
                        try{
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/TopView.fxml"));
                            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/FXML/SideView.fxml"));
                            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/FXML/SimultaneousView.fxml"));
                            Parent root = loader.load();
                            Parent root1 = loader1.load();
                            Parent root2 = loader2.load();
                            topViewController = loader.getController();
                            sideViewController = loader1.getController();
                            topViewTab.setContent(root);
                            sideViewTab.setContent(root1);
                            simultaneousViewTab.setContent(root2);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                if(!flag){
                    airportMenu.setText(temp.getName());
                    PhysicalRunway runway = physRunwayItem.get();
                    if(runway != null){
                        physicalRunwayMenu.setText(runway.getName());
                    }
                }
            }
            airports.addListener(listener);
        }
    };

    //getters
    public ObservableList<Obstacle> getObstacles(){return obstacles;}
    public static ObservableList<Airport> getAirports(){return airports;}
    public static PhysicalRunway getPhysRunwaySelected() {return physRunwayItem.get();}
    public static boolean needRedeclare(){return needRedeclare;}
    public static Obstacle getObstacleSelected() {return obstacleProperty.get();}
    public static Airport getAirportSelected() {return airportItem.get();}
    public MenuButton getAirportMenu() {return this.airportMenu;}
    public TopViewController getTopViewController() { return topViewController;}
    public SideViewController getSideViewController() { return sideViewController;}
    public VBox getNotiVBox() {return notiVBox;}
    public Pane getNotiPane() {return notiPane;}
    public ScrollPane getNotiScrollPane() {return notiScrollPane;}
    public Label getNotificationLabel() {return notificationLabel;}
    public static boolean beforeCalculation = false;

    //event handlers

    @FXML
    public void handleLogout(ActionEvent event) throws Exception {
        beforeCalculation = true;
        physRunwayItem.set(null);
        airportItem.set(null);
        obstacleProperty.set(null);

        MainController.Theme.setState(false);
        inactivityTimer.cancel();
        Main.getStage().close();
        new Login().start(new Stage());
    }

    @FXML
    public void toDefaultTheme(ActionEvent event){
        themeProperty.set(1);
        defaultTheme.setDisable(true);
        rgTheme.setDisable(false);
        byTheme.setDisable(false);
        addNotificationLabel("Status: Theme set to Default");
    }

    @FXML
    public void toRGTheme(ActionEvent event){
        themeProperty.set(2);
        defaultTheme.setDisable(false);
        rgTheme.setDisable(true);
        byTheme.setDisable(false);
        addNotificationLabel("Status: Theme set to Red Green");
    }

    @FXML
    public void toBYTheme(ActionEvent event){
        themeProperty.set(3);
        defaultTheme.setDisable(false);
        rgTheme.setDisable(false);
        byTheme.setDisable(true);
        addNotificationLabel("Status: Theme set to Blue Yellow");
    }

    @FXML
    public void loadAboutProject(ActionEvent event) throws IOException {
        inactivityTimer.cancel();
        Utility.loadAboutProject("Download Project Definition", Main.getStage(), "/Printer/runwayprojectdefinition.pdf");
        resetInactivityTimer();
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
        Utility.loadAboutProject("Download User Guide", Main.getStage(), "/Printer/UserGuide.pdf");
        resetInactivityTimer();
    }


    @FXML
    public void goUserManager(ActionEvent event) throws Exception {
        inactivityTimer.cancel();
        Main.getStage().close();
        new UserManager(Main.getUsername()).start(new Stage());
        addNotificationLabel("Status: Go to User Manager");
    }

    @FXML
    public void handleReset(ActionEvent event) throws IOException {
        reset();
    }

    private void reset () throws IOException {
        resetInactivityTimer();
        boolean flag = new Confirmation().confirm("Are you sure you want to reset the system?", "Warning: This action cannot be undone.\nAll inputs and selections will be cleared.");
        Main.setReset(true);
        if(flag) {
            beforeCalculation = true;
            physRunwayItem.set(null);
            airportItem.set(null);
            obstacleProperty.set(null);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Main.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            if(MainController.Theme.getState()){
                darkModePng.setVisible(true);
                lightModePng.setVisible(false);
                modeButtonManager.setStyle("-fx-background-color: rgb(49,73,88); -fx-background-radius:50");
            } else{
                lightModePng.setVisible(true);
                darkModePng.setVisible(false);
                modeButtonManager.setStyle("-fx-background-color: rgb(149,195,240); -fx-background-radius: 50");
            }

            if(MainController.Theme.getState()){
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/MainStylesheetDark.css")).toExternalForm());
            } else{
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/MainStylesheet.css")).toExternalForm());
            }

            Main.setClassScene(scene);

            Stage stage = Main.getStage();
            stage.setTitle("SEG Runway Project");
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    public void exportAirport(ActionEvent event) throws IOException, ParserConfigurationException, TransformerException {
        inactivityTimer.cancel();
        Utility.exportAirport(Main.getStage(), FXCollections.observableArrayList(airportMap.get(Main.getAirportID())));
        resetInactivityTimer();
    }

    public void refreshTab(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/TopView.fxml"));
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/FXML/SideView.fxml"));
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/FXML/SimultaneousView.fxml"));
            Parent root = loader.load();
            Parent root1 = loader1.load();
            Parent root2 = loader2.load();
            topViewController = loader.getController();
            sideViewController = loader1.getController();
            topViewTab.setContent(root);
            sideViewTab.setContent(root1);
            simultaneousViewTab.setContent(root2);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void exportSideView(ActionEvent event) {
        addNotificationLabel("Status: Export Side View");
        refreshTab();
        inactivityTimer.cancel();
        javafx.scene.Node contentNode = sideViewTab.getContent();
        Utility.exportVisual(contentNode);
        resetInactivityTimer();
    }

    @FXML
    public void exportTopView(ActionEvent event){
        addNotificationLabel("Status: Export Top View");
        refreshTab();
        inactivityTimer.cancel();
        javafx.scene.Node contentNode = topViewTab.getContent();
        Utility.exportVisual(contentNode);
        resetInactivityTimer();
    }

    @FXML
    public void exportSimulView(ActionEvent event){
        addNotificationLabel("Status: Export Simultaneous View");
        refreshTab();
        inactivityTimer.cancel();
        javafx.scene.Node contentNode = simultaneousViewTab.getContent();
        Utility.exportVisual(contentNode);
        resetInactivityTimer();
    }

    @FXML
    public void checkDistFromThreshold(ActionEvent event){
        try {
            double distFromThreshold = Double.parseDouble(distanceThresholdTextField.getText().trim());
            getObstacleSelected().setDistFThreshold(distFromThreshold);
            disFromThreshold.set(distFromThreshold);
            distanceThresholdTextField.setText(distFromThreshold+"");
        } catch (NumberFormatException exception) {
            //display error message
            new Error().showError(distanceThresholdTextField, "Invalid input for distance from threshold\nHint: please input a numerical value", ""+getObstacleSelected().getDistFThreshold());
            addNotificationLabel("Error: Invalid input for distance from threshold! Hint: please input a numerical value");
        }
    }

    @FXML
    public void checkDistFromCentreLine(ActionEvent event){
        try{
            double distFromCentreLine = Double.parseDouble(clDistTextField.getText().trim());
            if(distFromCentreLine < 0){throw new NumberFormatException();}
            getObstacleSelected().setDistFCent(distFromCentreLine);
            disFromCentre.set(distFromCentreLine);
            clDistTextField.setText(distFromCentreLine+"");
        } catch (NumberFormatException exception){
            new Error().showError(clDistTextField, "Invalid input for distance from centre line\nHint: please input a numerical value greater or equal to 0", ""+getObstacleSelected().getDirFromCentre());
            addNotificationLabel("Error: Invalid input for distance from centre line\nHint: please input a numerical value greater or equal to 0");
        }
    }

    @FXML
    public void checkObstacleHeight(ActionEvent event){
        try{
            double height = Double.parseDouble(obstacleHeightField.getText().trim());
            if(height <= 0){throw new NumberFormatException();}
            getObstacleSelected().setHeight(height);
            obstacleHeight.set(height);
            obstacleHeightField.setText(""+height);
        } catch (NumberFormatException e){
            new Error().showError(obstacleHeightField, "Invalid obstacle height, please input a numerical value greater than 0", ""+getObstacleSelected().getHeight());
            addNotificationLabel("Error: Invalid obstacle height, please input a numerical value greater than 0");
        }
    }

    @FXML
    public void checkObstacleWidth(ActionEvent event){
        try{
            double width = Double.parseDouble(obstacleWidthField.getText().trim());
            if(width <= 0){throw new NumberFormatException();}
            getObstacleSelected().setWidth(width);
            obstacleWidth.set(width);
            obstacleWidthField.setText(""+width);
        } catch (NumberFormatException e){
            new Error().showError(obstacleWidthField, "Invalid obstacle width, please input a numerical value greater than 0", ""+getObstacleSelected().getWidth());
            addNotificationLabel("Error: Invalid obstacle width, please input a numerical value greater than 0");
        }
    }

    @FXML
    public void performCalculation(ActionEvent event){
        resetInactivityTimer();
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
            calcBDCmd.setDisable(false);
        } else{
            needRedeclare = false;
            showTable(false);
            new NoRedeclarationNeeded().showNoRedeclarationNeeded();
        }
        calculationBreakdown.setDisable(false);
        generateReport.setDisable(false);
        valueChanged.set(valueChanged.doubleValue() == 1? 0: 1);
        addNotificationLabel( "Status: Calculation performed");
        beforeCalculation = false;
    }

    @FXML
    public void printReport(ActionEvent action) throws DocumentException, IOException {
        addNotificationLabel("Status: Print report");
        refreshTab();
        inactivityTimer.cancel();
        new PDFGenerator(getAirportSelected(), getObstacleSelected(), getPhysRunwaySelected(), topViewTab.getContent(), sideViewTab.getContent(), simultaneousViewTab.getContent());
        resetInactivityTimer();
    }

    @FXML
    public void setStripEnd(ActionEvent event){
        resetInactivityTimer();
        try{
            double stripEnd = Double.parseDouble(stripEndTextField.getText().trim());
            if(stripEnd < 0 || stripEnd > 100){throw new NumberFormatException();}
            PhysicalRunway.setStripEnd(stripEnd);
            addNotificationLabel("Status: Strip End Value Changed to " + stripEnd);
        } catch (NumberFormatException e){
            new Error().showError(stripEndTextField, "Invalid input for strip end \nHint: please input a numerical value within this range 0-100", "60");
            addNotificationLabel("Error: Invalid input for strip end! Hint: please input a numerical value within this range 0-100");
        }
    }

    @FXML
    public void setBlastProtection(ActionEvent event){
        resetInactivityTimer();
        try{
            double blastProtection = Double.parseDouble(blastProtectionField.getText().trim());
            if(blastProtection < 300 || blastProtection > 500){
                throw new NumberFormatException();
            }
            PhysicalRunway.setBlastProtection(blastProtection);
            addNotificationLabel("Status: Blast Protection Value Changed to " + blastProtection);
        } catch (NumberFormatException e){
            new Error().showError(blastProtectionField, "Invalid input for blast protection\nHint: please input a numerical value within this range: 300-500 (for safety purpose)", "300");
            addNotificationLabel("Error: Invalid input for blast protection! Hint: please input a numerical value within this range: 300-500 (for safety purpose)");
        }
    }

    @FXML
    public void setRESA(ActionEvent event){
        resetInactivityTimer();
        try{
            double resa = Double.parseDouble(resaTextField.getText().trim());
            if(resa < 240 || resa > 500){
                throw new NumberFormatException();
            }
            PhysicalRunway.setResa(resa);
            addNotificationLabel("Status: RESA Value Changed to " + resa);
        } catch (NumberFormatException e){
            new Error().showError(resaTextField, "Invalid input for RESA\nHint: please input a numerical value within this range 240-500 (for safety purpose)", "240");
            addNotificationLabel("Error: Invalid input for RESA! Hint: please input a numerical value within this range 240-500 (for safety purpose)");
        }
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

            Main.getClassScene().getStylesheets().remove("CSS/MainStylesheet.css");
            Main.getClassScene().getStylesheets().add("CSS/MainStylesheetDark.css");


            System.out.println("Dark");
            mode = 0;
            MainController.Theme theme1 = new MainController.Theme();
            Theme.setState(true);

            System.out.println("setState: " + MainController.Theme.getState());
            stateSetter(theme1);
        }else {
            lightModePng.setVisible(true);
            darkModePng.setVisible(false);
            modeButtonManager.setStyle("-fx-background-color: rgb(149,195,240); -fx-background-radius: 50");

            Main.getClassScene().getStylesheets().remove("CSS/MainStylesheetDark.css");
            Main.getClassScene().getStylesheets().add("CSS/MainStylesheet.css");

            System.out.println("Light");
            mode = 1;
            MainController.Theme theme1 = new MainController.Theme();
            Theme.setState(false);

            System.out.println("setState:" + MainController.Theme.getState());
            stateSetter(theme1);

        }
    }

    @FXML
    public void showCalculationBreakdown(ActionEvent event) throws IOException {
        inactivityTimer.cancel();
        System.out.println("breakdown:" + Theme.getState());
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CalculationBreakdown.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/MainStylesheet.css")).toExternalForm());
        stage.setTitle("Calculation Breakdown");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        resetInactivityTimer();
    }

    @FXML
    public void setLeftRightDirection(ActionEvent event){
        resetInactivityTimer();
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

    public void loadUsers() {
        for(User user: LoginController.users.values()){
            if(user.getRole() == 2){
                managers.put(user.getUsername(), user);
                managerMap.put(user.getUsername(), MainController.airportMap.get(user.getAirportID()));
            } else if(user.getRole() == 3){
                ArrayList<User> list = new ArrayList<>();
                if(users.containsKey(airportMap.get(user.getAirportID()))){
                    list = users.get(airportMap.get(user.getAirportID()));
                }
                list.add(user);
                users.put(MainController.airportMap.get(user.getAirportID()), list);
            }
        }
    }


    //this function read from a xml file and instantiate list of airports available
    public void loadAirports(String file) throws Exception {
        airports = FXCollections.observableArrayList();
        airportMap = new HashMap<>();
        airportNames = FXCollections.observableArrayList();
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
                String reference = airportElement.getElementsByTagName("ID").item(0).getTextContent();
                // Get the airport name
                String airportName = airportElement.getElementsByTagName("name").item(0).getTextContent();
                airportNames.add(airportName);
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

                String manager = airportElement.getElementsByTagName("user").item(0).getTextContent();


                Airport airport = new Airport(reference, airportName, physicalRunways, manager);
                airportMap.put(airport.getID(), airport);
                getAirports().add(airport);
            }
        }
        airports.sort(Comparator.comparing(Airport::getName));
    }

    public void addAirportEvent() {
        if(Main.getRole() == 1){
            airportMenu.getItems().clear();
            for(Airport airport: airports){
                MenuItem airportMenuItem = new MenuItem(airport.getName());
                airportMenuItem.setStyle("-fx-font-family: Verdana; -fx-font-size: 16px; -fx-text-fill: black;");
                airportMenuItem.setOnAction(e -> {
                    resetInactivityTimer();
                    if(airport != getAirportSelected()){
                        airportItem.set(airport);
                        physicalRunwayMenu.getItems().clear();
                        getAirportMenu().setText(airport.getName());
                        physicalRunwayMenu.setText("Select Physical Runway");
                        physicalRunwayMenu.setDisable(false);
                        performCalculationButton.setDisable(true);
                        obstaclesEditing(true);
                        addPhysicalRunwayEvent(airport);
                    }
                });
                getAirportMenu().getItems().add(airportMenuItem);
            }
        } else{
            addPhysicalRunwayEvent(airportMap.get(Main.getAirportID()));
        }

    }

    public void addPhysicalRunwayEvent(Airport airport){
        for(PhysicalRunway runway: airport.getPhysicalRunways()){
            MenuItem runwayMenuItem = new MenuItem(runway.getName());
            runwayMenuItem.setOnAction(f -> {
                resetInactivityTimer();
                if(runway != getPhysRunwaySelected()){
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
                }

            });
            runwayMenuItem.setStyle("-fx-font-family: Verdana; -fx-font-size: 16px; -fx-text-fill: black;");
            physicalRunwayMenu.getItems().add(runwayMenuItem);
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

            revisedCol1.setCellFactory(cell -> new BlackTableCell<>());
            revisedCol2.setCellFactory(cell -> new BlackTableCell<>());

        } else{
            if(needRedeclare){
                data1.add(new Parameter("TORA (m)", String.valueOf(logRunway1.getTora()), String.valueOf(logRunway1.getNewTora())));
                data1.add(new Parameter("TODA (m)", String.valueOf(logRunway1.getToda()), String.valueOf(logRunway1.getNewToda())));
                data1.add(new Parameter("ASDA (m)", String.valueOf(logRunway1.getAsda()), String.valueOf(logRunway1.getNewAsda())));
                data1.add(new Parameter("LDA (m)", String.valueOf(logRunway1.getLda()), String.valueOf(logRunway1.getNewLda())));
            } else{
                data1.add(new Parameter("TORA (m)", String.valueOf(logRunway1.getTora()), "-"));
                data1.add(new Parameter("TODA (m)", String.valueOf(logRunway1.getToda()), "-"));
                data1.add(new Parameter("ASDA (m)", String.valueOf(logRunway1.getAsda()), "-"));
                data1.add(new Parameter("LDA (m)", String.valueOf(logRunway1.getLda()), "-"));
            }

            if(needRedeclare){
                data2.add(new Parameter("TORA (m)", String.valueOf(logRunway2.getTora()), String.valueOf(logRunway2.getNewTora())));
                data2.add(new Parameter("TODA (m)", String.valueOf(logRunway2.getToda()), String.valueOf(logRunway2.getNewToda())));
                data2.add(new Parameter("ASDA (m)", String.valueOf(logRunway2.getAsda()), String.valueOf(logRunway2.getNewAsda())));
                data2.add(new Parameter("LDA (m)", String.valueOf(logRunway2.getLda()), String.valueOf(logRunway2.getNewLda())));
            } else{
                data2.add(new Parameter("TORA (m)", String.valueOf(logRunway2.getTora()), "-"));
                data2.add(new Parameter("TODA (m)", String.valueOf(logRunway2.getToda()), "-"));
                data2.add(new Parameter("ASDA (m)", String.valueOf(logRunway2.getAsda()), "-"));
                data2.add(new Parameter("LDA (m)", String.valueOf(logRunway2.getLda()), "-"));
            }

            if(needRedeclare){
                revisedCol1.setCellFactory(cell -> new BlueTableCell<>());
                revisedCol2.setCellFactory(cell -> new BlueTableCell<>());
            } else{
                revisedCol1.setCellFactory(cell -> new BlackTableCell<>());
                revisedCol2.setCellFactory(cell -> new BlackTableCell<>());
            }
        }

        leftTableView.setItems(data1);
        rightTableView.setItems(data2);
    }

    public static class BlueTableCell<T> extends TableCell<T, String> {
        public BlueTableCell() {
            setTextFill(Color.DARKBLUE);
        }

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
    }

    public static class BlackTableCell<T> extends TableCell<T, String> {
        public BlackTableCell() {
            setTextFill(Color.BLACK);
        }

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
        obstacles = FXCollections.observableArrayList();
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
            obstacleMenuItem.setStyle("-fx-font-family: Verdana; -fx-font-size: 16px; -fx-text-fill: black;");
            obstacleMenuItem.setOnAction(e -> {
                resetInactivityTimer();
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

    public void initializeNotification( Pane pane, ScrollPane scrollPane, VBox vBox) {
        pane.setMinHeight(15);
        scrollPane.setMinHeight(0);
        scrollPane.setMaxHeight(1080);
        //set the scroll pane to the latest notification
        vBox.heightProperty().addListener((observableValue, number, t1) -> {
            scrollPane.setVvalue(scrollPane.getVmax());
        } );
        pane.setOnMousePressed(mouseEvent -> {
            resetInactivityTimer();
            double y = mouseEvent.getY();
            pane.setOnMouseDragged(event -> {
                //-ve when up, +ve when down
                double extension = event.getY() - y;
                double newPaneH = pane.getPrefHeight() - extension;
                double newSPaneH = scrollPane.getPrefHeight() - extension;
                double newPaneY = pane.getLayoutY() + extension;
                //set new Height and Layout
                pane.setPrefHeight(newPaneH);
                pane.setLayoutY(newPaneY);
                scrollPane.setPrefHeight(newSPaneH);
                getNotiVBox().setPrefHeight(newSPaneH);
                event.consume();
            });
            mouseEvent.consume();
        });
        //reset to bottom if our of bound
        pane.setOnMouseReleased(releaseEvent -> {
            resetInactivityTimer();
            double oriPaneY = 759;
            if (pane.getLayoutY() >= oriPaneY  || pane.getLayoutY() < 0 ){
                resetNotificationBar(getNotiPane(),getNotiScrollPane());
            }
        });
        getNotificationLabel().setOnMouseEntered(mouseEvent -> {
            resetInactivityTimer();
            getNotificationLabel().setOpacity(1.0);
            mouseEvent.consume();
        });
        getNotificationLabel().setOnMouseExited(mouseEvent -> {
            resetInactivityTimer();
            fadeTransition(getNotificationLabel());
            mouseEvent.consume();
        });
    }

    //add notification into history and set text into notification label
    public void addNotificationLabel(String string) {
        Label label = new Label(Utility.getDateTimeNow() + "\t\t"+ string);
        //label.setPrefHeight(40);
        label.setTextFill(Color.rgb(237, 92, 92));
        label.setPadding(new Insets(5));
        Font font = Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR,12);
        label.setFont(font);
        getNotiVBox().getChildren().add(label);
        getNotificationLabel().setText(string);
        fadeTransition(getNotificationLabel());
    }

    public void fadeTransition(Label label){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), label);
        //set opacity
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
    }

    public void resetNotificationBar(Pane pane, ScrollPane scrollPane){
        double oriPaneY = 759;
        double oriPaneH = 25;
        pane.setLayoutY(oriPaneY);
        pane.setPrefHeight(oriPaneH);
        scrollPane.setPrefHeight(0);
    }

    public void resetView(AnchorPane pane, Pane dragPane){
        resetInactivityTimer();
        dragPane.setTranslateX(0);
        dragPane.setTranslateY(0);
        pane.setScaleX(1);
        pane.setScaleY(1);
        pane.setRotate(0);
    }

    public void handleResetView(ActionEvent actionEvent) {
        resetView();
    }

    private void resetView(){
        //Variables
        AnchorPane topViewAnchorPane = getTopViewController().getTopDownRunwayPane();
        AnchorPane sideViewAnchorPane = getSideViewController().getSideOnPane();
        Pane topViewDragPane  = getTopViewController().getDragPane();
        Pane sideViewDragPane = getSideViewController().getDragPane();
        try {
            resetView(topViewAnchorPane,topViewDragPane);
            resetView(sideViewAnchorPane,sideViewDragPane);
            getTopViewController().initializeCompass();
            topViewController.rotateRunway();
        }catch (Exception e){
            System.out.println(e);
        }
        addNotificationLabel("Status: View Reset");
    }

    int clickCount = 0;
    public void showNotibar(ActionEvent actionEvent) {
        if (clickCount%2 ==0){
            notificationButton.setVisible(false);
            notiPane.setVisible(true);
            notificationLabel.setVisible(false);
            resetNotificationBar(getNotiPane(),getNotiScrollPane());
        }else {
            notificationButton.setVisible(true);
            notiPane.setVisible(false);
            notificationLabel.setVisible(true);
        }
        clickCount++;
    }

    public void extendNotiBar(ActionEvent actionEvent) {
        notificationButton.setVisible(true);
        resetInactivityTimer();
        resetNotificationBar(notiPane,notiScrollPane);
        notiPane.setVisible(false);
        notificationLabel.setVisible(true);
        clickCount++;
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


    // Reference https://blog.idrsolutions.com/tutorial-how-to-setup-key-combinations-in-javafx/
    public void initShortcut() {
        // Shortcut Keys
        KeyCombination RESET_VIEW_KEY = new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN);
        KeyCombination RESET_CALCULATOR_KEY = new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN,KeyCombination.SHIFT_DOWN);
        KeyCombination CALCULATION_BREAKDOWN_KEY = new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN);
        KeyCombination AIRPORT_MANAGER_KEY = new KeyCodeCombination(KeyCode.A,KeyCombination.SHORTCUT_DOWN);
        KeyCombination USER_MANAGER_KEY = new KeyCodeCombination(KeyCode.U,KeyCombination.SHORTCUT_DOWN);
        KeyCombination NOTIFICATION_HISTORY_KEY = new KeyCodeCombination(KeyCode.H,KeyCombination.SHORTCUT_DOWN);
        KeyCombination SWITCH_TAB_KEY = new KeyCodeCombination(KeyCode.TAB,KeyCombination.SHORTCUT_DOWN);
        KeyCombination LOGOUT_KEY = new KeyCodeCombination(KeyCode.L,KeyCombination.SHORTCUT_DOWN,KeyCombination.SHIFT_DOWN);
        KeyCombination ABOUT_PROJECT_KEY = new KeyCodeCombination(KeyCode.I,KeyCombination.SHORTCUT_DOWN);
        KeyCombination COPYRIGHT = new KeyCodeCombination(KeyCode.C,KeyCombination.SHIFT_DOWN);
        KeyCombination USERGUIDE = new KeyCodeCombination(KeyCode.G,KeyCombination.SHIFT_DOWN);
        KeyCombination EXPORT_SIDE_VIEW_KEY = new KeyCodeCombination(KeyCode.E,KeyCombination.SHORTCUT_DOWN);
        KeyCombination EXPORT_TOP_VIEW_KEY = new KeyCodeCombination(KeyCode.E,KeyCombination.ALT_DOWN,KeyCombination.SHORTCUT_DOWN);
        KeyCombination EXPORT_SIMULTANEOUS_VIEW_KEY = new KeyCodeCombination(KeyCode.E,KeyCombination.ALT_DOWN,KeyCombination.SHIFT_DOWN,KeyCombination.SHORTCUT_DOWN);
        KeyCombination EXPORT_REPORT_KEY = new KeyCodeCombination(KeyCode.E,KeyCombination.SHIFT_DOWN);
        KeyCombination ZOOM_IN_KEY = new KeyCodeCombination(KeyCode.EQUALS,KeyCombination.SHORTCUT_DOWN);
        KeyCombination ZOOM_OUT_KEY = new KeyCodeCombination(KeyCode.MINUS,KeyCombination.SHORTCUT_DOWN);
        KeyCombination ROTATE_RIGHT_KEY = new KeyCodeCombination(KeyCode.COMMA, KeyCombination.SHORTCUT_DOWN);
        KeyCombination ROTATE_LEFT_KEY = new KeyCodeCombination(KeyCode.PERIOD, KeyCombination.SHORTCUT_DOWN);
        KeyCombination DRAG_UP_KEY = new KeyCodeCombination(KeyCode.UP,KeyCombination.SHORTCUT_DOWN);
        KeyCombination DRAG_DOWN_KEY = new KeyCodeCombination(KeyCode.DOWN,KeyCombination.SHORTCUT_DOWN);
        KeyCombination DRAG_LEFT_KEY = new KeyCodeCombination(KeyCode.LEFT,KeyCombination.SHORTCUT_DOWN);
        KeyCombination DRAG_RIGHT_KEY = new KeyCodeCombination(KeyCode.RIGHT,KeyCombination.SHORTCUT_DOWN);
        KeyCombination THEME_1_KEY = new KeyCodeCombination(KeyCode.F1);
        KeyCombination THEME_2_KEY = new KeyCodeCombination(KeyCode.F2);
        KeyCombination THEME_3_KEY = new KeyCodeCombination(KeyCode.F3);


        //reset View
        resetViewCmd.setAccelerator(RESET_VIEW_KEY);
        resetViewCmd.setOnAction(this::handleResetView);
        //reset calculator
        resetCmd.setAccelerator(RESET_CALCULATOR_KEY);
        resetCmd.setOnAction(actionEvent -> {
            try {
                handleReset(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        //show calculation breakdown
        calcBDCmd.setAccelerator(CALCULATION_BREAKDOWN_KEY);
        calcBDCmd.setOnAction(actionEvent -> {
            try {
                showCalculationBreakdown(actionEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        //go to airport manager
        airportManager.setAccelerator(AIRPORT_MANAGER_KEY);
        //go to user manager
        userManager.setAccelerator(USER_MANAGER_KEY);
        //show notification history
        notiHistoryCmd.setAccelerator(NOTIFICATION_HISTORY_KEY);
        notiHistoryCmd.setOnAction(this::showNotibar);
        //allow switch visual tab
        tabCmd.setAccelerator(SWITCH_TAB_KEY);
        tabCmd.setOnAction(actionEvent -> {
            int currentTabIndex = visualPane.getSelectionModel().getSelectedIndex();
            int nextTabIndex = (currentTabIndex + 1) % visualPane.getTabs().size();
            visualPane.getSelectionModel().select(nextTabIndex);
            actionEvent.consume();
        });

        logoutItem.setAccelerator(LOGOUT_KEY);
        aboutProject.setAccelerator(ABOUT_PROJECT_KEY);
        userGuide.setAccelerator(USERGUIDE);
        copyright.setAccelerator(COPYRIGHT);
        regenerationReport.setAccelerator(EXPORT_SIDE_VIEW_KEY);
        regenerationReport1.setAccelerator(EXPORT_TOP_VIEW_KEY);
        regenerationReport2.setAccelerator(EXPORT_SIMULTANEOUS_VIEW_KEY);
        generateReport.setAccelerator(EXPORT_REPORT_KEY);

        Pane topPane = topViewController.getTopDownRunwayPane();
        ZoomInCmd.setAccelerator(ZOOM_IN_KEY);
        ZoomInCmd.setOnAction(actionEvent -> {
            double zoomInAmount = +0.05;
            double zoomInLimit = 2;
            if (topPane.getScaleX() + zoomInAmount < zoomInLimit){
                setPaneScale(zoomInAmount);
            }
        });

        ZoomOutCmd.setAccelerator(ZOOM_OUT_KEY);
        ZoomOutCmd.setOnAction(actionEvent -> {
            double zoomOutAmount = -0.05;
            double zoomOutLimit = 0.5;

            if (topPane.getScaleX() + zoomOutAmount > zoomOutLimit){
                setPaneScale(zoomOutAmount);
            }
        });

        Pane compass = topViewController.getCompass();
        Label compassDegree = topViewController.getCompassDegree();
        RotateLCmd.setAccelerator(ROTATE_LEFT_KEY);
        RotateLCmd.setOnAction(actionEvent -> {
            double topPaneAngle = topPane.getRotate()%360+5;
            double compassAngle = compass.getRotate()%360+5;
            topPane.setRotate(topPaneAngle);
            compass.setRotate(compassAngle);
            compassDegree.setText(Math.round(compassAngle *10)/10.0 + "°");
        });
        RotateRCmd.setAccelerator(ROTATE_RIGHT_KEY);
        RotateRCmd.setOnAction(actionEvent -> {
            double topPaneAngle = topPane.getRotate()%360-5;
            double compassAngle = compass.getRotate()%360-5;
            topPane.setRotate(topPaneAngle);
            compass.setRotate(compassAngle);
            compassDegree.setText(Math.round(compassAngle *10)/10.0 + "°");
        });

        final int dragSens = 5;
        upCmd.setAccelerator(DRAG_UP_KEY);
        upCmd.setOnAction(actionEvent -> {
            setPaneTranslation(0,-dragSens);
        });
        downCmd.setAccelerator(DRAG_DOWN_KEY);
        downCmd.setOnAction(actionEvent -> {
            setPaneTranslation(0,+dragSens);
        });
        leftCmd.setAccelerator(DRAG_LEFT_KEY);
        leftCmd.setOnAction(actionEvent -> {
            setPaneTranslation(-dragSens,0);
        });
        rightCmd.setAccelerator(DRAG_RIGHT_KEY);
        rightCmd.setOnAction(actionEvent -> {
            setPaneTranslation(+dragSens,0);
        });

        defaultTheme.setDisable(true);
        defaultTheme.setAccelerator(THEME_1_KEY);
        defaultTheme.setOnAction(this::toDefaultTheme);
        rgTheme.setAccelerator(THEME_2_KEY);
        rgTheme.setOnAction(this::toRGTheme);
        byTheme.setAccelerator(THEME_3_KEY);
        byTheme.setOnAction(this::toBYTheme);
    }

    private void setPaneScale(double d){
        Pane topPane = topViewController.getTopDownRunwayPane();
        Pane sidePane = sideViewController.getSideOnPane();
        topPane.setScaleX(topPane.getScaleX()+d);
        topPane.setScaleY(topPane.getScaleY()+d);
        sidePane.setScaleX(sidePane.getScaleX()+d);
        sidePane.setScaleY(sidePane.getScaleY()+d);
    }
    private void setPaneTranslation(double x,double y){
        Pane topDragPane = topViewController.getDragPane();
        Pane sideDragPane = sideViewController.getDragPane();
        topDragPane.setTranslateX(topDragPane.getTranslateX() + x);
        sideDragPane.setTranslateX(sideDragPane.getTranslateX() + x);
        topDragPane.setTranslateY(sideDragPane.getTranslateY() + y);
        sideDragPane.setTranslateY(sideDragPane.getTranslateY() + y);
    }



}