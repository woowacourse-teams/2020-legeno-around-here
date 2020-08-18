package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.domain.user.UserImage;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserUpdateRequest {

    private String nickname;
    private String password;
    private Long areaId;
    private Long imageId;

    public void setPassword(String password) {
        this.password = password;
    }

    public User toUser(Area area, UserImage userImage) {
        return User.builder()
            .email(null)
            .nickname(nickname)
            .password(password)
            .area(area)
            .image(userImage)
            .build();
    }
}
