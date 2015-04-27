package frontend.game;

import Interface.GameMechanics;
import Interface.WebSocketService;
import main.Context;
import mechanics.GameUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONObject;
import resource.LoggerMessages;
import resource.ResourceFactory;
import utils.JsonInterpreterFromRequest;

@WebSocket
public class GameWebSocket {

    final private Logger logger = LogManager.getLogger(GameWebSocket.class.getName());
    final private LoggerMessages loggerMessages = (LoggerMessages) ResourceFactory.instance().getResource("loggerMessages");

    final private String myName;
    final private GameMechanics gameMechanics;
    final private WebSocketService webSocketService;
    private Session session;
    private boolean closed;

    public GameWebSocket(String myName, Context context) {
        this.gameMechanics = (GameMechanics) context.get(GameMechanics.class);
        this.webSocketService = (WebSocketService) context.get(WebSocketService.class);
        this.myName = myName;
        closed = false;
        logger.info(loggerMessages.newSocketSuccess());
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
            // TODO перенести в xml
            logger.error("Socket has been already closed");
        }
    }

    public void startGame(GameUser user, String sequence) {
        JSONObject jsonStart = new JSONObject();
        //TODO перенести в xml
        jsonStart.put("status", "start");
        jsonStart.put("position", user.getMyPosition());
        jsonStart.put("enemyName", user.getEnemyName());
        jsonStart.put("sequence", sequence.substring(0, sequence.length() - 3) + "&#x200B;" + sequence.substring(sequence.length() - 3, sequence.length()));
        sendJSON(jsonStart);
    }

    public void gameOver(GameUser user, int result) {
        JSONObject jsonEnd = new JSONObject();
        //TODO перенести в xml
        jsonEnd.put("status", "finish");
        jsonEnd.put("result", result);
        sendJSON(jsonEnd);
    }

    public void sendAction(JSONObject action) {
        sendJSON(action);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        logger.info(loggerMessages.onMessage(), myName, data);
        JSONObject message = JsonInterpreterFromRequest.getJsonFromString(data);
        gameMechanics.analyzeMessage(myName, message);
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        logger.info(loggerMessages.onOpen(), myName);
        setSession(session);
        webSocketService.addUser(this);
        gameMechanics.addUser(myName);
    }

    public void setMyScore(GameUser user) {
        JSONObject jsonStart = new JSONObject();
        //TODO перенести в xml
        jsonStart.put("status", "increment");
        jsonStart.put("name", user.getMyName());
        jsonStart.put("score", user.getMyScore());
        sendJSON(jsonStart);
    }

    public void setEnemyScore(GameUser user) {
        JSONObject jsonStart = new JSONObject();
        //TODO перенести в xml
        jsonStart.put("status", "increment");
        jsonStart.put("name", user.getEnemyName());
        jsonStart.put("score", user.getEnemyScore());
        sendJSON(jsonStart);
    }

    public void setMyResult(String result) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", "result");
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
        logger.info(loggerMessages.onClose(), myName);
    }
}