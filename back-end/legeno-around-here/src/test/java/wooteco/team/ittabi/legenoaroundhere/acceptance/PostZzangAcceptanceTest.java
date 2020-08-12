package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
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
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.team.ittabi.legenoaroundhere.domain.post.zzang.ZzangState;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostZzangResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

public class PostZzangAcceptanceTest extends AcceptanceTest {

    private String accessToken;
    private Long postId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        // 로그인
        createUser(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        TokenResponse tokenResponse = login(TEST_EMAIL, TEST_PASSWORD);
        accessToken = tokenResponse.getAccessToken();
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
    void managePostZzang() {
        // 이미지가 포함되지 않은 글 등록
        Long sectorId = createSector();
        String postLocation = createPostWithoutImage(accessToken, sectorId);
        postId = getIdFromUrl(postLocation);
        PostResponse postResponse = findPost(postId, accessToken);

        // 글 생성 시 초기 글 좋아요 수
        assertThat(postResponse.getPostZzangResponse().getPostZzangCount()).isEqualTo(0L);

        // 비활성화된 좋아요를 활성화
        PostZzangResponse activatedPostZzangResponse = pressPostZzang(postResponse.getId(),
            accessToken);

        assertThat(activatedPostZzangResponse.getPostZzangCount()).isEqualTo(1L);
        assertThat(activatedPostZzangResponse.getZzangState())
            .isEqualTo(ZzangState.ACTIVATED.name());

        // 활성화된 좋아요를 비활성화
        PostZzangResponse inactivatedPostZzangResponse = pressPostZzang(postResponse.getId(),
            accessToken);

        assertThat(inactivatedPostZzangResponse.getPostZzangCount()).isEqualTo(0L);
        assertThat(inactivatedPostZzangResponse.getZzangState())
            .isEqualTo(ZzangState.INACTIVATED.name());

        // 비활성화된 좋아요를 다시 활성화
        PostZzangResponse againActivatedPostZzangResponse = pressPostZzang(postResponse.getId(),
            accessToken);

        assertThat(againActivatedPostZzangResponse.getPostZzangCount()).isEqualTo(1L);
        assertThat(againActivatedPostZzangResponse.getZzangState())
            .isEqualTo(ZzangState.ACTIVATED.name());
    }

    private PostZzangResponse pressPostZzang(Long postId, String accessToken) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/posts/" + postId + "/likes")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(PostZzangResponse.class);
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
}

