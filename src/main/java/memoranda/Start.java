/**
 * Start.java
 * Created on 19.08.2003, 20:40:08 Alex
 * Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package memoranda;

import java.net.ServerSocket;
import java.net.Socket;

import memoranda.busSchedule.models.*;
import memoranda.ui.*;
import memoranda.util.Configuration;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import memoranda.ui.*;
import memoranda.util.Configuration;


/**
 *
 */
/*$Id: Start.java,v 1.7 2004/11/22 10:02:37 alexeya Exp $*/
public class Start {

    static App app = null;
    static final String BUS_DATA_FILE = "bus_data.xml"; // Path to bus data file

    static int DEFAULT_PORT = 19432;
    static boolean checkIfAlreadyStartet = true;
    private static final String BUS_DATA_PATH = "bus_data.xml"; // Path to the bus data file

    private static final String driverDataFilePath = "drivers.xml";


    static {
        String port = Configuration.get("PORT_NUMBER").toString().trim();
        if (port.length() >0) {
            // The Portnumber must be between 1024 (in *nix all Port's < 1024
            // are privileged) and 65535 (the highest Portnumber everywhere)
            int i = Integer.parseInt(port);
            if ((i >= 1024) && (i <= 65535)) {
                DEFAULT_PORT = i;
            }
            /*DEBUG*/ //System.out.println("Port " + DEFAULT_PORT + " used.");
        }

        String check = Configuration.get("CHECK_IF_ALREADY_STARTED").toString().trim();
        if (check.length() > 0 && check.equalsIgnoreCase("no")) {
            checkIfAlreadyStartet = false;
        }
    }

    public static void main(String[] args) {
        // Step 1: Check Bus Data
        checkBusData();
        // Step 2: Check Driver Data
        checkDriverData();
        // Step 3: Check Node Data
        checkNodeData();
        // Step 4: Check Route Data
        checkRouteData();
        // Step 5: Check Tour Data
        checkTourData();

        if (checkIfAlreadyStartet) {
            try {
                // Try to open a socket. If socket opened successfully (app is already started), take no action and exit.
                Socket socket = new Socket("127.0.0.1", DEFAULT_PORT);
                socket.close();
                System.exit(0);

            } catch (Exception e) {
                // If socket is not opened (app is not started), continue
                // e.printStackTrace();
            }
            new SLThread().start();
        }
        //System.out.println(EventsScheduler.isEventScheduled());
        if ((args.length == 0) || (!args[0].equals("-m"))) {
            app = new App(true);
        }
        else
            app = new App(false);
    }

    // Method to check and verify Bus data
    private static void checkBusData() {
        List<Bus> buses = new ArrayList<>();
        Driver driver1 = new Driver();
        driver1.setName("John Doe");

        Bus bus1 = new Bus();
        bus1.setId(1);
        bus1.setSeats(50);
        bus1.setDriver(driver1);
        buses.add(bus1);

        BusManager.saveBuses(buses, "bus_data.xml");
        boolean isBusDataCorrect = BusManager.verifyBusStorage("bus_data.xml", buses);
        System.out.println("Bus data stored correctly: " + isBusDataCorrect);
    }

    // Method to check and verify Driver data
    private static void checkDriverData() {
        List<Driver> drivers = new ArrayList<>();

        Driver driver1 = new Driver();
        driver1.setId(1);
        driver1.setName("John Doe");
        driver1.setPhoneNumber("123-456-7890");

        Driver driver2 = new Driver();
        driver2.setId(2);
        driver2.setName("Jane Smith");
        driver2.setPhoneNumber("098-765-4321");

        drivers.add(driver1);
        drivers.add(driver2);

        DriverManager.saveDrivers(drivers, "drivers.xml");
        boolean isDriverDataCorrect = DriverManager.verifyDriverStorage("drivers.xml", drivers);
        System.out.println("Driver data stored correctly: " + isDriverDataCorrect);
    }

    // Method to check and verify Node data
    private static void checkNodeData() {
        List<Node> nodes = new ArrayList<>();

        Node node1 = new Node();
        node1.setId(1);

        Node node2 = new Node();
        node2.setId(2);

        nodes.add(node1);
        nodes.add(node2);

        NodeManager.saveNodes(nodes, "nodes.xml");
        boolean isNodeDataCorrect = NodeManager.verifyNodeStorage("nodes.xml", nodes);
        System.out.println("Node data stored correctly: " + isNodeDataCorrect);
    }

    // Method to check and verify Route data
    private static void checkRouteData() {
        List<Route> routes = new ArrayList<>();

        Route route1 = new Route();
        route1.setId(1);
        route1.setLength(120.5f);
        route1.setDurationH(2.5f);

        Route route2 = new Route();
        route2.setId(2);
        route2.setLength(250.0f);
        route2.setDurationH(5.0f);

        routes.add(route1);
        routes.add(route2);

        RouteManager.saveRoutes(routes, "routes.xml");
        boolean isRouteDataCorrect = RouteManager.verifyRouteStorage("routes.xml", routes);
        System.out.println("Route data stored correctly: " + isRouteDataCorrect);
    }

    // Method to check and verify Tour data
    private static void checkTourData() {
        List<Bus> availableBuses = new ArrayList<>();
        Bus bus1 = new Bus();
        bus1.setId(1);
        bus1.setSeats(50);
        availableBuses.add(bus1);

        List<Route> availableRoutes = new ArrayList<>();
        Route route1 = new Route();
        route1.setId(1);
        route1.setLength(120.5f);
        route1.setDurationH(2.5f);
        availableRoutes.add(route1);

        List<Tour> tours = new ArrayList<>();
        Tour tour1 = new Tour();
        tour1.setId(1);
        tour1.setName("Mountain Adventure");
        tour1.setBus(bus1);
        tour1.setRoute(route1);
        tours.add(tour1);

        TourManager.saveTours(tours, "tours.xml");
        boolean isTourDataCorrect = TourManager.verifyTourStorage("tours.xml", tours, availableBuses, availableRoutes);
        System.out.println("Tour data stored correctly: " + isTourDataCorrect);
    }

}



class SLThread extends Thread {

    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(Start.DEFAULT_PORT);
            serverSocket.accept();
            Start.app.show();
            serverSocket.close();
            new SLThread().start();

        } catch (Exception e) {
            System.err.println("Port:"+Start.DEFAULT_PORT);
            e.printStackTrace();
            new ExceptionDialog(e, "Cannot create a socket connection on localhost:"+Start.DEFAULT_PORT,
            "Make sure that other software does not use the port "+Start.DEFAULT_PORT+" and examine your security settings.");
        }
    }
}
