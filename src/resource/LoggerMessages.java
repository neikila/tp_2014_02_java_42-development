package resource;

import Interface.Resource;

import java.io.Serializable;

/**
 * Created by neikila on 03.04.15.
 */
public class LoggerMessages implements Serializable, Resource {
    private String doPostStart;
    private String doPostFinish;
    private String doGetStart;
    private String doGetFinish;
    private String isAdmin;
    private String isNotAdmin;
    private String wrongAction;
    private String statistic;
    private String stop;
    private String notAuthorised;
    private String hasAuthorised;
    private String lackOfParam;
    private String paramHasWrongType;
    private String signUp;
    private String signIn;
    private String alreadyLoggedIn;
    private String block;
    private String wrongPasOrLogin;
    private String loggedOut;
    private String loginIsAlreadyExist;
    private String wrongSignUpData;
    private String authorised;
    private String onMessage;
    private String onOpen;
    private String onClose;
    private String configure;
    private String setBlock;
    private String unsetBlock;
    private String startGame;
    private String sessionFinished;
    private String firstPlayer;
    private String secondPlayer;
    private String draw;
    private String isWinner;
    private String isLoser;
    private String signUpSuccess;
    private String gameUserPosition;
    private String newSocket;
    private String newSocketSuccess;
    private String requestGetParams;
    private String errorInReadingJSON;
    private String socketClosed;
    static private String jsonGotFromRequest = "JSON from request: {}";
    static private String errorXML = "Error while reading XML";
    static private String startXML = "Start Document";
    static private String endXML = "End document";
    static private String className = "Class name: {}";
    static private String aEqualB = "{} = {}";
    static private String resourceWasParsed = "Resource {} was parsed";
    static private String resourceFactoryWasCreated = "ResourceFactory was created";

    public String doPostStart() { return doPostStart;}
    public String doPostFinish() { return doPostFinish;}
    public String doGetStart() { return doGetStart;}
    public String doGetFinish() { return doGetFinish;}
    public String isAdmin() { return isAdmin;}
    public String isNotAdmin() { return isNotAdmin;}
    public String wrongAction() { return wrongAction;}
    public String statistic() {return statistic;}
    public String stop() {return stop;}
    public String notAuthorised() { return notAuthorised; }
    public String hasAuthorised() { return hasAuthorised; }
    public String lackOfParam() { return lackOfParam; }
    public String paramHasWrongType() { return  paramHasWrongType; }
    public String signUp() { return  signUp; }
    public String signIn() { return  signIn; }
    public String alreadyLoggedIn() { return alreadyLoggedIn; }
    public String block() { return block; }
    public String wrongPasOrLogin() { return wrongPasOrLogin; }
    public String loggedOut() { return loggedOut; }
    public String loginIsAlreadyExist() { return loginIsAlreadyExist; }
    public String wrongSignUpData() { return wrongSignUpData; }
    public String authorised() { return authorised;}
    public String onMessage() {return onMessage; }
    public String onOpen() {return onOpen; }
    public String onClose() {return onClose; }
    public String configure() {return configure; }
    public String setBlock() {return setBlock; }
    public String unsetBlock() {return unsetBlock; }
    public String startGame() { return startGame;}
    public String sessionFinished() { return sessionFinished;}
    public String firstPlayer() { return firstPlayer;}
    public String secondPlayer() { return secondPlayer;}
    public String draw() { return draw;}
    public String isWinner() { return isWinner;}
    public String isLoser() { return isLoser;}
    public String signUpSuccess() { return signUpSuccess;}
    public String gameUserPosition() { return gameUserPosition;}
    public String newSocket() {return newSocket;}
    public String requestGetParams() { return requestGetParams;}
    public String newSocketSuccess() { return newSocketSuccess;}
    public String errorInReadingJSON() { return errorInReadingJSON; };
    public String socketClosed() { return socketClosed; }
    static public String jsonGotFromRequest() { return jsonGotFromRequest;}
    static public String errorXML() {return errorXML; }
    static public String startXML() {return startXML; }
    static public String endXML() {return endXML; }
    static public String className() {return className; }
    static public String aEqualB() {return aEqualB; }
    static public String resourceWasParsed() {return resourceWasParsed; }
    static public String resourceFactoryWasCreated() {return  resourceFactoryWasCreated; }

    @Override
    public void checkState() {
    }
}
