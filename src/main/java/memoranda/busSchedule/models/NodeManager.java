package memoranda.busSchedule.models;

import nu.xom.*;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NodeManager {

    public static Element nodeXML(Node node) {
        Element nodeElement = new Element("Node");

        Element id = new Element("id");
        id.appendChild(String.valueOf(node.getId()));
        nodeElement.appendChild(id);

        return nodeElement;
    }

    public static Element nodesXML(List<Node> nodes) {
        Element root = new Element("Nodes");

        Element nodeElement;
        for (Node node : nodes) {
            nodeElement = nodeXML(node);
            root.appendChild(nodeElement);
        }
        return root;
    }

    public static Node xmlNode(Element nodeElement) {
        int id = Integer.parseInt(nodeElement.getFirstChildElement("id").getValue());
        return new Node(id);
    }

    public static List<Node> xmlNodes(Elements nodeElements) {
        List<Node> nodes = new ArrayList<>();

        Element nodeElement;
        for (int i = 0; i < nodeElements.size(); i++) {
            nodeElement = nodeElements.get(i);
            nodes.add(xmlNode(nodeElement));
        }

        return nodes;
    }

    // Method to save a list of Node objects to an XML file
    public static void saveNodes(List<Node> nodes, String filePath) {
        try {
            Element root = nodesXML(nodes);

            Document doc = new Document(root);
            FileOutputStream out = new FileOutputStream(filePath);
            Serializer serializer = new Serializer(out, "UTF-8");
            serializer.setIndent(4);
            serializer.write(doc);
            out.close();

            // System.out.println("Node data has been saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load a list of Node objects from an XML file
    public static List<Node> loadNodes(String filePath) {
        List<Node> nodes = new ArrayList<>();

        try {
            Builder builder = new Builder();
            Document doc = builder.build(new FileInputStream(filePath));

            Elements nodeElements = doc.getRootElement().getChildElements("Node");

            nodes = xmlNodes(nodeElements);
            // System.out.println("Node data has been loaded from " + filePath);
        } catch (ParsingException | IOException e) {
            e.printStackTrace();
        }

        return nodes;
    }
}