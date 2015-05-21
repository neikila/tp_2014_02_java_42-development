package mechanics;

import frontend.game.WebSocketService;
import main.Context;
import main.accountService.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import resource.GameMechanicsSettings;
import utils.LoggerMessages;
import utils.TimeHelper;

import java.util.*;

public class GameMechanicsImpl implements GameMechanics {
    final private Logger logger = LogManager.getLogger(GameMechanics.class.getName());

    private static final int STEP_TIME = 100;

    final private int gameTime;
    final private int weight;
    final private int minDelta;
    final private ArrayList<GameMap> maps;

    final private WebSocketService webSocketService;
    final private AccountService accountService;

    final private Map<String, GameSession> nameToGame = new HashMap<>();

    final private Set<GameSession> allSessions = new HashSet<>();

    final private GameUserManager userManager = new GameUserManager();

    private GameUser waiter = null;
    private int nextMap = 0;

    public GameMechanicsImpl(Context context, GameMechanicsSettings settings) {
        this.webSocketService = (WebSocketService) context.get(WebSocketService.class);
        this.accountService = (AccountService) context.get(AccountService.class);
        gameTime = settings.getTimeLimit() * 1000;
        weight = settings.getWeight();
        minDelta = settings.getMinDelta();
        maps = settings.getMaps();
    }

    public void addUser(String user) {
        if (waiter == null) {
            Random rand = new Random();
            nextMap = rand.nextInt(maps.size());

            waiter = new GameUser(user);
            waiter.setMyPosition(1);
            userManager.addUser(waiter);

            webSocketService.sendSettings(waiter, maps.get(nextMap));

            logger.info(LoggerMessages.firstPlayer());
        } else {
            GameUser secondPlayer = new GameUser(user);
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

    public void analyzeMessage(String userName, JSONObject message) {
        if (message.containsKey("action")) {
            GameUser myUser = userManager.getSelf(userName);
            GameSession myGameSession = nameToGame.get(userName);
            GameUser opponent = myGameSession.getEnemy(myUser.getMyPosition());

            message.put("player", myUser.getMyPosition());

            webSocketService.notifyAction(myUser, message);
            webSocketService.notifyAction(opponent, message);
        } else {
            logger.info(LoggerMessages.onMessage(), userName, message.toString());
        }
    }

    public void incrementScore(GameUser user) {
        GameSession myGameSession = nameToGame.get(user.getMyName());
        user.incrementMyScore();
        webSocketService.notifyMyNewScore(user);
        webSocketService.notifyEnemyNewScore(myGameSession, user.getMyPosition());
    }

    @Override
    public void run() {
        while (true) {
            gmStep();
            TimeHelper.sleep(STEP_TIME);
        }
    }

    private void gmStep() {
        for (GameSession session : allSessions) {
            if(session.getSessionTime() > gameTime) {
                finishGame(session);
            }
        }
    }

    private void finishGame(GameSession session) {
        GameUser first = session.getFirst();
        GameUser second = session.getSecond();

        String firstName = first.getMyName();
        String secondName = second.getMyName();

        int firstResult = session.getWinner();
        int secondResult = -1 * firstResult;

        if (firstResult == 0) {
            logger.info(LoggerMessages.draw(), firstName, secondName);
        } else {
            logger.info(LoggerMessages.isWinner(), firstResult > 0 ? firstName : secondName);
            logger.info(LoggerMessages.isLoser(), firstResult < 0 ? firstName : secondName);
        }
        int deltaScore = firstResult * (minDelta + weight * Math.abs(first.getMyScore() - second.getMyScore() ) );

        accountService.getUser(firstName).increaseScoreOnValue(deltaScore);
        accountService.getUser(secondName).increaseScoreOnValue(-1 * deltaScore);

        webSocketService.notifyGameOver(first, firstResult);
        webSocketService.notifyGameOver(second, secondResult);
        allSessions.remove(session);
        userManager.removeUser(first);
        userManager.removeUser(second);
        logger.info(LoggerMessages.sessionFinished());
    }

    private void starGame(GameUser first, GameUser second, GameMap nextMap) {
        GameSession gameSession = new GameSession(first, second, nextMap);
        logger.info(LoggerMessages.gameUserPosition(),
                gameSession.getFirst().getMyName(), gameSession.getFirst().getMyPosition());
        logger.info(LoggerMessages.gameUserPosition(),
                gameSession.getSecond().getMyName(), gameSession.getSecond().getMyPosition());
        allSessions.add(gameSession);
        nameToGame.put(first.getMyName(), gameSession);
        nameToGame.put(second.getMyName(), gameSession);

        webSocketService.notifyStartGame(gameSession, first.getMyPosition());
        webSocketService.notifyStartGame(gameSession, second.getMyPosition());
    }
}