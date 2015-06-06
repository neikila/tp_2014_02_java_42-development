package mechanics;

/**
 * Created by neikila on 24.05.15.
 */
public enum GameError {
    UnexpectedError("Something gone wrong"), OpponentLostConnection("Your Opponent lost connection");

    private String errorMessage;

    private GameError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
