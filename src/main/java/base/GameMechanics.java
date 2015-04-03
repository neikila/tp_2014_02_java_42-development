package base;

public interface GameMechanics {

    public void addUser(String user);

    public void incrementScore(String userName);

    public void checkSequence(String userName, String sequence);

    public void run();
}
