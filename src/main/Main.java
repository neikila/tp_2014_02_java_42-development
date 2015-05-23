package main;

import MBean.AccountServiceController;
import MBean.AccountServiceControllerMBean;
import dbService.DBService;
import dbService.DBServiceImpl;
import main.accountService.AccountService;
import main.accountService.AccountServiceDAO;
import main.accountService.AccountServiceDAOImpl;
import main.accountService.AccountServiceDAOMySQLImpl;
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

        AccountServiceDAO accountServiceDAO = serverSettings.isASTypeOfDatabase() ? new AccountServiceDAOMySQLImpl(context) : new AccountServiceDAOImpl(context) ;
        accountServiceDAO.createAdmin();

        if (!serverSettings.isProduction())
            accountServiceDAO.createTestAccount();

        context.add(AccountServiceDAO.class, accountServiceDAO);


        startMBean(context);

        logger.info(LoggerMessages.serverStart(), (String.valueOf(port)));

        AppServer server = new AppServer(context, port);

        final Thread accountServiceThread = new Thread(new AccountService(context));
        accountServiceThread.setDaemon(true);
        accountServiceThread.setName("Account Service");

        accountServiceThread.start();
//        final Thread gameMechanicsThread = new Thread(new GameMechanics(messageSystem));
//        gameMechanicsThread.setDaemon(true);
//        gameMechanicsThread.setName("Game Mechanics");
//        final FrontEnd frontEnd = new FrontEnd(messageSystem);
//        final Thread frontEndThread = new Thread(frontEnd);
//        frontEndThread.setDaemon(true);
//        frontEndThread.setName("FrontEnd");
//
//        accountServiceThread.start();
//        gameMechanicsThread.start();
//        frontEndThread.start();
        server.start();
        logger.info("Threads in process");

        GameMechanics gameMechanics;
        GameMechanicsSettings gameMechanicsSettings = (GameMechanicsSettings)ResourceFactory.instance().getResource("gameMechanicsSettings");
        gameMechanics = new GameMechanicsImpl(context, gameMechanicsSettings);
        context.add(GameMechanics.class, gameMechanics);

        gameMechanics.run();
    }

    private static void startMBean(Context context) throws Exception{
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        ObjectName name = new ObjectName("ServerManager:type=AccountServiceController");
        AccountServiceControllerMBean serverStatistics = new AccountServiceController(context);
        mbs.registerMBean(serverStatistics, name);

    }
}