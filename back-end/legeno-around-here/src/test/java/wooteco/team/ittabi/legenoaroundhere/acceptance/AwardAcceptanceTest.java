package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.team.ittabi.legenoaroundhere.dto.AwardResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

public class AwardAcceptanceTest extends AcceptanceTest {

    /**
     * Feature: 수상 내역을 조회한다. Scenario: 수상 내역을 조회한다.
     * <p>
     * When 수상 내역을 조회한다. Then 수상 내역이 조회된다.
     * <p>
     * When 나의 수상받은 내역을 조회한다. Then 수상 내역이 조회된다.
     */
    @DisplayName("수상 내역 조회")
    @Test
    void findAward() {
        Long userId = getIdFromUrl(
            createUser(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD));
        TokenResponse token = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        String accessToken = token.getAccessToken();

        List<AwardResponse> awards = findAllAward(accessToken, userId);
        assertThat(awards).hasSize(9);

        awards = findMyAward(accessToken);
        assertThat(awards).hasSize(9);
    }

    private List<AwardResponse> findAllAward(String accessToken, Long userId) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/users/" + userId + "/awards")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList(".", AwardResponse.class);
    }

    private List<AwardResponse> findMyAward(String accessToken) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/awards/my")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList(".", AwardResponse.class);
    }
}
