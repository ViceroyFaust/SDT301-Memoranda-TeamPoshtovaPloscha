package memoranda.busSchedule.models;

import memoranda.busSchedule.annotations.PrimaryKey;
import nu.xom.Element;

public class Node implements XMLable, IModel {
    @PrimaryKey
    private int id;
    // TODO: Implement node implementation in US3

    public Node() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Node(id: " + id + ")";
    }

    @Override
    public Element toXML() {
        Element nodeElement = new Element("Node");

        Element id = new Element("id");
        id.appendChild(String.valueOf(this.getId()));
        nodeElement.appendChild(id);

        return nodeElement;
    }

    @Override
    public void fromXML(Element xlmElement) {
        id = Integer.parseInt(xlmElement.getFirstChildElement("id").getValue());
    }
}
