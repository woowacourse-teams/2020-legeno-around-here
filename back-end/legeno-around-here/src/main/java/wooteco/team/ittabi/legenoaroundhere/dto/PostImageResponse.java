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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class PostImageResponse {

    private Long id;
    private String name;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static PostImageResponse of(PostImage postImage) {
        return new PostImageResponse(postImage.getId(), postImage.getName(), postImage.getUrl(),
            postImage.getCreatedAt(), postImage.getModifiedAt());
    }

    public static List<PostImageResponse> listOf(List<PostImage> postImages) {
        return postImages.stream()
            .map(PostImageResponse::of)
            .collect(Collectors.toList());
    }
}
