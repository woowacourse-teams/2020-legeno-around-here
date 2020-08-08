package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_ADMIN_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_ADMIN_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_ADMIN_PASSWORD;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_ANOTHER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_PASSWORD;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_SECTOR_ANOTHER_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_SECTOR_ANOTHER_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_SECTOR_NAME;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.dto.AdminSectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorDetailResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/init-table.sql")
public class SectorAcceptanceTest {

    @LocalServerPort
    public int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * Feature: 부문 관리
     * <p>
     * Scenario: 관리자가 부문을 관리한다.
     * <p>
     * Given 관리자가 로그인 되어있다.
     * <p>
     * When 부문을 등록한다. Then 부문이 등록되었다.
     * <p>
     * When 부문을 조회한다. Then 부문이 조회된다.
     * <p>
     * When 부문을 수정한다. Then 부문이 수정된다.
     * <p>
     * Given 부문을 추가로 등록한다. When 사용중인 부문을 전체 조회한다. Then 사용중인 부문이 전체 조회된다.
     * <p>
     * When 부문을 삭제한다. Then 부문이 삭제되었다. 전체 조회시 조회된다. 사용중인 건만 조회시 조회되지 않는다.
     */
    @DisplayName("관리자의 부문 관리")
    @Test
    void manageSector_Admin() {
        // 관리자 로그인
        String accessToken = getCreateAdminToken();

        // 부문 등록
        Long id = createSector(accessToken, TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);

        // 사용 중인 부문 조회
        SectorResponse sectorResponse = findInUseSector(accessToken, id);
        assertThat(sectorResponse.getId()).isEqualTo(id);
        assertThat(sectorResponse.getName()).isEqualToIgnoringCase(TEST_SECTOR_NAME);
        assertThat(sectorResponse.getDescription()).isEqualTo(TEST_SECTOR_DESCRIPTION);
        assertThat(sectorResponse.getCreator()).isNotNull();

        // Admin을 위한 부문 조회(상세)
        AdminSectorResponse adminSectorResponse = findSector(accessToken, id);
        assertThat(adminSectorResponse.getId()).isEqualTo(id);
        assertThat(adminSectorResponse.getName()).isEqualToIgnoringCase(TEST_SECTOR_NAME);
        assertThat(adminSectorResponse.getDescription()).isEqualTo(TEST_SECTOR_DESCRIPTION);
        assertThat(adminSectorResponse.getCreator()).isNotNull();
        assertThat(adminSectorResponse.getLastModifier()).isNotNull();
        assertThat(adminSectorResponse.getState()).isEqualTo(SectorState.PUBLISHED.name());

        // 부문 수정
        updateSector(accessToken, id, TEST_SECTOR_ANOTHER_NAME, TEST_SECTOR_ANOTHER_DESCRIPTION);

        // 사용 중인 부문 조회
        sectorResponse = findInUseSector(accessToken, id);
        assertThat(sectorResponse.getId()).isEqualTo(id);
        assertThat(sectorResponse.getName()).isEqualToIgnoringCase(TEST_SECTOR_ANOTHER_NAME);
        assertThat(sectorResponse.getDescription()).isEqualTo(TEST_SECTOR_ANOTHER_DESCRIPTION);
        assertThat(sectorResponse.getCreator()).isNotNull();

        // Admin을 위한 부문 조회(상세)
        adminSectorResponse = findSector(accessToken, id);
        assertThat(adminSectorResponse.getId()).isEqualTo(id);
        assertThat(adminSectorResponse.getName())
            .isEqualToIgnoringCase(TEST_SECTOR_ANOTHER_NAME);
        assertThat(adminSectorResponse.getDescription())
            .isEqualTo(TEST_SECTOR_ANOTHER_DESCRIPTION);
        assertThat(adminSectorResponse.getCreator()).isNotNull();
        assertThat(adminSectorResponse.getLastModifier()).isNotNull();
        assertThat(adminSectorResponse.getState()).isEqualTo(SectorState.PUBLISHED.name());

        // 부문 전체 조회 + 사용하는 부문 전체 조회
        Long anotherId = createSector(accessToken, TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        List<AdminSectorResponse> adminSectorResponses = findAllSector(accessToken);
        List<SectorResponse> sectorResponses = findAllInUseSector(accessToken);
        assertThat(adminSectorResponses).hasSize(2);
        assertThat(sectorResponses).hasSize(2);

        // 부문 삭제
        deleteSector(accessToken, id);
        assertThatThrownBy(() -> findInUseSector(accessToken, id));
        adminSectorResponse = findSector(accessToken, id);
        assertThat(adminSectorResponse.getState()).isEqualTo(SectorState.DELETED.name());

        adminSectorResponses = findAllSector(accessToken);
        sectorResponses = findAllInUseSector(accessToken);
        assertThat(adminSectorResponses).hasSize(2);
        assertThat(sectorResponses).hasSize(1);

        deleteSector(accessToken, anotherId);
        adminSectorResponses = findAllSector(accessToken);
        sectorResponses = findAllInUseSector(accessToken);
        assertThat(adminSectorResponses).hasSize(2);
        assertThat(sectorResponses).hasSize(0);
    }

    /**
     * Feature: 부문 관리 Scenario: 사용자가 부문을 관리한다.
     * <p>
     * Given 관리자, 사용자 A, 사용자 B가 있다.
     * <p>
     * Given 사용자 A가 로그인 되어있다.
     * <p>
     * When A, B, C 부문을 각각 승인 신청한다. Then A, B, C 부문이 각각 승인 신청된다.
     * <p>
     * When 본인이 승인 신청한 부문 목록을 확인한다. Then 본인이 승인 신청한 부문들이 확인된다.(A, B, C)
     * <p>
     * Given 관리자가 로그인 되어 있다. D 부문을 등록한다.
     * <p>
     * When A 부문을 승인한다. Then A 부문이 승인 상태이다.
     * <p>
     * When B 부문을 반려한다. Then B 부문이 반려 상태이다.
     * <p>
     * Given 사용자 B가 로그인 되어있다.
     * <p>
     * When A 부문을 승인 신청한다. Then A 부문을 승인 신청할 수 없다. (기존 - 승인됨)
     * <p>
     * When B 부문을 승인 신청한다. Then B 부문을 승인 신청할 수 없다. (기존 - 반려됨)
     * <p>
     * When C 부문을 승인 신청한다. Then C 부문을 승인 신청할 수 없다. (기존 - 승인 신청됨)
     * <p>
     * When D 부문을 승인 신청한다. Then D 부문을 승인 신청할 수 없다. (기존 - 등록됨)
     * <p>
     * When 사용할 수 있는 부문들을 조회한다. Then 사용할 수 있는 부문이 조회된다.(A, D), 사용할 수 없는 부문은 조회 실패(C, B)
     * <p>
     * Given 사용자 A가 로그인 되어있다.
     * <p>
     * When 본인이 승인 신청한 목록을 확인한다. Then 승인된 부문이 확인된다.(A) 반려된 부문이 확인된다.(B) 승인 신청한 부문이 확인된다.(C)
     */
    @DisplayName("사용자의 부문 관리")
    @Test
    void manageSector_User() {
        // 관리자, 사용자 A, 사용자 B 로그인
        String adminToken = getCreateAdminToken();
        String userAToken = getCreateUserToken(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        String userBToken = getCreateUserToken(TEST_ANOTHER_EMAIL, TEST_NICKNAME, TEST_PASSWORD);

        // 사용자 A - 부문 승인 신청
        Long sectorAId = createPendingSector(userAToken, "A", TEST_SECTOR_DESCRIPTION);
        Long sectorBId = createPendingSector(userAToken, "B", TEST_SECTOR_DESCRIPTION);
        Long sectorCId = createPendingSector(userAToken, "C", TEST_SECTOR_DESCRIPTION);

        // 사용자 A - 본인 승인 신청 부문 확인
        List<SectorDetailResponse> sectors = getMySectors(userAToken);
        List<Long> sectorIds = sectors.stream()
            .map(SectorDetailResponse::getId)
            .collect(Collectors.toList());
        assertThat(sectorIds).contains(sectorAId);
        assertThat(sectorIds).contains(sectorBId);
        assertThat(sectorIds).contains(sectorCId);

        // 관리자 - 부문 등록
        Long sectorDId = createSector(adminToken, "D", TEST_SECTOR_DESCRIPTION);

        // 관리자 - 부문 A 승인
        approveSector(adminToken, sectorAId);

//        // 관리자 - 부문 B 반려
//        String wrongSectorName = "부적합한 부문명";
//        rejectSector(adminToken, sectorBId, wrongSectorName);
//
//        // 사용자 B - 부문 A, B, C, D 등록 (실패)
//        String failReasonSectorA
//            = createPendingSectorAndFail(userAToken, "A", TEST_SECTOR_DESCRIPTION);
//        String failReasonSectorB
//            = createPendingSectorAndFail(userAToken, "B_반려", TEST_SECTOR_DESCRIPTION);
//        String failReasonSectorC
//            = createPendingSectorAndFail(userAToken, "C", TEST_SECTOR_DESCRIPTION);
//        String failReasonSectorD
//            = createPendingSectorAndFail(userAToken, "D", TEST_SECTOR_DESCRIPTION);
//
//        assertThat(failReasonSectorA).contains("사용");
//        assertThat(failReasonSectorB).contains("반려");
//        assertThat(failReasonSectorC).contains("요청");
//        assertThat(failReasonSectorD).contains("사용");
//
//        // 사용자 B - 사용할 수 있는 부문 조회
//        List<SectorResponse> allInUseSector = findAllInUseSector(userBToken);
//
//        assertThat(allInUseSector).contains(findInUseSector(userBToken, sectorAId));
//        assertThatThrownBy(() -> findInUseSector(userBToken, sectorBId));
//        assertThat(allInUseSector).contains(findInUseSector(userBToken, sectorCId));
//        assertThatThrownBy(() -> findInUseSector(userBToken, sectorDId));
//
//        // 사용자 A - 승인 목록 조회
//        sectors = getMySectors(userAToken);
//        Map<Long, SectorWithReasonResponse> sectorsWithId = sectors.stream()
//            .collect(Collectors.toMap(SectorWithReasonResponse::getId, sector -> sector));
//        assertThat(sectorsWithId.get(sectorAId).getState()).isEqualTo("승인");
//        assertThat(sectorsWithId.get(sectorBId).getState()).isEqualTo("반려");
//        assertThat(sectorsWithId.get(sectorBId).getReason()).isEqualTo(wrongSectorName);
//        assertThat(sectorsWithId.get(sectorCId).getState()).isEqualTo("승인 신청");
    }

    private String getCreateAdminToken() {
        createAdmin(TEST_ADMIN_EMAIL, TEST_ADMIN_NICKNAME, TEST_ADMIN_PASSWORD);
        TokenResponse tokenResponse = login(TEST_ADMIN_EMAIL, TEST_ADMIN_PASSWORD);
        return tokenResponse.getAccessToken();
    }

    private String getCreateUserToken(String email, String nickname, String password) {
        createUser(email, nickname, password);
        TokenResponse tokenResponse = login(email, password);
        return tokenResponse.getAccessToken();
    }

    private String createAdmin(String email, String nickname, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("nickname", nickname);
        params.put("password", password);

        return given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/joinAdmin")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");
    }

    private String createUser(String email, String nickname, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("nickname", nickname);
        params.put("password", password);

        return given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/join")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");
    }

