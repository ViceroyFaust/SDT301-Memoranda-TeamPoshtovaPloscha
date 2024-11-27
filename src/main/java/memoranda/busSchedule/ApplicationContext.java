package memoranda.busSchedule;

import memoranda.busSchedule.managers.ModelsContext;
import memoranda.busSchedule.managers.ModelsSubset;
import memoranda.busSchedule.models.*;

public class ApplicationContext extends ModelsContext {
    //region Singleton
    private static final ApplicationContext instance = new ApplicationContext();

    public static ApplicationContext getInstance() {
        return instance;
    }

    private ApplicationContext() {
    }

    //endregion

    //region ModelsSubsets
    public final ModelsSubset<Bus> buses = new ModelsSubset<>(Bus.class);
    public final ModelsSubset<Driver> drivers = new ModelsSubset<>(Driver.class);
    public final ModelsSubset<Node> nodes = new ModelsSubset<>(Node.class);
    public final ModelsSubset<Route> routes = new ModelsSubset<>(Route.class);
    public final ModelsSubset<Tour> tours = new ModelsSubset<>(Tour.class);
    //endregion

}
