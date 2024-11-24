package memoranda.busSchedule;

import memoranda.busSchedule.managers.ModelsContext;
import memoranda.busSchedule.managers.ModelsSubset;
import memoranda.busSchedule.models.*;

public class ApplicationContext extends ModelsContext {
    //region ModelsSubsets
    public ModelsSubset<Bus> buses = new ModelsSubset<>(Bus.class);
    public ModelsSubset<Driver> drivers = new ModelsSubset<>(Driver.class);
    public ModelsSubset<Node> nodes = new ModelsSubset<>(Node.class);
    public ModelsSubset<Route> routes = new ModelsSubset<>(Route.class);
    public ModelsSubset<Tour> tours = new ModelsSubset<>(Tour.class);
    //endregion
}
