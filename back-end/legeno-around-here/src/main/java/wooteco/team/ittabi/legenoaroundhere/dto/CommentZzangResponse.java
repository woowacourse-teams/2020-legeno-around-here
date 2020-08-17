package wooteco.team.ittabi.legenoaroundhere.dto;

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
public class CommentZzangResponse {

    private int count;
    private boolean activated;

    public static CommentZzangResponse of(User user, Comment comment) {
        return CommentZzangResponse.builder()
            .count(comment.getZzangCounts())
            .activated(comment.hasZzangCreator(user))
            .build();
    }
}
