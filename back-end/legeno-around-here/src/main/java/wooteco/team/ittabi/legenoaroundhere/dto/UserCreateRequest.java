package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Nickname;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Password;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class UserCreateRequest {

    private String email;
    private String nickname;
    private String password;

    public User toUser() {
        return new User(
            new Email(this.email),
            new Nickname(this.nickname),
            new Password(this.password)
        );
    }
}
