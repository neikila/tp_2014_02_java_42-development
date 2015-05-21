package resource.sax;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.LoggerMessages;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ReadXMLFileSAX {

    final static private Logger logger = LogManager.getLogger(ReadXMLFileSAX.class);

    public static Object readXML(String xmlFile) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            SaxHandler handler = new SaxHandler();
            saxParser.parse(xmlFile, handler);

            return handler.getObject();

        } catch (Exception e) {
            logger.fatal(LoggerMessages.errorXML());
            logger.fatal(e);
            throw new ReadXMLFileException(e);
        }
    }
}