package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import org.junit.jupiter.api.DisplayName;
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
    void findAward() {
        Long id = getIdFromUrl(createUser(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD));
        TokenResponse token = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        String accessToken = token.getAccessToken();

        List<AwardResponse> awards = findAllAward(accessToken, id);
        assertThat(awards).hasSize(10);

        awards = findMyAward(accessToken);
        assertThat(awards).hasSize(10);
    }
}
