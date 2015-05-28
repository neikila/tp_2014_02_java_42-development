package test;

import frontend.game.*;
import main.Context;
import main.accountService.AccountService;
import main.accountService.AccountServiceImpl;
import main.user.UserProfile;
import mechanics.GameUser;
import messageSystem.Address;
import messageSystem.AddressService;
import messageSystem.MessageSystem;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.junit.Before;
import org.junit.Test;
import utils.Id;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameWebSocketCreatorTest {

    private AccountService accountService;
    private Context context;
    private Address address;
    private Address GMAddress;
    private AddressService addressService;

    long id = 100;
    String sessionId = "000";
    String sessionPhoneId = "phone";
    ServletUpgradeRequest req;
    ServletUpgradeResponse res;

    @Before
    public void setUp() throws Exception {
        context = new Context();
        accountService = new AccountServiceImpl(context);

        context.add(AccountService.class, accountService);


        MessageSystem messageSystem = mock(MessageSystem.class);
        address = mock(Address.class);
        GMAddress = mock(Address.class);

        addressService = mock(AddressService.class);
        when(addressService.getWebSocketServiceAddress()).thenReturn(address);
        when(addressService.getGameMechanicsAddress()).thenReturn(address);
        when(messageSystem.getAddressService()).thenReturn(addressService);


        context.add(MessageSystem.class, messageSystem);

        req = mock(ServletUpgradeRequest.class);
        res = mock(ServletUpgradeResponse.class);
    }

    @Test
    public void testCreateWebSocket() throws Exception {

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(session.getId()).thenReturn(sessionPhoneId);
        when(httpServletRequest.getSession()).thenReturn(session);
        when(req.getHttpServletRequest()).thenReturn(httpServletRequest);

        Id<GameUser> id = new Id<>(this.id);

        GameWebSocket gameWebSocket = mock(GameWebSocket.class);
        when(gameWebSocket.getGMAdress()).thenReturn(GMAddress);
        when(gameWebSocket.getId()).thenReturn(id);

        WebSocketService webSocketService = new WebSocketServiceImpl(context);
        webSocketService.addUser(gameWebSocket);

        context.add(WebSocketService.class, webSocketService);

        UserProfile user = new UserProfile(this.id, "login", "password", "email@gmail.com");

        accountService.addSessions(sessionId, user);
        accountService.addPhoneSession(sessionPhoneId, user.getLogin());

        GameWebSocketCreator test = new GameWebSocketCreator(context);
        Object result = test.createWebSocket(req, res);

        assertNotEquals(null, result);
        assertEquals(true, result instanceof PhoneWebSocket);
        assertEquals(this.id, ((PhoneWebSocket)result).getId().getId());
    }

    @Test
    public void test2CreateWebSocket() throws Exception {

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(session.getId()).thenReturn(sessionId);
        when(httpServletRequest.getSession()).thenReturn(session);
        when(req.getHttpServletRequest()).thenReturn(httpServletRequest);

        UserProfile user = new UserProfile(id, "login", "password", "email@gmail.com");

        accountService.addSessions(sessionId, user);

        GameWebSocketCreator test = new GameWebSocketCreator(context);
        Object result = test.createWebSocket(req, res);

        assertNotEquals(null, result);
        assertEquals(true, result instanceof GameWebSocket);
        assertEquals(id, ((GameWebSocket) result).getId().getId());
    }

    @Test
    public void test3CreateWebSocket() throws Exception {

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(session.getId()).thenReturn(null);
        when(httpServletRequest.getSession()).thenReturn(session);
        when(req.getHttpServletRequest()).thenReturn(httpServletRequest);

        UserProfile user = new UserProfile(id, "login", "password", "email@gmail.com");

        accountService.addSessions(sessionId, user);

        GameWebSocketCreator test = new GameWebSocketCreator(context);
        Object result = test.createWebSocket(req, res);

        assertEquals(null, result);
    }
}