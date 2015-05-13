package main.user;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class UserProfile implements Serializable {
    private static final long serialVersionUID = -8706689714326132798L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "login")
    final private String login;

    @Column(name = "password")
    final private String password;

    @Column(name = "email")
    final private String email;

    @Column(name = "isSuperUser")
    private boolean isSuperUser;

    @Column(name = "score")
    private int score;

    public UserProfile(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.isSuperUser = false;
        this.score = 0;
        this.id = -1;
    }

    public UserProfile(long id, String login, String password, String email) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.isSuperUser = false;
        this.score = 0;
    }

    //Important to Hibernate!
    public UserProfile() {
        email = null;
        password = null;
        login = null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getScore() { return score; }

    public void setScore(int value) { score = value; }

    public void increaseScoreOnValue(int value) { score += value; }

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

    public String getRoleName() { return isSuperUser? "Admin": "User"; }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Id: " + id +
                " Login: " + login +
                " Password: " + password +
                " Email: " + email +
                " Role: " + getRoleName() +
                " Score: " + score;
    }
}