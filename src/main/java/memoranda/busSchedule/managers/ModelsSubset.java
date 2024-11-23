package memoranda.busSchedule.managers;

import memoranda.busSchedule.annotations.parsers.ForeignKeyParser;
import memoranda.busSchedule.models.IModel;
import memoranda.busSchedule.models.XMLable;
import nu.xom.Element;
import nu.xom.Elements;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ModelsSubset<T extends XMLable & IModel> {
    private final Class<T> modelClass;
    Map<Integer,T> models = new HashMap<>();
    public ModelsSubset(Class<T> modelClass) {
        this.modelClass = modelClass;
    }

    public Class<T> getModelClass() {
        return modelClass;
    }
    /**
     * Get all models in subset
     * @return map of models with id as key
     */
    public Map<Integer, T> getAll() {
        return models;
    }

    /**
     * Add model to subset. In case if model with same id already exists, it will be replaced
     * @param model model to add
     */
    public void add(T model) {
        models.put(model.getId(), model);
    }

    /**
     * Remove model from subset
     * @param model model to remove
     * @return true if model was removed, false if model was not found
     */
    public boolean remove(T model){
        return models.remove(model.getId()) == null;
    }

    /**
     * Get model by id
     * @param id id of model
     * @return model with specified id or null if model was not found
     */
    public T getById(int id) {
        return models.get(id);
    }

    /**
     * Check if subset is empty
     * @return true if subset is empty, false otherwise
     */
    public boolean isEmpty() {
        return models.isEmpty();
    }

    /**
     * Convert subset to XML element
     * @return XML element with all models in subset or null if subset is empty
     */
    public Element toXML(){
        if(models == null || models.isEmpty())
            return null;

        String classToCreateName = models.values().iterator().next().getClass().getName();

        try {
            Element root = new Element(classToCreateName);
            for (T model : models.values()) {
                Element currentObject = new Element("SubsetObject");
                currentObject.appendChild(model.toXML());
                currentObject.appendChild(ForeignKeyParser.getXmlForeignKeys(model));
                root.appendChild(currentObject);
            }
            return root;
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Parse subset from XML element and add all models to subset
     * @param subset XML element with subset data (memoranda.busSchedule.models.*)
     */
    public void fromXML(Element subset){

        //Getting all subset object
        Elements subsetsList = subset.getChildElements("SubsetObject");
        for(int i = 0; i < subsetsList.size(); i++) {
            //Getting single <SubsetObject> for parsing
            try {
                Element subsetObject = subsetsList.get(i);
                Element modelElement = subsetObject.getChildElements(modelClass.getSimpleName()).get(0);
                T model = getModelInstance(modelElement);

                //If model is broken skipping it
                if (model == null)
                    continue;

                //Check if we have foreign keys
                Element foreignKeys;
                try {
                    //Get foreign keys element
                    foreignKeys = subsetObject.getChildElements("ForeignKeys").get(0);
                }catch (IndexOutOfBoundsException e){
                    //If no foreign keys found
                    foreignKeys = null;
                }
                //Attach foreign keys to model
                if(foreignKeys != null && foreignKeys.getChildElements().size() != 0)
                    attachForeignKeysToModel(foreignKeys, model);

                this.add(model);

            }catch (IndexOutOfBoundsException e){
                System.out.println("Subset for " + modelClass.getName() + " has failed\n No model element found in subset object");
                e.printStackTrace();
            }
        }
    }

    private void attachForeignKeysToModel(Element foreignKeys, T model){
        Elements keys = foreignKeys.getChildElements();
        for(int i = 0; i < keys.size(); i++){
            Element key = keys.get(i);
            Field modelKey = getModelKey(key.getLocalName(), model);
            if(modelKey == null){
                System.out.println("Subset for: "+ modelClass.getSimpleName() + " has failed\n Failed to find model key");
                continue;
            }

            //Attach in case if key is a single value
            if(modelKey.getType() == Integer.class || modelKey.getType() == int.class) {
                try {
                    modelKey.set(model, Integer.parseInt(key.getValue()));
                } catch (IllegalAccessException e) {
                    System.out.println("Subset for: " + modelClass.getSimpleName() + " has failed\n Failed to set model key");
                    e.printStackTrace();
                }
            }
            //Attach in case if key is a collection
            if(Collection.class.isAssignableFrom(modelKey.getType())){
                try {
                    @SuppressWarnings("unchecked") //That is ok if cast fails, we will catch it
                    Collection<Integer> collection = (Collection<Integer>) modelKey.get(model);

                    collection.add(Integer.parseInt(key.getValue()));
                } catch (IllegalAccessException e) {
                    System.out.println("Subset for: "+ modelClass.getSimpleName() +
                            " has failed\n Failed to set model key");
                    e.printStackTrace();
                } catch (ClassCastException e){
                    System.out.println("Subset for: "+ modelClass.getSimpleName() +
                            " has failed\n Type mismatch, " +
                            "it looks like Foreign key is not an Integer collection");
                    e.printStackTrace();
                }
            }


        }
    }

    /**
     * Get model key by name
     * @param key key name
     * @param model model to get key from
     * @return field with key or null if key was not found
     */
    private Field getModelKey(String key, T model){
        for(Field field : model.getClass().getDeclaredFields()){
            if(field.getName().equals(key)){
                field.setAccessible(true);
                return field;
            }
        }
    return null;
    }
    /**
     * Create model instance from XML element
     * @param modelElement XML element with model data
     * @return model instance or null if failed to create instance
     */
     private T getModelInstance(Element modelElement){
        try {
            T model = modelClass.getDeclaredConstructor().newInstance();
            model.fromXML(modelElement);
            return model;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            System.out.println("Subset for: "+ modelClass.getSimpleName() + " has failed\n Failed to create model class instance");
            e.printStackTrace();
        }
        return null;
    }
}
