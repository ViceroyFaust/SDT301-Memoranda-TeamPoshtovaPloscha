package memoranda.busSchedule.managers;

import memoranda.busSchedule.annotations.parsers.ForeignKeyParser;
import memoranda.busSchedule.models.IModel;
import memoranda.busSchedule.models.XMLable;
import nu.xom.Element;

import java.util.HashMap;
import java.util.Map;

public class ModelsSubset<T extends XMLable & IModel> {
    Map<Integer,T> models = new HashMap<>();
    public ModelsSubset(Map<Integer, T> models) {
        this.models = models;
    }
    public ModelsSubset() {

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
                currentObject.appendChild(ForeignKeyParser.parseForeignKeys(model));
                root.appendChild(currentObject);
            }
            return root;
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
        return null;
    }


}
