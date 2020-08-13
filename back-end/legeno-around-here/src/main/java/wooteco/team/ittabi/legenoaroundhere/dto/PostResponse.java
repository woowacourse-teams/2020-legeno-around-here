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
import wooteco.team.ittabi.legenoaroundhere.domain.post.zzang.ZzangState;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PostResponse {

    private Long id;
    private String writing;
    private List<ImageResponse> images;
    private List<CommentResponse> comments;
    private AreaResponse area;
    private SectorResponse sector;
    private PostZzangResponse postZzangResponse;
    private UserResponse creator;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static PostResponse of(Post post, List<Comment> comments, ZzangState zzangState) {
        return PostResponse.builder()
            .id(post.getId())
            .writing(post.getWriting())
            .images(ImageResponse.listOf(post.getPostImages()))
            .comments(CommentResponse.listOf(comments))
            .area(AreaResponse.of(post.getArea()))
            .sector(SectorResponse.of(post.getSector()))
            .creator(UserResponse.from(post.getCreator()))
            .postZzangResponse(PostZzangResponse.of(post.getPostZzangCount(), zzangState))
            .createdAt(post.getCreatedAt())
            .modifiedAt(post.getModifiedAt())
            .build();
    }
}
