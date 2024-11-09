package memoranda.busSchedule.models;

public class Route {
    private int id;
    private float length;
    private float durationH; //Duration in hours
    private Node[] nodes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getDurationH() {
        return durationH;
    }

    public void setDurationH(float durationH) {
        this.durationH = durationH;
    }

    public Node[] getNodes() {
        return nodes;
    }

    public void setNodes(Node[] nodes) {
        this.nodes = nodes;
    }
}
