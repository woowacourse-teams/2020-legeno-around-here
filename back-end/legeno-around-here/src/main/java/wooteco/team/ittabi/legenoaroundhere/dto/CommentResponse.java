package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.Comment;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class CommentResponse {

    private Long id;
    private String writing;
    private UserResponse user;

    public static CommentResponse of(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getWriting(),
            UserResponse.from(comment.getUser()));
    }

    public static List<CommentResponse> listOf(List<Comment> comments) {
        return comments.stream()
            .map(CommentResponse::of)
            .collect(Collectors.toList());
    }
}
