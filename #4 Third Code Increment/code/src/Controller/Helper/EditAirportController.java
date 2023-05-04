package Controller.Helper;

import Model.Airport;
import Model.LogicalRunway;
import Model.PhysicalRunway;
import View.EditAirport;
import View.Error;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class EditAirportController implements Initializable {
    public static Airport airportSelected;
    public static Airport airportWithNewInfo;
    public static boolean actionCancel = false;

    @FXML
    private Label nameReference;
    @FXML
    private Label managerName;
    @FXML
    private MenuButton runwayMenu;
    @FXML
    private TableView<LogicalRunway> logRunwayTable;
    @FXML
    private TableColumn<LogicalRunway, String> designatorCol;
    @FXML
    private TableColumn<LogicalRunway, Double> todaCol;
    @FXML
    private TableColumn<LogicalRunway, Double> asdaCol;
    @FXML
    private TableColumn<LogicalRunway, Double> ldaCol;
    @FXML
    private TextField toraTextField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;

    public static void setAirportSelected(Airport airportSelected) {
        EditAirportController.airportSelected = airportSelected;
    }

    public EditAirportController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<PhysicalRunway> runways = FXCollections.observableArrayList();
        for(PhysicalRunway physRunway: airportSelected.getPhysicalRunways()){
            ObservableList<LogicalRunway> logicalRunways = FXCollections.observableArrayList();
            for(LogicalRunway logRunway: physRunway.getLogicalRunways()){
                logicalRunways.add(new LogicalRunway(logRunway.getDesignator(), logRunway.getTora(), logRunway.getToda(), logRunway.getAsda(), logRunway.getLda()));
            }
            runways.add(new PhysicalRunway(physRunway.getName(), logicalRunways));
        }
        airportWithNewInfo = new Airport(airportSelected.getID(), airportSelected.getName(), runways, airportSelected.getManager());
        cancelButton.setOnAction(actionEvent -> {
            EditAirportController.actionCancel = true;
            EditAirport.getStage().close();
        });
        saveButton.setOnAction(actionEvent -> {
            EditAirportController.actionCancel = false;
            EditAirport.getStage().close();
        });
        nameReference.setText("Airport Name: "+ airportSelected.getName() + " (" + airportSelected.getID() + ")");
        managerName.setText("Manager: "+airportSelected.getManager());

        for(PhysicalRunway runway: airportWithNewInfo.getPhysicalRunways()){
            toraTextField.setDisable(false);
            toraTextField.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    String input = toraTextField.getText();
                    try{
                        double newTora = Double.parseDouble(input);
                        if(newTora < 0){
                            throw new NumberFormatException();
                        }
                        if(newTora < runway.getLogicalRunways().get(0).getLda() || newTora < runway.getLogicalRunways().get(1).getLda()){
                            new Error().showError(toraTextField, "TORA value must be greater than or equal to LDA for both logical runways. Hint: LDA <= TORA <= ASDA <= TODA", String.valueOf(runway.getLogicalRunways().get(0).getTora()));
                        } else if(newTora > runway.getLogicalRunways().get(0).getAsda() || newTora > runway.getLogicalRunways().get(1).getAsda()){
                            new Error().showError(toraTextField, "TORA value must be smaller than or equal to ASDA for both logical runways. Hint: LDA <= TORA <= ASDA <= TODA", String.valueOf(runway.getLogicalRunways().get(0).getTora()));
                        } else if(newTora > runway.getLogicalRunways().get(0).getToda() || newTora > runway.getLogicalRunways().get(1).getToda()){
                            new Error().showError(toraTextField, "TORA value must be smaller than or equal to ASDA for both logical runways. Hint: LDA <= TORA <= ASDA <= TODA", String.valueOf(runway.getLogicalRunways().get(0).getTora()));
                        } else{
                            runway.getLogicalRunways().get(0).setTora(newTora);
                            runway.getLogicalRunways().get(1).setTora(newTora);
                        }
                    }
                    catch (NumberFormatException e){
                        new Error().showError(toraTextField, "Invalid TORA value. Hint: Please input a numerical value that is greater than 0", String.valueOf(runway.getLogicalRunways().get(0).getTora()));
                    }
                }
            });
            MenuItem runwayMenuItem = new MenuItem(runway.getName());
            runwayMenuItem.setOnAction(f -> {
                runwayMenu.setText(runwayMenuItem.getText());
                toraTextField.setText(""+runway.getLogicalRunways().get(0).getTora());
                designatorCol.setCellValueFactory(
                        new PropertyValueFactory<>("designator")
                );
                todaCol.setCellValueFactory(
                        new PropertyValueFactory<>("toda")
                );
                asdaCol.setCellValueFactory(
                        new PropertyValueFactory<>("asda")
                );
                ldaCol.setCellValueFactory(
                        new PropertyValueFactory<>("lda")
                );

                todaCol.setCellFactory(TextFieldTableCell.forTableColumn(new CustomDoubleStringConverter()));
                todaCol.setOnEditCommit(event -> {
                    try{
                        LogicalRunway logRunway = event.getRowValue();
                        double newToda = event.getNewValue();
                        if(newToda < logRunway.getAsda()){
                            new Error().errorPopUp("TODA value is smaller than ASDA value, invalid clearway. Hint: please input a TODA value that is greater than runway's ASDA value");
                        }
                        logRunway.setToda(event.getNewValue());
                    }catch (NullPointerException ignored){
                        LogicalRunway logRunway = event.getRowValue();
                        logRunway.setToda(event.getOldValue());
                    }
                    logRunwayTable.refresh();
                });

                todaCol.setCellFactory(TextFieldTableCell.forTableColumn(new CustomDoubleStringConverter()));
                todaCol.setOnEditCommit(event -> {
                    try{
                        LogicalRunway logRunway = event.getRowValue();
                        double newToda = event.getNewValue();
                        if(newToda < logRunway.getAsda()){
                            new Error().errorPopUp("TODA value is smaller than ASDA value, invalid clearway. Hint: TODA has to be greater than or equals to both TORA and ASDA");
                        } else if (newToda < logRunway.getTora()){
                            new Error().errorPopUp("TODA value is smaller than TORA value, invalid clearway. Hint: TODA has to be greater than or equals to both TORA and ASDA");
                        }
                        else{
                            logRunway.setToda(event.getNewValue());
                        }
                    }catch (NullPointerException ignored){
                        LogicalRunway logRunway = event.getRowValue();
                        logRunway.setToda(event.getOldValue());
                    }
                    logRunwayTable.refresh();
                });

                asdaCol.setCellFactory(TextFieldTableCell.forTableColumn(new CustomDoubleStringConverter()));
                asdaCol.setOnEditCommit(event -> {
                    try{
                        LogicalRunway logRunway = event.getRowValue();
                        double newAsda = event.getNewValue();
                        if(newAsda < logRunway.getTora()){
                            new Error().errorPopUp("ASDA value is smaller than TORA value, invalid stopway. Hint: please input an ASDA value that is greater than or equals to runway's TORA value");
                        } else{
                            logRunway.setAsda(event.getNewValue());
                        }
                    }catch (NullPointerException ignored){
                        LogicalRunway logRunway = event.getRowValue();
                        logRunway.setAsda(event.getOldValue());
                    }
                    logRunwayTable.refresh();
                });

                ldaCol.setCellFactory(TextFieldTableCell.forTableColumn(new CustomDoubleStringConverter()));
                ldaCol.setOnEditCommit(event -> {
                    try{
                        LogicalRunway logRunway = event.getRowValue();
                        double newLDA = event.getNewValue();
                        if( newLDA > logRunway.getTora()){
                            new Error().errorPopUp("LDA value is greater than TORA value, invalid displaced threshold. Hint: please input a LDA value that is smaller than or equals to runway's TORA value");
                        } else{
                            logRunway.setAsda(event.getNewValue());
                        }
                    }catch (NullPointerException ignored){
                        LogicalRunway logRunway = event.getRowValue();
                        logRunway.setAsda(event.getOldValue());
                    }
                    logRunwayTable.refresh();
                });

                logRunwayTable.setItems(runway.getLogicalRunways());

            });
            runwayMenuItem.setStyle("-fx-font-family: Verdana; -fx-font-size: 16px");
            runwayMenu.getItems().add(runwayMenuItem);
        }

    }


    //a class that will be used to handler double value for cell editing
    public static class CustomDoubleStringConverter extends DoubleStringConverter {
        @Override
        public String toString(Double object) {
            try {
                if(object < 0){
                    throw new NumberFormatException();
                }
                return object.toString();

            } catch (NumberFormatException | NullPointerException e) {
                new Error().errorPopUp("Invalid double value. Hint: please provide a double value that is greater than 0");
            }
            return null;
        }
        @Override
        public Double fromString(String string) {
            try {
                double value = Double.parseDouble(string);
                if(value < 0){
                    throw new NumberFormatException();
                }
                return value;
            } catch (NumberFormatException ignored) {
            }
            return null;
        }
    }





}
