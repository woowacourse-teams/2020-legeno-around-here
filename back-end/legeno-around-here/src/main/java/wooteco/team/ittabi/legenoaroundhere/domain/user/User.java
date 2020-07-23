package wooteco.team.ittabi.legenoaroundhere.domain.user;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Email email;
    @Embedded
    private Nickname nickname;
    @Embedded
    private Password password;

    protected User() {
    }

    public User(Email email, Nickname nickname, Password password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Nickname getNickname() {
        return nickname;
    }

    public Password getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", email=" + email +
            ", nickname=" + nickname +
            ", password=" + password +
            '}';
    }
}
