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
}
