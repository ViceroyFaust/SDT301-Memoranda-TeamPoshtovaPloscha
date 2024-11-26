package memoranda.busSchedule.models;

import memoranda.busSchedule.annotations.ForeignKey;
import memoranda.busSchedule.annotations.PrimaryKey;
import nu.xom.Element;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Route implements XMLable, IModel {
    @PrimaryKey
    private int id;
    private LinkedList<Node> nodes = new LinkedList<>();
    @ForeignKey(lazyLoadField = "nodes", referencedClass = Node.class)
    List<Integer> nodesIds = new ArrayList<>();

    public Route() {

    }

    public Route(LinkedList<Node> nodes) {
        this.setNodes(nodes);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLength() {
        // TODO: Implement length calculation based on Nodes
        return 0;
    }

    public float getDurationH() {
        // TODO: Implement duration calculation based on Nodes
        return 0;
    }

    public LinkedList<Node> getNodes() {
        return nodes;
    }

    public void addNode(Node node) {
        nodesIds.add(node.getId());
        nodes.add(node);
    }

    public void setNodes(List<Node> nodes) {
        for (Node node : nodes) {
            nodesIds.add(node.getId());
        }

        this.nodes = new LinkedList<>(nodes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Route(id: ").append(id).append(", nodes: (").append(nodes).append(")");
        return sb.toString();
    }

    @Override
    public Element toXML() {
        Element routeElement = new Element("Route");

        Element id = new Element("id");
        id.appendChild(String.valueOf(this.getId()));
        routeElement.appendChild(id);

        //routeElement.appendChild(nodes);

        return routeElement;
    }

    @Override
    public void fromXML(Element xlmElement) {
        id = Integer.parseInt(xlmElement.getFirstChildElement("id").getValue());
    }
}
