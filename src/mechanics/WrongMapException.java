package mechanics;

/**
 * Created by neikila on 10.05.15.
 */
public class WrongMapException extends RuntimeException {

    public WrongMapException() {
        super();
    }

    public WrongMapException(String string) {
        super(string);
    }

    public WrongMapException(Exception e) {
        super(e);
    }
}
