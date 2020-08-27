package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_REASON;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_PASSWORD;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.team.ittabi.legenoaroundhere.dto.AwardResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;

public class AwardAcceptanceTest extends AcceptanceTest {

    private Long userId = TEST_USER_ID;
    private String adminToken;
    private String accessToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        adminToken = getCreateAdminToken();
        accessToken = getCreateUserToken();
    }

    private String getCreateAdminToken() {
        TokenResponse tokenResponse = login(TEST_ADMIN_EMAIL, TEST_ADMIN_PASSWORD);
        return tokenResponse.getAccessToken();
    }

    private String getCreateUserToken() {
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        return tokenResponse.getAccessToken();
    }

    /**
     * Feature: 수상 내역을 조회한다. Scenario: 수상 내역을 조회한다.
     * <p>
     * Given 사용자가 로그인되어 있다.
     * <p>
     * When 수상 내역을 조회한다. Then 수상 내역이 조회된다.
     * <p>
     * When 나의 수상받은 내역을 조회한다. Then 수상 내역이 조회된다.
     */
    @DisplayName("수상 내역 조회")
    @Test
    void findAward() {
        List<AwardResponse> awards = findAllAward(accessToken, userId);
        assertThat(awards).hasSize(0);

        awards = findMyAward(accessToken);
        assertThat(awards).hasSize(0);
    }

    /**
     * Feature: 부문 창시자 수상을 관리한다. Scenario: 부문 창시자 수상을 관리한다.
     * <p>
     * Given 사용자가 로그인되어 있다.
     * <p>
     * When 나의 수상받은 내역을 조회한다. Then 수상 내역이 n건 조회된다.
     * <p>
     * When 나의 프로필을 조회한다. Then 부문 수상이 m개 조회된다.
     * <p>
     * Given 사용자가 부문 승인 요청을 한다. 관리자가 부문 승인을 한다.
     * <p>
     * When 나의 수상받은 내역을 조회한다. Then 수상 내용이 n + 1 건 조회된다. 수상 내역에 승인된 부문 내용이 추가되어있다.
     * <p>
     * When 나의 프로필을 조회한다. Then 부문 수상이 m + 1개 조회된다.
     */
    @DisplayName("부문 창시자 수상 관리")
    @Test
    void manageSectorCreatorAward() {

        // 나의 수상 내역 건수 조회 : n건
        int myAwardsCount = findMyAward(accessToken).size();

        // 나의 부문 수상 건수 조회 : m건
        int sectorAwardCount = findUser(accessToken).getAwardsCount().getSector();

        // 부문 신청 및 승인
        Long pendingSectorId
            = createPendingSector(accessToken, TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        updateStateSector(adminToken, pendingSectorId);

        // 나의 수상 내역 건수 조회 : n + 1건
        List<AwardResponse> newMyAwards = findMyAward(accessToken);

        assertThat(newMyAwards.size())
            .isEqualTo(myAwardsCount + 1);

        // 승인된 부문 내용 추가
        long count = newMyAwards.stream()
            .filter(award -> award.getName().contains(TEST_SECTOR_NAME.toUpperCase()))
            .count();
        assertThat(count).isEqualTo(1);

        // 나의 부문 수상 건수 조회 : m + 1건
        assertThat(findUser(accessToken).getAwardsCount().getSector())
            .isEqualTo(sectorAwardCount + 1);
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
            .get("/awards/me")
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList(".", AwardResponse.class);
    }

    private UserResponse findUser(String accessToken) {
        return given()
            .header("X-AUTH-TOKEN", accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/users/me")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(UserResponse.class);
    }

    private Long createPendingSector(String accessToken, String sectorName,
        String sectorDescription) {
        Map<String, String> params = new HashMap<>();
        params.put("name", sectorName);
        params.put("description", sectorDescription);

        String location = given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/sectors")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");

        return getIdFromUrl(location);
    }

    private void updateStateSector(String accessToken, Long sectorId) {
        Map<String, String> params = new HashMap<>();
        params.put("state", "승인");
        params.put("reason", TEST_SECTOR_REASON);

        given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .put("/admin/sectors/" + sectorId + "/state")
            .then()
            .statusCode(HttpStatus.OK.value());
    }
}
