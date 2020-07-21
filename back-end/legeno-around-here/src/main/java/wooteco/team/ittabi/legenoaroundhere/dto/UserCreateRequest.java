package wooteco.team.ittabi.legenoaroundhere.dto;

import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.NickName;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Password;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

public class UserCreateRequest {

    private String email;
    private String nickName;
    private String password;

    public UserCreateRequest() {
    }

    public UserCreateRequest(String email, String nickName, String password) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
    }

    public User toUser() {
        Email email = new Email(this.email);
        NickName nickName = new NickName(this.nickName);
        Password password = new Password(this.password);

        return new User(email, nickName, password);
    }

    public String getEmail() {
        return email;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPassword() {
        return password;
    }
}
