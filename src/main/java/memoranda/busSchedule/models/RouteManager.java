package memoranda.busSchedule.models;

import nu.xom.*;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RouteManager {

    // Method to save a list of Route objects to an XML file
    public static void saveRoutes(List<Route> routes, String filePath) {
        try {
            Element root = new Element("Routes");

            for (Route route : routes) {
                Element routeElement = new Element("Route");

                Element id = new Element("id");
                id.appendChild(String.valueOf(route.getId()));
                routeElement.appendChild(id);

                Element length = new Element("length");
                length.appendChild(String.valueOf(route.getLength()));
                routeElement.appendChild(length);

                Element durationH = new Element("durationH");
                durationH.appendChild(String.valueOf(route.getDurationH()));
                routeElement.appendChild(durationH);

                root.appendChild(routeElement);
            }

            Document doc = new Document(root);
            FileOutputStream out = new FileOutputStream(filePath);
            Serializer serializer = new Serializer(out, "UTF-8");
            serializer.setIndent(4);
            serializer.write(doc);
            out.close();

            System.out.println("Route data has been saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load a list of Route objects from an XML file
    public static List<Route> loadRoutes(String filePath) {
        List<Route> routes = new ArrayList<>();

        try {
            Builder builder = new Builder();
            Document doc = builder.build(new FileInputStream(filePath));

            Elements routeElements = doc.getRootElement().getChildElements("Route");

            for (int i = 0; i < routeElements.size(); i++) {
                Element routeElement = routeElements.get(i);

                Route route = new Route();

                // Parse and set ID
                int id = Integer.parseInt(routeElement.getFirstChildElement("id").getValue());
                route.setId(id);

                // Parse and set Length
                float length = Float.parseFloat(routeElement.getFirstChildElement("length").getValue());
                route.setLength(length);

                // Parse and set Duration in Hours
                float durationH = Float.parseFloat(routeElement.getFirstChildElement("durationH").getValue());
                route.setDurationH(durationH);

                routes.add(route);
            }

            System.out.println("Route data has been loaded from " + filePath);
        } catch (ParsingException | IOException e) {
            e.printStackTrace();
        }

        return routes;
    }

    // Method to verify if the route data was stored correctly
    public static boolean verifyRouteStorage(String filePath, List<Route> originalRoutes) {
        List<Route> loadedRoutes = loadRoutes(filePath);

        if (loadedRoutes.size() != originalRoutes.size()) {
            return false; // Size mismatch
        }

        for (int i = 0; i < originalRoutes.size(); i++) {
            Route original = originalRoutes.get(i);
            Route loaded = loadedRoutes.get(i);

            if (original.getId() != loaded.getId() ||
                    original.getLength() != loaded.getLength() ||
                    original.getDurationH() != loaded.getDurationH()) {
                return false; // Mismatch in data
            }
        }
        return true; // Data matches
    }
}