package test;

import org.junit.Test;
import resource.Resource;
import resource.ResourceFactory;

import static org.junit.Assert.*;

public class ResourceFactoryTest {

    @Test
    public void testGetResource() throws Exception {
        TestResource resource = (TestResource) ResourceFactory.instance().getResource("test");

        assertEquals(8080, resource.getSuccess());
        assertNotEquals(null, resource);
    }

    @Test
    public void testGetNotExistingResource() throws Exception {
        Resource resource;
        try {
            resource = ResourceFactory.instance().getResource("testXMLDifferentFromResource");
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
            resource = ResourceFactory.instance().getResource("notExist");
        } catch (Exception  e) {
            e.printStackTrace();
            resource = null;
        }
        assertEquals(null, resource);
    }

    @Test
    public void testGetWrongXMLResource() throws Exception {
        Resource resource;
        try {
            resource = ResourceFactory.instance().getResource("wrongTest");
        } catch (Exception  e) {
            e.printStackTrace();
            resource = null;
        }
        assertEquals(null, resource);
    }
}