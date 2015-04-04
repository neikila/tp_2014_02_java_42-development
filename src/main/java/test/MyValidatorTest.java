package test;

import main.user.MyValidator;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyValidatorTest {


    @Test
    public void testIsSingletone() throws Exception {

        MyValidator temp = MyValidator.instance();

        assertNotNull("isSingletone", temp);
    }

    @Test
    public void testIsUserNameValid() throws Exception {
        String usernameOK = "Test1";

        assertEquals("OK", true, MyValidator.isUserNameValid(usernameOK));
    }

    @Test
    public void testIsUserNameValidShort() throws Exception {
        String usernameShort = "test";

        assertEquals("Short", false, MyValidator.isUserNameValid(usernameShort));
    }

    @Test
    public void testIsUserNameValidLong() throws Exception {
        String usernameLong = "VeryLongStringWithTheLengthMoreThen63SymbolsAndItIsStillNotMoreThan63INeedSomeMore";

        assertEquals("Long", false, MyValidator.isUserNameValid(usernameLong));
    }

    @Test
    public void testIsUserNameValidWrongSym() throws Exception {
        String usernameWrongSymbol = ";test";

        assertEquals("WrongSymbol", false, MyValidator.isUserNameValid(usernameWrongSymbol));
    }

    @Test
    public void testIsPasswordValid() throws Exception {
        String passwordOK = "Test1w";

        assertEquals("OK", true, MyValidator.isPasswordValid(passwordOK));
    }

    @Test
    public void testIsPasswordValidShort() throws Exception {
        String passwordShort = "test";

        assertEquals("Short", false, MyValidator.isPasswordValid(passwordShort));
    }

    @Test
    public void testIsPasswordValidLong() throws Exception {
        String passwordLong = "VeryLongStringWithTheLengthMoreThen63SymbolsAndItIsStillNotMoreThan63INeedSomeMore";

        assertEquals("Long", false, MyValidator.isPasswordValid(passwordLong));
    }

    @Test
    public void testIsPasswordValidWrongSym() throws Exception {
        String passwordWrongSym = "^test";

        assertEquals("WrongSym", false, MyValidator.isPasswordValid(passwordWrongSym));
    }

    @Test
    public void testIsEmailValid() throws Exception {
        String emailOK = "neikila@gmail.com";

        assertEquals("OK", true, MyValidator.isEmailValid(emailOK));
    }

    @Test
    public void testIsEmailValidWrongSymbolInName() throws Exception {
        String emailWrongSymbolInName = "b;asdf@mail.ru";

        assertEquals("WrongSymbolInName", false, MyValidator.isEmailValid(emailWrongSymbolInName));
    }

    @Test
    public void testIsEmailValidWrongSymbolInAddress() throws Exception {
        String emailWrongSymbolInAddress = "basdf@m;ail.ru";

        assertEquals("WrongSymbolInAddress", false, MyValidator.isEmailValid(emailWrongSymbolInAddress));
    }

    @Test
    public void testIsEmailValidWrongSymbolInClaster() throws Exception {
        String emailWrongSymbolInClaster = "basdf@mail.r;u";

        assertEquals("WrongSymbolInClaster", false, MyValidator.isEmailValid(emailWrongSymbolInClaster));
    }

    @Test
    public void testIsEmailValidNoDog() throws Exception {
        String emailNoDog = "basdfmail.ru";

        assertEquals("NoDog", false, MyValidator.isEmailValid(emailNoDog));
    }

    @Test
    public void testIsEmailValidNoClaster() throws Exception {
        String emailNoClaster = "basdf@mail.";

        assertEquals("NoClaster", false, MyValidator.isEmailValid(emailNoClaster));
    }

    @Test
    public void testIsEmailValidNoClasterAtAll() throws Exception {
        String emailNoClasterAtAll = "basdf@mail";

        assertEquals("NoClasterAtAll", false, MyValidator.isEmailValid(emailNoClasterAtAll));
    }

    @Test
    public void testIsEmailValidNoNameAtAll() throws Exception {
        String emailNoNameAtAll = "@mail.ru";

        assertEquals("NoClasterAtAll", false, MyValidator.isEmailValid(emailNoNameAtAll));
    }
}