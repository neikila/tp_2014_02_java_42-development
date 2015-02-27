package main;

/**
 * Created by v.chibrikov on 13.09.2014.
 */
public class UserProfile {
    private String login;
    private String password;
    private String email;
    private String server;
    private String role;

    public UserProfile(String login, String password, String email, String server) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.server = server;
        this.role = "User";
    }

    public boolean setRole(String role) {
        boolean returnValue = true;
        switch(role) {
            case "User":;
            case "Admin": this.role = role; break;
            default: returnValue = false;
        }
        return returnValue;
    }

    public boolean isAdmin() { return role.equals("Admin"); }

    public String getRole() { return role; }

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
