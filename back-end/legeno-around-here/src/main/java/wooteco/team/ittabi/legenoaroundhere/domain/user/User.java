package wooteco.team.ittabi.legenoaroundhere.domain.user;

import java.util.Objects;
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
        validate(email, nickname, password);
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    private void validate(Email email, Nickname nickname, Password password) {
        validateEmailIsNull(email);
        validateNicknameIsNull(nickname);
        validatePasswordIsNull(password);
    }

    private void validateEmailIsNull(Email email) {
        if (Objects.isNull(email)) {
            throw new IllegalArgumentException("이메일이 null 입니다.");
        }
    }

    private void validateNicknameIsNull(Nickname nickname) {
        if (Objects.isNull(nickname)) {
            throw new IllegalArgumentException("닉네임이 null 입니다.");
        }
    }

    private void validatePasswordIsNull(Password password) {
        if (Objects.isNull(password)) {
            throw new IllegalArgumentException("비밀번호가 null 입니다.");
        }
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
}
