package memoranda.busSchedule;

import memoranda.busSchedule.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class IoTests {
    @BeforeAll
    public static void init() throws IOException {
        ApplicationContext context = new ApplicationContext();
        Driver daniel = new Driver("Daniel Danielson", "067 555 6666");
        Driver johnnie = new Driver("Johnnie Dowel", "+1 535 125 6486");
        context.drivers.add(daniel);
        context.drivers.add(johnnie);

        Bus maryJane = new Bus(9, daniel);
        Bus titanicBus = new Bus(50, johnnie);
        context.buses.add(maryJane);
        context.buses.add(titanicBus);

        Node node1 = new Node();
        Node node2 = new Node();
        context.nodes.add(node1);
        context.nodes.add(node2);

        Route backwards = new Route();
        backwards.addNode(node2);
        backwards.addNode(node1);

        Route forwards = new Route();
        context.routes.add(forwards);
        context.routes.add(backwards);

        forwards.addNode(node1);
        forwards.addNode(node2);


        Tour onWeGo = new Tour("On we go!", maryJane, forwards);
        Tour bonVoyage = new Tour("Bon Voyage!", titanicBus, backwards);
        context.tours.add(onWeGo);
        context.tours.add(bonVoyage);


        context.save("save.xml");
    }

    @Test
    public void basicSaveTest() throws IOException {

    }

    @Test
    public void basicLoadTest() {
        ApplicationContext context = new ApplicationContext();
        context.load("save.xml");

        for (Driver driver : context.drivers.getAll().values()) {
            System.out.println(driver);
        }

        for (Node node : context.nodes.getAll().values()) {
            System.out.println(node);
        }

        for (Bus bus : context.buses.getAll().values()) {
            System.out.println(bus);
        }

        for (Route route : context.routes.getAll().values()) {
            System.out.println(route);
        }

        for (Tour tour : context.tours.getAll().values()) {
            System.out.println(tour);
        }
    }
}
