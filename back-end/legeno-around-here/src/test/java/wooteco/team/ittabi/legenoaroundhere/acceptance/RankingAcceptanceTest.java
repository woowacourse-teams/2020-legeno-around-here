package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_A_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_B_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_EMPTY_IMAGES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_PASSWORD;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_RANKING_EMAIL_A;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_RANKING_EMAIL_B;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_RANKING_EMAIL_C;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
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
import wooteco.team.ittabi.legenoaroundhere.domain.ranking.RankingCriteria;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostWithCommentsCountResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

public class RankingAcceptanceTest extends AcceptanceTest {

    private String adminToken;
    private String accessToken;
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
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        accessToken = tokenResponse.getAccessToken();

        adminToken = getCreateAdminToken();

        TokenResponse tokenResponseA = login(TEST_RANKING_EMAIL_A, TEST_USER_PASSWORD);
        TokenResponse tokenResponseB = login(TEST_RANKING_EMAIL_B, TEST_USER_PASSWORD);
        TokenResponse tokenResponseC = login(TEST_RANKING_EMAIL_C, TEST_USER_PASSWORD);
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
        List<PostWithCommentsCountResponse> rankingPostResponse;

        //랭킹 조회를 위한 글 생성
        makePosts();

        // 모든 지역, 모든 부문 필터 설정
        filter = "areaId=&sectorIds=";
        // 모든 지역, 모든 부문에 대해서 랭킹을 조회
        rankingPostResponse = searchRanking(accessToken, RankingCriteria.TOTAL, filter);
        assertThat(rankingPostResponse).hasSize(4);
        assertThat(rankingPostResponse.get(0).getId()).isEqualTo(firstPostId);
        assertThat(rankingPostResponse.get(0).getZzang().getCount()).isEqualTo(3);
        assertThat(rankingPostResponse.get(1).getId()).isEqualTo(secondPostId);
        assertThat(rankingPostResponse.get(1).getZzang().getCount()).isEqualTo(2);
        assertThat(rankingPostResponse.get(2).getId()).isEqualTo(thirdPostId);
        assertThat(rankingPostResponse.get(2).getZzang().getCount()).isEqualTo(1);
        assertThat(rankingPostResponse.get(3).getId()).isEqualTo(fourthPostId);
        assertThat(rankingPostResponse.get(3).getZzang().getCount()).isEqualTo(0);

        // 모든 지역, 특정 부문 필터 설정
        filter = "areaId=&sectorIds=" + sectorAId;
        // 모든 지역, 특정 부문에 대해서 랭킹을 조회
        rankingPostResponse = searchRanking(accessToken, RankingCriteria.TOTAL, filter);
        assertThat(rankingPostResponse).hasSize(2);
        assertThat(rankingPostResponse.get(0).getId()).isEqualTo(firstPostId);
        assertThat(rankingPostResponse.get(0).getZzang().getCount()).isEqualTo(3);
        assertThat(rankingPostResponse.get(1).getId()).isEqualTo(thirdPostId);
        assertThat(rankingPostResponse.get(1).getZzang().getCount()).isEqualTo(1);

        // 특정 지역, 모든 부문 필터 설정
        filter = "areaId=" + TEST_AREA_A_ID + "&sectorIds=";
        // 특정 지역, 모든 부문에 대해서 랭킹을 조회
        rankingPostResponse = searchRanking(accessToken, RankingCriteria.TOTAL, filter);
        assertThat(rankingPostResponse).hasSize(2);
        assertThat(rankingPostResponse.get(0).getId()).isEqualTo(firstPostId);
        assertThat(rankingPostResponse.get(0).getZzang().getCount()).isEqualTo(3);
        assertThat(rankingPostResponse.get(1).getId()).isEqualTo(secondPostId);
        assertThat(rankingPostResponse.get(1).getZzang().getCount()).isEqualTo(2);

        // 특정 지역, 특정 부문 필터 설정
        filter = "areaId=" + TEST_AREA_B_ID + "&sectorIds=" + sectorBId;
        // 특정 지역, 특정 부문에 대해서 랭킹을 조회
        rankingPostResponse = searchRanking(accessToken, RankingCriteria.TOTAL, filter);
        assertThat(rankingPostResponse).hasSize(1);
        assertThat(rankingPostResponse.get(0).getId()).isEqualTo(fourthPostId);
        assertThat(rankingPostResponse.get(0).getZzang().getCount()).isEqualTo(0);
    }

    private void makePosts() {
        sectorAId = createSector(adminToken, "TEST_A");
        sectorBId = createSector(adminToken, "TEST_B");

        //좋아요를 3개 받은 TEST_AREA_A 지역 TEST_A 부문 글 생성
        String firstPostLocation
            = createPostWithoutImageWithAreaAndSector(accessToken, TEST_AREA_A_ID, sectorAId);
        firstPostId = getIdFromUrl(firstPostLocation);
        PostResponse firstPost = findPost(accessToken, firstPostId);

        pressPostZzang(accessTokenA, firstPost.getId());
        pressPostZzang(accessTokenB, firstPost.getId());
        pressPostZzang(accessTokenC, firstPost.getId());

        //좋아요를 2개 받은 TEST_AREA_A 지역 TEST_B 부문 글 생성
        String secondPostLocation = createPostWithoutImageWithAreaAndSector(accessToken,
            TEST_AREA_A_ID,
            sectorBId);
        secondPostId = getIdFromUrl(secondPostLocation);
        PostResponse secondPost = findPost(accessToken, secondPostId);

        pressPostZzang(accessTokenA, secondPost.getId());
        pressPostZzang(accessTokenB, secondPost.getId());

        //좋아요를 1개 받은 TEST_AREA_B 지역 TEST_A 부문 글 생성
        String thirdPostLocation = createPostWithoutImageWithAreaAndSector(accessToken,
            TEST_AREA_B_ID, sectorAId);
        thirdPostId = getIdFromUrl(thirdPostLocation);
        PostResponse thirdPost = findPost(accessToken, thirdPostId);

        pressPostZzang(accessTokenA, thirdPost.getId());

        //좋아요를 0개 받은 TEST_AREA_B 지역 TEST_B 부문 글 생성
        String fourthPostLocation = createPostWithoutImageWithAreaAndSector(accessToken,
            TEST_AREA_B_ID, sectorBId);
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
        TokenResponse tokenResponse = login(TEST_ADMIN_EMAIL, TEST_ADMIN_PASSWORD);
        return tokenResponse.getAccessToken();
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

    private String createPostWithoutImageWithAreaAndSector(String accessToken, Long areaId,
        Long sectorId) {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, areaId, sectorId);

        return given()
            .log().all()
            .header("X-AUTH-TOKEN", accessToken)
            .body(postCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/posts")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");
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

    private List<PostWithCommentsCountResponse> searchRanking(String accessToken,
        RankingCriteria rankingCriteria,
        String filter) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/ranking?page=0&size=50&sortedBy=id&direction=asc&criteria="
                + rankingCriteria.getCriteriaName() + "&" + filter)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList("content", PostWithCommentsCountResponse.class);
    }
}
