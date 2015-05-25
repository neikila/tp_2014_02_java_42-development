package test;

import main.Context;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ContextTest {
    private Context context;

    @Before
    public void setUp() throws Exception {
        context = new Context();
    }

    @Test
    public void testAdd() throws Exception {
        int size = 0;
        int newSize = 0;

        try {
            Field field = context.getClass().getDeclaredField("context");
            field.setAccessible(true);

            size = ((Map)field.get(context)).size();

            context.add(Object.class, new Object());

            newSize = ((Map)field.get(context)).size();

        } catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        assertEquals(1, newSize - size);
    }

    @Test
    public void testGet() throws Exception {
        Float num = new Float(10);
        context.add(Float.class, num);

        Float returnFloat = (Float) context.get(Float.class);

        assertEquals(num, returnFloat);
    }

    @Test
    public void testRemove() throws Exception {
        int size = 0;
        int newSize = 0;

        try {
            Field field = context.getClass().getDeclaredField("context");
            field.setAccessible(true);
            ((Map)field.get(context)).put(Object.class, new Object());

            size = ((Map)field.get(context)).size();

            context.remove(Object.class);

            newSize = ((Map)field.get(context)).size();

            field.setAccessible(false);
        } catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        assertEquals(1, size - newSize);
    }

    @Test
    public void testSetBlock() throws Exception {
        context.setBlock();

        assertEquals(true, context.isBlocked());
    }

    @Test
    public void testUnsetBlock() throws Exception {
        try {
            Field field = context.getClass().getDeclaredField("block");
            field.setAccessible(true);

            field.set(context, true);

            field.setAccessible(false);
        } catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        context.unsetBlock();

        assertEquals(false, context.isBlocked());
    }
}