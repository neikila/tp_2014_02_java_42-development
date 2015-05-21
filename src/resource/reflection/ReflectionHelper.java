package resource.reflection;

/**
 * Created by neikila on 02.04.15.
 */
import java.lang.reflect.Field;

public class ReflectionHelper {
    public static Object createInstance(String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (IllegalArgumentException | SecurityException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setFieldValue(Object object,
                                     String fieldName,
                                     String value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            if (field.getType().equals(String.class)) {
                field.set(object, value);
            } else if (field.getType().equals(int.class)) {
                field.set(object, Integer.decode(value));
            }

            field.setAccessible(false);
        } catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setFieldValue(Object object,
                                     String fieldName,
                                     Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            field.set(object, value);

            field.setAccessible(false);
        } catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
