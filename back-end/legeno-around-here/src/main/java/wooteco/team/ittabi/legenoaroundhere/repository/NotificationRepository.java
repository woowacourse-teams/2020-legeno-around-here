package wooteco.team.ittabi.legenoaroundhere.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.notification.Notification;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByReceiverAndCreatedAtIsAfter(User receiver,
        LocalDateTime afterDateTime);

    List<Notification> findAllByReceiverAndPost(User receiver, Post post);
}
