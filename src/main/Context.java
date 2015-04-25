package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by neikila on 31.03.15.
 */
public class Context {

    static final Logger logger = LogManager.getLogger(Context.class.getName());
    private Map<Class<?>, Object> context = new HashMap<>();
    private boolean block;

    public Context () {
        block = false;
    }

    public void add(Class<?> clazz, Object object) {
        if (context.containsKey(clazz)) {
            logger.info("Class {} is already in context.", clazz.getName());
        } else {
            context.put(clazz, object);
        }
    }

    public Object get(Class<?> clazz) {
        if (context.containsKey(clazz)) {
            return context.get(clazz);
        } else {
            logger.info("Class {} is not in context.", clazz.getName());
            return null;
            // TODO добавить проверки при вызове
        }
    }

    public void remove(Class<?> clazz) {
        context.remove(clazz);
    }

    public void setBlock() {
        block = true;
    }

    public void unsetBlock() {
        block = false;
    }

    public boolean isBlocked() {
        return block;
    }
}
