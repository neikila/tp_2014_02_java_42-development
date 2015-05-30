package resource;

import java.io.Serializable;

/**
 * Created by neikila on 03.04.15.
 */
public class ThreadsSettings implements Serializable, Resource {

    private int GMThreadsAmount;
    private int GMTimeStep;
    private int ASThreadsAmount;
    private int ASTimeStep;
    private int webSocketServiceTimeStep;

    public ThreadsSettings() {
        GMThreadsAmount = 1;
        GMTimeStep = 100;
        ASThreadsAmount = 1;
        ASTimeStep = 100;
        webSocketServiceTimeStep = 10;
    }

    public int getASThreadsAmount() {
        return ASThreadsAmount;
    }

    public int getGMThreadsAmount() {
        return GMThreadsAmount;
    }

    public int getGMTimeStep() {
        return GMTimeStep;
    }

    public int getASTimeStep() {
        return ASTimeStep;
    }

    public int getWebSocketServiceTimeStep() {
        return webSocketServiceTimeStep;
    }

    public void checkState() {
    }
}
