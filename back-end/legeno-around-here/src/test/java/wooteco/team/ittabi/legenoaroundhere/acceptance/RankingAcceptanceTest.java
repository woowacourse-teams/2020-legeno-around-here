package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_A_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_B_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_PASSWORD;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.team.ittabi.legenoaroundhere.domain.rank.Criteria;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.RankingResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

public class RankingAcceptanceTest extends AcceptanceTest {

    private String adminToken;
    private String accessTokenA;
    private String accessTokenB;
    private String accessTokenC;
    private Long sectorAId;
    private Long sectorBId;
    private Long firstPostId;
    private Long secondPostId;
    private Long thirdPostId;
    private Long fourthPostId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        adminToken = getCreateAdminToken();
        createUser("a@capzzang.co.kr", "A", "passwordA");
        createUser("b@capzzang.co.kr", "B", "passwordB");
        createUser("c@capzzang.co.kr", "C", "passwordC");
        TokenResponse tokenResponseA = login("a@capzzang.co.kr", "passwordA");
        TokenResponse tokenResponseB = login("b@capzzang.co.kr", "passwordB");
        TokenResponse tokenResponseC = login("c@capzzang.co.kr", "passwordC");
        accessTokenA = tokenResponseA.getAccessToken();
        accessTokenB = tokenResponseB.getAccessToken();
        accessTokenC = tokenResponseC.getAccessToken();
    }

    /**
     * Feature: 글 랭킹 조회
     * <p>
     * Scenario: 글을 랭킹 조회 한다.
     * <p>
     * <p>
     * When 모든 지역, 모든 부문에 대해서 전일 랭킹을 조회한다. Then 필터에 대한 전일 랭킹이 조회된다.
     * <p>
     * When 모든 지역, 모든 부문에 대해서 전주 랭킹을 조회한다. Then 필터에 대한 전주 랭킹이 조회된다.
     * <p>
     * When 모든 지역, 모든 부문에 대해서 전달 랭킹을 조회한다. Then 필터에 대한 전달 랭킹이 조회된다.
     * <p>
     * When 모든 지역, 모든 부문에 대해서 누적 랭킹을 조회한다. Then 필터에 대한 누적 랭킹이 조회된다.
     * <p>
     *
     * <p>
     * When 모든 지역, 특정 부문에 대해서 전일 랭킹을 조회한다. Then 필터에 대한 전일 랭킹이 조회된다.
     * <p>
     * When 모든 지역, 특정 부문에 대해서 전주 랭킹을 조회한다. Then 필터에 대한 전주 랭킹이 조회된다.
     * <p>
     * When 모든 지역, 특정 부문에 대해서 전달 랭킹을 조회한다. Then 필터에 대한 전달 랭킹이 조회된다.
     * <p>
     * When 모든 지역, 특정 부문에 대해서 누적 랭킹을 조회한다. Then 필터에 대한 누적 랭킹이 조회된다.
     * <p>
     *
     * <p>
     * When 특정 지역, 모든 부문에 대해서 전일 랭킹을 조회한다. Then 필터에 대한 전일 랭킹이 조회된다.
     * <p>
     * When 특정 지역, 모든 부문에 대해서 전주 랭킹을 조회한다. Then 필터에 대한 전주 랭킹이 조회된다.
     * <p>
     * When 특정 지역, 모든 부문에 대해서 전달 랭킹을 조회한다. Then 필터에 대한 전달 랭킹이 조회된다.
     * <p>
     * When 특정 지역, 모든 부문에 대해서 누적 랭킹을 조회한다. Then 필터에 대한 누적 랭킹이 조회된다.
     * <p>
     *
     * <p>
     * When 특정 지역, 특정 부문에 대해서 전일 랭킹을 조회한다. Then 필터에 대한 전일 랭킹이 조회된다.
     * <p>
     * When 특정 지역, 특정 부문에 대해서 전주 랭킹을 조회한다. Then 필터에 대한 전주 랭킹이 조회된다.
     * <p>
     * When 특정 지역, 특정 부문에 대해서 전달 랭킹을 조회한다. Then 필터에 대한 전달 랭킹이 조회된다.
     * <p>
     * When 특정 지역, 특정 부문에 대해서 누적 랭킹을 조회한다. Then 필터에 대한 누적 랭킹이 조회된다.
     */
    @DisplayName("글 랭킹 조회")
    @Test
    void searchRanking() {
        String filter;

        //랭킹 조회를 위한 글 생성
        makePosts();

        // 모든 지역, 모든 부문 필터 설정
        filter = "areaIds=&sectorIds=";
        // 모든 지역, 모든 부문에 대해서 전일 랭킹을 조회
        searchRanks(accessTokenA, Criteria.LAST_DAY, filter);
        // 모든 지역, 모든 부문에 대해서 전주 랭킹을 조회
        searchRanks(accessTokenA, Criteria.LAST_WEEK, filter);
        // 모든 지역, 모든 부문에 대해서 전달 랭킹을 조회
        searchRanks(accessTokenA, Criteria.LAST_MONTH, filter);
        // 모든 지역, 모든 부문에 대해서 누적 랭킹을 조회
        searchRanks(accessTokenA, Criteria.TOTAL, filter);

        // 모든 지역, 특정 부문 필터 설정
        filter = "areaIds=&sectorIds="+sectorAId;
        // 모든 지역, 특정 부문에 대해서 전일 랭킹을 조회
        searchRanks(accessTokenA, Criteria.LAST_DAY, filter);
        // 모든 지역, 특정 부문에 대해서 전주 랭킹을 조회
        searchRanks(accessTokenA, Criteria.LAST_WEEK, filter);
        // 모든 지역, 특정 부문에 대해서 전달 랭킹을 조회
        searchRanks(accessTokenA, Criteria.LAST_MONTH, filter);
        // 모든 지역, 특정 부문에 대해서 누적 랭킹을 조회
        searchRanks(accessTokenA, Criteria.TOTAL, filter);

        // 특정 지역, 모든 부문 필터 설정
        filter = "areaIds="+TEST_AREA_A_ID+"&sectorIds=";
        // 특정 지역, 모든 부문에 대해서 전일 랭킹을 조회
        searchRanks(accessTokenA, Criteria.LAST_DAY, filter);
        // 특정 지역, 모든 부문에 대해서 전주 랭킹을 조회
        searchRanks(accessTokenA, Criteria.LAST_WEEK, filter);
        // 특정 지역, 모든 부문에 대해서 전달 랭킹을 조회
        searchRanks(accessTokenA, Criteria.LAST_MONTH, filter);
        // 특정 지역, 모든 부문에 대해서 누적 랭킹을 조회
        searchRanks(accessTokenA, Criteria.TOTAL, filter);

        // 특정 지역, 특정 부문 필터 설정
        filter = "areaIds="+TEST_AREA_B_ID+"&sectorIds="+sectorBId;
        // 특정 지역, 특정 부문에 대해서 전일 랭킹을 조회
        searchRanks(accessTokenA, Criteria.LAST_DAY, filter);
        // 특정 지역, 특정 부문에 대해서 전주 랭킹을 조회
        searchRanks(accessTokenA, Criteria.LAST_WEEK, filter);
        // 특정 지역, 특정 부문에 대해서 전달 랭킹을 조회
        searchRanks(accessTokenA, Criteria.LAST_MONTH, filter);
        // 특정 지역, 특정 부문에 대해서 누적 랭킹을 조회
        searchRanks(accessTokenA, Criteria.TOTAL, filter);
    }

    private void makePosts() {
        sectorAId = createSector(adminToken, "TEST_A");
        sectorBId = createSector(adminToken, "TEST_B");

        //좋아요를 3개 받은 TEST_AREA_A 지역 TEST_A 부문 글 생성
        String firstPostLocation = createPostWithoutImageWithAreaAndSector(accessTokenA,
            TEST_AREA_A_ID,
            sectorAId);
        firstPostId = getIdFromUrl(firstPostLocation);
        PostResponse firstPost = findPost(accessTokenA, firstPostId);

        pressPostZzang(accessTokenA, firstPost.getId());
        pressPostZzang(accessTokenB, firstPost.getId());
        pressPostZzang(accessTokenC, firstPost.getId());

        //좋아요를 2개 받은 TEST_AREA_A 지역 TEST_B 부문 글 생성
        String secondPostLocation = createPostWithoutImageWithAreaAndSector(accessTokenA,
            TEST_AREA_A_ID,
            sectorBId);
        secondPostId = getIdFromUrl(secondPostLocation);
        PostResponse secondPost = findPost(accessTokenA, secondPostId);

        pressPostZzang(accessTokenA, secondPost.getId());
        pressPostZzang(accessTokenB, secondPost.getId());

        //좋아요를 1개 받은 TEST_AREA_B 지역 TEST_B 부문 글 생성
        String thirdPostLocation = createPostWithoutImageWithAreaAndSector(accessTokenA,
            TEST_AREA_B_ID,
            sectorBId);
        thirdPostId = getIdFromUrl(thirdPostLocation);
        PostResponse thirdPost = findPost(accessTokenA, thirdPostId);

        pressPostZzang(accessTokenA, thirdPost.getId());

        //좋아요를 0개 받은 TEST_AREA_B 지역 TEST_B 부문 글 생성
        String fourthPostLocation = createPostWithoutImageWithAreaAndSector(accessTokenA,
            TEST_AREA_B_ID,
            sectorBId);
        fourthPostId = getIdFromUrl(fourthPostLocation);
    }

    private Long createSector(String accessToken, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("description", TEST_SECTOR_DESCRIPTION);

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

    private String getCreateAdminToken() {
        createAdmin();
        TokenResponse tokenResponse = login(TEST_ADMIN_EMAIL, TEST_ADMIN_PASSWORD);
        return tokenResponse.getAccessToken();
    }

    private void createAdmin() {
        Map<String, String> params = new HashMap<>();
        params.put("email", TEST_ADMIN_EMAIL);
        params.put("nickname", TEST_ADMIN_NICKNAME);
        params.put("password", TEST_ADMIN_PASSWORD);

        given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/joinAdmin")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }

    private String createPostWithoutImageWithAreaAndSector(String accessToken, Long areaId,
        Long sectorId) {
        return given()
            .log().all()
            .formParam("writing", TEST_POST_WRITING)
            .formParam("areaId", areaId)
            .formParam("sectorId", sectorId)
            .header("X-AUTH-TOKEN", accessToken)
            .config(RestAssuredConfig.config()
                .encoderConfig(encoderConfig().defaultContentCharset("UTF-8")))
            .when()
            .post("/posts")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");
    }

    private List<RankingResponse> searchRanks(String accessToken, Criteria criteria,
        String filter) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/ranks/" + criteria.getCriteriaName() + "&" + filter)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList(".", RankingResponse.class);
    }

    private void pressPostZzang(String accessToken, Long postId) {
        given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .post("/posts/" + postId + "/zzangs")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private PostResponse findPost(String accessToken, Long id) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/posts/" + id)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(PostResponse.class);
    }
}
