package test;

import org.junit.Before;
import org.junit.Test;
import resource.Resource;
import resource.ResourceFactory;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class ResourceFactoryTest {
    private ResourceFactory resourceFactory;

    @Before
    public void setup() throws Exception {
        resourceFactory = ResourceFactory.instance();
        Field field = resourceFactory.getClass().getDeclaredField("resourceDirectory");
        try {
            field.setAccessible(true);
            field.set(resourceFactory, "data/testFiles/");
            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetResource() throws Exception {
        TestResource resource = (TestResource) resourceFactory.getResource("test");

        assertEquals(8080, resource.getSuccess());
        assertNotEquals(null, resource);
    }

    @Test
    public void testGetNotExistingResource() throws Exception {
        Resource resource;
        try {
            resource = resourceFactory.getResource("testXMLDifferentFromResource");
        } catch (Exception  e) {
            e.printStackTrace();
            resource = null;
        }
        assertEquals(null, resource);
    }

    @Test
    public void testGetNotExistingXML() throws Exception {
        Resource resource;
        try {
            resource = resourceFactory.getResource("notExist");
        } catch (Exception  e) {
            resource = null;
        }
        assertEquals(null, resource);
    }

    @Test
    public void testGetWrongXMLResource() throws Exception {
        Resource resource;
        try {
            resource = resourceFactory.getResource("wrongTest");
        } catch (Exception  e) {
            e.printStackTrace();
            resource = null;
        }
        assertEquals(null, resource);
    }
}