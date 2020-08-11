package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.assertj.core.api.Assertions.assertThat;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/init-table.sql")
public class CommentAcceptanceTest {

    @LocalServerPort
    public int port;

    private String accessToken;
    private Long postId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        // 로그인
        createUser(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        TokenResponse tokenResponse = login(TEST_EMAIL, TEST_PASSWORD);
        accessToken = tokenResponse.getAccessToken();

        Long sectorId = createSector();
        String postLocation = createPostWithoutImage(accessToken, sectorId);
        postId = getIdFromUrl(postLocation);
    }

    /**
     * Feature: 댓글 관리
     * <p>
     * Scenario: 댓글을 관리한다.
     * <p>
     * When 댓글을 등록한다. Then 댓글이 등록되었다.
     * <p>
     * When 댓글 목록을 조회한다. Then 댓글 목록을 응답받는다. And 댓글 목록은 n개이다.
     * <p>
     * When 댓글을 조회한다. Then 글을 응답 받는다.
     * <p>
     * When 댓글을 삭제한다. Then 댓글이 삭제 상태로 변경된다. And 다시 댓글을 조회할 수 없다. And 댓글 목록은 n-1개이다.
     */
    @DisplayName("댓글 관리")
    @Test
    void manageComment() {
        // 댓글 등록 및 조회
        String commentLocation = createComment(postId, accessToken);
        Long commentId = getIdFromUrl(commentLocation);

        CommentResponse commentResponse = findComment(postId, commentId, accessToken);

        assertThat(commentResponse.getId()).isEqualTo(commentId);
        assertThat(commentResponse.getWriting()).isEqualTo(TEST_WRITING);

        // 댓글 목록 조회
        List<CommentResponse> commentResponses = findAllComment(postId, accessToken);
        assertThat(commentResponses).hasSize(1);

        // 삭제
        deleteComment(postId, commentId, accessToken);
        findNotExistsComment(postId, commentId, accessToken);

        List<CommentResponse> reFoundPostResponses = findAllComment(postId, accessToken);

        assertThat(reFoundPostResponses).hasSize(0);
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

    private List<CommentResponse> findAllComment(Long postId, String accessToken) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/posts/" + postId + "/comments")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList(".", CommentResponse.class);
    }

    private void findNotExistsComment(Long postId, Long commentId, String accessToken) {
        given()
            .header("X-AUTH-TOKEN", accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/posts/" + postId + "/comments/" + commentId)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private void deleteComment(Long postId, Long commentId, String accessToken) {
        given()
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .delete("/posts/" + postId + "/comments/" + commentId)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }


    private Long getIdFromUrl(String location) {
        int lastIndex = location.lastIndexOf("/");
        return Long.valueOf(location.substring(lastIndex + 1));
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

    private String createPostWithoutImage(String accessToken, Long sectorId) {
        return given()
            .log().all()
            .formParam("writing", TEST_WRITING)
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

    private String createComment(Long postId, String accessToken) {
        Map<String, String> params = new HashMap<>();
        params.put("writing", TEST_WRITING);

        return given()
            .log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .post("/posts/" + postId + "/comments")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");
    }

    private CommentResponse findComment(Long postId, Long commentId, String accessToken) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/posts/" + postId + "/comments/" + commentId)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(CommentResponse.class);
    }
}
