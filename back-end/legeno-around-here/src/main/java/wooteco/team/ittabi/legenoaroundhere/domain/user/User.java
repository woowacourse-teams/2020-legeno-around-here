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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Embedded
    private Email email;
    @Embedded
    private NickName nickName;
    @Embedded
    private Password password;

    public User() {
    }

    public User(Email email, NickName nickName, Password password) {
        validate(email, nickName, password);
        this.email = email;
        this.nickName = nickName;
        this.password = password;
    }

    private void validate(Email email, NickName nickName, Password password) {
        validateEmailIsNull(email);
        validateNickNameIsNull(nickName);
        validatePasswordIsNull(password);
    }

    private void validateEmailIsNull(Email email) {
        if (Objects.isNull(email)) {
            throw new IllegalArgumentException("이메일이 null 입니다.");
        }
    }

    private void validateNickNameIsNull(NickName nickName) {
        if (Objects.isNull(nickName)) {
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

    public NickName getNickName() {
        return nickName;
    }

    public Password getPassword() {
        return password;
    }
}
