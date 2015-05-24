package utils;

/**
 * Created by neikila on 16.05.15.
 */
public class LoggerMessages {
    public static String doPostStart() { return "doPost Start";}
    public static String doPostFinish() { return "doPost Success";}
    public static String doGetStart() { return "doGet Start";}
    public static String doGetFinish() { return "doGet Success";}
    public static String isAdmin() { return "User:{} is admin";}
    public static String isNotAdmin() { return "User:{} is not admin";}
    public static String wrongAction() { return "Wrong action";}
    public static String statistic() {return "Getting Statistic";}
    public static String stop() {return "Stopping server";}
    public static String notAuthorised() { return "Not Authorised"; }
    public static String authorised() { return "User:{} is Authorised";}
    public static String hasAuthorised() { return "User:{} has Authorised"; }
    public static String lackOfParam() { return "No param \"{}\" in request"; }
    public static String paramHasWrongType() { return "Param:\"{}\" has wrong type"; }
    public static String signUp() { return "New user is signing up"; }
    public static String signIn() { return "User is signing in"; }
    public static String alreadyLoggedIn() { return "User:{} is already Logged In"; }
    public static String block() { return "Authorization and signing up is blocked"; }
    public static String wrongPasOrLogin() { return "Wrong password:\"{}\" or login:\"{}\""; }
    public static String loggedOut() { return "User:{} logged out"; }
    public static String loginIsAlreadyExist() { return "User with such login \"{}\" is already exist"; }
    public static String wrongSignUpData() { return "Wrong login:\"{}\" or email:\"{}\" or password:\"{}\""; }
    public static String onMessage() {return "User:{} has sent Message:{}"; }
    public static String onOpen() {return "User:{} is opening connection"; }
    public static String onClose() {return "User:{} has closed connection"; }
    public static String configure() {return "Configure"; }
    public static String setBlock() {return "Signing in and Signing up are blocked"; }
    public static String unsetBlock() {return "Signing in and Signing up are released"; }
    public static String startGame() { return "Start game";}
    public static String firstPlayer() { return "First player";}
    public static String secondPlayer() { return "Second player";}
    public static String sessionFinished() { return "Session finished";}
    public static String errorInReadingJSON() { return "Error while getting the JSON: {}"; };
    public static String draw() { return "Draw between {}, {}";}
    public static String isWinner() { return "{} is winner";}
    public static String isLoser() { return "{} is loser";}
    public static String signUpSuccess() { return "User {} was successfully signed up";}
    public static String gameUserPosition() { return "User {} is the {}";}
    public static String newSocket() {return "Start creating New socket";}
    public static String socketClosed() { return "Socket has been already closed"; }
    public static String serverStart() { return "Starting at port: {}";}
    public static String newSocketSuccess() { return "New Socket Successfully constructed";}
    public static String requestGetParams() { return "Request params {}"; }
    public static String jsonGotFromRequest() { return "JSON from request: {}";}
    public static String startXML() {return "Start Document"; }
    public static String errorXML() {return "Error while reading XML"; }
    public static String endXML() {return "End document"; }
    public static String className() {return "Class name: {}"; }
    public static String aEqualB() {return "{} = {}"; }
    public static String resourceWasParsed() {return "Resource {} was parsed"; }
    public static String resourceFactoryWasCreated() {return  "ResourceFactory was created"; }
}
