package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostTestConstants.TEST_WRITING;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/init-table.sql")
public abstract class AcceptanceTest {

    @LocalServerPort
    public int port;

    static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected String createUser(String email, String nickname, String password) {
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

    protected TokenResponse login(String email, String password) {
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

    protected Long getIdFromUrl(String location) {
        int lastIndex = location.lastIndexOf("/");
        return Long.valueOf(location.substring(lastIndex + 1));
    }

    protected String createPostWithoutImage(String accessToken) {

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

    protected PostResponse findPost(Long id, String accessToken) {
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
