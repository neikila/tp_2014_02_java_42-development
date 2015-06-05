package main;

import MBean.AccountServiceController;
import MBean.AccountServiceControllerMBean;
import dbService.DBService;
import dbService.DBServiceImpl;
import frontend.game.WebSocketService;
import frontend.game.WebSocketServiceImpl;
import main.accountService.AccountService;
import main.accountService.AccountServiceImpl;
import main.accountService.AccountServiceMySQLImpl;
import main.accountService.AccountServiceThread;
import mechanics.GameMechanics;
import mechanics.GameMechanicsImpl;
import messageSystem.MessageSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resource.*;
import utils.LoggerMessages;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class Main {

    public static void main(String[] args) throws Exception {
        ResourceFactory resourceFactory = ResourceFactory.instance();
        resourceFactory.getAllResources();
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

        ThreadsSettings threadsSettings = (ThreadsSettings) ResourceFactory.instance().getResource("threadsSettings");

        for (int i = 0; i < threadsSettings.getASThreadsAmount(); ++i) {
            Thread accountServiceThread = new Thread(new AccountServiceThread(context));
            accountServiceThread.setDaemon(false);
            accountServiceThread.setName("AccountService" + (i + 1));

            accountServiceThread.start();
        }

        WebSocketService webSocketService = new WebSocketServiceImpl(context);
        context.add(WebSocketService.class, webSocketService);
        final Thread webSocketServiceThread = new Thread(webSocketService);
        webSocketServiceThread.start();

        server.start();

        GameMechanics gameMechanics;
        GameMechanicsSettings gameMechanicsSettings = (GameMechanicsSettings)ResourceFactory.instance().getResource("gameMechanicsSettings");
        for (int i = 0; i < threadsSettings.getGMThreadsAmount(); ++i) {
            gameMechanics = new GameMechanicsImpl(context, gameMechanicsSettings);

            final Thread gameMechanicsThread = new Thread(gameMechanics);
            gameMechanicsThread.setDaemon(false);
            gameMechanicsThread.setName("GameMechanics" + (i + 1));
            gameMechanicsThread.start();
        }

        logger.info("version: Threads in process");
        logger.info("Start");
        server.getServer().join();
    }

    private static void startMBean(Context context) throws Exception{
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        ObjectName name = new ObjectName("ServerManager:type=AccountServiceController");
        AccountServiceControllerMBean serverStatistics = new AccountServiceController(context);
        mbs.registerMBean(serverStatistics, name);
    }
}