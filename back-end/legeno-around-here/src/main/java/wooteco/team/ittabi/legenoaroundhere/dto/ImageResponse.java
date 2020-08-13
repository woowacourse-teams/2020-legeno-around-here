package wooteco.team.ittabi.legenoaroundhere.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.post.image.PostImage;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ImageResponse {

    private Long id;
    private String name;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ImageResponse of(PostImage postImage) {
        return new ImageResponse(postImage.getId(), postImage.getName(), postImage.getUrl(),
            postImage.getCreatedAt(), postImage.getModifiedAt());
    }

    public static List<ImageResponse> listOf(List<PostImage> postImages) {
        return postImages.stream()
            .map(ImageResponse::of)
            .collect(Collectors.toList());
    }
}
