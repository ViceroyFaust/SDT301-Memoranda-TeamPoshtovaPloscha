package memoranda.busSchedule.models;

import memoranda.busSchedule.annotations.ForeignKey;
import memoranda.busSchedule.annotations.PrimaryKey;
import nu.xom.Element;

public class Bus implements XMLable, IModel {
    @PrimaryKey
    private int id;
    private int seats;
    private Driver driver;
    //This is our connection to the driver
    @ForeignKey(lazyLoadField = "driver", referencedClass = Driver.class)
    private int driverId;

    public Bus() {

    }

    public Bus(int seats, Driver driver) {
        this.seats = seats;
        this.setDriver(driver);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
        this.driverId = driver.getId();
    }

    public int getDriverId() {
        return driverId;
    }

    @Override
    public String toString() {
        if (driver != null)
            return "Bus(id: " + id + ", seats: " + seats + ", DriverID: " + driverId + ", Driver: " + driver.toString() + ")";
        else
            return "Bus(id: " + id + ", seats: " + seats + ", DriverID: " + driverId + ")";
    }

    @Override
    public Element toXML() {
        Element busElement = new Element("Bus");

        Element id = new Element("id");
        id.appendChild(String.valueOf(this.getId()));
        busElement.appendChild(id);

        Element seats = new Element("seats");
        seats.appendChild(String.valueOf(this.getSeats()));
        busElement.appendChild(seats);

        return busElement;
    }

    @Override
    public void fromXML(Element xlmElement) {
        id = Integer.parseInt(xlmElement.getFirstChildElement("id").getValue());
        seats = Integer.parseInt(xlmElement.getFirstChildElement("seats").getValue());
    }
}
