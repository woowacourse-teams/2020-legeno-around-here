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
import wooteco.team.ittabi.legenoaroundhere.domain.post.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.post.like.LikeState;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PostWithCommentsCountResponse {

    private Long id;
    private String writing;
    private List<ImageResponse> images;
    private int commentsCount;
    private LikeResponse likeResponse;
    private UserResponse creator;
    private LocalDateTime createdAt;

    public static PostWithCommentsCountResponse of(Post post,
        List<Comment> comments, LikeState likeState) {
        return PostWithCommentsCountResponse.builder()
            .id(post.getId())
            .writing(post.getWriting())
            .images(ImageResponse.listOf(post.getImages()))
            .commentsCount(CommentResponse.listOf(comments).size())
            .creator(UserResponse.from(post.getCreator()))
            .likeResponse(LikeResponse.of(post.getLikeCount(), likeState))
            .createdAt(post.getCreatedAt())
            .build();
    }
}
