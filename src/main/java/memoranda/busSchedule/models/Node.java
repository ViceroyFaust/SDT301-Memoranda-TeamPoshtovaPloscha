package memoranda.busSchedule.models;

public class Node {
    private int id;
    // TODO: Implement node implementation in US3

    public Node(int id) {
        this.id = id;
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
}
