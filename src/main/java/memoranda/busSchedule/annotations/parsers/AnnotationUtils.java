package memoranda.busSchedule.annotations.parsers;

import java.lang.reflect.Field;

public class AnnotationUtils {
    public static boolean checkIfFieldIsInteger(Field field) {
        return (field.getType() == int.class || field.getType() == Integer.class);
    }
}
