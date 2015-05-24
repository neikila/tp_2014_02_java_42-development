package main;

import MBean.AccountServiceController;
import MBean.AccountServiceControllerMBean;
import dbService.DBService;
import dbService.DBServiceImpl;
import main.accountService.AccountServiceThread;
import main.accountService.AccountService;
import main.accountService.AccountServiceImpl;
import main.accountService.AccountServiceMySQLImpl;
import mechanics.GameMechanics;
import mechanics.GameMechanicsImpl;
import messageSystem.MessageSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resource.DbServerSettings;
import resource.GameMechanicsSettings;
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
        context.add(MessageSystem.class, messageSystem);

        AccountService accountService = serverSettings.isASTypeOfDatabase() ? new AccountServiceMySQLImpl(context) : new AccountServiceImpl(context) ;
        accountService.createAdmin();

        if (!serverSettings.isProduction())
            accountService.createTestAccount();

        context.add(AccountService.class, accountService);


        startMBean(context);

        logger.info(LoggerMessages.serverStart(), (String.valueOf(port)));

        AppServer server = new AppServer(context, port);
//        context.add(AppServer.class, server);

        for (int i = 0; i < 2; ++i) {
            final Thread accountServiceThread = new Thread(new AccountServiceThread(context));
            accountServiceThread.setDaemon(false);
            accountServiceThread.setName("AccountService" + (i + 1));
//        context.add(AccountService.class, accountServiceThread);

            accountServiceThread.start();
        }
        server.start();

        GameMechanics gameMechanics;
        GameMechanicsSettings gameMechanicsSettings = (GameMechanicsSettings)ResourceFactory.instance().getResource("gameMechanicsSettings");
        gameMechanics = new GameMechanicsImpl(context, gameMechanicsSettings);
        context.add(GameMechanics.class, gameMechanics);

        final Thread gameMechanicsThread = new Thread(gameMechanics);
        gameMechanicsThread.setDaemon(false);
        gameMechanicsThread.setName("GameMechanics");
//        context.add(GameMechanics.class, gameMechanicsThread);

        gameMechanicsThread.start();

        logger.info("version: Threads in process");
        logger.info("Start");
        gameMechanicsThread.join();
    }

    private static void startMBean(Context context) throws Exception{
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        ObjectName name = new ObjectName("ServerManager:type=AccountServiceController");
        AccountServiceControllerMBean serverStatistics = new AccountServiceController(context);
        mbs.registerMBean(serverStatistics, name);

    }
}