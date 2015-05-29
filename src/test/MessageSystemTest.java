package test;

import messageSystem.Abonent;
import messageSystem.Message;
import messageSystem.MessageSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MessageSystemTest {

    MessageSystem messageSystem;

    @Before
    public void setUp() throws Exception {
        messageSystem = new MessageSystem();
    }

    @Test
    public void testGetAddressService() throws Exception {
        assertNotEquals(null, messageSystem.getAddressService());
    }
    @Test
    public void testAddService() throws Exception {
        Abonent abonent = new AbonentForTest();
        messageSystem.addService(abonent);

        assertEquals(0, messageSystem.getMessagesAmount(abonent));
    }

    @Test
    public void testSendMessage() throws Exception {
        Abonent abonent1 = new AbonentForTest();
        Abonent abonent2 = new AbonentForTest();
        messageSystem.addService(abonent1);
        messageSystem.addService(abonent2);
        Message message = new Message(abonent1.getAddress(), abonent2.getAddress()) {
            @Override
            public void exec(Abonent abonent) {

            }
        };
        messageSystem.sendMessage(message);

        assertEquals(1, messageSystem.getMessagesAmount(abonent2));
    }

    @Test
    public void testSendMessageToNotExistingAbonent() throws Exception {
        Abonent abonent1 = new AbonentForTest();
        Abonent abonent2 = new AbonentForTest();
        messageSystem.addService(abonent1);

        Message message = new Message(abonent1.getAddress(), abonent2.getAddress()) {
            @Override
            public void exec(Abonent abonent) {

            }
        };
        messageSystem.sendMessage(message);

        assertEquals(-1, messageSystem.getMessagesAmount(abonent2));
    }

    @Test
    public void testExecForAbonent() throws Exception {
        Abonent abonent1 = new AbonentForTest();
        Abonent abonent2 = new AbonentForTest();
        messageSystem.addService(abonent1);
        messageSystem.addService(abonent2);

        Message message = new Message(abonent1.getAddress(), abonent2.getAddress()) {
            @Override
            public void exec(Abonent abonent) {
                ((AbonentForTest)abonent).execute();
            }
        };
        messageSystem.sendMessage(message);

        messageSystem.execForAbonent(abonent2);
        assertEquals(0, messageSystem.getMessagesAmount(abonent2));
        assertEquals(true, ((AbonentForTest)abonent2).getResult());
    }

    @Test
    public void testExecForNotRegisteredAbonent() throws Exception {
        Abonent abonent1 = new AbonentForTest();
        Abonent abonent2 = new AbonentForTest();

        Message message = new Message(abonent1.getAddress(), abonent2.getAddress()) {
            @Override
            public void exec(Abonent abonent) {
                ((AbonentForTest)abonent).execute();
            }
        };
        messageSystem.sendMessage(message);

        boolean result = true;
        try {
            messageSystem.execForAbonent(abonent2);
        } catch (Exception e) {
            result = false;
        }

        assertEquals(true, result);
    }
}