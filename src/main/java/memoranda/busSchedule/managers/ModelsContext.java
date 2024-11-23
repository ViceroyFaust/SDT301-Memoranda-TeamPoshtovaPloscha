package memoranda.busSchedule.managers;

import memoranda.busSchedule.annotations.ForeignKey;
import memoranda.busSchedule.annotations.parsers.AnnotationUtils;
import memoranda.busSchedule.annotations.parsers.ForeignKeyParser;
import memoranda.busSchedule.models.*;
import memoranda.busSchedule.models.Node;
import nu.xom.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;


public abstract class ModelsContext {
    public ModelsContext() {

    }

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

        for(ModelsSubset<?> subset : getSubsets()){
            subset.clear();
        }

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

        lazyLoadModels();
    }

    public void erase(){
        for(ModelsSubset<?> subset : getSubsets()){
            subset.clear();
        }
    }

    /**
     * This class initializes lazy loading of foreign keys for all subsets
     */
    private void lazyLoadModels(){
        //Get all subsets
        ArrayList<ModelsSubset<?>> subsets = getSubsets();
        //Iterate over all subsets
        for (ModelsSubset<?> subset : subsets) {
            //Iterate through subset models
            for(Object objectModel : subset.getAll().values()){
                processLazyLoadField(objectModel, subset.getModelClass());
            }
        }
    }

    /**
     * Process lazy load for all foreign keys in subset models
     * @param objectModel subset model object to process
     * @param classOfModel class of model object
     */
    private void processLazyLoadField(Object objectModel, Class<?> classOfModel){
        if(!classOfModel.isAssignableFrom(objectModel.getClass()))
            throw new IllegalArgumentException("Model class is not assignable from model object");

        for(Field field : ForeignKeyParser.getForeignKeys(objectModel)){
            try {
                lazyLoadField(field, objectModel);
            }catch (IllegalArgumentException | IllegalAccessException e) {
                System.out.println("Failed to lazy load field " + field.getName() + " for model " + objectModel.getClass().getName());
                e.printStackTrace();
            }
        }
    }

    /**
     * Takes field and tries to lazy load field finding the appropriate object in subsets
     * @param field field to lazy load
     * @param model model object to lazy load field for
     * @throws IllegalArgumentException if field is not a valid foreign key
     * @throws IllegalAccessException if field is not accessible
     */
    private void lazyLoadField(Field field, Object model) throws IllegalArgumentException, IllegalAccessException {

        //If field is a single foreign key
        if (AnnotationUtils.checkIfFieldIsInteger(field)) {
         lazyLoadSingleField(field, model);
            return;
        }
        //If field is a collection
        if (Collection.class.isAssignableFrom(field.getType())) {
            lazyLoadCollection(field, model);
            return;
        }
        throw new IllegalArgumentException("Field is not a valid foreign key");
    }

    /**
     * Lazy loads single field
     * @param field field to lazy load (a foreign key)
     * @param model model object to lazy load field for
     * @throws IllegalArgumentException if field is not a valid foreign key
     * @throws IllegalAccessException if field is not accessible
     */
    private void lazyLoadSingleField(Field field, Object model) throws IllegalArgumentException, IllegalAccessException {
        Class<?> referencedClass = field.getAnnotation(ForeignKey.class).referencedClass();
        String lazyLoadFieldString = field.getAnnotation(ForeignKey.class).lazyLoadField();
        int foreignKeyValue = (int) field.get(model);
        ModelsSubset<?> foreignSubset = getSubsetForModel(referencedClass);
        if (foreignSubset == null)
            throw new IllegalArgumentException("Foreign subset for model " + referencedClass.getName() +
                    " was not found");

        Object foreignModel = foreignSubset.getById(foreignKeyValue);

        //Stage to set lazy load field in the original model
        try {
            Field lazyLoadField = model.getClass().getDeclaredField(lazyLoadFieldString);
            lazyLoadField.setAccessible(true);
            lazyLoadField.set(model, foreignModel);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Lazy load field is not specified");
        }
    }

    /**
     * Lazy loads collection field
     * @param field field to lazy load (a foreign key)
     * @param model model object to lazy load field for
     * @throws IllegalAccessException if field is not accessible
     */
    private void lazyLoadCollection(Field field, Object model) throws IllegalAccessException {

        Class<?> referencedClass = field.getAnnotation(ForeignKey.class).referencedClass();
        String lazyLoadFieldString = field.getAnnotation(ForeignKey.class).lazyLoadField();

        Collection<?> foreignKeys = (Collection<?>) field.get(model);
        for (Object foreignKey : foreignKeys) {
            ModelsSubset<?> foreignSubset = getSubsetForModel(referencedClass);
            if (foreignSubset == null)
                throw new IllegalArgumentException("Foreign subset for model " + referencedClass.getName() +
                        " was not found");

            Object foreignModel = foreignSubset.getById((int) foreignKey);

            //Stage to set lazy load field in the original model
            try {
                Field lazyLoadField = model.getClass().getDeclaredField(lazyLoadFieldString);
                lazyLoadField.setAccessible(true);

                @SuppressWarnings("unchecked")
                Collection<Object> collection = (Collection<Object>) lazyLoadField.get(model);
                if (collection == null) {
                    collection = new ArrayList<>(); // Initialize if null
                    lazyLoadField.set(model, collection);
                }

                collection.add(foreignModel);
                lazyLoadField.set(model, collection);

            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException("Lazy load field is not specified");
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Lazy load field is not a collection");
            }
        }
    }

    /**
     * Get all subsets in current context
     * @return list of all subsets
     */
    private ArrayList<ModelsSubset<?>> getSubsets() {
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
    private ModelsSubset<?> getSubsetForModel(Class<?> modelClass) {
        return getSubsets().stream().filter(subset -> subset.getModelClass().equals(modelClass)).findFirst().orElse(null);
    }

}
