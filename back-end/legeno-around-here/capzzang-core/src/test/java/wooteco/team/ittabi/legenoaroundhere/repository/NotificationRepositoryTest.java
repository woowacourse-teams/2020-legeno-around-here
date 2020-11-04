package wooteco.team.ittabi.legenoaroundhere.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.domain.util.constants.NotificationConstants.TEST_NOTIFICATION_BEFORE_DATE_TIME;
import static wooteco.team.ittabi.legenoaroundhere.domain.util.constants.UserConstants.TEST_USER_ID;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.notification.Notification;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/init-table.sql")
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.findById(TEST_USER_ID)
            .orElseThrow(() -> new NotExistsException("ID에 해당하는 User가 없습니다."));
    }

    @DisplayName("알림 역정렬 테스트")
    @Test
    void findAllByReceiverAndCreatedAtIsAfterAndOrderByIdDesc() {
        for (int i = 0; i < 5; i++) {
            Notification notification = Notification.builder()
                .content("")
                .receiver(user)
                .build();
            notificationRepository.save(notification);
        }
        List<Notification> notifications = notificationRepository
            .findAllByReceiverAndCreatedAtIsAfterOrderByIdDesc(user,
                TEST_NOTIFICATION_BEFORE_DATE_TIME);

        List<Long> notificationIds = notifications.stream()
            .map(BaseEntity::getId)
            .collect(Collectors.toList());

        assertThat(notificationIds.get(0) > notificationIds.get(1)).isTrue();
        assertThat(notificationIds.get(1) > notificationIds.get(2)).isTrue();
        assertThat(notificationIds.get(2) > notificationIds.get(3)).isTrue();
        assertThat(notificationIds.get(3) > notificationIds.get(4)).isTrue();
    }
}
