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
import wooteco.team.ittabi.legenoaroundhere.domain.post.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.post.zzang.ZzangState;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PostWithCommentsCountResponse {

    private Long id;
    private String writing;
    private List<PostImageResponse> images;
    private AreaResponse area;
    private SectorResponse sector;
    private int commentsCount;
    private PostZzangResponse zzang;
    private UserResponse creator;
    private LocalDateTime createdAt;

    public static PostWithCommentsCountResponse of(Post post,
        List<Comment> comments, ZzangState zzangState) {
        return PostWithCommentsCountResponse.builder()
            .id(post.getId())
            .writing(post.getWriting())
            .images(PostImageResponse.listOf(post.getPostImages()))
            .area(AreaResponse.of(post.getArea()))
            .sector(SectorResponse.of(post.getSector()))
            .commentsCount(CommentResponse.listOf(comments).size())
            .creator(UserResponse.from(post.getCreator()))
            .zzang(PostZzangResponse.of(post.getPostZzangCount(), zzangState))
            .createdAt(post.getCreatedAt())
            .build();
    }
}
