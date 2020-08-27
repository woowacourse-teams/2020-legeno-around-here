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
public class UserOtherResponse {

    private Long id;
    private String nickname;
    private UserImageResponse image;
    private AwardsCountResponse awardsCount;

    public static UserOtherResponse from(User user) {
        return UserOtherResponse.builder()
            .id(user.getId())
            .nickname(user.getNickname())
            .image(UserImageResponse.of(user.getImage()))
            .awardsCount(AwardsCountResponse.of(user))
            .build();
    }
}
