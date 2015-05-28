package messageSystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageSystem {
    private final Logger logger = LogManager.getLogger(MessageSystem.class.getName());
    private final Map<Address, ConcurrentLinkedQueue<Message>> messages = new HashMap<>();
    private final AddressService addressService = new AddressService();

    public MessageSystem() {
    }

    public AddressService getAddressService() {
        return addressService;
    }

    public void addService(Abonent abonent) {
        messages.put(abonent.getAddress(), new ConcurrentLinkedQueue<>());
    }

    public void sendMessage(Message message) {
        ConcurrentLinkedQueue<Message> queue = messages.get(message.getTo());
        if (queue != null)
            queue.add(message);
        else {
            logger.error("Such address are not existing {}", message.getTo());
        }
    }

    public void execForAbonent(Abonent abonent) {
        ConcurrentLinkedQueue<Message> queue = messages.get(abonent.getAddress());
        if (queue != null) {
            while (!queue.isEmpty()) {
                Message message = queue.poll();
                message.exec(abonent);
            }
        } else {
            logger.error("Abonent with address {} is not registered", abonent.getAddress());
        }
    }

    public int getMessagesAmount(Abonent abonent) {
        ConcurrentLinkedQueue<Message> queue = messages.get(abonent.getAddress());
        if (queue == null)
            return -1;
        else
            return queue.size();
    }
}
