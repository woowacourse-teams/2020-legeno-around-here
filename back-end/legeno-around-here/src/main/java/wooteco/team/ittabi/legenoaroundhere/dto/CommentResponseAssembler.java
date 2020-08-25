package wooteco.team.ittabi.legenoaroundhere.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponseAssembler {

    public static CommentResponse of(User user, Comment comment) {
        if (comment.isNotAvailable()) {
            return getNotAvailableCommentResponse(user, comment);
        }
        return getAvailableCommentResponse(user, comment);
    }

    private static CommentResponse getNotAvailableCommentResponse(User user, Comment comment) {
        return CommentResponse.builder()
            .id(comment.getId())
            .cocomments(listOf(user, comment.getCocomments()))
            .deleted(true)
            .build();
    }

    private static CommentResponse getAvailableCommentResponse(User user, Comment comment) {
        return CommentResponse.builder()
            .id(comment.getId())
            .writing(comment.getWriting())
            .zzang(CommentZzangResponse.of(user, comment))
            .creator(UserSimpleResponse.from(comment.getCreator()))
            .cocomments(listOf(user, comment.getCocomments()))
            .createdAt(comment.getCreatedAt())
            .modifiedAt(comment.getModifiedAt())
            .deleted(comment.isNotAvailable())
            .build();
    }

    public static List<CommentResponse> listOf(User user, List<Comment> comments) {
        return comments.stream()
            .map(comment -> of(user, comment))
            .collect(Collectors.toList());
    }
}
