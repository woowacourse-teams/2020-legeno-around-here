package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NOTIFICATION_ID;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.NotificationResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;

class NotificationServiceTest extends ServiceTest {

    @Autowired
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        User user = userRepository.findById(TEST_USER_NOTIFICATION_ID)
            .orElseThrow(() -> new NotExistsException("User가 존재하지 않습니다."));
        setAuthentication(user);
    }

    @DisplayName("내 알림 확인")
    @Test
    void findMyNotices_Success() {
        List<NotificationResponse> myNotice = notificationService.findMyNotice();
        assertThat(myNotice).hasSize(7);
    }

    @DisplayName("내 알림 읽기")
    @Test
    void readMyNotice_Success() {
        List<NotificationResponse> myNotice = notificationService.findMyNotice();
        assertThat(myNotice).hasSize(7);

        Long notificationId = myNotice.get(0).getId();
        notificationService.readMyNotice(notificationId);

        myNotice = notificationService.findMyNotice();
        assertThat(myNotice).hasSize(7);

        List<NotificationResponse> readNotices = myNotice.stream()
            .filter(NotificationResponse::getIsRead)
            .collect(Collectors.toList());
        assertThat(readNotices).hasSize(1);

        assertThat(readNotices.get(0).getId()).isEqualTo(notificationId);
    }

    @DisplayName("내 알림 읽기, 아무것도 하지 않음 - 이미 읽은 알람")
    @Test
    void readMyNotice_ReadTrue_Nothing() {
        List<NotificationResponse> myNotice = notificationService.findMyNotice();

        Long notificationId = myNotice.get(0).getId();
        notificationService.readMyNotice(notificationId);

        myNotice = notificationService.findMyNotice();
        notificationService.readMyNotice(notificationId);

        List<NotificationResponse> foundNotices = notificationService.findMyNotice();
        assertThat(myNotice).isEqualTo(foundNotices);
    }
}