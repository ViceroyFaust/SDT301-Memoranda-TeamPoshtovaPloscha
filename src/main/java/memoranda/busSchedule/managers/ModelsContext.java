package memoranda.busSchedule.managers;

import memoranda.busSchedule.models.Bus;
import memoranda.busSchedule.models.Driver;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;


public class ModelsContext {
    //region Singleton Implementation
    private static ModelsContext instance;
    private static final String filePath = "data.xml";

    private ModelsContext() {
        this.load(filePath);
    }

    public static ModelsContext getInstance() {
        if (instance == null) {
            instance = new ModelsContext();
        }
        return instance;
    }
    //endregion

    //region ModelsSubsets
    public ModelsSubset<Bus> buses = new ModelsSubset<>();
    public ModelsSubset<Driver> drivers = new ModelsSubset<>();
    //endregion


    public void save(String filePath) throws IOException {
        Element root = new Element("root");
        for (ModelsSubset<?> subset : getSubsets()) {
            if(subset == null || subset.isEmpty())
                continue;
            root.appendChild(subset.toXML());
        }
        Document doc = new Document(root);
        FileOutputStream out = new FileOutputStream(filePath);
        Serializer serializer = new Serializer(out, "UTF-8");
        serializer.setIndent(4);
        serializer.write(doc);
        out.close();
    }

    public void load(String filePath){
       // this.buses = new ModelsSubset<>(Bus.class);
       // this.drivers = new ModelsSubset<>(Driver.class);

    }

    public ModelsSubset<?>[] getSubsets() {
        ArrayList<ModelsSubset<?>> subsets = new ArrayList<>();
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (ModelsSubset.class.isAssignableFrom(field.getType())) {
                    //Only changes the behaviour of AccessibleObject
                    field.setAccessible(true);
                    subsets.add((ModelsSubset<?>) field.get(this));
                }
            }
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
        return subsets.toArray(new ModelsSubset<?>[0]);
    }

}
