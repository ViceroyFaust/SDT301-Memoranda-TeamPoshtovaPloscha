package memoranda.busSchedule.models;

public class Tour {
    private int id;
    private String name;
    private Bus bus;
    private Route route;

    public Tour(int id, String name, Bus bus, Route route) {
        this.id = id;
        this.name = name;
        this.bus = bus;
        this.route = route;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    @Override
    public String toString() {
        return "Tour(id: " + id + ", name: " + name + ", bus: " + bus.toString() + ", route: " + route.toString() + ")";
    }
}
