package memoranda.busSchedule.models;

import java.util.Arrays;

public class Route {
    private int id;
    private Node[] nodes;

    public Route(int id, Node[] nodes) {
        this.id = id;
        this.nodes = nodes;
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

    public Node[] getNodes() {
        return nodes;
    }

    public void setNodes(Node[] nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return "Route(id: " + id + ", nodes: " + Arrays.toString(nodes) + ")";
    }
}
