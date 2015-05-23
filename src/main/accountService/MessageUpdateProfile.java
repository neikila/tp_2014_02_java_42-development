package main.accountService;

import main.user.UserProfile;
import mechanics.MessageUpdateResult;
import messageSystem.Address;
import messageSystem.Message;

public final class MessageUpdateProfile extends MessageToAccountService {
    private UserProfile user;

    public MessageUpdateProfile(Address from, Address to, UserProfile user) {
        super(from, to);
        this.user = user;
    }

    @Override
    protected void exec(AccountService service) {
        service.getAccountServiceDAO().updateUser(user);
        final Message back = new MessageUpdateResult(getTo(), getFrom(), true);
        service.getMessageSystem().sendMessage(back);
    }
}
