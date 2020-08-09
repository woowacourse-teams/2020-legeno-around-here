package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostTestConstants.TEST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_PASSWORD;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.team.ittabi.legenoaroundhere.domain.State;
import wooteco.team.ittabi.legenoaroundhere.dto.LikeResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/init-table.sql")
public class LikeAcceptanceTest {

    @LocalServerPort
    public int port;

    static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * Feature: 글의 좋아요 기능
     * <p>
     * Scenario: 좋아요 버튼을 누르고, 좋아요 수를 표시한다.
     * <p>
     * When 글을 등록한다. Then 좋아요 수가 0인 글이 등록되었다.
     * <p>
     * When 좋아요 버튼을 누른다. Then 글의 좋아요 버튼을 누른 상태가 됐다.
     * <p>
     * When 좋아요 버튼이 눌러진 상태에서 다시 좋아요 버튼을 누른다. Then 글의 좋아요 버튼을 누른 상태가 해제됐다.
     * <p>
     * When 글을 조회하면 해당 글의 좋아요 수가 표시된다.
     */

    @DisplayName("글의 좋아요 관리")
    @Test
    void manageLike() {
        createUser(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        TokenResponse tokenResponse = login(TEST_EMAIL, TEST_PASSWORD);
        String accessToken = tokenResponse.getAccessToken();

        // 이미지가 포함되지 않은 글 등록
        String postLocation = createPostWithoutImage(accessToken);
        Long postLocationId = getIdFromUrl(postLocation);
        PostResponse postResponse = findPost(postLocationId, accessToken);

        // 글 생성 시 초기 글 좋아요 수
        assertThat(postResponse.getLikeResponse().getLikeCount()).isEqualTo(0L);

        // 비활성화된 좋아요를 활성화
        LikeResponse activatedLikeResponse = pressLike(postResponse.getId(), accessToken);

        assertThat(activatedLikeResponse.getLikeCount()).isEqualTo(1L);
        assertThat(activatedLikeResponse.getState()).isEqualTo(State.ACTIVATED);

        // 활성화된 좋아요를 비활성화
        LikeResponse inactivatedLikeResponse = pressLike(postResponse.getId(), accessToken);

        assertThat(inactivatedLikeResponse.getLikeCount()).isEqualTo(0L);
        assertThat(inactivatedLikeResponse.getState()).isEqualTo(State.INACTIVATED);
    }

    private LikeResponse pressLike(Long postId, String accessToken) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/posts/" + postId + "/likes")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(LikeResponse.class);

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

    private Long getIdFromUrl(String location) {
        int lastIndex = location.lastIndexOf("/");
        return Long.valueOf(location.substring(lastIndex + 1));
    }

    private String createPostWithoutImage(String accessToken) {

        return given()
            .log().all()
            .formParam("writing", TEST_WRITING)
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
}
