package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_DIR;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_PASSWORD;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_PASSWORD;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import java.io.File;
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
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostWithCommentsCountResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

public class PostAcceptanceTest extends AcceptanceTest {

    private String accessToken;
    private Long sectorId;
    private SectorResponse sector;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        createUser(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        TokenResponse tokenResponse = login(TEST_EMAIL, TEST_PASSWORD);
        accessToken = tokenResponse.getAccessToken();

        sectorId = createSector();
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

        PostResponse postWithoutImageResponse = findPost(postWithoutImageId, accessToken);

        assertThat(postWithoutImageResponse.getId()).isEqualTo(postWithoutImageId);
        assertThat(postWithoutImageResponse.getWriting()).isEqualTo(TEST_WRITING);
        assertThat(postWithoutImageResponse.getArea().getId()).isEqualTo(TEST_AREA_ID);
        assertThat(postWithoutImageResponse.getSector()).isEqualTo(sector);

        // 이미지가 포함된 글 등록
        String postWithImageLocation = createPostWithImage(accessToken);
        Long postWithImageId = getIdFromUrl(postWithImageLocation);

        PostResponse postWithImageResponse = findPost(postWithImageId, accessToken);

        assertThat(postWithImageResponse.getId()).isEqualTo(postWithImageId);
        assertThat(postWithImageResponse.getWriting()).isEqualTo(TEST_WRITING);
        assertThat(postWithImageResponse.getImages()).hasSize(2);
        assertThat(postWithoutImageResponse.getArea().getId()).isEqualTo(TEST_AREA_ID);
        assertThat(postWithoutImageResponse.getSector()).isEqualTo(sector);

        // 목록 조회
        List<PostWithCommentsCountResponse> postResponses = findAllPost(accessToken);
        assertThat(postResponses).hasSize(2);

        // 수정
        String updatedWriting = "BingBong and Jamie";
        updatePost(postWithoutImageId, updatedWriting, accessToken);

        PostResponse updatedPostResponse = findPost(postWithoutImageId, accessToken);

        assertThat(updatedPostResponse.getId()).isEqualTo(postWithoutImageId);
        assertThat(updatedPostResponse.getWriting()).isEqualTo(updatedWriting);

        // 조회
        PostResponse postFindResponse = findPost(postWithoutImageId, accessToken);

        assertThat(postFindResponse.getId()).isEqualTo(postWithoutImageId);
        assertThat(postFindResponse.getWriting()).isEqualTo(updatedWriting);

        // 삭제
        deletePost(postWithoutImageId, accessToken);
        findNotExistsPost(postWithoutImageId, accessToken);

        List<PostWithCommentsCountResponse> foundPostResponses = findAllPost(accessToken);

        assertThat(foundPostResponses).hasSize(1);
    }

    /**
     * Feature: 글 조회
     * <p>
     * Scenario: 글 페이징 조회한다.
     * <p>
     * Given 관리자가 로그인 되어있다. 글 100개가 등록되어 있다.
     * <p>
     * When 글 1Page 20Size를 정렬(기준:id 방향:오름차순) 조회한다. Then 1~20까지의 Post가 조회된다.
     * <p>
     * When 글 1Page 20Size를 정렬(기준:id 방향:내림차순) 조회한다. Then 100~81까지의 Post가 조회된다.
     * <p>
     * When 글 2Page 40Size를 정렬(기준:id 방향:오름차순) 조회한다. Then 21~40까지의 Post가 조회된다.
     * <p>
     * When 글 -1Page -1Size를 정렬(기준:id 방향:abc) 조회한다. Then 1의 Post가 조회된다. (기본 값 : 1Page, 1Size,
     * 방향:오름차순)
     * <p>
     * When 글 1Page 20Size를 정렬(기준:test-id 방향:오름차순) 조회한다. Then BadRequest가 발생한다.
     */
    @DisplayName("글 페이징 조회")
    @Test
    void pagingFindPost() {
        List<Long> ids = new ArrayList<>();
        // 글 100개 등록
        for (int i = 0; i < 100; i++) {
            String postWithoutImageLocation = createPostWithoutImage(accessToken);
            ids.add(getIdFromUrl(postWithoutImageLocation));
        }

        // 글 1Page 20Size를 정렬(기준:id 방향:오름차순) 조회
        List<PostWithCommentsCountResponse> posts
            = findAllPost(accessToken, "page=1&size=20&sortedBy=id&direction=asc");
        assertThat(posts).hasSize(20);
        List<Long> expectedIds = ids.subList(0, 20);
        assertThat(getPostIds(posts)).isEqualTo(expectedIds);

        // 글 1Page 20Size를 정렬(기준:id 방향:내림차순) 조회
        posts = findAllPost(accessToken, "page=1&size=20&sortedBy=id&direction=desc");
        assertThat(posts).hasSize(20);
        expectedIds = ids.subList(80, 100);
        Collections.reverse(expectedIds);
        assertThat(getPostIds(posts)).isEqualTo(expectedIds);

        // 글 2Page 20Size를 정렬(기준:id 방향:오름차순) 조회
        posts = findAllPost(accessToken, "page=2&size=20&sortedBy=id&direction=asc");
        assertThat(posts).hasSize(20);
        expectedIds = ids.subList(20, 40);
        assertThat(getPostIds(posts)).isEqualTo(expectedIds);
    }

    private List<Long> getPostIds(List<PostWithCommentsCountResponse> posts) {
        return posts.stream()
            .map(PostWithCommentsCountResponse::getId)
            .collect(Collectors.toList());
    }

    private Long createSector() {
        String accessToken = getCreateAdminToken();

        Map<String, String> params = new HashMap<>();
        params.put("name", TEST_SECTOR_NAME);
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

    private List<PostWithCommentsCountResponse> findAllPost(String accessToken) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/posts?page=1&size=50&sortedBy=id&direction=asc")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList("content", PostWithCommentsCountResponse.class);
    }

    private List<PostWithCommentsCountResponse> findAllPost(String accessToken, String parameter) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/posts?" + parameter)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList("content", PostWithCommentsCountResponse.class);
    }

    private PostResponse findPost(Long id, String accessToken) {
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
            .log().all()
            .formParam("writing", TEST_WRITING)
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

    private String createPostWithImage(String accessToken) {
        // TODO: 2020/07/28 이미지를 포함했을 때 한글이 안 나오는 문제

        return given()
            .log().all()
            .formParam("writing", TEST_WRITING)
            .multiPart("images", new File(TEST_IMAGE_DIR + "right_image1.jpg"))
            .multiPart("images", new File(TEST_IMAGE_DIR + "right_image2.jpg"))
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
}
