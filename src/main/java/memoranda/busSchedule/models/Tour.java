package memoranda.busSchedule.models;

import memoranda.busSchedule.annotations.ForeignKey;
import memoranda.busSchedule.annotations.PrimaryKey;
import nu.xom.Element;

public class Tour implements XMLable, IModel {
    @PrimaryKey
    private int id;
    private String name;
    private Bus bus;
    @ForeignKey(lazyLoadField = "bus", referencedClass = Bus.class)
    private int busId;
    private Route route;
    @ForeignKey(lazyLoadField = "route", referencedClass = Route.class)
    private int routeId;

    public Tour(){

    }
    public Tour(String name, Bus bus, Route route) {
        this.name = name;
        setBus(bus);
        setRoute(route);
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
        busId = bus.getId();
        this.bus = bus;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        routeId = route.getId();
        this.route = route;
    }

    @Override
    public String toString() {
        return "Tour(id: " + id + ", name: " + name + ", bus: " + bus.toString() + ", route: " + route.toString() + ")";
    }

    @Override
    public Element toXML() {
        Element tourElement = new Element("Tour");

        Element id = new Element("id");
        id.appendChild(String.valueOf(this.getId()));
        tourElement.appendChild(id);

        Element name = new Element("name");
        name.appendChild(this.getName());
        tourElement.appendChild(name);

        return tourElement;
    }

    @Override
    public void fromXML(Element xlmElement) {
        id = Integer.parseInt(xlmElement.getFirstChildElement("id").getValue());
        name = xlmElement.getFirstChildElement("name").getValue();
    }
}
