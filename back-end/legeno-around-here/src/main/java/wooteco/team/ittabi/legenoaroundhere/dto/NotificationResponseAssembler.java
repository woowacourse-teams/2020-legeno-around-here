package wooteco.team.ittabi.legenoaroundhere.dto;

import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.COMMENTS_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.HOME_PATH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.POSTS_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.SECTORS_PATH_WITH_SLASH;
import static wooteco.team.ittabi.legenoaroundhere.utils.UrlPathConstants.USERS_PATH_WITH_SLASH;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.notification.Notification;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

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
            .isRead(notification.getRead())
            .location(makeLocation(notification))
            .build();
    }

    private static String makeLocation(Notification notification) {
        if (Objects.nonNull(notification.getComment())) {
            return makeCommentLocation(notification.getComment());
        }
        if (Objects.nonNull(notification.getPost())) {
            return makePostLocation(notification.getPost());
        }
        if (Objects.nonNull(notification.getSector())) {
            return makeSectorLocation(notification.getSector());
        }
        if (Objects.nonNull(notification.getUser())) {
            return makeUserLocation(notification.getUser());
        }
        return HOME_PATH;
    }

    private static String makeCommentLocation(Comment comment) {
        return COMMENTS_PATH_WITH_SLASH + comment.getId();
    }

    private static String makePostLocation(Post post) {
        return POSTS_PATH_WITH_SLASH + post.getId();
    }

    private static String makeSectorLocation(Sector sector) {
        return SECTORS_PATH_WITH_SLASH + sector.getId();
    }

    private static String makeUserLocation(User user) {
        return USERS_PATH_WITH_SLASH + user.getId();
    }
}
