package mechanics;

import frontend.game.messages.*;
import main.Context;
import main.accountService.messages.MessageSaveGameSession;
import main.user.UserProfile;
import messageSystem.Address;
import messageSystem.Message;
import messageSystem.MessageSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import resource.GameMechanicsSettings;
import resource.ResourceFactory;
import resource.ThreadsSettings;
import utils.Id;
import utils.LoggerMessages;

import java.util.*;

public final class GameMechanicsImpl implements GameMechanics {
    final private Logger logger = LogManager.getLogger(GameMechanics.class.getName());

    private final Address address = new Address();
    private final MessageSystem messageSystem;

    private final int stepTime;
    final private int syncFrequency;
    final private int gameTime;
    final private int weight;
    final private int minDelta;
    final private long deltaPressed;
    final private int damage;
    final private ArrayList<GameMap> maps;

    final private Map<Id <GameUser>, GameSession> nameToGame = new HashMap<>();

    final private Set<GameSession> allSessions = new HashSet<>();

    final private GameUserManager userManager = new GameUserManager();

    private Queue<GameUser> waiters = new LinkedList<>();

    public GameMechanicsImpl(Context context, GameMechanicsSettings settings) {
        this.messageSystem = (MessageSystem) context.get(MessageSystem.class);
        messageSystem.addService(this);
        messageSystem.getAddressService().registerGameMechanics(this);

        gameTime = settings.getTimeLimit() * 1000;
        weight = settings.getWeight();
        minDelta = settings.getMinDelta();
        maps = settings.getMaps();
        syncFrequency = settings.getSyncFrequency();
        deltaPressed = settings.getDeltaPressed();
        damage = settings.getDamage();
        stepTime = ((ThreadsSettings)ResourceFactory.instance().getResource("threadsSettings")).getGMTimeStep();
    }

