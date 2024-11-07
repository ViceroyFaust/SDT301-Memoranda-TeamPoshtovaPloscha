package memoranda.busSchedule.models;

import nu.xom.*;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TourManager {

    // Method to save a list of Tour objects to an XML file
    public static void saveTours(List<Tour> tours, String filePath) {
        try {
            Element root = new Element("Tours");

            for (Tour tour : tours) {
                Element tourElement = new Element("Tour");

                Element id = new Element("id");
                id.appendChild(String.valueOf(tour.getId()));
                tourElement.appendChild(id);

                Element name = new Element("name");
                name.appendChild(tour.getName() != null ? tour.getName() : "Unknown");
                tourElement.appendChild(name);

                Element busId = new Element("busId");
                if (tour.getBus() != null) {
                    busId.appendChild(String.valueOf(tour.getBus().getId()));
                } else {
                    busId.appendChild("0"); // Default value if bus is null
                }
                tourElement.appendChild(busId);

                Element routeId = new Element("routeId");
                if (tour.getRoute() != null) {
                    routeId.appendChild(String.valueOf(tour.getRoute().getId()));
                } else {
                    routeId.appendChild("0"); // Default value if route is null
                }
                tourElement.appendChild(routeId);

                root.appendChild(tourElement);
            }

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
    public static List<Tour> loadTours(String filePath, List<Bus> availableBuses, List<Route> availableRoutes) {
        List<Tour> tours = new ArrayList<>();

        try {
            Builder builder = new Builder();
            Document doc = builder.build(new FileInputStream(filePath));

            Elements tourElements = doc.getRootElement().getChildElements("Tour");

            for (int i = 0; i < tourElements.size(); i++) {
                Element tourElement = tourElements.get(i);

                Tour tour = new Tour();

                // Parse and set ID
                int id = Integer.parseInt(tourElement.getFirstChildElement("id").getValue());
                tour.setId(id);

                // Parse and set Name
                String name = tourElement.getFirstChildElement("name").getValue();
                tour.setName(name);

                // Parse and set Bus
                int busId = Integer.parseInt(tourElement.getFirstChildElement("busId").getValue());
                Bus bus = availableBuses.stream()
                        .filter(b -> b.getId() == busId)
                        .findFirst()
                        .orElse(null);
                tour.setBus(bus);

                // Parse and set Route
                int routeId = Integer.parseInt(tourElement.getFirstChildElement("routeId").getValue());
                Route route = availableRoutes.stream()
                        .filter(r -> r.getId() == routeId)
                        .findFirst()
                        .orElse(null);
                tour.setRoute(route);

                tours.add(tour);
            }

            System.out.println("Tour data has been loaded from " + filePath);
        } catch (ParsingException | IOException e) {
            e.printStackTrace();
        }

        return tours;
    }

    // Method to verify if the tour data was stored correctly
    public static boolean verifyTourStorage(String filePath, List<Tour> originalTours, List<Bus> availableBuses, List<Route> availableRoutes) {
        List<Tour> loadedTours = loadTours(filePath, availableBuses, availableRoutes);

        if (loadedTours.size() != originalTours.size()) {
            return false; // Size mismatch
        }

        for (int i = 0; i < originalTours.size(); i++) {
            Tour original = originalTours.get(i);
            Tour loaded = loadedTours.get(i);

            if (original.getId() != loaded.getId() ||
                    !original.getName().equals(loaded.getName()) ||
                    (original.getBus() != null && loaded.getBus() != null && original.getBus().getId() != loaded.getBus().getId()) ||
                    (original.getRoute() != null && loaded.getRoute() != null && original.getRoute().getId() != loaded.getRoute().getId())) {
                return false; // Mismatch in data
            }
        }
        return true; // Data matches
    }
}