package test;

import messageSystem.Address;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AddressTest {

    @Test
    public void testForGettingDifferentId() throws Exception {
        Address address1 = new Address();
        Address address2 = new Address();

        assertEquals(1 , address2.hashCode() - address1.hashCode());
    }

    @Test
    public void testForConflict() throws Exception {
        int addressToCreateInEachThread = 100;
        Address firstAddress = new Address();
        Thread first = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i <addressToCreateInEachThread; ++i) {
                    new Address();
                }
            }
        };
        Thread second = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < addressToCreateInEachThread; ++i) {
                    new Address();
                }
            }
        };

        first.start();
        second.start();
        first.join();
        second.join();

        Address lastAddress = new Address();
        assertEquals(1 + 2 * addressToCreateInEachThread, lastAddress.hashCode() - firstAddress.hashCode());
    }
}