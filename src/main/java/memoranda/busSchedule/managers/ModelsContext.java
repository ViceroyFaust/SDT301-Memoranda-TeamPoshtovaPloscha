package memoranda.busSchedule.managers;

import memoranda.busSchedule.models.Bus;
import memoranda.busSchedule.models.Driver;
import nu.xom.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class ModelsContext {
    //region Singleton Implementation
    private static ModelsContext instance;
    private static final String filePath = "/home/vladyslav/Projects/uni/test/data.xml";

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
    public ModelsSubset<Bus> buses = new ModelsSubset<>(Bus.class);
    public ModelsSubset<Driver> drivers = new ModelsSubset<>(Driver.class);
    //endregion


    /**
     * Save all subsets to xml file
     * @param filePath path to file to save
     * @throws IOException if path is invalid file is not writable, etc.
     */
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

    public void load(String filePath) throws RuntimeException{
        try {
            Builder builder = new Builder();
            Document doc = builder.build(new FileInputStream(filePath));

            Element rootElement = doc.getRootElement();
            Elements subsets = rootElement.getChildElements();

            //Iterating over all subsets
            for (int i = 0; i < subsets.size(); i++) {
                Element subset = subsets.get(i);
                Class<?> subsetModelClass = Class.forName(subset.getLocalName());

                ModelsSubset<?> subsetForModel = getSubsetForModel(subsetModelClass);
                if(subsetForModel == null)
                    throw new RuntimeException("Subset for model " + subsetModelClass.getName() + " was not found");

                subsetForModel.fromXML(subset);

            }

        } catch (ParsingException | IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // this.buses = new ModelsSubset<>(Bus.class);
       // this.drivers = new ModelsSubset<>(Driver.class);

    }

    /**
     * Get all subsets in current context
     * @return list of all subsets
     */
    public ArrayList<ModelsSubset<?>> getSubsets() {
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
        return subsets;
    }

    /**
     * Get subset for specified model class
     * @param modelClass model class to get subset for
     * @return subset for model class or null if subset was not found
     */
    public ModelsSubset<?> getSubsetForModel(Class<?> modelClass) {
        return getSubsets().stream().filter(subset -> subset.getModelClass().equals(modelClass)).findFirst().orElse(null);
    }

}
