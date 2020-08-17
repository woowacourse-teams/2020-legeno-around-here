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

    public static CommentResponse of(User user, Comment comment) {
        return CommentResponse.builder()
            .id(comment.getId())
            .writing(comment.getWriting())
            .zzang(new CommentZzangResponse(comment.getZzangSize(), comment.hasZzangCreator(user)))
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
}
