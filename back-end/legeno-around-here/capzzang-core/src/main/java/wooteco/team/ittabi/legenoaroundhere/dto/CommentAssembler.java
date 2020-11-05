package wooteco.team.ittabi.legenoaroundhere.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentAssembler {

    public static Comment assemble(User user, CommentRequest commentRequest) {
        return new Comment(user, commentRequest.getWriting());
    }
}
