package Model.Helper;

import Model.Airport;
import Model.LogicalRunway;
import Model.PhysicalRunway;
import Model.User;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XMLParserWriter {
    public static void writeToFile(ObservableList<Airport> airports, String fileName) throws ParserConfigurationException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource domSource = new DOMSource(XMLParserWriter.airportToXML(airports));
        StreamResult streamResult = new StreamResult(new File(fileName));
        transformer.transform(domSource, streamResult);
    }

    public static void updateUserXML(ObservableList<User> users, String fileName) throws ParserConfigurationException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource domSource = new DOMSource(XMLParserWriter.userToXML(users));
        StreamResult streamResult = new StreamResult(new File(fileName));
        transformer.transform(domSource, streamResult);
    }

    public static Document airportToXML(ObservableList<Airport> airports) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        document.setXmlStandalone(true);

        Element root = document.createElement("airports");
        document.appendChild(root);

        for (Airport airport : airports) {
            Element airportElement = document.createElement("airport");
            root.appendChild(airportElement);

            Element idElement = document.createElement("ID");
            idElement.appendChild(document.createTextNode(airport.getID()));
            airportElement.appendChild(idElement);

            Element nameElement = document.createElement("name");
            nameElement.appendChild(document.createTextNode(airport.getName()));
            airportElement.appendChild(nameElement);

            Element physicalRunwaysElement = document.createElement("physicalRunways");
            airportElement.appendChild(physicalRunwaysElement);

            ObservableList<PhysicalRunway> physicalRunways = airport.getPhysicalRunways();
            for (PhysicalRunway physicalRunway : physicalRunways) {
                Element physicalRunwayElement = document.createElement("physicalRunway");
                physicalRunwayElement.setAttribute("name", physicalRunway.getName());
                physicalRunwaysElement.appendChild(physicalRunwayElement);

                ObservableList<LogicalRunway> logicalRunways = physicalRunway.getLogicalRunways();
                for (LogicalRunway logicalRunway : logicalRunways) {
                    Element logicalRunwayElement = document.createElement("logicalRunway");
                    logicalRunwayElement.setAttribute("designator", logicalRunway.getDesignator());
                    logicalRunwayElement.setAttribute("tora", String.valueOf(logicalRunway.getTora()));
                    logicalRunwayElement.setAttribute("toda", String.valueOf(logicalRunway.getToda()));
                    logicalRunwayElement.setAttribute("asda", String.valueOf(logicalRunway.getAsda()));
                    logicalRunwayElement.setAttribute("lda", String.valueOf(logicalRunway.getLda()));
                    physicalRunwayElement.appendChild(logicalRunwayElement);
                }
            }

            Element userElement = document.createElement("user");
            userElement.appendChild(document.createTextNode(airport.getManager()));
            airportElement.appendChild(userElement);
        }
        return document;
    }

    public static Document userToXML(ObservableList<User> users) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        document.setXmlStandalone(true);

        Element root = document.createElement("users");
        document.appendChild(root);

        for (User user : users) {
            Element userElement = document.createElement("user");
            root.appendChild(userElement);

            Element usernameElement = document.createElement("username");
            usernameElement.appendChild(document.createTextNode(user.getUsername()));
            userElement.appendChild(usernameElement);

            Element nameElement = document.createElement("name");
            nameElement.appendChild(document.createTextNode(user.getName()));
            userElement.appendChild(nameElement);

            Element passwordElement = document.createElement("password");
            passwordElement.appendChild(document.createTextNode(user.getPassword()));
            userElement.appendChild(passwordElement);

            Element airportID = document.createElement("airportID");
            airportID.appendChild(document.createTextNode(user.getAirportID()));
            userElement.appendChild(airportID);

            Element roleElement = document.createElement("role");
            roleElement.appendChild(document.createTextNode(user.getRole()+""));
            userElement.appendChild(roleElement);
        }
        return document;
    }
}
