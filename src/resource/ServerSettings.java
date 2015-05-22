package resource;

import java.io.Serializable;

/**
 * Created by neikila on 02.04.15.
 */
public class ServerSettings implements Serializable, Resource {
    private int port;
    private String mode;
    private String ASType;

    public ServerSettings() {
        this.port = 80;
    }

    public ServerSettings(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String toString() {
        return "Port: " + port;
    }

    public boolean isProduction() { return mode.equals("Production"); }

    public boolean isASTypeOfDatabase() { return ASType.equals("Database"); }

    @Override
    public void checkState() {
        if (port < 50 || port > 65000)
            port = 8080;
    }
}
