package frontend.game;

import main.Context;
import mechanics.GameUser;
import mechanics.messages.MessageFromWebSocket;
import messageSystem.Address;
import messageSystem.MessageSystem;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONObject;
import utils.Id;
import utils.LoggerMessages;

import static org.apache.logging.log4j.LogManager.getLogger;
import static utils.JsonInterpreterFromRequest.getJsonFromString;

@WebSocket
public class PhoneWebSocket {

    final private Logger logger = getLogger(PhoneWebSocket.class.getName());

    final private Address GMAdress;
    final private Address webSocketAddr;
    final private MessageSystem messageSystem;
    final private Id<GameUser> id;
    private Session session;

    public PhoneWebSocket(Id <GameUser> id, Context context) {
        this.id = id;
        this.messageSystem = (MessageSystem) context.get(MessageSystem.class);
        WebSocketService webSocketService = (WebSocketService) context.get(WebSocketService.class);
        this.GMAdress = webSocketService.getSocket(id).getGMAdress();
        this.webSocketAddr = messageSystem.getAddressService().getWebSocketServiceAddress();

        session = null;
        logger.info(LoggerMessages.newSocketSuccess());
        System.out.println("Phone Socket created  with id = " + id.getId());
    }

    public Id <GameUser> getId() { return id; }

    @OnWebSocketMessage
    public void onMessage(String data) {
        JSONObject message = getJsonFromString(data);
        messageSystem.sendMessage(new MessageFromWebSocket(webSocketAddr, GMAdress, id, message));
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        System.out.println("Phone socket");
        logger.info("User with id {} open gamePad socket", id.getId());
        setSession(session);
    }

    public Session getSession() {
        return session;
    }

    private void setSession(Session session) {
        this.session = session;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        logger.info(LoggerMessages.onClose(), id.getId());
    }
}