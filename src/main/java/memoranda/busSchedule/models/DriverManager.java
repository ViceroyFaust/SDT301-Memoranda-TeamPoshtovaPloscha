package memoranda.busSchedule.models;

import nu.xom.*;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DriverManager {

    // Method to save a list of Driver objects to an XML file
    public static void saveDrivers(List<Driver> drivers, String filePath) {
        try {
            Element root = new Element("Drivers");

            Element driverElement;
            for (Driver driver : drivers) {
                driverElement = new Element("Driver");

                Element id = new Element("id");
                id.appendChild(String.valueOf(driver.getId()));
                driverElement.appendChild(id);

                Element name = new Element("name");
                name.appendChild(driver.getName() != null ? driver.getName() : "Unknown");
                driverElement.appendChild(name);

                Element phoneNumber = new Element("phoneNumber");
                phoneNumber.appendChild(driver.getPhoneNumber() != null ? driver.getPhoneNumber() : "Unknown");
                driverElement.appendChild(phoneNumber);

                root.appendChild(driverElement);
            }

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

            for (int i = 0; i < driverElements.size(); i++) {
                Element driverElement = driverElements.get(i);

                // Parse and set ID
                int id = Integer.parseInt(driverElement.getFirstChildElement("id").getValue());

                // Parse and set Name
                String name = driverElement.getFirstChildElement("name").getValue();

                // Parse and set Phone Number
                String phoneNumber = driverElement.getFirstChildElement("phoneNumber").getValue();

                Driver driver = new Driver(id, name, phoneNumber);

                drivers.add(driver);
            }

            System.out.println("Driver data has been loaded from " + filePath);
        } catch (ParsingException | IOException e) {
            e.printStackTrace();
        }

        return drivers;
    }
}