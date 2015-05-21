package MBean;

import main.Context;
import main.accountService.AccountService;
import org.apache.logging.log4j.Logger;
import utils.LoggerMessages;

import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * Created by neikila on 30.03.15.
 */
public class AccountServiceController implements AccountServiceControllerMBean {
    final private Logger logger = getLogger(AccountServiceController.class.getName());
    final private AccountService accountServer;
    final private Context context;

    public AccountServiceController(Context context) {
        this.accountServer = (AccountService) context.get(AccountService.class);
        this.context = context;
    }

    @Override
    public long getUsers() {
        return accountServer.getAmountOfUsers();
    }

    @Override
    public void setBlock() {
        logger.info(LoggerMessages.setBlock());
        context.setBlock();
    }

    @Override
    public void unsetBlock() {
        logger.info(LoggerMessages.unsetBlock());
        context.unsetBlock();
    }
}
