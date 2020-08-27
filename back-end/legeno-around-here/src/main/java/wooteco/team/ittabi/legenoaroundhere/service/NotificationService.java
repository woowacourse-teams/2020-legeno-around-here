package wooteco.team.ittabi.legenoaroundhere.service;

import static java.time.LocalDateTime.now;
import static wooteco.team.ittabi.legenoaroundhere.utils.NotificationContentMaker.KEYWORD_COMMENT;
import static wooteco.team.ittabi.legenoaroundhere.utils.NotificationContentMaker.KEYWORD_ZZANG;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.notification.Notification;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.NotificationResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.NotificationResponseAssembler;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.NotificationRepository;
import wooteco.team.ittabi.legenoaroundhere.utils.NotificationContentMaker;

@Service
@AllArgsConstructor
public class NotificationService {

    private static final int DAY_OF_ONE_WEEK = 7;

    private final NotificationRepository notificationRepository;
    private final IAuthenticationFacade authenticationFacade;

    @Transactional(readOnly = true)
    public List<NotificationResponse> findMyNotice() {
        User user = (User) authenticationFacade.getPrincipal();

        LocalDateTime beforeOneWeek = now().minusDays(DAY_OF_ONE_WEEK);
        List<Notification> notifications
            = notificationRepository.findAllByReceiverAndCreatedAtIsAfter(user, beforeOneWeek);

        return NotificationResponseAssembler.listAssemble(notifications);
    }

    @Transactional
    void notifyPostZzangNotification(Post post) {
        User receiver = post.getCreator();

        deleteBeforePostNotificationOfKeyword(post, receiver, KEYWORD_ZZANG);

        String zzangNotificationContent
            = NotificationContentMaker.makePressZzangNotificationContent(post);
        createNewPostNotificationOfContent(post, receiver, zzangNotificationContent);
    }

    void deleteBeforePostNotificationOfKeyword(Post post, User receiver, String keyword) {
        notificationRepository.findAllByReceiverAndPost(receiver, post)
            .stream()
            .filter(notification -> notification.getContent().contains(keyword))
            .findFirst()
            .ifPresent(notificationRepository::delete);
    }

    void createNewPostNotificationOfContent(Post post, User receiver, String content) {
        Notification notification = Notification.builder()
            .content(content)
            .post(post)
            .receiver(receiver)
            .build();

        notificationRepository.save(notification);
    }

    @Transactional
    void notifyPostAddCommentNotification(Post post) {
        User receiver = post.getCreator();

        deleteBeforePostNotificationOfKeyword(post, receiver, KEYWORD_COMMENT);

        String zzangNotificationContent
            = NotificationContentMaker.makeCreatedCommentNotificationContent(post);
        createNewPostNotificationOfContent(post, receiver, zzangNotificationContent);
    }

    @Transactional
    void notifyCommentZzangNotification(Comment comment) {
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

    private void createNewCommentNotificationOfContent(Comment comment, User receiver,
        String content) {
        Notification notification = Notification.builder()
            .content(content)
            .comment(comment)
            .receiver(receiver)
            .build();

        notificationRepository.save(notification);
    }

    @Transactional
    void notifyCommentAddCommentNotification(Comment comment) {
        User receiver = comment.getCreator();

        deleteBeforeCommentNotificationOfKeyword(comment, receiver, KEYWORD_COMMENT);

        String zzangNotificationContent
            = NotificationContentMaker.makeCreatedCommentNotificationContent(comment);
        createNewCommentNotificationOfContent(comment, receiver, zzangNotificationContent);
    }

    @Transactional
    public void readMyNotice(Long noticeId) {
        User user = (User) authenticationFacade.getPrincipal();

        Notification notification = notificationRepository.findById(noticeId)
            .orElseThrow(
                () -> new NotExistsException("ID : " + noticeId + " 에 해당하는 Notice가 없습니다!"));

        validateReceiver(user, notification);
        notification.read();
    }

    private void validateReceiver(User user, Notification notification) {
        if (notification.isDifferentReceiver(user)) {
            throw new NotAuthorizedException("권한이 없습니다.");
        }
    }
}
