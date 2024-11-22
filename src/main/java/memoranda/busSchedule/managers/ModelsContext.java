package memoranda.busSchedule.managers;

import memoranda.busSchedule.models.Bus;
import memoranda.busSchedule.models.Driver;

public class ModelsContext {
    //region Singleton Implementation
    private static ModelsContext instance;

    private ModelsContext() {
    }

    public static ModelsContext getInstance() {
        if (instance == null) {
            instance = new ModelsContext();
        }
        return instance;
    }
    //endregion

    //region ModelsSubsets
    private ModelsSubset<Bus> buses;
    private ModelsSubset<Driver> drivers;
    //endregion



}
