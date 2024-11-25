package memoranda.busSchedule.models;

import memoranda.busSchedule.annotations.PrimaryKey;
import nu.xom.Element;

public class Driver implements XMLable, IModel {
    @PrimaryKey
    private int id;
    private String name;
    private String phoneNumber;

    public Driver() {

    }

    public Driver(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Driver(id: " + id + ", name: " + name + ", phone: " + phoneNumber + ")";
    }

    @Override
    public Element toXML() {
        Element driverElement = new Element("Driver");

        Element id = new Element("id");
        id.appendChild(String.valueOf(this.getId()));
        driverElement.appendChild(id);

        Element name = new Element("name");
        name.appendChild(this.getName() != null ? this.getName() : "Unknown");
        driverElement.appendChild(name);

        Element phoneNumber = new Element("phoneNumber");
        phoneNumber.appendChild(this.getPhoneNumber() != null ? this.getPhoneNumber() : "Unknown");
        driverElement.appendChild(phoneNumber);

        return driverElement;
    }

    @Override
    public void fromXML(Element xlmElement) {
        // Parse and set ID
        id = Integer.parseInt(xlmElement.getFirstChildElement("id").getValue());

        // Parse and set Name
        name = xlmElement.getFirstChildElement("name").getValue();

        // Parse and set Phone Number
        phoneNumber = xlmElement.getFirstChildElement("phoneNumber").getValue();
    }
}
