package memoranda.busSchedule.models;

import nu.xom.*;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteManager {

    public static Element routeXML(Route route) {
        Element routeElement = new Element("Route");

        Element id = new Element("id");
        id.appendChild(String.valueOf(route.getId()));
        routeElement.appendChild(id);

        Element nodes = NodeManager.nodesXML(Arrays.asList(route.getNodes()));
        routeElement.appendChild(nodes);

        return  routeElement;
    }

    public static Element routesXML(List<Route> routes) {
        Element root = new Element("Routes");

        Element routeElement;
        for (Route route : routes) {
            routeElement = routeXML(route);
            root.appendChild(routeElement);
        }

        return root;
    }

    public static Route xmlRoute(Element routeElement) {
        int id = Integer.parseInt(routeElement.getFirstChildElement("id").getValue());

        List<Node> nodesList = NodeManager.xmlNodes(routeElement.getFirstChildElement("Nodes").getChildElements("Node"));
        Node[] nodes = nodesList.toArray(new Node[0]);

        return new Route(id, nodes);
    }

    public static List<Route> xmlRoutes(Elements routeElements) {
        List<Route> routes = new ArrayList<>();

        for (int i = 0; i < routeElements.size(); i++) {
            routes.add(xmlRoute(routeElements.get(i)));
        }

        return routes;
    }

    // Method to save a list of Route objects to an XML file
    public static void saveRoutes(List<Route> routes, String filePath) {
        try {
            Element root = routesXML(routes);
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

            routes = xmlRoutes(routeElements);

            System.out.println("Route data has been loaded from " + filePath);
        } catch (ParsingException | IOException e) {
            e.printStackTrace();
        }

        return routes;
    }
}