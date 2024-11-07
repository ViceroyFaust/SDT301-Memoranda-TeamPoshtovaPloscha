package memoranda.busSchedule.models;

import nu.xom.*;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BusManager {

    // Method to save a list of Bus objects to an XML file
    public static void saveBuses(List<Bus> buses, String filePath) {
        try {
            Element root = new Element("Buses");

            for (Bus bus : buses) {
                Element busElement = new Element("Bus");

                Element id = new Element("id");
                id.appendChild(String.valueOf(bus.getId()));
                busElement.appendChild(id);

                Element seats = new Element("seats");
                seats.appendChild(String.valueOf(bus.getSeats()));
                busElement.appendChild(seats);

                Element driver = new Element("driver");
                if (bus.getDriver() != null) {
                    driver.appendChild(bus.getDriver().getName()); // Assuming Driver has a getName() method
                } else {
                    driver.appendChild("Unknown"); // Placeholder if driver is null
                }
                busElement.appendChild(driver);

                root.appendChild(busElement);
            }

            Document doc = new Document(root);
            FileOutputStream out = new FileOutputStream(filePath);
            Serializer serializer = new Serializer(out, "UTF-8");
            serializer.setIndent(4);
            serializer.write(doc);
            out.close();

            System.out.println("Bus data has been saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load a list of Bus objects from an XML file
    public static List<Bus> loadBuses(String filePath) {
        List<Bus> buses = new ArrayList<>();

        try {
            Builder builder = new Builder();
            Document doc = builder.build(new FileInputStream(filePath));

            Elements busElements = doc.getRootElement().getChildElements("Bus");

            for (int i = 0; i < busElements.size(); i++) {
                Element busElement = busElements.get(i);

                Bus bus = new Bus();

                // Parse and set ID
                int id = Integer.parseInt(busElement.getFirstChildElement("id").getValue());
                bus.setId(id);

                // Parse and set seats
                int seats = Integer.parseInt(busElement.getFirstChildElement("seats").getValue());
                bus.setSeats(seats);

                // Parse and set driver
                String driverName = busElement.getFirstChildElement("driver").getValue();
                Driver driver = new Driver(); // Ensure Driver class has a default constructor
                driver.setName(driverName); // Assuming Driver has a setName() method
                bus.setDriver(driver);

                buses.add(bus);
            }

            System.out.println("Bus data has been loaded from " + filePath);
        } catch (ParsingException | IOException e) {
            e.printStackTrace();
        }

        return buses;
    }

    // Method to check if the bus data was stored correctly
    public static boolean verifyBusStorage(String filePath, List<Bus> originalBuses) {
        List<Bus> loadedBuses = loadBuses(filePath);

        if (loadedBuses.size() != originalBuses.size()) {
            return false; // Size mismatch
        }

        for (int i = 0; i < originalBuses.size(); i++) {
            Bus original = originalBuses.get(i);
            Bus loaded = loadedBuses.get(i);

            if (original.getId() != loaded.getId() ||
                    original.getSeats() != loaded.getSeats() ||
                    !original.getDriver().getName().equals(loaded.getDriver().getName())) {
                return false; // Mismatch in data
            }
        }
        return true; // Data matches
    }
}
