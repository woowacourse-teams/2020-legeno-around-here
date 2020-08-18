package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_DIR;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_OTHER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;

public class UserAcceptanceTest extends AcceptanceTest {

    private static final String USER_LOCATION_FORMAT = "^/users/[1-9][0-9]*$";
    private static final int TOKEN_MIN_SIZE = 1;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * Feature: 회원관리 Scenario: 회원을 관리한다.
     * <p>
     * When 회원 가입 요청을 한다. Then 회원으로 가입이되었다.
     * <p>
     * When 로그인을 한다. Then 로그인이 되었다.
     * <p>
     * When 내 정보를 조회한다. Then 내 정보가 조회된다.
     * <p>
     * When 내 정보를 수정한다. Then 내 정보가 수정된다.
     * <p>
     * Given 로그인이 되어있는 상태이다. * When 회원 탈퇴 요청을 한다. Then 회원 탈퇴가 되었다.
     */
    @Test
    @DisplayName("회원 관리")
    void manageUser() {
        //회원 가입
        String location = createUserWithoutArea(TEST_USER_EMAIL, TEST_USER_NICKNAME,
            TEST_USER_PASSWORD);
        assertThat(location).matches(USER_LOCATION_FORMAT);
        TokenResponse joinedTokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        UserResponse joinedUserResponse = findUser(joinedTokenResponse.getAccessToken());
        assertThat(joinedUserResponse).isNotNull();
        assertThat(joinedUserResponse.getId()).isNotNull();
        assertThat(joinedUserResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(joinedUserResponse.getNickname()).isEqualTo(TEST_USER_NICKNAME);

        // 로그인
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken())
            .hasSizeGreaterThanOrEqualTo(TOKEN_MIN_SIZE);

        // 내 정보 조회
        UserResponse userResponse = findUser(tokenResponse.getAccessToken());
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(userResponse.getNickname()).isEqualTo(TEST_USER_NICKNAME);

        // 내 정보 수정
        updateUserWithoutAreaAndImage(tokenResponse.getAccessToken(), "newname", "newpassword");
        UserResponse updatedUserResponse = findUser(tokenResponse.getAccessToken());
        assertThat(updatedUserResponse.getNickname()).isEqualTo("newname");
        TokenResponse updatedTokenResponse = login(TEST_USER_EMAIL, "newpassword");
        assertThat(updatedTokenResponse).isNotNull();
        assertThat(updatedTokenResponse.getAccessToken())
            .hasSizeGreaterThanOrEqualTo(TOKEN_MIN_SIZE);

        // 회원 탈퇴
        deleteUser(updatedTokenResponse.getAccessToken());
        findNotExistUser(updatedTokenResponse.getAccessToken());
    }

    /**
     * Feature: 회원 지역 관리 Scenario: 회원의 지역을 관리한다.
     * <p>
     * When 회원 가입 요청을 한다. Then 가입된 회원 정보에 지역이 NUll로 조회된다.
     * <p>
     * When 지역을 포함하여 회원 가입 요청을 한다. Then 가입된 회원 정보에 지역이 조회된다.
     * <p>
     * When 지역을 포함한 회원의 지역을 null로 수정한다. Then 가입된 회원 정보에 지역이 NUll로 조회된다.
     * <p>
     * When 지역을 포함하지 않은 회원의 지역을 포함하여 수정한다. Then 가입된 회원 정보에 지역이 조회된다.
     */
    @Test
    @DisplayName("회원 지역 관리")
    void joinUserWithArea() {
        createUserWithoutArea(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        UserResponse userResponse = findUser(tokenResponse.getAccessToken());
        assertThat(userResponse.getArea()).isNull();

        createUserWithArea(TEST_USER_OTHER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        tokenResponse = login(TEST_USER_OTHER_EMAIL, TEST_USER_PASSWORD);
        String accessToken = tokenResponse.getAccessToken();
        userResponse = findUser(accessToken);
        assertThat(userResponse.getArea()).isNotNull();
        assertThat(userResponse.getArea().getId()).isEqualTo(TEST_AREA_ID);

        updateUserWithoutAreaAndImage(accessToken, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        userResponse = findUser(accessToken);
        assertThat(userResponse.getArea()).isNull();

        updateUserWithAreaAndWithoutImage(accessToken, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        userResponse = findUser(accessToken);
        assertThat(userResponse.getArea()).isNotNull();
        assertThat(userResponse.getArea().getId()).isEqualTo(TEST_AREA_ID);

        userResponse = updateUserWithAreaAndImage(accessToken, TEST_USER_NICKNAME,
            TEST_USER_PASSWORD);
        assertThat(userResponse.getArea()).isNotNull();
        assertThat(userResponse.getArea().getId()).isEqualTo(TEST_AREA_ID);
        assertThat(userResponse.getImage()).isNotNull();
    }

    private String createUserWithoutArea(String email, String nickname, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("nickname", nickname);
        params.put("password", password);

        return createUserBy(params);
    }

    private String createUserWithArea(String email, String nickname, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("nickname", nickname);
        params.put("password", password);
        params.put("areaId", String.valueOf(TEST_AREA_ID));

        return createUserBy(params);
    }

    private String createUserBy(Map<String, String> params) {
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

    private UserResponse findUser(String accessToken) {
        return given()
            .header("X-AUTH-TOKEN", accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/users/myinfo")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(UserResponse.class);
    }

    private void findNotExistUser(String accessToken) {
        given()
            .header("X-AUTH-TOKEN", accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/users/myinfo")
            .then()
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private UserResponse updateUserWithoutAreaAndImage(String accessToken, String nickname,
        String password) {
        return given()
            .log().all()
            .formParam("nickname", nickname)
            .formParam("password", password)
            .header("X-AUTH-TOKEN", accessToken)
            .config(RestAssuredConfig.config()
                .encoderConfig(encoderConfig().defaultContentCharset("UTF-8")))
            .when()
            .put("/users/myinfo")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(UserResponse.class);
    }

    private UserResponse updateUserWithAreaAndWithoutImage(String accessToken, String nickname,
        String password) {
        return given()
            .log().all()
            .formParam("nickname", nickname)
            .formParam("password", password)
            .formParam("areaId", TEST_AREA_ID)
            .header("X-AUTH-TOKEN", accessToken)
            .config(RestAssuredConfig.config()
                .encoderConfig(encoderConfig().defaultContentCharset("UTF-8")))
            .when()
            .put("/users/myinfo")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(UserResponse.class);
    }

    private UserResponse updateUserWithAreaAndImage(String accessToken, String nickname,
        String password) {        // TODO: 2020/07/28 이미지를 포함했을 때 한글이 안 나오는 문제

        return given()
            .log().all()
            .formParam("nickname", nickname)
            .formParam("password", password)
            .formParam("areaId", TEST_AREA_ID)
            .multiPart("image", new File(TEST_IMAGE_DIR + TEST_IMAGE_NAME))
            .header("X-AUTH-TOKEN", accessToken)
            .config(RestAssuredConfig.config()
                .encoderConfig(encoderConfig().defaultContentCharset("UTF-8")))
            .when()
            .put("/users/myinfo")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(UserResponse.class);
    }

    private void deleteUser(String accessToken) {
        given()
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .delete("/users/myinfo")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
