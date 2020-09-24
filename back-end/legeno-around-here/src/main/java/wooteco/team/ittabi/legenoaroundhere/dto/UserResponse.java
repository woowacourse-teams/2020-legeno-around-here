package wooteco.team.ittabi.legenoaroundhere.dto;

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
public class UserResponse {

    static final String DEACTIVATED_USER_NICKNAME = "탈퇴한 회원";
    static final String DEACTIVATED_USER_EMAIL = "";

    private Long id;
    private String email;
    private String nickname;
    private UserImageResponse image;
    private AreaResponse area;
    private AwardsCountResponse awardsCount;

    public static UserResponse from(User user) {
        if (user.isDeactivated()) {
            return fromDeactivated(user);
        }
        return fromActivated(user);
    }

    private static UserResponse fromDeactivated(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .nickname(DEACTIVATED_USER_NICKNAME)
            .email(DEACTIVATED_USER_EMAIL)
            .awardsCount(AwardsCountResponse.ofInitialValue())
            .build();
    }

    private static UserResponse fromActivated(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail().getEmail())
            .nickname(user.getNickname())
            .image(UserImageResponse.of(user.getImage()))
            .area(AreaResponse.of(user.getArea()))
            .awardsCount(AwardsCountResponse.of(user))
            .build();
    }
}
