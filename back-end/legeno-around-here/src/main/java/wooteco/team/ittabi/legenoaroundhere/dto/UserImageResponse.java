package wooteco.team.ittabi.legenoaroundhere.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.user.UserImage;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class UserImageResponse {

    private Long id;
    private String name;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static UserImageResponse of(UserImage userImage) {
        if (Objects.isNull(userImage)) {
            return null;
        }
        return UserImageResponse.builder()
            .id(userImage.getId())
            .name(userImage.getName())
            .url(userImage.getUrl())
            .createdAt(userImage.getCreatedAt())
            .modifiedAt(userImage.getModifiedAt())
            .build();
    }
}
