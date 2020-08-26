package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_EMPTY_IMAGES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_ANOTHER_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_ANOTHER_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_PASSWORD;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_OTHER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import io.restassured.RestAssured;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.dto.AdminSectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.ErrorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorDetailResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorSimpleResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

public class SectorAcceptanceTest extends AcceptanceTest {

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
        SectorResponse sectorResponse = findAvailableSector(accessToken, id);
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
        assertThat(adminSectorResponse.getState()).isEqualTo(SectorState.PUBLISHED.getName());

        // 부문 수정
        updateSector(accessToken, id, TEST_SECTOR_ANOTHER_NAME, TEST_SECTOR_ANOTHER_DESCRIPTION);

        // 사용 중인 부문 조회
        sectorResponse = findAvailableSector(accessToken, id);
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
        assertThat(adminSectorResponse.getState()).isEqualTo(SectorState.PUBLISHED.getName());

        // 부문 전체 조회 + 사용하는 부문 전체 조회
        Long anotherId = createSector(accessToken, TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);
        List<AdminSectorResponse> adminSectorResponses = findAllSector(accessToken);
        assertThat(adminSectorResponses).hasSize(2);
        List<SectorResponse> sectorResponses = searchAvailableSectors(accessToken);
        assertThat(sectorResponses).hasSize(2);

        // 부문 삭제
        deleteSector(accessToken, id);
        assertThatThrownBy(() -> findAvailableSector(accessToken, id));
        assertThatThrownBy(() -> findSector(accessToken, id));

        adminSectorResponses = findAllSector(accessToken);
        sectorResponses = searchAvailableSectors(accessToken);
        assertThat(adminSectorResponses).hasSize(1);
        assertThat(sectorResponses).hasSize(1);

        deleteSector(accessToken, anotherId);
        adminSectorResponses = findAllSector(accessToken);
        sectorResponses = searchAvailableSectors(accessToken);
        assertThat(adminSectorResponses).hasSize(0);
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
        String userAToken = getCreateUserToken(TEST_USER_EMAIL, TEST_USER_NICKNAME,
            TEST_USER_PASSWORD);
        String userBToken = getCreateUserToken(TEST_USER_OTHER_EMAIL, TEST_USER_NICKNAME,
            TEST_USER_PASSWORD);

        // 사용자 A - 부문 승인 신청
        Long sectorAId = createPendingSector(userAToken, "A", TEST_SECTOR_DESCRIPTION);
        Long sectorBId = createPendingSector(userAToken, "B", TEST_SECTOR_DESCRIPTION);
        Long sectorCId = createPendingSector(userAToken, "C", TEST_SECTOR_DESCRIPTION);

        // 사용자 A - 본인 승인 신청 부문 확인
        List<SectorDetailResponse> sectors = findMySectors(userAToken);
        List<Long> sectorIds = sectors.stream()
            .map(SectorDetailResponse::getId)
            .collect(Collectors.toList());
        assertThat(sectorIds).contains(sectorAId);
        assertThat(sectorIds).contains(sectorBId);
        assertThat(sectorIds).contains(sectorCId);

        // 관리자 - 부문 등록
        Long sectorDId = createSector(adminToken, "D", TEST_SECTOR_DESCRIPTION);

        // 관리자 - 부문 A 승인
        updateStateSector(adminToken, sectorAId, "승인", "승인함");

        // 관리자 - 부문 B 반려
        String wrongSectorName = "부적합한 부문명";
        updateStateSector(adminToken, sectorBId, "반려", wrongSectorName);

        // 사용자 B - 부문 B 등록 (성공)
        Long sectorNewBId = createPendingSector(userAToken, "B", TEST_SECTOR_DESCRIPTION);

        // 사용자 B - 부문 A, C, D 등록 (실패)
        String failReasonSectorA
            = createPendingSectorAndFail(userAToken, "A", TEST_SECTOR_DESCRIPTION);
        String failReasonSectorC
            = createPendingSectorAndFail(userAToken, "C", TEST_SECTOR_DESCRIPTION);
        String failReasonSectorD
            = createPendingSectorAndFail(userAToken, "D", TEST_SECTOR_DESCRIPTION);

