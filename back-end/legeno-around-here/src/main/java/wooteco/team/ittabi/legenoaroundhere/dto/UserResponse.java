package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class UserResponse {

    private Long id;
    private String email;
    private String nickname;

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getEmailByString(), user.getNicknameByString());
    }
}
