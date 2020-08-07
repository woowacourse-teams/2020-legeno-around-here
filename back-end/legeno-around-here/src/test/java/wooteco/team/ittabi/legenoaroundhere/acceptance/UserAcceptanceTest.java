package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_PASSWORD;

import io.restassured.RestAssured;
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
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/init-table.sql")
public class UserAcceptanceTest {

    private static final String USER_LOCATION_FORMAT = "^/users/[1-9][0-9]*$";
    private static final int TOKEN_MIN_SIZE = 1;

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
     * <<<<<<< HEAD When 로그아웃을 한다. Then 로그아웃이 된다.
     * <p>
     * ======= >>>>>>> feat: 조회/탈퇴 인수테스트 작성 Given 로그인이 되어있는 상태이다. When 회원 탈퇴 요청을 한다. Then 회원 탈퇴가
     * 되었다.
     */
    @Test
    @DisplayName("회원 관리")
    void manageUser() {

        //회원 가입
        String location = createUser(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        assertThat(location).matches(USER_LOCATION_FORMAT);
        TokenResponse tokenResponseForJoin = login(TEST_EMAIL, TEST_PASSWORD);
        UserResponse userResponseForJoin = findUser(tokenResponseForJoin.getAccessToken());
        assertThat(userResponseForJoin).isNotNull();
        assertThat(userResponseForJoin.getId()).isNotNull();
        assertThat(userResponseForJoin.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(userResponseForJoin.getNickname()).isEqualTo(TEST_NICKNAME);

        // 로그인
        TokenResponse tokenResponse = login(TEST_EMAIL, TEST_PASSWORD);
        assertThat(tokenResponse).isNotNull();
        assertThat(tokenResponse.getAccessToken())
            .hasSizeGreaterThanOrEqualTo(TOKEN_MIN_SIZE);

        // 내 정보 조회
        UserResponse userResponse = findUser(tokenResponse.getAccessToken());
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(userResponse.getNickname()).isEqualTo(TEST_NICKNAME);

        // 내 정보 수정
        updateUser(tokenResponse.getAccessToken(), "newname", "newpassword");
        UserResponse updatedUserResponse = findUser(tokenResponse.getAccessToken());
        assertThat(updatedUserResponse.getNickname()).isEqualTo("newname");
        TokenResponse updatedTokenResponse = login(TEST_EMAIL, "newpassword");
        assertThat(updatedTokenResponse).isNotNull();
        assertThat(updatedTokenResponse.getAccessToken())
            .hasSizeGreaterThanOrEqualTo(TOKEN_MIN_SIZE);

        // 회원 탈퇴
        deleteUser(updatedTokenResponse.getAccessToken());
        findNotExistUser(updatedTokenResponse.getAccessToken());
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

    private UserResponse updateUser(String accessToken, String nickname, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("nickname", nickname);
        params.put("password", password);

        return given()
            .header("X-AUTH-TOKEN", accessToken)
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
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
