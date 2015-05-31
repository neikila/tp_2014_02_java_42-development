package test;

import main.Context;
import mechanics.GameMechanics;
import messageSystem.Address;
import messageSystem.AddressService;
import messageSystem.MessageSystem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AddressServiceTest {

    AddressService addressService;
    MessageSystem messageSystem;
    int countThreads;
    Context context;

    @Before
    public void setUp() throws Exception {
        context = new Context();
        messageSystem = new MessageSystem();
        context.add(MessageSystem.class, messageSystem);

    }

    private int getGMId(){
        return messageSystem.getAddressService().getGameMechanicsAddress().hashCode();
    }

    @Test
    public void getSecondaryGMAddress(){
        GameMechanics gameMechanics = mock(GameMechanics.class);
        GameMechanics gameMechanicsSecond = mock(GameMechanics.class);
        when(gameMechanics.getAddress()).thenReturn(new Address());
        when(gameMechanicsSecond.getAddress()).thenReturn(new Address());

        messageSystem.getAddressService().registerGameMechanics(gameMechanics);
        messageSystem.getAddressService().registerGameMechanics(gameMechanicsSecond);

        for (int i=0; i < 3; ++i){
            assertEquals(0, getGMId());
            assertEquals(0, getGMId());
            assertEquals(1,getGMId());
            assertEquals(1,getGMId());
        }
    }
}