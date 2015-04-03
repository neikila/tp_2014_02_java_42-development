package main;

import java.io.Serializable;

public class UserProfile implements Serializable {
    // TODO переделать в final
    private String login;
    private String password;
    private String email;
    private boolean isSuperUser;
    private int score;

    public UserProfile(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.isSuperUser = false;
        this.score = 0;
    }

    public UserProfile() {
        this.login = "";
        this.password = "";
        this.email = "";
        this.isSuperUser = false;
        this.score = 0;
    }

    public int getScore() { return score; }

    public void setScore(int value) { score = value; }

    public void setAdmin(boolean value) {
        isSuperUser = value;
    }

    public boolean isAdmin() { return isSuperUser; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserProfile profile = (UserProfile) o;

        if (isSuperUser != profile.isSuperUser) return false;
        if (score != profile.score) return false;
        if (email != null ? !email.equals(profile.email) : profile.email != null) return false;
        if (login != null ? !login.equals(profile.login) : profile.login != null) return false;
        if (password != null ? !password.equals(profile.password) : profile.password != null) return false;

        return true;
    }

    public String getRole() { return isSuperUser?"Admin":"User"; }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String toString() { return "Login: " + login + " Password: " + password + " Email: " + email + " Role: " + getRole(); }
}