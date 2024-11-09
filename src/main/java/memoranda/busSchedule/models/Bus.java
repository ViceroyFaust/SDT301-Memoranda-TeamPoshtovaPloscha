package memoranda.busSchedule.models;

public class Bus {
    private int id;
    private int seats;
    private Driver driver;

    public Bus(int id, int seats, Driver driver) {
        this.id = id;
        this.seats = seats;
        this.driver = driver;
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
    }

    @Override
    public String toString() {
        return "Bus(id: " + id + ", seats: " + seats + ", Driver: " + driver.toString() + ")";
    }
}
