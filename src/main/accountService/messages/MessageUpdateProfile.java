package main.accountService.messages;

import main.accountService.AccountService;
import main.user.UserProfile;
import mechanics.messages.MessageUpdateResult;
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
