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
    private SectorResponse sector;
    private UserResponse creator;
    private LocalDateTime createdAt;

    public static PostWithCommentsCountResponse of(Post post,
        List<CommentResponse> commentResponses) {
        return PostWithCommentsCountResponse.builder()
            .id(post.getId())
            .writing(post.getWriting())
            .images(ImageResponse.listOf(post.getImages()))
            .commentsCount(commentResponses.size())
            .sector(SectorResponse.of(post.getSector()))
            .creator(UserResponse.from(post.getCreator()))
            .createdAt(post.getCreatedAt())
            .build();
    }
}
