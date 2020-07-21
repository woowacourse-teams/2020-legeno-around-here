package wooteco.team.ittabi.legenoaroundhere.domain.user;

import java.util.Objects;

public class User {

    private Email email;
    private NickName nickName;
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
        if (Objects.isNull(email)) {
            throw new IllegalArgumentException("이메일이 null 입니다.");
        }
        if (Objects.isNull(nickName)) {
            throw new IllegalArgumentException("닉네임이 null 입니다.");
        }
        if (Objects.isNull(password)) {
            throw new IllegalArgumentException("비밀번호가 null 입니다.");
        }
    }
}
