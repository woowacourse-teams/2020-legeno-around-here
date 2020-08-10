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
import wooteco.team.ittabi.legenoaroundhere.domain.Post;

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
        List<CommentResponse> commentResponses, LikeResponse likeResponse) {
        return PostWithCommentsCountResponse.builder()
            .id(post.getId())
            .writing(post.getWriting())
            .images(ImageResponse.listOf(post.getImages()))
            .commentsCount(commentResponses.size())
            .creator(UserResponse.from(post.getCreator()))
            .likeResponse(likeResponse)
            .createdAt(post.getCreatedAt())
            .build();
    }
}
