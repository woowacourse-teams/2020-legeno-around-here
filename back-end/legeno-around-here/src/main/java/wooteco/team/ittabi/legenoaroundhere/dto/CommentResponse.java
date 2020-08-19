package wooteco.team.ittabi.legenoaroundhere.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
            .cocomments(makeCocommentResponses(user, comment))
            .createdAt(comment.getCreatedAt())
            .modifiedAt(comment.getModifiedAt())
            .build();
    }

    private static List<CommentResponse> makeCocommentResponses(User user, Comment comment) {
        List<Comment> cocomments = comment.getCocomments();
        if (Objects.isNull(cocomments)) {
            return null;
        }
        return cocommentListOf(user, cocomments);
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

    private static List<CommentResponse> cocommentListOf(User user, List<Comment> comments) {
        return comments.stream()
            .map(comment -> cocommentOf(user, comment))
            .collect(Collectors.toList());
    }
}
