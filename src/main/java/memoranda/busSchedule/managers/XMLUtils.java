package memoranda.busSchedule.managers;
import nu.xom.*;

import java.lang.reflect.Field;
import java.util.List;

public class XMLUtils {
    public static Element convertToXML(Object obj) throws IllegalAccessException {

        //Getting class name
        Element root = new Element(obj.getClass().getSimpleName());

        //Getting fields
        for (Field field : obj.getClass().getDeclaredFields()) {
            // Ignore synthetic fields
            if (field.isSynthetic())
                continue;

            //Suppressing language access control
            field.setAccessible(true);
            //Getting field name
            String fieldName = field.getName();
            //Getting field value
            Object fieldValue = field.get(obj);
            //Ignore the field value
            if (fieldValue == null)
                continue;

            //Creating a new element
            Element element = new Element(fieldName);

            //Making sure that we are writing only primitive types or strings
            if (!(field.getType().isPrimitive() || field.getType().equals(String.class)))
                element.appendChild(convertToXML(fieldValue));
            else
                element.appendChild(fieldValue.toString());

            root.appendChild(element);
        }
        return root;
    }

    public static <T> Element convertListToXML(List<T> list) throws IllegalAccessException {
        if(list == null || list.isEmpty())
            return null;

        //List is not empty, so the first element will be used to get the class name
        Element root = new Element(list.getFirst().getClass().getSimpleName() + "s");
        for(T obj : list){
            root.appendChild(convertToXML(obj));
        }

        return root;
    }

}
