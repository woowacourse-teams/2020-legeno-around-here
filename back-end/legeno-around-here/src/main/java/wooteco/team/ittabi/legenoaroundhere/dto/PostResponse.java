package wooteco.team.ittabi.legenoaroundhere.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PostResponse {

    private Long id;
    private String writing;
    private List<ImageResponse> images;
    private List<CommentResponse> comments;
    private UserResponse user;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static PostResponse of(Post post, List<CommentResponse> commentResponses) {
        List<ImageResponse> imageResponses = ImageResponse.listOf(post.getImages());
        return new PostResponse(post.getId(), post.getWriting(), imageResponses, commentResponses,
            UserResponse.from(post.getUser()), post.getCreatedAt(), post.getModifiedAt());
    }
}
