package wooteco.team.ittabi.legenoaroundhere.dto;

import static wooteco.team.ittabi.legenoaroundhere.dto.UserResponse.DEACTIVATED_USER_NICKNAME;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class UserOtherResponse {

    private Long id;
    private String nickname;
    private UserImageResponse image;
    private AwardsCountResponse awardsCount;

    public static UserOtherResponse from(User user) {
        if (user.isDeactivated()) {
            return fromDeactivated(user);
        }
        return fromActivated(user);
    }

    private static UserOtherResponse fromDeactivated(User user) {
        return UserOtherResponse.builder()
            .id(user.getId())
            .nickname(DEACTIVATED_USER_NICKNAME)
            .awardsCount(AwardsCountResponse.ofInitialValue())
            .build();
    }

    private static UserOtherResponse fromActivated(User user) {
        return UserOtherResponse.builder()
            .id(user.getId())
            .nickname(user.getNickname())
            .image(UserImageResponse.of(user.getImage()))
            .awardsCount(AwardsCountResponse.of(user))
            .build();
    }
}
