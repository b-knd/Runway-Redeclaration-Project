package Controller;

import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    ObservableList<User> airports = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
//            loadUsers("src/Data/users.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //have the xml ready before writing this writer
    //this function read from a xml file and instantiate list of airports available
//    public void loadUsers(String file) throws Exception {
//        // Create a DocumentBuilder
//        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//
//        // Parse the XML file into a Document
//        Document doc = docBuilder.parse(file);
//
//        // Get a NodeList of all airport elements
//        NodeList userElements = doc.getElementsByTagName("user");
//
//        // Create a list to hold the airports
//        ObservableList<User> users = FXCollections.observableArrayList();
//
//        // Loop over each airport element and create an Airport object
//        for (int i = 0; i < userElements.getLength(); i++) {
//            Node userNode = userElements.item(i);
//
//            if (userNode.getNodeType() == Node.ELEMENT_NODE) {
//                Element userElement = (Element) userNode;
//                // Get the airport name
//                String airportName = userElement.getElementsByTagName("username").item(0).getTextContent();
//                // Create a list to hold the physical runways
//                ObservableList<PhysicalRunway> physicalRunways = FXCollections.observableArrayList();
//                // Get a NodeList of all physical runway elements for the current airport
//                NodeList physRunwayElements = userElement.getElementsByTagName("password");
//                // Loop over each physical runway element and create a PhysicalRunway object
//                for (int j = 0; j < physRunwayElements.getLength(); j++) {
//                    Node physRunwayNode = physRunwayElements.item(j);
//
//                    if (physRunwayNode.getNodeType() == Node.ELEMENT_NODE) {
//                        Element physRunwayElement = (Element) physRunwayNode;
//                        // Get the physical runway name
//                        String physRunwayName = physRunwayElement.getAttribute("name");
//                        // Create a list to hold the logical runways
//                        ObservableList<LogicalRunway> logicalRunways = FXCollections.observableArrayList();
//                        // Get a NodeList of all logical runway elements for the current physical runway
//                        NodeList logRunwayElements = physRunwayElement.getElementsByTagName("logicalRunway");
//                        // Loop over each logical runway element and create a LogicalRunway object
//                        for (int k = 0; k < logRunwayElements.getLength(); k++) {
//                            Node logRunwayNode = logRunwayElements.item(k);
//                            if (logRunwayNode.getNodeType() == Node.ELEMENT_NODE) {
//                                Element logRunwayElement = (Element) logRunwayNode;
//                                // Get the logical runway designator and dimensions
//                                String designator = logRunwayElement.getAttribute("designator");
//                                double tora = Double.parseDouble(logRunwayElement.getAttribute("tora"));
//                                double toda = Double.parseDouble(logRunwayElement.getAttribute("toda"));
//                                double asda = Double.parseDouble(logRunwayElement.getAttribute("asda"));
//                                double lda = Double.parseDouble(logRunwayElement.getAttribute("lda"));
//                                // Create a new LogicalRunway object and add it to the list of logical runways
//                                LogicalRunway logicalRunway = new LogicalRunway(designator, tora, toda, asda, lda);
//                                logicalRunways.add(logicalRunway);
//                            }
//                        }
//                        // Create a new PhysicalRunway object with the logical runways and add it to the list of physical runways
//                        PhysicalRunway physicalRunway = new PhysicalRunway(physRunwayName, logicalRunways);
//                        physicalRunways.add(physicalRunway);
//                    }
//                }
//                // Create a new Airport object with the physical runways and add it to the list of airports
//                Airport airport = new Airport(airportName, physicalRunways);
//                getAirports().add(airport);
//            }
//        }
//        airports.sort(Comparator.comparing(Airport::getName));
//    }
}
