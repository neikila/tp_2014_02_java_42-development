package mechanics;

import frontend.game.WebSocketService;
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
import utils.Id;
import utils.LoggerMessages;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class GameMechanicsImpl implements GameMechanics {
    final private Logger logger = LogManager.getLogger(GameMechanics.class.getName());

    private final Address address = new Address();
    private final MessageSystem messageSystem;

    private static final int STEP_TIME = 100;

    final private int gameTime;
    final private int weight;
    final private int minDelta;
    final private ArrayList<GameMap> maps;

    final private WebSocketService webSocketService;

    final private Map<Id <GameUser>, GameSession> nameToGame = new HashMap<>();

    final private Set<GameSession> allSessions = new HashSet<>();

    final private GameUserManager userManager = new GameUserManager();

    private ConcurrentLinkedQueue<GameUser> waiters = new ConcurrentLinkedQueue<>();

    public GameMechanicsImpl(Context context, GameMechanicsSettings settings) {
        this.messageSystem = (MessageSystem) context.get(MessageSystem.class);
        messageSystem.addService(this);
        messageSystem.getAddressService().registerGameMechanics(this);

        this.webSocketService = (WebSocketService) context.get(WebSocketService.class);

        gameTime = settings.getTimeLimit() * 1000;
        weight = settings.getWeight();
        minDelta = settings.getMinDelta();
        maps = settings.getMaps();
    }

    @Override
    public void run() {
        while (true){
            createSessions();
            gmStep();
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(STEP_TIME);
            } catch (InterruptedException e) {
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

            webSocketService.sendSettings(first, map);
            webSocketService.sendSettings(second, map);

            starGame(first, second, map);
            logger.info(LoggerMessages.firstPlayer());
            logger.info(LoggerMessages.secondPlayer());
            logger.info("Map Number: {}", nextMap);
            logger.info(LoggerMessages.startGame());
        }
    }

    public void analyzeMessage(Id<GameUser> id, JSONObject message) {
        if (message.containsKey("action")) {
            GameUser myUser = userManager.getSelf(id);
            GameSession myGameSession = nameToGame.get(id);
            GameUser opponent = myGameSession.getEnemy(myUser.getMyPosition());

            message.put("player", myUser.getMyPosition());

            webSocketService.notifyAction(myUser, message);
            webSocketService.notifyAction(opponent, message);
        } else {
            logger.info(LoggerMessages.onMessage(), id, message.toString());
        }
    }

    public void incrementScore(GameUser user) {
        GameSession myGameSession = nameToGame.get(user.getId());
        user.incrementMyScore();
        webSocketService.notifyMyNewScore(user);
        webSocketService.notifyEnemyNewScore(myGameSession, user.getMyPosition());
    }

    private void gmStep() {
        for (GameSession session : allSessions) {
            if(!session.isFinished() && session.getSessionTime() > gameTime) {
                finishGame(session);
            }
        }
    }

    private void finishGame(GameSession session) {
        GameResult gameResult = session.getWinner();
        GameUser first = session.getFirst();
        GameUser second = session.getSecond();

        int deltaScore = (minDelta + weight * Math.abs(first.getMyScore() - second.getMyScore()));

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

        webSocketService.notifyGameOver(first, gameResult.ordinal());
        webSocketService.notifyGameOver(second, gameResult.ordinal());

        session.finish();

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

    public void removeSession(GameSession session) {
        if (session.isFinished())
            allSessions.remove(session);
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

        webSocketService.notifyStartGame(gameSession, first.getMyPosition());
        webSocketService.notifyStartGame(gameSession, second.getMyPosition());
    }
}