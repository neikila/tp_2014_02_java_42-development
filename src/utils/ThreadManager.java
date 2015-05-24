package utils;

import main.AppServer;
import org.eclipse.jetty.server.Server;

import java.util.ArrayList;

/**
 * Created by neikila on 23.05.15.
 */
public class ThreadManager {
    private ArrayList <Thread> ASThreads;
    private ArrayList <Thread> GMThreads;
    private AppServer frontEnd;

    public ThreadManager() {
        ASThreads = new ArrayList<>();
        GMThreads = new ArrayList<>();
    }

    public void addASThread(Thread thread) {
        ASThreads.add(thread);
    }

    public void addGMThread(Thread thread) {
        GMThreads.add(thread);
    }

    public void setFrontEndThread(AppServer appServer) {
        frontEnd = appServer;
    }

    public void interruptThreads() {
        new Thread() {
            @Override
            public void run() {
                if(frontEnd != null) {
                    Server server = frontEnd.getServer();
                    try {
                        server.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (Thread a: GMThreads) {
                    a.interrupt();
                }
                GMThreads = null;

                for (Thread a: ASThreads) {
                    a.interrupt();
                }
                ASThreads = null;
            }
        }.start();
    }
}
