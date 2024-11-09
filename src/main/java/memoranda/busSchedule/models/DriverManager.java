package memoranda.busSchedule.models;

import nu.xom.*;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DriverManager {

    public static Element driverXML(Driver driver) {
        Element driverElement = new Element("Driver");

        Element id = new Element("id");
        id.appendChild(String.valueOf(driver.getId()));
        driverElement.appendChild(id);

        Element name = new Element("name");
        name.appendChild(driver.getName() != null ? driver.getName() : "Unknown");
        driverElement.appendChild(name);

        Element phoneNumber = new Element("phoneNumber");
        phoneNumber.appendChild(driver.getPhoneNumber() != null ? driver.getPhoneNumber() : "Unknown");
        driverElement.appendChild(phoneNumber);

        return driverElement;
    }

    public static Element driversXML(List<Driver> drivers) {
        Element root = new Element("Drivers");

        Element driverElement;
        for (Driver driver : drivers) {
            driverElement = driverXML(driver);
            root.appendChild(driverElement);
        }
        return root;
    }

    public static Driver xmlDriver(Element driverElement) {
        // Parse and set ID
        int id = Integer.parseInt(driverElement.getFirstChildElement("id").getValue());

        // Parse and set Name
        String name = driverElement.getFirstChildElement("name").getValue();

        // Parse and set Phone Number
        String phoneNumber = driverElement.getFirstChildElement("phoneNumber").getValue();

        return new Driver(id, name, phoneNumber);
    }

    public static List<Driver> xmlDrivers(Elements driverElements) {
        List<Driver> drivers = new ArrayList<>();
        for (int i = 0; i < driverElements.size(); i++) {
            Driver driver = xmlDriver(driverElements.get(i));
            drivers.add(driver);
        }
        return drivers;
    }

    // Method to save a list of Driver objects to an XML file
    public static void saveDrivers(List<Driver> drivers, String filePath) {
        try {
            Element root = driversXML(drivers);
            Document doc = new Document(root);
            FileOutputStream out = new FileOutputStream(filePath);
            Serializer serializer = new Serializer(out, "UTF-8");
            serializer.setIndent(4);
            serializer.write(doc);
            out.close();

            System.out.println("Driver data has been saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load a list of Driver objects from an XML file
    public static List<Driver> loadDrivers(String filePath) {
        List<Driver> drivers = new ArrayList<>();

        try {
            Builder builder = new Builder();
            Document doc = builder.build(new FileInputStream(filePath));

            Elements driverElements = doc.getRootElement().getChildElements("Driver");
            drivers = xmlDrivers(driverElements);

            System.out.println("Driver data has been loaded from " + filePath);
        } catch (ParsingException | IOException e) {
            e.printStackTrace();
        }

        return drivers;
    }
}