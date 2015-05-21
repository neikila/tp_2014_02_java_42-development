package frontend.game;

import main.Context;
import mechanics.GameMap;
import mechanics.GameMechanics;
import mechanics.GameSession;
import mechanics.GameUser;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONObject;
import utils.LoggerMessages;
import utils.Messages;

import static org.apache.logging.log4j.LogManager.getLogger;
import static utils.JsonInterpreterFromRequest.getJsonFromString;

@WebSocket
public class GameWebSocket {

    final private Logger logger = getLogger(GameWebSocket.class.getName());

    final private String myName;
    final private GameMechanics gameMechanics;
    final private WebSocketService webSocketService;
    private Session session;
    private boolean closed; // TODO возможно ли обратиться к этомму объекту после того как сработает OnSocketClosed
    // На случай, если сокет закрыт, но объект остался
    private boolean gameSessionClosed;

    public GameWebSocket(String myName, Context context) {
        this.gameMechanics = (GameMechanics) context.get(GameMechanics.class);
        this.webSocketService = (WebSocketService) context.get(WebSocketService.class);
        this.myName = myName;
        session = null;
        closed = true;
        gameSessionClosed = true;
        logger.info(LoggerMessages.newSocketSuccess());
    }

    public String getMyName() {
        return myName;
    }

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

    public void startGame(GameSession session, int position) {
        String sequence = "123123123123";
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", Messages.JSONStatusStart());
        jsonStart.put("position", position);
        jsonStart.put("enemyName", session.getEnemy(position).getMyName());
        jsonStart.put("sequence", sequence.substring(0, sequence.length() - 3) + "&#x200B;" + sequence.substring(sequence.length() - 3, sequence.length()));
        sendJSON(jsonStart);
    }

    public void gameOver(GameUser user, int result) {
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
            gameMechanics.analyzeMessage(myName, message);
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        closed = false;
        gameSessionClosed = false;
        logger.info(LoggerMessages.onOpen(), myName);
        setSession(session);
        webSocketService.addUser(this);
        gameMechanics.addUser(myName);
    }

    public void setMyScore(GameUser user) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", Messages.JSONStatusIncrement());
        jsonStart.put("name", user.getMyName());
        jsonStart.put("score", user.getMyScore());
        sendJSON(jsonStart);
    }

    public void setMyResult(String result) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", Messages.JSONStatusResult());
        jsonStart.put("result", result);
        sendJSON(jsonStart);
    }

    public Session getSession() {
        return session;
    }

    private void setSession(Session session) {
        this.session = session;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        closed = true;
        logger.info(LoggerMessages.onClose(), myName);
    }
}