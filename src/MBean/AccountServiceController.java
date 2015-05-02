package MBean;

import Interface.AccountService;
import main.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resource.LoggerMessages;
import resource.ResourceFactory;

/**
 * Created by neikila on 30.03.15.
 */
public class AccountServiceController implements AccountServiceControllerMBean {
    final private Logger logger = LogManager.getLogger(AccountServiceController.class.getName());
    final private LoggerMessages loggerMessages = (LoggerMessages) ResourceFactory.instance().getResource("loggerMessages");
    final private AccountService accountServer;
    final private Context context;

    public AccountServiceController (Context context) {
        this.accountServer = (AccountService)context.get(AccountService.class);
        this.context = context;
    }

    @Override
    public long getUsers() {
        return accountServer.getAmountOfUsers();
    }

    @Override
    public void setBlock() {
        logger.info(loggerMessages.setBlock());
        context.setBlock();
    }

    @Override
    public void unsetBlock() {
        logger.info(loggerMessages.unsetBlock());
        context.unsetBlock();
    }
}
