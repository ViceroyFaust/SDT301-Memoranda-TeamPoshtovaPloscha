package memoranda.busSchedule.annotations.parsers;

import memoranda.busSchedule.annotations.PrimaryKey;

import java.lang.reflect.Field;

public class PrimaryKeyParser {
    /**
     * Ensure that primary key is present in referenced class of foreign key and is Integer
     * @param classToParse class to parse
     * @return true if primary key is present and is Integer, false otherwise
     */
    public static boolean ensureThatPrimaryKeyPresent(Class<?> classToParse){
        for (Field field : classToParse.getDeclaredFields()) {
            if(!field.isAnnotationPresent(PrimaryKey.class))
                continue;

            try {
                AnnotationUtils.checkIfFieldIsInteger(field);
                return true;
            }catch (IllegalArgumentException e){
                throw new IllegalArgumentException(classToParse.getName() +  ": Primary key in referenced class must be Integer");
            }

        }
        return false;
    }

    public static Field getPrimaryKeyField(Class<?> classToParse){
        for (Field field : classToParse.getDeclaredFields()) {
            if(field.isAnnotationPresent(PrimaryKey.class))
                return field;
        }
        return null;
    }
}
