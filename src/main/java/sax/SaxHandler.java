package sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import reflection.ReflectionHelper;

public class SaxHandler extends DefaultHandler {
    private static String CLASSNAME = "class";
    private String element = null;
    private Object object = null;
    private Object object2 = null;
    private String element2 = null;
    private String currentString = null;
    private Object currentObject = null;
    // TODO Извините за костыль. Будет пофиксено
    // Парсит файлы с одноуровневым вложением классов
    // TODO заменить на логгер

    public void startDocument() throws SAXException {
        System.out.println("Start document");
    }

    public void endDocument() throws SAXException {
        System.out.println("End document ");
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(!qName.equals(CLASSNAME)){
            currentString = qName;
            if (element == null) {
                element = currentString;
            } else {
                element2 = currentString;
            }
        }
        else{
            String className = attributes.getValue(0);
            System.out.println("Class name: " + className);
            currentObject = ReflectionHelper.createInstance(className);
            if (object == null) {
                object = currentObject;
            } else {
                object2 = currentObject;
            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals(CLASSNAME) && object2 != null) {
            System.out.println(element + " = " + object2.toString());
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
            System.out.println(element2 + " = " + value);
            ReflectionHelper.setFieldValue(object2, element2, value);
        } else {
            if (element != null) {
                String value = new String(ch, start, length);
                System.out.println(element + " = " + value);
                ReflectionHelper.setFieldValue(object, element, value);
            }
        }
    }

    public Object getObject(){
        return object;
    }
}