package wooteco.team.ittabi.legenoaroundhere.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
@ToString
public class PostResponse {

    private Long id;
    private String writing;
    private List<ImageResponse> images;
    private List<CommentResponse> comments;
    private LikeResponse likeResponse;
    private UserResponse creator;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static PostResponse of(Post post, List<CommentResponse> commentResponses,
        LikeResponse likeResponse) {
        return PostResponse.builder()
            .id(post.getId())
            .writing(post.getWriting())
            .images(ImageResponse.listOf(post.getImages()))
            .comments(commentResponses)
            .creator(UserResponse.from(post.getCreator()))
            .likeResponse(likeResponse)
            .createdAt(post.getCreatedAt())
            .modifiedAt(post.getModifiedAt())
            .build();
    }
}
