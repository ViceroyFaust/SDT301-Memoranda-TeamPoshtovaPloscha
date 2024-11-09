package memoranda.busSchedule.models;

import nu.xom.*;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TourManager {
    public static Element tourXML(Tour tour) {
        Element tourElement = new Element("Tour");

        Element id = new Element("id");
        id.appendChild(String.valueOf(tour.getId()));
        tourElement.appendChild(id);

        Element name = new Element("name");
        name.appendChild(tour.getName());
        tourElement.appendChild(name);

        Element bus = BusManager.busXML(tour.getBus());
        tourElement.appendChild(bus);

        Element route = RouteManager.routeXML(tour.getRoute());
        tourElement.appendChild(route);

        return tourElement;
    }

    public static Element toursXML(List<Tour> tours) {
        Element root = new Element("Tours");

        for (Tour tour : tours) {
            root.appendChild(tourXML(tour));
        }

        return root;
    }

    public static Tour xmlTour(Element tour) {
        int id = Integer.parseInt(tour.getFirstChildElement("id").getValue());
        String name = tour.getFirstChildElement("name").getValue();
        Bus bus = BusManager.xmlBus(tour.getFirstChildElement("Bus"));
        Route route = RouteManager.xmlRoute(tour.getFirstChildElement("Route"));
        return new Tour(id, name, bus, route);
    }

    public static List<Tour> xmlTours(Elements tourElements) {
        List<Tour> tours = new ArrayList<>();

        for (int i = 0; i < tourElements.size(); i++) {
            tours.add(xmlTour(tourElements.get(i)));
        }

        return tours;
    }

    // Method to save a list of Tour objects to an XML file
    public static void saveTours(List<Tour> tours, String filePath) {
        try {
            Element root = toursXML(tours);

            Document doc = new Document(root);
            FileOutputStream out = new FileOutputStream(filePath);
            Serializer serializer = new Serializer(out, "UTF-8");
            serializer.setIndent(4);
            serializer.write(doc);
            out.close();

            System.out.println("Tour data has been saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load a list of Tour objects from an XML file
    public static List<Tour> loadTours(String filePath) {
        List<Tour> tours = new ArrayList<>();
        try {
            Builder builder = new Builder();
            Document doc = builder.build(new FileInputStream(filePath));

            Elements tourElements = doc.getRootElement().getChildElements("Tour");

            tours = xmlTours(tourElements);

            System.out.println("Tour data has been loaded from " + filePath);
        } catch (ParsingException | IOException e) {
            e.printStackTrace();
        }

        return tours;
    }
}