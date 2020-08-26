package wooteco.team.ittabi.legenoaroundhere.service;

import static java.time.LocalDateTime.now;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import wooteco.team.ittabi.legenoaroundhere.domain.notification.Notification;
import wooteco.team.ittabi.legenoaroundhere.dto.NotificationResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.NotificationResponseAssembler;
import wooteco.team.ittabi.legenoaroundhere.repository.NotificationRepository;

@Service
@AllArgsConstructor
public class NotificationService {

    private static final int ONE_WEEK = 7;

    private final NotificationRepository notificationRepository;

    public List<NotificationResponse> findMyNotice() {
        LocalDateTime beforeOneWeek = now().minusDays(ONE_WEEK);
        List<Notification> notifications
            = notificationRepository.findAllByCreatedAtIsAfter(beforeOneWeek);

        return NotificationResponseAssembler.listAssemble(notifications);
    }
}
