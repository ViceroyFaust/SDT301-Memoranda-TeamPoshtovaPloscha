package memoranda.busSchedule.models;

import nu.xom.*;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BusManager {

    public static Element busXML(Bus bus) {
        Element busElement = new Element("Bus");

        Element id = new Element("id");
        id.appendChild(String.valueOf(bus.getId()));
        busElement.appendChild(id);

        Element seats = new Element("seats");
        seats.appendChild(String.valueOf(bus.getSeats()));
        busElement.appendChild(seats);

        Element driver = DriverManager.driverXML(bus.getDriver());
        busElement.appendChild(driver);

        return busElement;
    }

    public static Element bussesXML(List<Bus> busses) {
        Element root = new Element("Buses");

        Element busElement;
        for (Bus bus : busses) {
            busElement = busXML(bus);
            root.appendChild(busElement);
        }

        return root;
    }

    public static Bus xmlBus(Element busElement) {
        int id = Integer.parseInt(busElement.getFirstChildElement("id").getValue());
        int seats = Integer.parseInt(busElement.getFirstChildElement("seats").getValue());
        Driver driver = DriverManager.xmlDriver(busElement.getFirstChildElement("Driver"));

        return new Bus(id, seats, driver);
    }

    public static List<Bus> xmlBusses(Elements busElements) {
        List<Bus> busses = new ArrayList<>();

        Element busElement;
        for (int i = 0; i < busElements.size(); i++) {
            busElement = busElements.get(i);
            busses.add(xmlBus(busElement));
        }

        return busses;
    }

    // Method to save a list of Bus objects to an XML file
    public static void saveBusses(List<Bus> busses, String filePath) {
        try {
            Element root = bussesXML(busses);
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
    public static List<Bus> loadBusses(String filePath) {
        List<Bus> busses = new ArrayList<>();

        try {
            Builder builder = new Builder();
            Document doc = builder.build(new FileInputStream(filePath));

            Elements busElements = doc.getRootElement().getChildElements("Bus");

            busses = xmlBusses(busElements);

            System.out.println("Bus data has been loaded from " + filePath);
        } catch (ParsingException | IOException e) {
            e.printStackTrace();
        }

        return busses;
    }

}
