package main;

import Interface.AccountService;
import MBean.*;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class Main {

    public static void main(String[] args) throws Exception {

        int port = 80;
        if (args.length == 1) {
            String portString = args[0];
            port = Integer.valueOf(portString);
        } else {
            System.out.append("Порт не задан.");
        }

        AccountService accountService = new AccountServiceImpl();
        accountService.createAdmin();

        AccountServiceControllerMBean serverStatistics = new AccountServiceController(accountService);
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ServerManager:type=AccountServiceController");
        mbs.registerMBean(serverStatistics, name);


        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');

        AppServer server = new AppServer(port, accountService);
        server.start();
    }
}