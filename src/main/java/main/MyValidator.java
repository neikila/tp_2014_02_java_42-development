package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyValidator {

    // Валидация UserName'а
    public boolean isUserNameValid(String userName) {
        Pattern p = Pattern.compile("^\\w{5,}$");
        Matcher m = p.matcher(userName);
        return m.matches();
    }

    // Валидация пароля (пока что они похожи, в дальнейшем возможно наожим другие ограничения)
    public boolean isPasswordValid(String password) {
        Pattern p = Pattern.compile("^\\w{6,}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    // Валидация имейла
    public boolean isEmailValid(String email) {
        Pattern p = Pattern.compile("^.+@[a-zA-Z]+\\.[a-z]+$");
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
