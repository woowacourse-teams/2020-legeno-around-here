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
import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class CommentResponse {

    private Long id;
    private String writing;
    private CommentZzangResponse zzang;
    private UserResponse creator;
    private List<CommentResponse> cocomments;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static CommentResponse of(User user, Comment comment) {
        return CommentResponse.builder()
            .id(comment.getId())
            .writing(comment.getWriting())
            .zzang(CommentZzangResponse.of(user, comment))
            .creator(UserResponse.from(comment.getCreator()))
            .cocomments(cocommentListOf(user, comment.getCocomments()))
            .createdAt(comment.getCreatedAt())
            .modifiedAt(comment.getModifiedAt())
            .build();
    }

    private static CommentResponse cocommentOf(User user, Comment comment) {
        return CommentResponse.builder()
            .id(comment.getId())
            .writing(comment.getWriting())
            .zzang(CommentZzangResponse.of(user, comment))
            .creator(UserResponse.from(comment.getCreator()))
            .createdAt(comment.getCreatedAt())
            .modifiedAt(comment.getModifiedAt())
            .build();
    }

    public static List<CommentResponse> listOf(User user, List<Comment> comments) {
        return comments.stream()
            .map(comment -> of(user, comment))
            .collect(Collectors.toList());
    }

    public static List<CommentResponse> cocommentListOf(User user, List<Comment> comments) {
        return comments.stream()
            .map(comment -> cocommentOf(user, comment))
            .collect(Collectors.toList());
    }
}
