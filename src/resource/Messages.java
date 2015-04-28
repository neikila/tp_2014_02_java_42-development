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
    private String askToSignIn;
    private String JSONStatusIncrement;
    private String JSONStatusStart;
    private String JSONStatusFinish;
    private String JSONStatusResult;

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
    public String wrongSignUpData() { return wrongSignUpData; }
    public String askToSignIn() { return askToSignIn; }
    public String JSONStatusIncrement() { return JSONStatusIncrement; };
    public String JSONStatusStart() { return JSONStatusStart; };
    public String JSONStatusFinish() { return JSONStatusFinish; };
    public String JSONStatusResult() { return JSONStatusResult; };

    @Override
    public void checkState() {
    }
}
