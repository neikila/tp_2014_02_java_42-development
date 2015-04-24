package resource;

import Interface.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sax.ReadXMLFileSAX;

import java.util.HashMap;

/**
 * Created by neikila on 02.04.15.
 */
public class ResourceFactory {

    static final Logger logger = LogManager.getLogger(ResourceFactory.class.getName());

    final private HashMap<String, Resource> resources = new HashMap<>();

    private static ResourceFactory resourceFactory = null;

    public static ResourceFactory instance() {
        if (resourceFactory == null) {
            resourceFactory = new ResourceFactory();
            logger.info(LoggerMessages.resourceFactoryWasCreated());
        }
        return resourceFactory;
    }

    public Resource getResource(String resourceWay) {
        Resource object;
        if((object = resources.get(resourceWay)) == null) {
            object = (Resource) ReadXMLFileSAX.readXML("data/" + resourceWay + ".xml");
            object.checkState();
            resources.put(resourceWay, object);
            logger.info(LoggerMessages.resourceWasParsed(), resourceWay);
        }
        return object;
    }

    private ResourceFactory () {}
}