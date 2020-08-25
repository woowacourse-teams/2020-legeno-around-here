package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_OTHER_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_SUB_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_DIR;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_OTHER_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_REPORT_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_PASSWORD;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.team.ittabi.legenoaroundhere.dto.PostReportCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostReportResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostWithCommentsCountResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

public class PostAcceptanceTest extends AcceptanceTest {

    private String adminToken;
    private String accessToken;
    private Long sectorId;
    private SectorResponse sector;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        accessToken = tokenResponse.getAccessToken();

        adminToken = getCreateAdminToken();
        sectorId = createSector(adminToken, TEST_SECTOR_NAME);
        sector = findAvailableSector(accessToken, sectorId);
    }

    /**
     * Feature: 글 관리
     * <p>
     * Scenario: 글을 관리한다.
     * <p>
     * When 글을 등록한다. Then 글이 등록되었다.
     * <p>
     * When 글 목록을 조회한다. Then 글 목록을 응답받는다. And 글 목록은 n개이다.
     * <p>
     * When 글을 조회한다. Then 글을 응답 받는다.
     * <p>
     * When 글을 수정한다. Then 글이 수정되었다.
     * <p>
     * When 글을 삭제한다. Then 글이 삭제 상태로 변경된다. And 다시 글을 조회할 수 없다. And 글 목록은 n-1개이다.
     */
    @DisplayName("글 관리")
    @Test
    void manageMyPost() {
        // 이미지가 포함되지 않은 글 등록
        String postWithoutImageLocation = createPostWithoutImage(accessToken);
        Long postWithoutImageId = getIdFromUrl(postWithoutImageLocation);

        PostResponse postWithoutImageResponse = findPost(accessToken, postWithoutImageId);

        assertThat(postWithoutImageResponse.getId()).isEqualTo(postWithoutImageId);
        assertThat(postWithoutImageResponse.getWriting()).isEqualTo(TEST_POST_WRITING);
        assertThat(postWithoutImageResponse.getArea().getId()).isEqualTo(TEST_AREA_ID);
        assertThat(postWithoutImageResponse.getSector()).isEqualTo(sector);

        // 이미지가 포함된 글 등록
        String postWithImageLocation = createPostWithImage(accessToken);
        Long postWithImageId = getIdFromUrl(postWithImageLocation);

        PostResponse postWithImageResponse = findPost(accessToken, postWithImageId);

        assertThat(postWithImageResponse.getId()).isEqualTo(postWithImageId);
        assertThat(postWithImageResponse.getWriting()).isEqualTo(TEST_POST_WRITING);
        assertThat(postWithImageResponse.getImages()).hasSize(2);
        assertThat(postWithoutImageResponse.getArea().getId()).isEqualTo(TEST_AREA_ID);
        assertThat(postWithoutImageResponse.getSector()).isEqualTo(sector);

        // 목록 조회
        List<PostWithCommentsCountResponse> postResponses = searchAllPost(accessToken);
        assertThat(postResponses).hasSize(2);

        // 수정
        String updatedWriting = "BingBong and Jamie";
        updatePost(postWithoutImageId, updatedWriting, accessToken);

        PostResponse updatedPostResponse = findPost(accessToken, postWithoutImageId);

        assertThat(updatedPostResponse.getId()).isEqualTo(postWithoutImageId);
        assertThat(updatedPostResponse.getWriting()).isEqualTo(updatedWriting);

        // 조회
        PostResponse postFindResponse = findPost(accessToken, postWithoutImageId);

        assertThat(postFindResponse.getId()).isEqualTo(postWithoutImageId);
        assertThat(postFindResponse.getWriting()).isEqualTo(updatedWriting);

        // 삭제
        deletePost(postWithoutImageId, accessToken);
        findNotExistsPost(postWithoutImageId, accessToken);

        List<PostWithCommentsCountResponse> foundPostResponses = searchAllPost(accessToken);

        assertThat(foundPostResponses).hasSize(1);
    }

    /**
     * Feature: 글 필터 조회
     * <p>
     * Scenario: 글을 필터 조회 한다.
     * <p>
     * Given 글들이 등록되어 있다.
     * <p>
     * When 글을 areaId / sectorIds 없이 조회한다. Then 글이 전체 조회되었다.
     * <p>
     * When 글을 areaId / sectorIds 값 없이 조회한다. Then 글이 전체 조회되었다.
     * <p>
     * When 글을 areaId만 포함하여 조회한다. Then areaId에 해당하는(하위지역 포함) 글들만 조회되었다.
     * <p>
     * When 글을 sectorIds만 포함하여 조회한다. Then sectorIds에 해당하는 글들만 조회되었다.
     * <p>
     * When 글을 areaId, sectorIds를 포함하여 조회한다. Then areaId, sectorIds에 해당하는(하위지역 포함) 글들만 조회되었다.
     */
    @DisplayName("글 필터 조회")
    @Test
    void searchPost() {
        Long sectorAId = createSector(adminToken, "TEST_A");
        Long sectorBId = createSector(adminToken, "TEST_B");
        Long sectorCId = createSector(adminToken, "TEST_C");

        createPostWithoutImageWithAreaAndSector(accessToken, TEST_AREA_ID, sectorAId);
        createPostWithoutImageWithAreaAndSector(accessToken, TEST_AREA_ID, sectorBId);
        createPostWithoutImageWithAreaAndSector(accessToken, TEST_AREA_ID, sectorBId);
        createPostWithoutImageWithAreaAndSector(accessToken, TEST_AREA_ID, sectorCId);
        createPostWithoutImageWithAreaAndSector(accessToken, TEST_AREA_SUB_ID, sectorCId);
        createPostWithoutImageWithAreaAndSector(accessToken, TEST_AREA_SUB_ID, sectorCId);
        createPostWithoutImageWithAreaAndSector(accessToken, TEST_AREA_OTHER_ID, sectorAId);
        createPostWithoutImageWithAreaAndSector(accessToken, TEST_AREA_OTHER_ID, sectorAId);
        createPostWithoutImageWithAreaAndSector(accessToken, TEST_AREA_OTHER_ID, sectorBId);
        createPostWithoutImageWithAreaAndSector(accessToken, TEST_AREA_OTHER_ID, sectorBId);
        createPostWithoutImageWithAreaAndSector(accessToken, TEST_AREA_OTHER_ID, sectorBId);
        createPostWithoutImageWithAreaAndSector(accessToken, TEST_AREA_OTHER_ID, sectorCId);
        createPostWithoutImageWithAreaAndSector(accessToken, TEST_AREA_OTHER_ID, sectorCId);
        createPostWithoutImageWithAreaAndSector(accessToken, TEST_AREA_OTHER_ID, sectorCId);
        createPostWithoutImageWithAreaAndSector(accessToken, TEST_AREA_OTHER_ID, sectorCId);

        // 글을 areaId / sectorIds 없이 조회 - 전체
        List<PostWithCommentsCountResponse> posts = searchAllPost(accessToken);
        assertThat(posts).hasSize(15);

        // 글을 areaId / sectorIds 값 없이 조회 - 전체
        posts = searchAllPostWithFilter(accessToken, "areaId=&sectorIds=");
        assertThat(posts).hasSize(15);

        // 글을 areaId만 포함하여 조회 - 하위지역 포함 조회
        posts = searchAllPostWithFilter(accessToken, "areaId=" + TEST_AREA_ID);
        assertThat(posts).hasSize(6);

        // 글을 sectorIds만 포함하여 조회
        posts = searchAllPostWithFilter(accessToken, "sectorIds=" + sectorAId + "," + sectorBId);
        assertThat(posts).hasSize(8);

        // 글을 areaId, sectorIds를 포함하여 조회
        posts = searchAllPostWithFilter(accessToken,
            "areaId=" + TEST_AREA_ID + "&sectorIds=" + sectorAId + "," + sectorBId);
        assertThat(posts).hasSize(3);
    }

    /**
     * Feature: 글의 좋아요 기능
     * <p>
     * Scenario: 좋아요 버튼을 누르고, 좋아요 수를 표시한다.
     * <p>
     * When 글을 등록한다. Then 좋아요 수가 0인 글이 등록되었다. 글의 짱을 누르지 않은 상태이다.
     * <p>
     * When 좋아요 버튼을 누른다. Then 성공했다. 글의 짱을 누른 상태이다.
     * <p>
     * When 좋아요 버튼이 눌러진 상태에서 다시 좋아요 버튼을 누른다. Then 성공했다. 글의 짱을 누르지 않은 상태이다.
     */
    @DisplayName("글의 좋아요 관리")
    @Test
    void managePostZzang() {
        // 이미지가 포함되지 않은 글 등록
        String postLocation = createPostWithoutImage(accessToken);
        Long postId = getIdFromUrl(postLocation);
        PostResponse post = findPost(accessToken, postId);

        // 글 생성 시 초기 글 좋아요 수
        assertThat(post.getZzang().getCount()).isEqualTo(0L);
        assertThat(post.getZzang().isActivated()).isFalse();

        // 비활성화된 좋아요를 활성화
        pressPostZzang(accessToken, post.getId());
        post = findPost(accessToken, postId);

        assertThat(post.getZzang().getCount()).isEqualTo(1L);
        assertThat(post.getZzang().isActivated()).isTrue();

        // 활성화된 좋아요를 비활성화
        pressPostZzang(accessToken, post.getId());
        post = findPost(accessToken, postId);

        assertThat(post.getZzang().getCount()).isEqualTo(0L);
        assertThat(post.getZzang().isActivated()).isFalse();

        // 비활성화된 좋아요를 다시 활성화
        pressPostZzang(accessToken, post.getId());
        post = findPost(accessToken, postId);

        assertThat(post.getZzang().getCount()).isEqualTo(1L);
        assertThat(post.getZzang().isActivated()).isTrue();
    }

    /**
     * Feature: 글의 신고 기능
     * <p>
     * Scenario: 신고 버튼을 누르고, 신고 내용을 작성한다.
     * <p>
     * When 글을 등록한다. Then 글이 등록되었다.
     * <p>
     * When 신고 버튼을 누르고 신고 내용을 작성한다. Then 글을 신고하는데 성공했다.
     * <p>
     * When 관리자가 신고 내용을 조회한다. Then 작성했던 신고글이 조회된다.
     * <p>
     * When 관리자가 해당 신고 내용을 삭제한다. Then 작성했던 신고글이 삭제된다.
     */
    @DisplayName("글의 신고 관리")
    @Test
    void managePostReport() {
        String postLocation = createPostWithoutImage(accessToken);
        Long postId = getIdFromUrl(postLocation);

        // 해당 글을 유저가 신고
        String reportLocation = createReport(accessToken, postId);
        Long postReportId = getIdFromUrl(reportLocation);

        // 관리자가 해당 신고 글을 조회
        PostReportResponse postReportResponse = findPostReport(adminToken, postReportId);
        assertThat(postReportResponse.getWriting()).isEqualTo(TEST_POST_REPORT_WRITING);

        // 관리자가 페이지 별 신고 글을 조회
        List<PostReportResponse> postReportResponses = findPostReportByPage(adminToken);
        assertThat(postReportResponses).hasSize(1);

    }

    private List<PostReportResponse> findPostReportByPage(String accessToken) {
        return findPostReportByPageWithParameter(accessToken,
            "page=0&size=10&sortedBy=id&direction=asc");
    }

    private List<PostReportResponse> findPostReportByPageWithParameter(String adminToken,
        String parameter) {
        return given()
            .header("X-AUTH-TOKEN", adminToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/admin/reports?" + parameter)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList("content", PostReportResponse.class);
    }

    private PostReportResponse findPostReport(String adminToken, Long postReportId) {

        return given()
            .header("X-AUTH-TOKEN", adminToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/admin/reports/" + postReportId)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(PostReportResponse.class);
    }

    private String createReport(String accessToken, Long postId) {
        PostReportCreateRequest postReportCreateRequest = new PostReportCreateRequest(
            TEST_POST_REPORT_WRITING);

        return given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(postReportCreateRequest)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .post("/posts/" + postId + "/reports")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");
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

    private void findNotExistsPost(Long id, String accessToken) {
        given()
            .header("X-AUTH-TOKEN", accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/posts/" + id)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private void deletePost(Long id, String accessToken) {
        given()
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .delete("/posts/" + id)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private void updatePost(Long id, String writing, String accessToken) {
        Map<String, String> params = new HashMap<>();
        params.put("writing", writing);

        given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .put("/posts/" + id)
            .then()
            .statusCode(HttpStatus.OK.value());
    }

    private List<PostWithCommentsCountResponse> searchAllPost(String accessToken) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/posts?page=0&size=50&sortedBy=id&direction=asc")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList("content", PostWithCommentsCountResponse.class);
    }

    private List<PostWithCommentsCountResponse> searchAllPostWithFilter(String accessToken,
        String filter) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/posts?page=0&size=50&sortedBy=id&direction=asc&" + filter)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList("content", PostWithCommentsCountResponse.class);
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

    private String createPostWithoutImage(String accessToken) {
        return given()
            .formParam("writing", TEST_POST_WRITING)
            .formParam("areaId", TEST_AREA_ID)
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

    private String createPostWithoutImageWithAreaAndSector(String accessToken, Long areaId,
        Long sectorId) {
        return given()
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

    private String createPostWithImage(String accessToken) {
        // TODO: 2020/07/28 이미지를 포함했을 때 한글이 안 나오는 문제

        return given()
            .formParam("writing", TEST_POST_WRITING)
            .multiPart("images", new File(TEST_IMAGE_DIR + TEST_IMAGE_NAME))
            .multiPart("images", new File(TEST_IMAGE_DIR + TEST_IMAGE_OTHER_NAME))
            .formParam("areaId", TEST_AREA_ID)
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

    private void pressPostZzang(String accessToken, Long postId) {
        given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .post("/posts/" + postId + "/zzangs")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
