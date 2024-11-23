package memoranda.busSchedule.annotations.parsers;

import memoranda.busSchedule.annotations.ForeignKey;
import nu.xom.Element;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class ForeignKeyParser {

    public static <T> ArrayList<Field> getForeignKeys(T model){
        ArrayList<Field> validForeignKeys = new ArrayList<>();
        for(Field field : model.getClass().getDeclaredFields()) {
            if(!checkIfFieldIsAValidForeignKey(field, model))
                continue;

            //Only changes the behaviour of AccessibleObject
            field.setAccessible(true);

            //Check if primary key is present in referenced class of foreign key
            ForeignKey fk = field.getAnnotation(ForeignKey.class);
            if (!PrimaryKeyParser.ensureThatPrimaryKeyPresent(fk.referencedClass()))
                throw new IllegalArgumentException("Primary key is not present in referenced class of foreign key");

            Class<?> referencedClass = fk.referencedClass();
            String lazyLoadField = fk.lazyLoadField();

            //Checks if the lazy load field is specified
            try {
                Field lField = model.getClass().getDeclaredField(lazyLoadField);

                if(!Collection.class.isAssignableFrom(lField.getType()) && lField.getType() != referencedClass)
                    throw new IllegalArgumentException(" Model -> " + model.getClass().getName() +
                            " Lazy load field is not of the same type as referenced class or is not a collection");

            }catch (NoSuchFieldException e){
                throw new IllegalArgumentException(" Model -> " + model.getClass().getName() +
                        " Lazy load field is not specified");
            }
            //Add foreign key to XML
            field.setAccessible(true);
            //If we have a list of foreign keys
            validForeignKeys.add(field);
        }
        return validForeignKeys;
    }
    public static <T> Element getXmlForeignKeys(T model) throws IllegalAccessException {
        Element root = new Element("ForeignKeys");
        //Element fkElement = new Element(field.getName());

        for(Field field : getForeignKeys(model)){
            if (Collection.class.isAssignableFrom(field.getType())) {
                try {
                    Collection<?> collection = (Collection<?>) field.get(model);
                    for (Object obj : collection) {
                        //Add foreign key to XML
                        Element fkElement = new Element(field.getName());
                        fkElement.appendChild(String.valueOf(obj));
                        root.appendChild(fkElement);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            } else{ //If we have a single foreign key
                //Add foreign key to XML
                Element fkElement = new Element(field.getName());
                fkElement.appendChild(String.valueOf(field.get(model)));
                root.appendChild(fkElement);
            }
        }

        return root;
    }

    /**
     * Check if field is a valid foreign key
     * @param field Field to check
     * @param model Model object. Needed to get the value of the field
     * @return True if field is a valid foreign key, false otherwise
     * @param <T> Type of model object
     */
    public static <T> boolean checkIfFieldIsAValidForeignKey(Field field, T model) {
        if (!field.isAnnotationPresent(ForeignKey.class))
            return false;

        //Check if foreign key is of type Integer or Collection
        if(!(AnnotationUtils.checkIfFieldIsInteger(field) || Collection.class.isAssignableFrom(field.getType())))
            throw new IllegalArgumentException(field.getName() +  " Foreign key must be of type Integer or Collection");

        //If we have a list of foreign keys
        if(Collection.class.isAssignableFrom(field.getType()))
            return checkIfCollectionIsAValidForeignKey(field, model);

        return true;
    }

    /**
     * Check if collection is a valid foreign key
     * @param field Field to check
     * @param model Model object. Needed to get the value of the field
     * @return True if collection is a valid foreign key, false otherwise
     * @param <T> Type of model object
     */
    protected static <T> boolean checkIfCollectionIsAValidForeignKey(Field field, T model) {
        if (!field.isAnnotationPresent(ForeignKey.class))
            return false;

        if(!Collection.class.isAssignableFrom(field.getType()))
            throw new IllegalArgumentException("Foreign key must be of type Collection");

        try {
            field.setAccessible(true);
            Collection<?> collection = (Collection<?>) field.get(model);

            //Empty collection is allowed
            if(collection.isEmpty())
                return true;

            //Check if all elements in collection are of type Integer
            for(Object obj : collection) {
                if(!(obj instanceof Integer))
                    throw new IllegalArgumentException("Foreign key must be of type Integer");
            }

        }catch (IllegalAccessException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return true;
    }
}
