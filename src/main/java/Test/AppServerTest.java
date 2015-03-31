package Test;

import main.AppServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AppServerTest {

    private AppServer appServer;

    @Before
    public void setUp() throws Exception {
        appServer = new AppServer(8080, null);
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void testStart() throws Exception {

    }

    @Test
    public void testStop() throws Exception {

    }
}