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
public class UserSimpleResponse {

    private Long id;
    private String email;
    private String nickname;
    private UserImageResponse image;

    public static UserSimpleResponse from(User user) {
        return UserSimpleResponse.builder()
            .id(user.getId())
            .email(user.getEmail().getEmail())
            .nickname(user.getNickname())
            .image(UserImageResponse.of(user.getImage()))
            .build();
    }
}
