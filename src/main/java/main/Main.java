package main;

import Interface.AccountService;
import MBean.*;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import org.apache.logging.log4j.*;

public class Main {

    static final Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {
        Context context = new Context();
        int port = 80;
        if (args.length > 0) {
            String portString = args[0];
            port = Integer.valueOf(portString);
            context.setPort(port);
        } else {
            logger.fatal("Порт не задан.");
        }

        AccountService accountService = new AccountServiceImpl();
        accountService.createAdmin();
        context.add(AccountService.class, accountService);

        AccountServiceControllerMBean serverStatistics = new AccountServiceController(context);
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ServerManager:type=AccountServiceController");
        mbs.registerMBean(serverStatistics, name);

        logger.info("Starting at port: " + (String.valueOf(port)) + "\n");

        AppServer server = new AppServer(context);
        server.start();
    }
}