package MBean;

import Interface.AccountService;
import main.Context;

/**
 * Created by neikila on 30.03.15.
 */
public class AccountServiceController implements AccountServiceControllerMBean {
    private final AccountService accountServer;
    private final Context context;
    public AccountServiceController (Context context) {
        this.accountServer = (AccountService)context.get(AccountService.class);
        this.context = context;
    }

    @Override
    public int getUsers() {
        return accountServer.getAmountOfUsers();
    }

    @Override
    public void setBlock() {
        context.setBlock();
    }

    @Override
    public void unsetBlock() {
        context.unsetBlock();
    }
}
