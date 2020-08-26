package wooteco.team.ittabi.legenoaroundhere.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wooteco.team.ittabi.legenoaroundhere.domain.notification.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByCreatedAtIsAfter(LocalDateTime afterDateTime);
}
