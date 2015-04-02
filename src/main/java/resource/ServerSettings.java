package resource;

import Interface.Resource;

import java.io.Serializable;

/**
 * Created by neikila on 02.04.15.
 */
public class ServerSettings implements Serializable, Resource {
    private int port;

    public ServerSettings() {
        this.port = 80;
    }

    public ServerSettings(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String toString() {
        return "Port: " + port;
    }
}
