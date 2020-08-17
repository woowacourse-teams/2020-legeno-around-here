package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
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
    private Long areaId;

    public User toUser(Area area) {
        return User.builder()
            .email(email)
            .nickname(nickname)
            .password(password)
            .area(area)
            .build();
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
