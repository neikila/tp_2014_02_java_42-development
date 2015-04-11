package frontend.game;

import Interface.AccountService;
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

@WebSocket
public class GameWebSocket {

    final private Logger logger = LogManager.getLogger(GameWebSocket.class.getName());
    final private LoggerMessages loggerMessages = (LoggerMessages) ResourceFactory.instance().getResource("loggerMessages");

    final private String myName;
    final private GameMechanics gameMechanics;
    final private WebSocketService webSocketService;
    private Session session;

    public GameWebSocket(String sessionId, Context context) {
        this.gameMechanics = (GameMechanics) context.get(GameMechanics.class);
        this.webSocketService = (WebSocketService) context.get(WebSocketService.class);
        this.myName = ((AccountService) context.get(AccountService.class)).getSessions(sessionId).getLogin();
    }

    public String getMyName() {
        return myName;
    }

    public void startGame(GameUser user, String sequence) {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "start");
            jsonStart.put("enemyName", user.getEnemyName());
            jsonStart.put("sequence", sequence.substring(0,sequence.length()-3) + "&#x200B;" + sequence.substring(sequence.length()-3, sequence.length()));
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public void gameOver(GameUser user, int result) {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "finish");
            jsonStart.put("result", result);
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        logger.info(loggerMessages.onMessage(), myName, data);
        if (gameMechanics.checkSequence(myName, data))
            gameMechanics.incrementScore(myName);
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
        jsonStart.put("status", "increment");
        jsonStart.put("name", user.getMyName());
        jsonStart.put("score", user.getMyScore());
        try {
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public void setEnemyScore(GameUser user) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", "increment");
        jsonStart.put("name", user.getEnemyName());
        jsonStart.put("score", user.getEnemyScore());
        try {
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public void setMyResult(String result) {
        JSONObject jsonStart = new JSONObject();
        jsonStart.put("status", "result");
        jsonStart.put("result", result);
        try {
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public Session getSession() {
        return session;
    }

    private void setSession(Session session) {
        this.session = session;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        logger.info(loggerMessages.onClose(), myName);
    }
}