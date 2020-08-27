package wooteco.team.ittabi.legenoaroundhere.dto;

import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.HOME_PATH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.MY_PROFILE_PATH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.POSTS_PATH_WITH_SLASH;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.notification.Notification;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationResponseAssembler {

    public static List<NotificationResponse> listAssemble(List<Notification> notifications) {
        return notifications.stream()
            .map(NotificationResponseAssembler::assemble)
            .collect(Collectors.toList());
    }

    public static NotificationResponse assemble(Notification notification) {
        return NotificationResponse.builder()
            .id(notification.getId())
            .content(notification.getContent())
            .isRead(notification.getIsRead())
            .location(makeLocation(notification))
            .build();
    }

    private static String makeLocation(Notification notification) {
        if (Objects.nonNull(notification.getComment())) {
            Comment comment = notification.getComment();
            return makePostLocation(comment.getPost());
        }
        if (Objects.nonNull(notification.getPost())) {
            return makePostLocation(notification.getPost());
        }
        if (Objects.nonNull(notification.getSector()) || Objects.nonNull(notification.getUser())) {
            return MY_PROFILE_PATH;
        }
        return HOME_PATH;
    }

    private static String makePostLocation(Post post) {
        return POSTS_PATH_WITH_SLASH + post.getId();
    }
}
