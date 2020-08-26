package wooteco.team.ittabi.legenoaroundhere.service;

import static wooteco.team.ittabi.legenoaroundhere.domain.State.DELETED;
import static wooteco.team.ittabi.legenoaroundhere.utils.NotificationContentMaker.KEYWORD_ZZANG;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.notification.Notification;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentAssembler;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentResponseAssembler;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAvailableException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.CommentRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.NotificationRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;
import wooteco.team.ittabi.legenoaroundhere.utils.NotificationContentMaker;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final NotificationRepository notificationRepository;

    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest commentRequest) {
        User user = (User) authenticationFacade.getPrincipal();
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 POST가 없습니다."));
        Comment comment = CommentAssembler.assemble(user, commentRequest);

        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);
        return CommentResponseAssembler.of(user, savedComment);
    }

    @Transactional(readOnly = true)
    public CommentResponse findComment(Long commentId) {
        User user = (User) authenticationFacade.getPrincipal();

        Comment comment = findCommentBy(commentId);
        return CommentResponseAssembler.of(user, comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findAllComment(Long postId) {
        User user = (User) authenticationFacade.getPrincipal();

        List<Comment> comments = commentRepository.findAllByPostId(postId)
            .stream()
            .filter(Comment::hasSuperComment)
            .collect(Collectors.toList());

        return CommentResponseAssembler.listOf(user, comments);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = findAvailableCommentBy(commentId);
        validateIsCreator(comment);
        if (comment.hasCocomments()) {
            comment.setState(DELETED);
            return;
        }
        if (isSuperCommentNotAvailableAndOnlyCocomment(comment)) {
            commentRepository.delete(comment.getSuperComment());
        }
        commentRepository.delete(comment);
    }

    private boolean isSuperCommentNotAvailableAndOnlyCocomment(Comment comment) {
        Comment superComment = comment.getSuperComment();
        return Objects.nonNull(superComment)
            && superComment.isNotAvailable()
            && superComment.hasOnlyCocomment();
    }

    private Comment findCommentBy(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() ->
                new NotExistsException("Comment ID : " + commentId + " 에 해당하는 Comment가 없습니다!"));
    }

    private void validateIsCreator(Comment comment) {
        User user = (User) authenticationFacade.getPrincipal();

        if (user.isNotEquals(comment.getCreator())) {
            throw new NotAuthorizedException("권한이 없습니다.");
        }
    }

    @Transactional
    public void pressZzang(Long commentId) {
        User user = (User) authenticationFacade.getPrincipal();

        Comment comment = findAvailableCommentBy(commentId);
        comment.pressZzang(user);

        if (comment.hasZzangCreator(user)) {
            notifyCommentZzangNotification(comment);
        }
    }

    private void notifyCommentZzangNotification(Comment comment) {
        User receiver = comment.getCreator();

        deleteBeforeCommentNotificationOfKeyword(comment, receiver, KEYWORD_ZZANG);

        String zzangNotificationContent
            = NotificationContentMaker.makePressZzangNotificationContent(comment);
        createNewCommentNotificationOfContent(comment, receiver, zzangNotificationContent);
    }

    private void deleteBeforeCommentNotificationOfKeyword(Comment comment, User receiver,
        String keyword) {
        notificationRepository.findAllByReceiverAndComment(receiver, comment)
            .stream()
            .filter(notification -> notification.getContent().contains(keyword))
            .findFirst()
            .ifPresent(notificationRepository::delete);
    }

    private void createNewCommentNotificationOfContent(Comment omment, User receiver,
        String content) {
        Notification notification = Notification.builder()
            .content(content)
            .comment(omment)
            .receiver(receiver)
            .build();

        notificationRepository.save(notification);
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest) {
        User user = (User) authenticationFacade.getPrincipal();
        Comment comment = findAvailableCommentBy(commentId);

        comment.setWriting(commentRequest.getWriting());

        return CommentResponseAssembler.of(user, comment);
    }

    @Transactional
    public CommentResponse createCocomment(Long commentId, CommentRequest commentRequest) {
        User user = (User) authenticationFacade.getPrincipal();

        Comment comment = findAvailableCommentBy(commentId);
        Comment cocomment = CommentAssembler.assemble(user, commentRequest);
        cocomment.setSuperComment(comment);

        Comment savedCocomment = commentRepository.save(cocomment);
        return CommentResponseAssembler.of(user, savedCocomment);
    }

    private Comment findAvailableCommentBy(Long commentId) {
        Comment comment = findCommentBy(commentId);
        if (comment.isNotAvailable()) {
            throw new NotAvailableException(
                "Comment ID : " + comment.getId() + " 에 해당하는 Comment가 유효하지 않습니다!");
        }
        return comment;
    }
}
