package resource.sax;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import resource.reflection.ReflectionHelper;
import utils.LoggerMessages;

public class SaxHandler extends DefaultHandler {

    final private Logger logger = LogManager.getLogger(SaxHandler.class.getName());
    private static String CLASSNAME = "class";
    private String element = null;
    private Object object = null;
    private Object object2 = null;
    private String element2 = null;
    // TODO Извините за костыль. Будет пофиксено
    // Парсит файлы с одноуровневым вложением классов

    public void startDocument() throws SAXException {
        logger.info(LoggerMessages.startXML());
    }

    public void endDocument() throws SAXException {
        logger.info(LoggerMessages.endXML());
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(!qName.equals(CLASSNAME)){
            if (element == null) {
                element = qName;
            } else {
                element2 = qName;
            }
        } else{
            String className = attributes.getValue(0);
            logger.info(LoggerMessages.className(), className);
            if (object == null) {
                object = ReflectionHelper.createInstance(className);
            } else {
                object2 = ReflectionHelper.createInstance(className);
            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals(CLASSNAME) && object2 != null) {
            logger.info(LoggerMessages.aEqualB(), element, object2.toString());
            ReflectionHelper.setFieldValue(object, element, object2);
            object2 = null;
        }
        if (element2 != null)
            element2 = null;
        else
            element = null;
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        if (element2 != null) {
            String value = new String(ch, start, length);
            logger.info(LoggerMessages.aEqualB(), element2, value);
            ReflectionHelper.setFieldValue(object2, element2, value);
        } else {
            if (element != null) {
                String value = new String(ch, start, length);
                logger.info(LoggerMessages.aEqualB(), element, value);
                ReflectionHelper.setFieldValue(object, element, value);
            }
        }
    }

    public Object getObject(){
        return object;
    }
}