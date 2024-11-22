package memoranda.busSchedule.managers;

import memoranda.busSchedule.models.IModel;
import memoranda.busSchedule.models.XMLable;
import java.util.Map;

public class ModelsSubset<T extends XMLable & IModel> {
    Map<Integer,T> models;
    public ModelsSubset(Map<Integer, T> models) {
        this.models = models;
    }
    public ModelsSubset() {}

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
}
