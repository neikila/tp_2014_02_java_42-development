package Interface;

public interface GameMechanics {

    public void addUser(String user);

    public void incrementScore(String userName);

    public boolean checkSequence(String userName, String sequence);

    public void run();
}
