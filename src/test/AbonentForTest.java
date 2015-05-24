package test;

import messageSystem.Abonent;
import messageSystem.Address;

/**
 * Created by neikila on 24.05.15.
 */
public class AbonentForTest implements Abonent{
    final private Address address;

    private boolean isExecuted = false;

    public AbonentForTest () {
        this.address = new Address();
    }

    public Address getAddress() {
        return address;
    }

    public void execute() {
        isExecuted = true;
    }

    public boolean getResult() { return isExecuted; }
}
