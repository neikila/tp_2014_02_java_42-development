package main;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class UserProfile {
    private String login;
    private String password;
    private String email;
    private String server;

    public UserProfile(String login, String password, String email, String server) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.server = server;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getServer() {
        return server;
    }
}
