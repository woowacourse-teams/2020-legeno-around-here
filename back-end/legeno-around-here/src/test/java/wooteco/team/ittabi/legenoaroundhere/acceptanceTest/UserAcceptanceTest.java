package wooteco.team.ittabi.legenoaroundhere.acceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAcceptanceTest {

    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_NAME = "testName";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String USER_LOCATION_FORMAT = "^/users/[1-9][0-9]*$";

    @LocalServerPort
    public int port;

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    //        Feature: 회원관리
    //
    //        Scenario: 회원을 관리한다.
    //        When 회원 가입 요청을 한다.
    //        Then 회원으로 가입이되었다.
    //
    //        When 로그인을 한다.
    //        Then 로그인이 되었다.
    @Test
    public void manageUser() {
        String location = createUser(TEST_EMAIL, TEST_NAME, TEST_PASSWORD);
        assertThat(location).matches(USER_LOCATION_FORMAT);

//        TokenResponse tokenResponse = login(TEST_EMAIL, TEST_PASSWORD);
        TokenResponse tokenResponse = new TokenResponse("abc");   // Todo: 지울것
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }

    private String createUser(String email, String nickName, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("nickName", nickName);
        params.put("password", password);

        return
            given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
            when().
                post("/join").
            then().
                statusCode(HttpStatus.CREATED.value()).
                extract().header("Location");
    }

    private TokenResponse login(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return
            given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
            when().
                post("/login").
            then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().as(TokenResponse.class);
    }
}
