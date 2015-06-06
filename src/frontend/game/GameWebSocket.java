package frontend.game;

import main.Context;
import main.user.UserProfile;
import mechanics.GameError;
import mechanics.GameMap;
import mechanics.GameSession;
import mechanics.GameUser;
import mechanics.messages.MessageAddUser;
import mechanics.messages.MessageFromWebSocket;
import mechanics.messages.MessageLostConnection;
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
import utils.Messages;

import static org.apache.logging.log4j.LogManager.getLogger;
import static utils.JsonInterpreterFromRequest.getJsonFromString;

@WebSocket
public class GameWebSocket {

    final private Logger logger = getLogger(GameWebSocket.class.getName());

    final private UserProfile user;
    final private Address GMAdress;
    final private Address webSocketAddr;
    final private MessageSystem messageSystem;
    final private Id<GameUser> id;
    private Session session;
    private boolean closed; // TODO возможно ли обратиться к этомму объекту после того как сработает OnSocketClosed
    // На случай, если сокет закрыт, но объект остался
    private boolean gameSessionClosed;
    final private WebSocketService webSocketService;

    public GameWebSocket(UserProfile userProfile, Context context) {
        this.user = userProfile;
        this.id = new Id<>(user.getId());
        this.messageSystem = (MessageSystem) context.get(MessageSystem.class);
        this.GMAdress = messageSystem.getAddressService().getGameMechanicsAddress();
        this.webSocketAddr = messageSystem.getAddressService().getWebSocketServiceAddress();

        this.webSocketService = (WebSocketService) context.get(WebSocketService.class);

        session = null;
        closed = true;
        gameSessionClosed = true;
        logger.info(LoggerMessages.newSocketSuccess());
    }

    public Address getGMAdress() {
        return GMAdress;
    }

    public Id <GameUser> getId() { return id; }

    private void sendJSON(JSONObject jsonObject) {
        if (!closed) {
            try {
                session.getRemote().sendString(jsonObject.toJSONString());
            } catch (Exception e) {
                logger.error(e);
                e.printStackTrace();
            }
        } else {
            logger.error(LoggerMessages.socketClosed());
        }
    }

    public void settings(GameMap map) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", Messages.JSONStatusSettings());
        jsonStart.put("map", map);
        sendJSON(jsonStart);
    }

    public void sync(GameSession session, Id <GameUser> id) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", Messages.JSONStatusSync());
        jsonStart.put("time", session.getSessionTime());

        GameUser gameUser1 = session.getFirst();
        JSONObject firstPlayer = new JSONObject();
        firstPlayer.put("x", gameUser1.getCoordinate().getX());
        firstPlayer.put("y", gameUser1.getCoordinate().getY());
        firstPlayer.put("health", gameUser1.getHealth());
        firstPlayer.put("position", gameUser1.getMyPosition());

        GameUser gameUser2 = session.getSecond();
        JSONObject secondPlayer = new JSONObject();
        secondPlayer.put("x", gameUser2.getCoordinate().getX());
        secondPlayer.put("y", gameUser2.getCoordinate().getY());
        secondPlayer.put("health", gameUser2.getHealth());
        secondPlayer.put("position", gameUser2.getMyPosition());

        jsonStart.put("firstPlayer", firstPlayer);
        jsonStart.put("secondPlayer", secondPlayer);

        sendJSON(jsonStart);
    }

    public void startGame(GameSession session, Id <GameUser> id) {
        int position = session.getUser(id).getMyPosition();
        // Заглушка
        String sequence = "123123123123";
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", Messages.JSONStatusStart());
        jsonStart.put("position", position);
        jsonStart.put("enemyName", session.getEnemy(position).getUser().getLogin());
        jsonStart.put("sequence", sequence.substring(0, sequence.length() - 3) + "&#x200B;" + sequence.substring(sequence.length() - 3, sequence.length()));
        sendJSON(jsonStart);
    }

    public void gameOver(int result) {
        gameSessionClosed = true;
        JSONObject jsonEnd = new JSONObject();
        jsonEnd.put("status", Messages.JSONStatusFinish());
        jsonEnd.put("result", result);
        sendJSON(jsonEnd);
    }

    public void sendAction(JSONObject action) {
        sendJSON(action);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        if (!gameSessionClosed) {
            JSONObject message = getJsonFromString(data);
            messageSystem.sendMessage(new MessageFromWebSocket(webSocketAddr, GMAdress, id, message));
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        closed = false;
        gameSessionClosed = false;
        logger.info(LoggerMessages.onOpen(), user.getLogin());
        setSession(session);

        webSocketService.addUser(this);
        messageSystem.sendMessage(new MessageAddUser(webSocketAddr, GMAdress, id, user));
    }

    public Session getSession() {
        return session;
    }

    private void setSession(Session session) {
        this.session = session;
    }

    public void opponentLostConnection(GameError gameError) {
        gameSessionClosed = true;
        JSONObject jsonEnd = new JSONObject();
        jsonEnd.put("error", gameError.getErrorMessage());
        jsonEnd.put("code", gameError.ordinal());
        sendJSON(jsonEnd);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        closed = true;
        if (!gameSessionClosed) {       // Закрытие во время игры
            webSocketService.removeUser(id);
            messageSystem.sendMessage(new MessageLostConnection(webSocketAddr, GMAdress, id));
            gameSessionClosed = true;
        }
        logger.info(LoggerMessages.onClose(), user.getLogin());
    }
}