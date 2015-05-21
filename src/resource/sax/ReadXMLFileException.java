package resource.sax;

/**
 * Created by neikila on 11.04.15.
 */
public class ReadXMLFileException extends RuntimeException {

    public ReadXMLFileException() {
        super();
    }

    public ReadXMLFileException(String string) {
        super(string);
    }

    public ReadXMLFileException(Exception e) {
        super(e);
    }
}
