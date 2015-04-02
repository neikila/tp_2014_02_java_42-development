package main;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by neikila on 31.03.15.
 */
public class Context {
    private Map<Class<?>, Object> context = new HashMap<>();
    private boolean block;

    public Context () {
        block = false;
    }

    public void add(Class<?> clazz, Object object) {
        if (context.containsKey(clazz)) {
            // TODO logger say about already containing such class
        } else {
            context.put(clazz, object);
            // TODO what is @Flow
        }
    }

    public Object get(Class<?> clazz) {
        if (context.containsKey(clazz)) {
            return context.get(clazz);
        } else {
            // TODO logger say about no containing such class
            return null;
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
