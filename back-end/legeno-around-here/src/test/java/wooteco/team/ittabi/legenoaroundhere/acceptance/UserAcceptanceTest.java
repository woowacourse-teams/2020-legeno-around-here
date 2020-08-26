package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AUTH_NUMBER;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_DIR;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_NEW_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_NEW_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_NEW_USER_PASSWORD;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_THE_OTHER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_OTHER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_OTHER_PASSWORD;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth.MailAuth;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserImageResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.repository.MailAuthRepository;

public class UserAcceptanceTest extends AcceptanceTest {

    private static final String USER_LOCATION_FORMAT = "^/users/[1-9][0-9]*$";
    private static final int TOKEN_MIN_SIZE = 1;

    @MockBean
    private MailAuthRepository mailAuthRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * Feature: 회원관리 Scenario: 회원을 관리한다.
     * <p>
     * When 회원 가입 요청을 한다. Then 회원으로 가입이되었다.
     * <p>
     * When 로그인을 한다. Then 이메일 인증을 요구한다.
     * <p>
     * When 이미 가입된 이메일 인증이 된 회원으로 로그인을 한다. Then 로그인이 되었다.
     * <p>
     * When 내 정보를 조회한다. Then 내 정보가 조회된다.
     * <p>
     * When 내 정보를 이미지를 포함하여 수정한다. Then 내 정보가 수정된다.
     * <p>
     * When 내 정보를 이미지를 사용하지 않는 방향으로 수정한다. Then 내 정보가 수정된다.
     * <p>
     * Given 로그인이 되어있는 상태이다. * When 회원 탈퇴 요청을 한다. Then 회원 탈퇴가 되었다.
     */
    @Test
    @DisplayName("회원 관리")
    void manageUser() {
        //메일 인증
        MailAuth mailAuth = new MailAuth(TEST_USER_EMAIL, TEST_AUTH_NUMBER);
        when(mailAuthRepository.findByEmail(any())).thenReturn(java.util.Optional.of(mailAuth));

        //회원 가입
        String location = createUserWithoutArea(TEST_NEW_USER_EMAIL, TEST_NEW_USER_NICKNAME,
            TEST_NEW_USER_PASSWORD);
        assertThat(location).matches(USER_LOCATION_FORMAT);

        // 메일 인증 완료 한 로그인
        TokenResponse tokenResponse = login(TEST_THE_OTHER_EMAIL, TEST_USER_PASSWORD);
        String accessToken = tokenResponse.getAccessToken();
        assertThat(tokenResponse).isNotNull();
        assertThat(accessToken).hasSizeGreaterThanOrEqualTo(TOKEN_MIN_SIZE);

        // 내 정보 조회
        UserResponse userResponse = findUser(accessToken);
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getEmail()).isEqualTo(TEST_THE_OTHER_EMAIL);
        assertThat(userResponse.getNickname()).isEqualTo(TEST_USER_NICKNAME);
        assertThat(userResponse.getAwardsCount()).isNotNull();

        // 프로필 사진 등록
        UserImageResponse userImageResponse = createUserImage(accessToken);

        // 내 정보 수정 - 프로필 사진 포함
        updateMyInfoWithImage(accessToken, "newname", userImageResponse.getId());
        UserResponse updatedUserResponse = findUser(accessToken);
        assertThat(updatedUserResponse.getNickname()).isEqualTo("newname");
        assertThat(updatedUserResponse.getImage()).isNotNull();
        login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        // 내 정보 수정 - 프로필 사진 사용 안함(기본 이미지)
        updateMyInfoWithImage(accessToken, TEST_USER_NICKNAME, null);
        updatedUserResponse = findUser(accessToken);
        assertThat(updatedUserResponse.getNickname()).isEqualTo(TEST_USER_NICKNAME);
        assertThat(updatedUserResponse.getImage()).isNull();
        login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        // 비밀번호 수정
        changeMyPassword(accessToken, TEST_USER_OTHER_PASSWORD);
        login(TEST_THE_OTHER_EMAIL, TEST_USER_OTHER_PASSWORD);

        // 비밀번호 수정 실패 - 동일한 비밀번호로 수정
        assertThatThrownBy(() -> changeMyPassword(accessToken, TEST_USER_OTHER_PASSWORD));

        // 회원 탈퇴
        deleteUser(accessToken);
        findNotExistUser(accessToken);
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
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        UserResponse userResponse = findUser(tokenResponse.getAccessToken());
        assertThat(userResponse.getArea()).isNull();

        tokenResponse = login(TEST_USER_OTHER_EMAIL, TEST_USER_PASSWORD);
        String accessToken = tokenResponse.getAccessToken();
        userResponse = findUser(accessToken);
        assertThat(userResponse.getArea()).isNotNull();
        assertThat(userResponse.getArea().getId()).isEqualTo(TEST_AREA_ID);

        updateMyInfoWithoutAreaAndImage(accessToken, TEST_USER_NICKNAME);
        userResponse = findUser(accessToken);
        assertThat(userResponse.getArea()).isNull();

        updateMyInfoWithAreaAndWithoutImage(accessToken, TEST_USER_NICKNAME);
        userResponse = findUser(accessToken);
        assertThat(userResponse.getArea()).isNotNull();
        assertThat(userResponse.getArea().getId()).isEqualTo(TEST_AREA_ID);
    }

    private String createUserWithoutArea(String email, String nickname, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("nickname", nickname);
        params.put("password", password);
        params.put("authNumber", String.valueOf(TEST_AUTH_NUMBER));

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
            .get("/users/me")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(UserResponse.class);
    }

    private void findNotExistUser(String accessToken) {
        given()
            .header("X-AUTH-TOKEN", accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/users/me")
            .then()
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private UserImageResponse createUserImage(String accessToken) {
        return given()
            .multiPart("image", new File(TEST_IMAGE_DIR + TEST_IMAGE_NAME))
            .header("X-AUTH-TOKEN", accessToken)
            .config(RestAssuredConfig.config()
                .encoderConfig(encoderConfig().defaultContentCharset("UTF-8")))
            .when()
            .post("/users/images")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract().as(UserImageResponse.class);
    }

    private UserResponse updateMyInfoWithoutAreaAndImage(String accessToken, String nickname) {
        Map<String, String> params = new HashMap<>();
        params.put("nickname", nickname);

        return updateUserBy(accessToken, params);
    }

    private UserResponse updateMyInfoWithAreaAndWithoutImage(String accessToken, String nickname) {
        Map<String, String> params = new HashMap<>();
        params.put("nickname", nickname);
        params.put("areaId", String.valueOf(TEST_AREA_ID));

        return updateUserBy(accessToken, params);
    }

    private UserResponse updateMyInfoWithImage(String accessToken, String nickname, Long imageId) {
        Map<String, String> params = new HashMap<>();
        params.put("nickname", nickname);
        params.put("areaId", String.valueOf(TEST_AREA_ID));
        params.put("imageId", String.valueOf(imageId));

        return updateUserBy(accessToken, params);
    }

    private UserResponse updateUserBy(String accessToken, Map<String, String> params) {
        return given()
            .header("X-AUTH-TOKEN", accessToken)
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put("/users/me")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(UserResponse.class);
    }

    private void changeMyPassword(String accessToken, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("password", password);

        given()
            .header("X-AUTH-TOKEN", accessToken)
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put("/users/me/password")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private void deleteUser(String accessToken) {
        given()
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .delete("/users/me")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