    private TokenResponse login(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/login")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(TokenResponse.class);
    }

    private Long createSector(String accessToken, String sectorName, String sectorDescription) {
        Map<String, String> params = new HashMap<>();
        params.put("name", sectorName);
        params.put("description", sectorDescription);

        String location = given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/admin/sectors")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");

        return getIdFromUrl(location);
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

    private Long getIdFromUrl(String location) {
        int lastIndex = location.lastIndexOf("/");
        return Long.valueOf(location.substring(lastIndex + 1));
    }

    private AdminSectorResponse findSector(String accessToken, Long id) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/admin/sectors/" + id)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(AdminSectorResponse.class);
    }

    private SectorResponse findInUseSector(String accessToken, Long id) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/sectors/" + id)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(SectorResponse.class);
    }

    private void updateSector(String accessToken, Long id,
        String sectorName, String sectorDescription) {
        Map<String, String> params = new HashMap<>();
        params.put("name", sectorName);
        params.put("description", sectorDescription);

        given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .put("/admin/sectors/" + id)
            .then()
            .statusCode(HttpStatus.OK.value());
    }

    private List<AdminSectorResponse> findAllSector(String accessToken) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/admin/sectors")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList(".", AdminSectorResponse.class);
    }

    private List<SectorResponse> findAllInUseSector(String accessToken) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/sectors")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList(".", SectorResponse.class);
    }

    private List<SectorDetailResponse> getMySectors(String accessToken) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/my/sectors")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList(".", SectorDetailResponse.class);
    }

    private void deleteSector(String accessToken, Long id) {
        given()
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .delete("/admin/sectors/" + id)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private void approveSector(String accessToken, Long sectorId) {
        updateStateSector(accessToken, sectorId, "approve", "승인함");
    }

    private void updateStateSector(String accessToken, Long id, String state, String reason) {
        Map<String, String> params = new HashMap<>();
        params.put("state", state);
        params.put("reason", reason);

        given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .put("/admin/sectors/" + id + "/state")
            .then()
            .statusCode(HttpStatus.OK.value());
    }
}
