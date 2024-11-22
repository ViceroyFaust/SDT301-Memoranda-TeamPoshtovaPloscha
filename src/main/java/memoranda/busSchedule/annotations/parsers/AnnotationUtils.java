package memoranda.busSchedule.annotations.parsers;

import java.lang.reflect.Field;

public class AnnotationUtils {
    public static void checkIfFieldIsInteger(Field field){
        if(field.getType() != int.class && field.getType() != Integer.class)
            throw new IllegalArgumentException(field.getName() + ": Field must be Integer");
    }
}
