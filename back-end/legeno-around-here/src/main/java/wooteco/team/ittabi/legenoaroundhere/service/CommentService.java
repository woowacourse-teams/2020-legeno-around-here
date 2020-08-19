package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentAssembler;
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
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
        Comment comment = CommentAssembler.assemble(user, commentRequest);

        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.of(user, savedComment);
    }

    @Transactional(readOnly = true)
    public CommentResponse findComment(Long commentId) {
        User user = (User) authenticationFacade.getPrincipal();

        Comment comment = findCommentBy(commentId);
        return CommentResponse.of(user, comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findAllCommentBy(Long postId) {
        User user = (User) authenticationFacade.getPrincipal();

        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return CommentResponse.listOf(user, comments);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = findCommentBy(commentId);
        validateIsCreator(comment);
        commentRepository.delete(comment);
    }

    private Comment findCommentBy(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() ->
                new NotExistsException("Comment ID : " + commentId + " 에 해당하는 Comment가 없습니다!"));
    }

    private void validateIsCreator(Comment comment) {
        User user = (User) authenticationFacade.getPrincipal();

        if (user.isNotSame(comment.getCreator())) {
            throw new NotAuthorizedException("권한이 없습니다.");
        }
    }

    @Transactional
    public void pressZzang(Long commentId) {
        User user = (User) authenticationFacade.getPrincipal();

        Comment comment = findCommentBy(commentId);
        comment.pressZzang(user);
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest) {
        User user = (User) authenticationFacade.getPrincipal();
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new NotExistsException("코멘트가 존재하지 않습니다."));

        comment.setWriting(commentRequest.getWriting());

        return CommentResponse.of(user, comment);
    }

    @Transactional
    public CommentResponse createCocomment(Long commentId, CommentRequest commentRequest) {
        User user = (User) authenticationFacade.getPrincipal();

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new NotExistsException("해당 도메인이 없습니다."));

        Comment cocomment = commentRepository.save(CommentAssembler.assemble(user, commentRequest));
        cocomment.setComment(comment);
        return CommentResponse.of(user, cocomment);
    }
}
