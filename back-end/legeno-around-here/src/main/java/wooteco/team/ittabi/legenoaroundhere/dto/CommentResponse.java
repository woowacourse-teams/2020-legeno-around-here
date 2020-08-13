package wooteco.team.ittabi.legenoaroundhere.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.post.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.post.zzang.ZzangState;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class CommentResponse {

    private Long id;
    private String writing;
    private CommentZzangResponse zzang;
    private UserResponse creator;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static CommentResponse of(Comment comment) {
        return CommentResponse.builder()
            .id(comment.getId())
            .writing(comment.getWriting())
            .zzang(new CommentZzangResponse(23, ZzangState.ACTIVATED.name()))
            .creator(UserResponse.from(comment.getCreator()))
            .createdAt(comment.getCreatedAt())
            .modifiedAt(comment.getModifiedAt())
            .build();
    }

    public static List<CommentResponse> listOf(List<Comment> comments) {
        return comments.stream()
            .map(CommentResponse::of)
            .collect(Collectors.toList());
    }
}
