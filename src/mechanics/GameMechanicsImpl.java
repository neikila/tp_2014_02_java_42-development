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
    final private Map<Id <GameSession>, GameSession> idToSession = new HashMap<>();

    final private Set<GameSession> allSessions = new HashSet<>();

    final private GameUserManager userManager = new GameUserManager();

    private GameUser waiter = null;
    private int nextMap = 0;

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
            gmStep();
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(100);
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
        if (waiter == null) {
            Random rand = new Random();
            nextMap = rand.nextInt(maps.size());
            waiter = new GameUser(id, user);
            waiter.setMyPosition(1);
            userManager.addUser(waiter);

            GameMap map = maps.get(nextMap);

            webSocketService.sendSettings(waiter, map);

            logger.info(LoggerMessages.firstPlayer());
        } else {
            GameUser secondPlayer = new GameUser(id, user);
            secondPlayer.setMyPosition(2);
            userManager.addUser(secondPlayer);

            logger.info(LoggerMessages.secondPlayer());
            logger.info(LoggerMessages.startGame());

            webSocketService.sendSettings(waiter, maps.get(nextMap));

            starGame(waiter, secondPlayer, maps.get(nextMap));
            logger.info("Map Number: {}", nextMap);
            waiter = null;
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
        GameUser first = session.getFirst();
        GameUser second = session.getSecond();

        Id <GameUser> firstId = first.getId();
        Id <GameUser> secondId = second.getId();

        int firstResult = session.getWinner();
        int secondResult = -1 * firstResult;

        if (firstResult == 0) {
            logger.info(LoggerMessages.draw(), firstId, secondId);
        } else {
            logger.info(LoggerMessages.isWinner(), firstResult > 0 ? firstId: secondId);
            logger.info(LoggerMessages.isLoser(), firstResult < 0 ? firstId: secondId);
        }
        int deltaScore = firstResult * (minDelta + weight * Math.abs(first.getMyScore() - second.getMyScore() ) );

        // TODO не забыть про 123 =)
        first.getUser().increaseScoreOnValue(deltaScore + 123);
        second.getUser().increaseScoreOnValue(-1 * deltaScore - 123);

        saveSession(session);

        webSocketService.notifyGameOver(first, firstResult);
        webSocketService.notifyGameOver(second, secondResult);
        session.save();
        nameToGame.remove(firstId);
        nameToGame.remove(secondId);
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