        assertThat(failReasonSectorA).contains("사용");
        assertThat(failReasonSectorC).contains("신청");
        assertThat(failReasonSectorD).contains("사용");

        // 사용자 B - 사용할 수 있는 부문 조회
        List<SectorResponse> allInUseSector = searchAvailableSectors(userBToken);

        assertThat(allInUseSector).contains(findAvailableSector(userBToken, sectorAId)); // 승인
        assertThatThrownBy(() -> findAvailableSector(userBToken, sectorBId));            // 반려
        assertThatThrownBy(() -> findAvailableSector(userBToken, sectorCId));            // 승인 신청
        assertThat(allInUseSector).contains(findAvailableSector(userBToken, sectorDId)); // 등록
        assertThatThrownBy(() -> findAvailableSector(userBToken, sectorNewBId));         // 승인 신청

        // 사용자 A - 승인 목록 조회
        sectors = findMySectors(userAToken);
        Map<Long, SectorDetailResponse> sectorsWithId = sectors.stream()
            .collect(Collectors.toMap(SectorDetailResponse::getId, sector -> sector));
        assertThat(sectorsWithId.get(sectorAId).getState()).isEqualTo("승인");
        assertThat(sectorsWithId.get(sectorBId).getState()).isEqualTo("반려");
        assertThat(sectorsWithId.get(sectorBId).getReason()).isEqualTo(wrongSectorName);
        assertThat(sectorsWithId.get(sectorCId).getState()).isEqualTo("승인 신청");
    }

    private String getCreateAdminToken() {
        TokenResponse tokenResponse = login(TEST_ADMIN_EMAIL, TEST_ADMIN_PASSWORD);
        return tokenResponse.getAccessToken();
    }

    private String getCreateUserToken(String email, String nickname, String password) {
        TokenResponse tokenResponse = login(email, password);
        return tokenResponse.getAccessToken();
    }

    /**
     * Feature: 부문 페이징 조회
     * <p>
     * Scenario: 부문을 페이징 조회한다.
     * <p>
     * Given 관리자가 로그인 되어있다. 부문 100개가 등록되어 있다.
     * <p>
     * When 부문 1Page 20Size를 정렬(기준:id 방향:오름차순) 조회한다. Then 1~20까지의 Sector가 조회된다.
     * <p>
     * When 부문 1Page 20Size를 정렬(기준:id 방향:내림차순) 조회한다. Then 100~81까지의 Sector가 조회된다.
     * <p>
     * When 부문 2Page 40Size를 정렬(기준:id 방향:오름차순) 조회한다. Then 21~40까지의 Sector가 조회된다.
     * <p>
     * When 부문 -1Page -1Size를 정렬(기준:id 방향:abc) 조회한다. Then 1의 Sector가 조회된다. (기본 값 : 1Page, 1Size,
     * 방향:오름차순)
     * <p>
     * When 부문 1Page 20Size를 정렬(기준:test-id 방향:오름차순) 조회한다. Then BadRequest가 발생한다.
     */
    @DisplayName("부문 페이징 조회")
    @Test
    void pagingFindSector() {
        // 관리자 로그인
        TokenResponse tokenResponse = login(TEST_ADMIN_EMAIL, TEST_ADMIN_PASSWORD);
        String accessToken = tokenResponse.getAccessToken();

        List<Long> ids = new ArrayList<>();
        // 부문 100개 등록
        for (int i = 0; i < 100; i++) {
            ids.add(createSector(accessToken, TEST_SECTOR_NAME + i, TEST_SECTOR_DESCRIPTION));
        }

        // 부문 1Page 20Size를 정렬(기준:id 방향:오름차순) 조회
        List<AdminSectorResponse> sectors
            = findAllSectorWithParameter(accessToken, "page=0&size=20&sortedBy=id&direction=asc");
        assertThat(sectors).hasSize(20);
        List<Long> expectedIds = ids.subList(0, 20);
        assertThat(getSectorIds(sectors)).isEqualTo(expectedIds);

        // 부문 1Page 20Size를 정렬(기준:id 방향:내림차순) 조회
        sectors = findAllSectorWithParameter(accessToken,
            "page=0&size=20&sortedBy=id&direction=desc");
        assertThat(sectors).hasSize(20);
        expectedIds = ids.subList(80, 100);
        Collections.reverse(expectedIds);
        assertThat(getSectorIds(sectors)).isEqualTo(expectedIds);

        // 부문 2Page 20Size를 정렬(기준:id 방향:오름차순) 조회
        sectors = findAllSectorWithParameter(accessToken,
            "page=1&size=20&sortedBy=id&direction=asc");
        assertThat(sectors).hasSize(20);
        expectedIds = ids.subList(20, 40);
        assertThat(getSectorIds(sectors)).isEqualTo(expectedIds);

        // 비워두고 조회
        sectors = findAllSectorWithParameter(accessToken, "size=1&sortedBy=id&direction=asc");
        assertThat(sectors).hasSize(1);
        expectedIds = ids.subList(0, 1);
        assertThat(getSectorIds(sectors)).isEqualTo(expectedIds);

        sectors = findAllSectorWithParameter(accessToken, "page=&size=1&sortedBy=id&direction=asc");
        assertThat(sectors).hasSize(1);
        expectedIds = ids.subList(0, 1);
        assertThat(getSectorIds(sectors)).isEqualTo(expectedIds);

        sectors = findAllSectorWithParameter(accessToken, "page=0&sortedBy=id&direction=asc");
        assertThat(sectors).hasSize(10);
        expectedIds = ids.subList(0, 10);
        assertThat(getSectorIds(sectors)).isEqualTo(expectedIds);

        sectors = findAllSectorWithParameter(accessToken, "page=0&size=&sortedBy=id&direction=asc");
        assertThat(sectors).hasSize(10);
        expectedIds = ids.subList(0, 10);
        assertThat(getSectorIds(sectors)).isEqualTo(expectedIds);

        sectors = findAllSectorWithParameter(accessToken, "page=0&size=1&sortedBy=id");
        assertThat(sectors).hasSize(1);
        expectedIds = ids.subList(0, 1);
        assertThat(getSectorIds(sectors)).isEqualTo(expectedIds);

        sectors = findAllSectorWithParameter(accessToken, "page=0&size=1&sortedBy=id&direction=");
        assertThat(sectors).hasSize(1);
        expectedIds = ids.subList(0, 1);
        assertThat(getSectorIds(sectors)).isEqualTo(expectedIds);

        // 유효하지 않은 필드로 정렬
        findAllSectorWithWrongParameter(accessToken, "page=-1&size=1&sortedBy=id&direction=asc");
        findAllSectorWithWrongParameter(accessToken, "page=0&size=-1&sortedBy=id&direction=asc");
        findAllSectorWithWrongParameter(accessToken, "page=0&size=501&sortedBy=id&direction=asc");
        findAllSectorWithWrongParameter(accessToken, "page=0&size=1&sortedBy=id&direction=abc");
        findAllSectorWithWrongParameter(accessToken, "page=ㄱ&size=1&sortedBy=id");
        findAllSectorWithWrongParameter(accessToken, "page=0&size=ㄴ&sortedBy=id");
        findAllSectorWithWrongParameter(accessToken, "page=0&size=20&sortedBy=ㄷ&direction=asc");
    }

    /**
     * Feature: 부문 키워드 조회
     * <p>
     * Scenario: 부문을 키워드 조회한다.
     * <p>
     * Given 관리자가 로그인 되어있다. 부문 100개가 등록되어 있다.
     * <p>
     * When 사용자가 키워드를 입력하지 않고 부문을 10Page, 11Size 조회한다. Then 1건이 조회된다.
     * <p>
     * When 사용자가 키워드를 비워두고 부문을 10Page, 11Size 조회한다. Then 1건이 조회된다.
     * <p>
     * When 사용자가 "SECTOR"라는 키워드로 지역을 10Page, 11Size 조회한다. 조회한다. Then 1건이 조회된다.
     * <p>
     * When 사용자가 "sector"라는 키워드로 지역을 10Page, 11Size 조회한다. 조회한다. Then 1건이 조회된다.
     * <p>
     * When 사용자가 "Sector"라는 키워드로 지역을 10Page, 11Size 조회한다. 조회한다. Then 1건이 조회된다.
     * <p>
     * When 사용자가 "9"라는 키워드로 지역을 1Page, 20Size 조회한다. Then 19건이 조회된다.
     */
    @DisplayName("부문 키워드 조회")
    @Test
    void findAreaByKeyword() {
        // 관리자 로그인
        TokenResponse tokenResponse = login(TEST_ADMIN_EMAIL, TEST_ADMIN_PASSWORD);
        String accessToken = tokenResponse.getAccessToken();

        // 부문 100개 등록
        for (int i = 0; i < 100; i++) {
            createSector(accessToken, TEST_SECTOR_NAME + i, TEST_SECTOR_DESCRIPTION);
        }

        List<SectorResponse> sectors
            = searchAvailableSectorsWithParameter(accessToken, "page=9&size=11");
        assertThat(sectors).hasSize(1);

        sectors = searchAvailableSectorsWithParameter(accessToken, "keyword=&page=9&size=11");
        assertThat(sectors).hasSize(1);

        sectors
            = searchAvailableSectorsWithParameter(accessToken, "keyword=SECTOR&page=9&size=11");
        assertThat(sectors).hasSize(1);

        sectors
            = searchAvailableSectorsWithParameter(accessToken, "keyword=sector&page=9&size=11");
        assertThat(sectors).hasSize(1);

        sectors
            = searchAvailableSectorsWithParameter(accessToken, "keyword=Sector&page=9&size=11");
        assertThat(sectors).hasSize(1);

        sectors
            = searchAvailableSectorsWithParameter(accessToken, "keyword=9&page=0&size=20");
        assertThat(sectors).hasSize(19);
    }

    /**
     * Feature: 부문 조회, Scenario: 부문을 조회한다.
     * <p>
     * Given 부문을 5개 등록한다, 부문이 5개 등록된다.
     * <p>
     * 부문 1) 글 0건 + 삭제 글 8건 부문 2) 글 1건 + 삭제 글 6건 부문 3) 글 2건 + 삭제 글 4건 부문 4) 글 3건 + 삭제 글 2건 부문 5) 글
     * 4건 + 삭제 글 0건
     * <p>
     * When 전체 부문 단순 조회 Then 5개가 조회된다.
     * <p>
     * When 인기 부문 조회 Then 4개가 조회된다.
     * <p>
     * When 인기 부문 4개 조회 Then 4개가 조회된다.
     * <p>
     * When 인기 부문 2개 조회 Then 2개가 조회된다.
     */
    @DisplayName("부문 조회")
    @Test
    void findSectors() {
        // 초기 데이터 셋팅
        TokenResponse tokenResponse = login(TEST_ADMIN_EMAIL, TEST_ADMIN_PASSWORD);
        String accessToken = tokenResponse.getAccessToken();

        List<Long> sectorIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Long sectorId
                = createSector(accessToken, TEST_SECTOR_NAME + i, TEST_SECTOR_DESCRIPTION);
            sectorIds.add(sectorId);
            for (int j = 0; j <= i; j++) {
                createPostWithoutImageWithSector(accessToken, sectorId);
            }
            for (int k = 0; k < 8 - (2 * i); k++) {
                Long postId = createPostWithoutImageWithSector(accessToken, sectorId);
                deletePost(accessToken, postId);
            }
        }
        Collections.reverse(sectorIds);

        // 전체 부문 단순(ID+NAME) 조회
        List<SectorSimpleResponse> sectorResponses = findSectorsForKeywordSearch(accessToken);
        assertThat(sectorResponses).hasSize(5);

        // 인기 부문 조회
        sectorResponses = findBestSectors(accessToken, "");
        assertThat(sectorResponses).hasSize(4);
        assertThat(getSectorIdsBy(sectorResponses)).isEqualTo(sectorIds.subList(0, 4));

        sectorResponses = findBestSectors(accessToken, "?count=");
        assertThat(sectorResponses).hasSize(4);
        assertThat(getSectorIdsBy(sectorResponses)).isEqualTo(sectorIds.subList(0, 4));

        sectorResponses = findBestSectors(accessToken, "?count=4");
        assertThat(sectorResponses).hasSize(4);
        assertThat(getSectorIdsBy(sectorResponses)).isEqualTo(sectorIds.subList(0, 4));

        sectorResponses = findBestSectors(accessToken, "?count=2");
        assertThat(sectorResponses).hasSize(2);
        assertThat(getSectorIdsBy(sectorResponses)).isEqualTo(sectorIds.subList(0, 2));
    }

    private List<Long> getSectorIds(List<AdminSectorResponse> sectors) {
        return sectors.stream()
            .map(AdminSectorResponse::getId)
            .collect(Collectors.toList());
    }

    private List<Long> getSectorIdsBy(List<SectorSimpleResponse> sectors) {
        return sectors.stream()
            .map(SectorSimpleResponse::getId)
            .collect(Collectors.toList());
    }

    private Long createPostWithoutImageWithSector(String accessToken, Long sectorId) {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);

        String location = given()
            .header("X-AUTH-TOKEN", accessToken)
            .body(postCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/posts")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");
        return getIdFromUrl(location);
    }

    private void deletePost(String accessToken, Long id) {
        given()
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .delete("/posts/" + id)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
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

    private String createPendingSectorAndFail(String accessToken, String sectorName,
        String sectorDescription) {
        Map<String, String> params = new HashMap<>();
        params.put("name", sectorName);
        params.put("description", sectorDescription);

        return given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/sectors")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .extract()
            .as(ErrorResponse.class)
            .getErrorMessage();
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

    private SectorResponse findAvailableSector(String accessToken, Long id) {
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

    private void updateSector(String accessToken, Long id, String sectorName,
        String sectorDescription) {
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
        return findAllSectorWithParameter(accessToken, "page=0&size=10&sortedBy=id&direction=asc");
    }

    private List<AdminSectorResponse> findAllSectorWithParameter(String accessToken,
        String parameter) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/admin/sectors?" + parameter)
            .then()
            .statusCode(HttpStatus.OK.value())
            .log().body()
            .extract()
            .jsonPath()
            .getList("content", AdminSectorResponse.class);
    }

    private void findAllSectorWithWrongParameter(String accessToken, String parameter) {
        given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/admin/sectors?" + parameter)
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .log().all();
    }

    private List<SectorResponse> searchAvailableSectors(String accessToken) {
        return searchAvailableSectorsWithParameter(accessToken,
            "page=0&size=10&sortedBy=id&direction=asc");
    }

    private List<SectorResponse> searchAvailableSectorsWithParameter(String accessToken,
        String parameter) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/sectors?" + parameter)
            .then()
            .statusCode(HttpStatus.OK.value())
            .log().body()
            .extract()
            .jsonPath()
            .getList("content", SectorResponse.class);
    }

    private List<SectorDetailResponse> findMySectors(String accessToken) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/sectors/me")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList("content", SectorDetailResponse.class);
    }

    private List<SectorSimpleResponse> findBestSectors(String accessToken, String parameter) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/sectors/best" + parameter)
            .then()
            .statusCode(HttpStatus.OK.value())
            .log().body()
            .extract()
            .jsonPath()
            .getList(".", SectorSimpleResponse.class);
    }

    private List<SectorSimpleResponse> findSectorsForKeywordSearch(String accessToken) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/sectors/simple")
            .then()
            .statusCode(HttpStatus.OK.value())
            .log().body()
            .extract()
            .jsonPath()
            .getList(".", SectorSimpleResponse.class);
    }

    private void deleteSector(String accessToken, Long id) {
        given()
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .delete("/admin/sectors/" + id)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
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
