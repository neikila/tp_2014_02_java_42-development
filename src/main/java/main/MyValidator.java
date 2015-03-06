package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.validator.EmailValidator;

public  class MyValidator {
    private static EmailValidator emailValidator = EmailValidator.getInstance();
    private static Pattern usernamePattern = Pattern.compile("^\\w{5,}$");
    private static Pattern passwordPattern = Pattern.compile("^\\w{6,}$");

    public static boolean isUserNameValid(String userName) {
        Matcher m = usernamePattern.matcher(userName);
        return m.matches();
    }

    public static boolean isPasswordValid(String password) {
        Matcher m = passwordPattern.matcher(password);
        return m.matches();
    }

    public static boolean isEmailValid(String email) {
        return emailValidator.isValid(email);
    }
}

