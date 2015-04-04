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
        if (resourceWay == null) {
            return null;
        }
        Resource object;
        // TODO можно делать в одной действие. object == null после get дает понять что ресурса там нет
        if(!resources.containsKey(resourceWay)) {
            object = (Resource) ReadXMLFileSAX.readXML("data/" + resourceWay + ".xml");
            object.checkState();
            resources.put(resourceWay, object);
            logger.info(LoggerMessages.resourceWasParsed(), resourceWay);
        } else {
            object = resources.get(resourceWay);
        }
        return object;
    }

    private ResourceFactory () {};
}