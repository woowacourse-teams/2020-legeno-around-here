package wooteco.team.ittabi.legenoaroundhere.dto;

import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

public class CommentAssembler {

    public static Comment assemble(User user, CommentRequest commentRequest) {
        return new Comment(user, commentRequest.getWriting());
    }
}
