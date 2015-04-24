package resource;

import Interface.Resource;

import java.io.Serializable;

/**
 * Created by neikila on 03.04.15.
 */
public class Messages implements Serializable, Resource {
    private String adminPage;
    private String notFound;
    private String wrongParamAction;
    private String notAuthorised;
    private String wrongLimit;
    private String logInStatus;
    private String alreadyLoggedIn;
    private String block;
    private String fillAllTheGaps;
    private String logOutFirst;
    private String exist;
    private String lackOfParam;
    private String paramHasWrongType;
    private String signUp;
    private String signIn;
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
    static private String errorXML = "Error while reading XML";
    static private String startXML = "Start Document";
    static private String endXML = "End document";
    static private String className = "Class name: {}";
    static private String aEqualB = "{} = {}";
    static private String resourceWasParsed = "Resource {} was parsed";
    static private String resourceFactoryWasCreated = "ResourceFactory was created";

    public String adminPage() { return adminPage;}
    public String notFound() { return notFound;}
    public String wrongParamAction() { return wrongParamAction;}
    public String notAuthorised() { return notAuthorised; }
    public String wrongLimit() { return wrongLimit;}
    public String logInStatus() { return logInStatus;}
    public String alreadyLoggedIn() { return alreadyLoggedIn; }
    public String block() { return block; }
    public String wrongPasOrLogin() { return wrongPasOrLogin; }
    public String fillAllTheGaps() { return fillAllTheGaps;}
    public String logOutFirst() {return logOutFirst;}
    public String exist() { return exist; }
    public String lackOfParam() { return lackOfParam; }
    public String paramHasWrongType() { return  paramHasWrongType; }
    public String signUp() { return  signUp; }
    public String signIn() { return  signIn; }
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
