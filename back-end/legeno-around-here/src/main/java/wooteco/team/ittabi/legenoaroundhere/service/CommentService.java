package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.post.State;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.CommentRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;

@Slf4j
@Service
@AllArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final IAuthenticationFacade authenticationFacade;

    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest commentRequest) {
        User user = (User) authenticationFacade.getPrincipal();
        Post post = postRepository.findByIdAndStateNot(postId, State.DELETED)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
        Comment comment = commentRequest.toComment(user);

        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.of(savedComment);
    }

    @Transactional(readOnly = true)
    public CommentResponse findComment(Long commentId) {
        Comment comment = findNotDeletedComment(commentId);
        return CommentResponse.of(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findAllComment(Long postId) {
        List<Comment> comments =
            new ArrayList<>(commentRepository.findAllByPostIdAndStateNot(postId, State.DELETED));
        return CommentResponse.listOf(comments);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = findNotDeletedComment(commentId);
        validateIsCreator(comment);
        comment.setState(State.DELETED);
    }

    private Comment findNotDeletedComment(Long commentId) {
        return commentRepository.findByIdAndStateNot(commentId, State.DELETED)
            .orElseThrow(() ->
                new NotExistsException("Comment ID : " + commentId + " 에 해당하는 Comment가 없습니다!"));
    }

    private void validateIsCreator(Comment comment) {
        User user = (User) authenticationFacade.getPrincipal();

        if (user.isNotSame(comment.getCreator())) {
            throw new NotAuthorizedException("권한이 없습니다.");
        }
    }
}
