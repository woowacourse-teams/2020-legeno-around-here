package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.domain.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.State;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.CommentRepository;

@Slf4j
@Service
@AllArgsConstructor
public class CommentService {

    private final PostService postService;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest commentRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postService.findNotDeletedPost(postId);
        Comment comment = commentRequest.toComment(user, post);

        post.addComment(comment);

        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.of(savedComment);
    }

    @Transactional(readOnly = true)
    public CommentResponse findComment(Long commentId) {
        Comment comment = findNotDeletedComment(commentId);
        return CommentResponse.of(comment);
    }

    public List<CommentResponse> findAllComment(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId).stream()
            .filter(comment -> comment.isNotSameState(State.DELETED))
            .collect(Collectors.toList());
        return CommentResponse.listOf(comments);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = findNotDeletedComment(commentId);
        validateIsOwner(comment);
        comment.setState(State.DELETED);
    }

    private Comment findNotDeletedComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new NotExistsException(
                "Comment ID : " + commentId + " 에 해당하는 Comment가 없습니다!"));
        if (comment.isSameState(State.DELETED)) {
            log.debug("이미 삭제된 COMMENT, id = {}", commentId);
            throw new NotExistsException("삭제된 COMMENT 입니다!");
        }
        return comment;
    }

    private void validateIsOwner(Comment comment) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.isNotSame(comment.getUser())) {
            throw new NotAuthorizedException("권한이 없습니다.");
        }
    }
}
