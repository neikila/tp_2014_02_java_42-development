package main;

import MBean.AccountServiceController;
import MBean.AccountServiceControllerMBean;
import dbService.DBService;
import dbService.DBServiceImpl;
import main.accountService.AccountService;
import main.accountService.AccountServiceImpl;
import main.accountService.AccountServiceMySQLImpl;
import messageSystem.MessageSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resource.DbServerSettings;
import resource.ResourceFactory;
import resource.ServerSettings;
import utils.LoggerMessages;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class Main {

    public static void main(String[] args) throws Exception {
        ResourceFactory resourceFactory = ResourceFactory.instance();
        Context context = new Context();

        final Logger logger = LogManager.getLogger(Main.class.getName());

        ServerSettings serverSettings = (ServerSettings)resourceFactory.getResource("serverSettings");
        int port = serverSettings.getPort();

        if (serverSettings.isASTypeOfDatabase()) {
            DBService dbService = new DBServiceImpl((DbServerSettings) resourceFactory.getResource("dbServerSettings"));
            context.add(DBService.class, dbService);
        }

        final MessageSystem messageSystem = new MessageSystem();

        AccountService accountService = serverSettings.isASTypeOfDatabase() ? new AccountServiceMySQLImpl(context) : new AccountServiceImpl(context) ;
        accountService.createAdmin();

        if (serverSettings.isProduction())
            accountService.createTestAccount();

        context.add(AccountService.class, accountService);

        startMBean(context);

        logger.info(LoggerMessages.serverStart(), (String.valueOf(port)));

        AppServer server = new AppServer(context, port);
        server.start();
    }

    private static void startMBean(Context context) throws Exception{
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        ObjectName name = new ObjectName("ServerManager:type=AccountServiceController");
        AccountServiceControllerMBean serverStatistics = new AccountServiceController(context);
        mbs.registerMBean(serverStatistics, name);

    }
}