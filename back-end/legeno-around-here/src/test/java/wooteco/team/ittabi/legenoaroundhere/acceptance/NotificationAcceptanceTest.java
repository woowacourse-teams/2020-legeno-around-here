package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NOTIFICATION_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import io.restassured.RestAssured;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.team.ittabi.legenoaroundhere.dto.NotificationResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

public class NotificationAcceptanceTest extends AcceptanceTest {

    private String accessToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        // 로그인
        TokenResponse tokenResponse = login(TEST_USER_NOTIFICATION_EMAIL, TEST_USER_PASSWORD);
        accessToken = tokenResponse.getAccessToken();
    }

    /**
     * Feature: 알림 관리
     * <p>
     * Scenario: 알림을 관리한다.
     * <p>
     * Given 사용자가 로그인 되어있다.
     * <p>
     * When 내 알림을 조회한다.
     * <p>
     * When
     */
    @DisplayName("알림 관리")
    @Test
    void manageMyNotices() {
        // 알림 조회 - 읽은 알림 0
        List<NotificationResponse> notices = findMyNotice(accessToken);
        assertThat(notices).hasSize(7);

        List<NotificationResponse> readNotices = notices.stream()
            .filter(NotificationResponse::getIsRead)
            .collect(Collectors.toList());
        assertThat(readNotices).hasSize(0);

        // 알림 1개 읽음 - 안읽었던 알림 : 읽은 알림 1
        Long noticeId = notices.get(0).getId();
        readMyNotices(accessToken, noticeId);

        notices = findMyNotice(accessToken);
        assertThat(notices).hasSize(7);

        readNotices = notices.stream()
            .filter(NotificationResponse::getIsRead)
            .collect(Collectors.toList());
        assertThat(readNotices).hasSize(1);

        // 알림 1개 읽음 - 읽었던 알림 : 읽은 알림 1
        readMyNotices(accessToken, noticeId);

        notices = findMyNotice(accessToken);
        assertThat(notices).hasSize(7);

        readNotices = notices.stream()
            .filter(NotificationResponse::getIsRead)
            .collect(Collectors.toList());
        assertThat(readNotices).hasSize(1);
    }

    private List<NotificationResponse> findMyNotice(String accessToken) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/notifications/me")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList(".", NotificationResponse.class);
    }

    private void readMyNotices(String accessToken, Long noticeId) {
        given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .put("/notifications/" + noticeId + "/read")
            .then()
            .log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
