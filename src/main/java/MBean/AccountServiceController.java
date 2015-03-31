package MBean;

import Interface.AccountService;

/**
 * Created by neikila on 30.03.15.
 */
public class AccountServiceController implements AccountServiceControllerMBean {
    private final AccountService accountServer;
    public AccountServiceController (AccountService accountServer) {
        this.accountServer = accountServer;
    }

    @Override
    public int getUsers() {
        return accountServer.getAmountOfUsers();
    }
}
