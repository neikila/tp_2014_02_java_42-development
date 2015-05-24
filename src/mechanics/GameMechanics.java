package mechanics;

import main.Context;
import main.accountService.messages.MessageUpdateProfile;
import main.user.UserProfile;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import messageSystem.MessageSystem;

public final class GameMechanics implements Abonent, Runnable {
    private final Address address = new Address();
    private final MessageSystem messageSystem;

    private final GameMechanicsDAO gameMechanicsDAO;

    public GameMechanics(Context context) {
        this.messageSystem = (MessageSystem) context.get(MessageSystem.class);
        messageSystem.addService(this);
        messageSystem.getAddressService().registerGameMechanics(this);

        this.gameMechanicsDAO = (GameMechanicsDAO) context.get(GameMechanicsDAO.class);
        gameMechanicsDAO.setShellAbove(this);
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public GameMechanicsDAO getGameMechanicsDAO() {
        return gameMechanicsDAO;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void run() {
        int count = 0;
        while (true){
            count++;
            System.out.println("GM " + count);
            gameMechanicsDAO.gmStep();
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void updateUser(UserProfile user) {
        Message updateMes = new MessageUpdateProfile(this.getAddress(), messageSystem.getAddressService().getAccountServiceAddress(), user);
        messageSystem.sendMessage(updateMes);
    }
}
