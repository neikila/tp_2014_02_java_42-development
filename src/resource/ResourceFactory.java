package resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resource.sax.ReadXMLFileSAX;
import utils.LoggerMessages;
import utils.vfs.VFS;
import utils.vfs.VFSImpl;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by neikila on 02.04.15.
 */
public class ResourceFactory {

    static final Logger logger = LogManager.getLogger(ResourceFactory.class.getName());

    final private HashMap<String, Resource> resources = new HashMap<>();

    private static ResourceFactory resourceFactory = null;

    private String resourceDirectory = "data/resourceFiles/";

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
            object = (Resource) ReadXMLFileSAX.readXML(resourceDirectory + resourceWay + ".xml");
            object.checkState();
            resources.put(resourceWay, object);
            logger.info(LoggerMessages.resourceWasParsed(), resourceWay);
        }
        return object;
    }

    private ResourceFactory () { resourceDirectory = "data/resourceFiles/"; }

    public void getAllResources() {
        VFS temp = new VFSImpl(resourceDirectory);
        Iterator<String> iterator = temp.getIterator("");
        while (iterator.hasNext()) {
            String fileName = iterator.next();
            int indexPoint = fileName.lastIndexOf(".");
            if (indexPoint > 0 && fileName.length() - indexPoint == 4) {
                fileName = fileName.substring(fileName.lastIndexOf("/") + 1, indexPoint);
                getResource(fileName);
            }
        }
    }
}