    @Override
    public void run() {
        long startTime = (new Date()).getTime();
        while (true){
            try {
                createSessions();
                gmStep();
                messageSystem.execForAbonent(this);
                try {
                    Thread.sleep(stepTime - (new Date().getTime() - startTime) % stepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                logger.error("I'll never die!");
                logger.error(e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public void addUser(Id <GameUser> id, UserProfile user) {
        waiters.add(new GameUser(id, user));
    }

    public void analyzeMessage(Id<GameUser> id, JSONObject message) {

        GameUser myUser = userManager.getSelf(id);
        GameSession myGameSession = nameToGame.get(id);
        if (myUser == null || myGameSession == null) {
            logger.info("Huston we have some problems");
            return;
        }

        try {
            if (message.containsKey("action")) {

                if (message.containsKey("touchEvent")) {
                    String touchEvent = (String) message.get("touchEvent");
                    GameDirection dir = GameDirection.getDirection(((Number) message.get("action")).intValue());
                    if (touchEvent.equals("touchStart")) {
                        myUser.press(dir);
                        message.remove("touchEvent");
                    } else {
                        myUser.release();
                        return;
                    }
                }

                GameUser opponent = myGameSession.getEnemy(myUser.getMyPosition());

                message.put("player", myUser.getMyPosition());

                Address to = messageSystem.getAddressService().getWebSocketServiceAddress();
                messageSystem.sendMessage(new MessageAction(address, to, id, message));
                messageSystem.sendMessage(new MessageAction(address, to, opponent.getId(), message));
            }

            if (message.containsKey("check")) {
                myUser.getCoordinate().setXY(((Number) message.get("x")).doubleValue(), ((Number) message.get("y")).doubleValue());
            }

            if (message.containsKey("fire")) {
                if (myUser.reduceHealth(damage) <= 0) {
                    sendSync(myGameSession);
                    finishGame(myGameSession);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Bad json. Very bad JSON.");
        }
    }

    public void removeSession(GameSession session) {
        if (session.isFinished())
            allSessions.remove(session);
    }

    @Override
    public void userLostConnection(Id <GameUser> id) {

        GameUser first = userManager.getSelf(id);

        waiters.remove(first);
        userManager.removeUser(first);

        GameSession session = nameToGame.get(id);
        if (session != null) {
            GameUser second = session.getEnemy(first.getMyPosition());

            Address to = messageSystem.getAddressService().getWebSocketServiceAddress();
            messageSystem.sendMessage(new MessageErrorInGameProcess(address, to, second.getId(), GameError.OpponentLostConnection));

            finishOnLostConnection(session, first);

            nameToGame.remove(first.getId());
            nameToGame.remove(second.getId());
            userManager.removeUser(second);
        }
    }

    private void createSessions() {
        while (waiters.size() > 1) {
            GameUser first = waiters.poll();
            GameUser second = waiters.poll();

            first.setMyPosition(1);
            userManager.addUser(first);

            second.setMyPosition(2);
            userManager.addUser(second);

            Random rand = new Random();
            int nextMap = rand.nextInt(maps.size());
            GameMap map = maps.get(nextMap);

            Address to = messageSystem.getAddressService().getWebSocketServiceAddress();
            messageSystem.sendMessage(new MessageSendSettings(address, to, first.getId(), map));
            messageSystem.sendMessage(new MessageSendSettings(address, to, second.getId(), map));

            starGame(first, second, map);
            logger.info(LoggerMessages.firstPlayer());
            logger.info(LoggerMessages.secondPlayer());
            logger.info("Map Number: {}", nextMap);
            logger.info(LoggerMessages.startGame());
        }
    }

    private void gmStep() {
        for (GameSession session : allSessions) {
            if (!session.isFinished()) {
                session.makeStep();

                GameUser first = session.getFirst();
                GameUser second = session.getSecond();

                sendPressedForUser(first, second);
                sendPressedForUser(second, first);

                if (session.getCountStep() == (syncFrequency / stepTime) ) {
                    sendSync(session);
                    session.counterToZero();
                }
                if (session.getSessionTime() > gameTime) {
                    finishGame(session);
                }
            }
        }
    }

    private void sendPressedForUser(GameUser first, GameUser second) {
        GameDirection temp;
        if ((temp = first.getDirection()) != GameDirection.None && (new Date().getTime() - first.getTime()) > deltaPressed) {
            first.appendTime(deltaPressed);

            JSONObject message = new JSONObject();
            message.put("action", temp.ordinal());
            message.put("player", first.getMyPosition());

            Address to = messageSystem.getAddressService().getWebSocketServiceAddress();
            messageSystem.sendMessage(new MessageAction(address, to, first.getId(), message));
            messageSystem.sendMessage(new MessageAction(address, to, second.getId(), message));
        }
    }

    private void sendSync(GameSession session) {
        Id <GameUser> first = session.getFirst().getId();
        Id <GameUser> second = session.getSecond().getId();

        Address to = messageSystem.getAddressService().getWebSocketServiceAddress();
        messageSystem.sendMessage(new MessageSynchronize(address, to, session, first));
        messageSystem.sendMessage(new MessageSynchronize(address, to, session, second));
    }

    private void finishOnLostConnection(GameSession session, GameUser iAmOut) {
        GameUser winner = session.getEnemy(iAmOut.getMyPosition());
        session.lostConnectionToPlayer(iAmOut);
        GameResult gameResult = session.getWinner();

        int deltaScore = Math.abs(winner.getHealth() / 2 );

        winner.getUser().increaseScoreOnValue(deltaScore);
        iAmOut.getUser().increaseScoreOnValue(-1 * deltaScore);

        logger.info(LoggerMessages.isWinner(), winner.getUser().getLogin());
        logger.info(LoggerMessages.isLoser(), iAmOut.getUser().getLogin());

        saveSession(session);

        Address to = messageSystem.getAddressService().getWebSocketServiceAddress();
        messageSystem.sendMessage(new MessageGameOver(address, to, winner.getId(), gameResult.ordinal()));

    }

    private void finishGame(GameSession session) {
        session.gameOver();
        GameResult gameResult = session.getWinner();
        GameUser first = session.getFirst();
        GameUser second = session.getSecond();

        int deltaScore = minDelta + weight * Math.abs(first.getHealth() - second.getHealth());

        switch (gameResult) {
            case Draw:
                logger.info(LoggerMessages.draw(), first.getUser().getLogin(), second.getUser().getLogin());
                break;
            case FirstWon:
                first.getUser().increaseScoreOnValue(deltaScore);
                second.getUser().increaseScoreOnValue(-1 * deltaScore);
                logger.info(LoggerMessages.isWinner(), first.getUser().getLogin());
                logger.info(LoggerMessages.isLoser(), second.getUser().getLogin());
                break;
            case SecondWon:
                first.getUser().increaseScoreOnValue(-1 * deltaScore);
                second.getUser().increaseScoreOnValue(deltaScore);
                logger.info(LoggerMessages.isLoser(), first.getUser().getLogin());
                logger.info(LoggerMessages.isWinner(), second.getUser().getLogin());
                break;
        }

        saveSession(session);

        Address to = messageSystem.getAddressService().getWebSocketServiceAddress();
        messageSystem.sendMessage(new MessageGameOver(address, to, first.getId(), gameResult.ordinal()));
        messageSystem.sendMessage(new MessageGameOver(address, to, second.getId(), gameResult.ordinal()));

        nameToGame.remove(first.getId());
        nameToGame.remove(second.getId());
        userManager.removeUser(first);
        userManager.removeUser(second);
        logger.info(LoggerMessages.sessionFinished());
    }

    private void saveSession(GameSession gameSession) {
        Message updateMes = new MessageSaveGameSession(this.getAddress(), messageSystem.getAddressService().getAccountServiceAddress(), gameSession);
        messageSystem.sendMessage(updateMes);
    }

    private void starGame(GameUser first, GameUser second, GameMap nextMap) {
        GameSession gameSession = new GameSession(first, second, nextMap);
        logger.info(LoggerMessages.gameUserPosition(),
                first.getUser().getLogin(), gameSession.getFirst().getMyPosition());
        logger.info(LoggerMessages.gameUserPosition(),
                second.getUser().getLogin(), gameSession.getSecond().getMyPosition());
        allSessions.add(gameSession);
        nameToGame.put(first.getId(), gameSession);
        nameToGame.put(second.getId(), gameSession);

        Address to = messageSystem.getAddressService().getWebSocketServiceAddress();
        messageSystem.sendMessage(new MessageStartGame(address, to, gameSession, first.getId()));
        messageSystem.sendMessage(new MessageStartGame(address, to, gameSession, second.getId()));
    }
}