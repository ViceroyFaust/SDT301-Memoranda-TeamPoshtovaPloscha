package memoranda.busSchedule.models;

import nu.xom.*;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NodeManager {

    // Method to save a list of Node objects to an XML file
    public static void saveNodes(List<Node> nodes, String filePath) {
        try {
            Element root = new Element("Nodes");

            for (Node node : nodes) {
                Element nodeElement = new Element("Node");

                Element id = new Element("id");
                id.appendChild(String.valueOf(node.getId()));
                nodeElement.appendChild(id);

                root.appendChild(nodeElement);
            }

            Document doc = new Document(root);
            FileOutputStream out = new FileOutputStream(filePath);
            Serializer serializer = new Serializer(out, "UTF-8");
            serializer.setIndent(4);
            serializer.write(doc);
            out.close();

            System.out.println("Node data has been saved to " + filePath);
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

            for (int i = 0; i < nodeElements.size(); i++) {
                Element nodeElement = nodeElements.get(i);

                int id = Integer.parseInt(nodeElement.getFirstChildElement("id").getValue());
                Node node = new Node(id);

                nodes.add(node);
            }

            System.out.println("Node data has been loaded from " + filePath);
        } catch (ParsingException | IOException e) {
            e.printStackTrace();
        }

        return nodes;
    }

    /* Commented out, as this is needed only for debug
    // Method to verify if the node data was stored correctly
    public static boolean verifyNodeStorage(String filePath, List<Node> originalNodes) {
        List<Node> loadedNodes = loadNodes(filePath);

        if (loadedNodes.size() != originalNodes.size()) {
            return false; // Size mismatch
        }

        for (int i = 0; i < originalNodes.size(); i++) {
            Node original = originalNodes.get(i);
            Node loaded = loadedNodes.get(i);

            if (original.getId() != loaded.getId()) {
                return false; // Mismatch in data
            }
        }
        return true; // Data matches
    } */

    /* Test code
    public static void main(String[] args) {
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(1));
        nodes.add(new Node(2));
        nodes.add(new Node(3));
        nodes.add(new Node(4));
        nodes.add(new Node(5));

        saveNodes(nodes, "testNodes.xml");
    } */
}