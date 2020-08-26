package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.team.ittabi.legenoaroundhere.dto.AreaResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

public class AreaAcceptanceTest extends AcceptanceTest {

    private String accessToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        TokenResponse tokenResponse = login();
        accessToken = tokenResponse.getAccessToken();
    }

    private TokenResponse login() {
        Map<String, String> params = new HashMap<>();
        params.put("email", TEST_USER_EMAIL);
        params.put("password", TEST_USER_PASSWORD);

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

    /**
     * Feature: 지역 키워드 조회
     * <p>
     * Scenario: 지역을 키워드로 조회한다.
     * <p>
     * Given 사용자가 로그인 되어있다. 법정동은 기등록된 상태(서울특별시 이하 지역)이다.
     * <p>
     * When 사용자가 키워드를 입력하지 않고 지역을 50Page, 10Size 조회한다. Then 3건이 조회된다.
     * <p>
     * When 사용자가 키워드를 비워두고 지역을 50Page, 10Size 조회한다. Then 3건이 조회된다.
     * <p>
     * When 사용자가 "서울특별시"라는 키워드로 지역을 50Page, 10Size 조회한다. Then 2건이 조회된다.
     * <p>
     * When 사용자가 "서울특별시 "라는 키워드로 지역을 50Page, 10Size 조회한다. 조회한다. Then 1건이 조회된다.
     * <p>
     * When 사용자가 "서울시"라는 키워드로 지역을 1Page, 10Size 조회한다. Then 1건도 조회되지 않는다.
     * <p>
     * When 사용자가 " 잠실"이라는 키워드로 지역을 1Page, 10Size 조회한다. Then 1건이 조회된다.
     * <p>
     * When 사용자가 "잠실"이라는 키워드로 지역을 1Page, 10Size 조회한다. Then 1건이 조회된다.
     * <p>
     * When 사용자가 "잠실 "이라는 키워드로 지역을 1Page, 10Size 조회한다. Then 1건도 조회되지 않는다.
     */
    @DisplayName("지역 키워드 조회")
    @Test
    void searchAreaByKeyword() {
        List<AreaResponse> areas = searchAreas(accessToken, "page=49&size=10");
        assertThat(areas).hasSize(3);

        areas = searchAreas(accessToken, "page=49&size=10&keyword=");
        assertThat(areas).hasSize(3);

        areas = searchAreas(accessToken, "page=49&size=10&keyword=서울특별시");
        assertThat(areas).hasSize(2);

        areas = searchAreas(accessToken, "page=49&size=10&keyword=서울특별시 ");
        assertThat(areas).hasSize(1);

        areas = searchAreas(accessToken, "page=0&size=10&keyword=서울시");
        assertThat(areas).hasSize(0);

        areas = searchAreas(accessToken, "page=0&size=10&keyword= 잠실");
        assertThat(areas).hasSize(1);

        areas = searchAreas(accessToken, "page=0&size=10&keyword=잠실");
        assertThat(areas).hasSize(1);

        areas = searchAreas(accessToken, "page=0&size=10&keyword=잠실 ");
        assertThat(areas).hasSize(0);
    }

    private List<AreaResponse> searchAreas(String accessToken, String parameter) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/areas?" + parameter)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList("content", AreaResponse.class);
    }
}
