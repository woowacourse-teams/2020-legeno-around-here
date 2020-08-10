package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_PASSWORD;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.team.ittabi.legenoaroundhere.dto.AreaResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.repository.AreaRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/init-table.sql")
public class AreaAcceptanceTest {

    @LocalServerPort
    public int port;
    @Autowired
    AreaRepository areaRepository;
    private String accessToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        createUser();
        TokenResponse tokenResponse = login();
        accessToken = tokenResponse.getAccessToken();
    }

    private void createUser() {
        Map<String, String> params = new HashMap<>();
        params.put("email", TEST_EMAIL);
        params.put("nickname", TEST_NICKNAME);
        params.put("password", TEST_PASSWORD);

        given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/join")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }

    private TokenResponse login() {
        Map<String, String> params = new HashMap<>();
        params.put("email", TEST_EMAIL);
        params.put("password", TEST_PASSWORD);

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
     * When 사용자가 "서울특별시"라는 키워드로 지역을 50Page, 10Size 조회한다. Then 3건이 조회된다.
     * <p>
     * When 사용자가 "서울특별시 "라는 키워드로 지역을 50Page, 10Size 조회한다. 조회한다. Then 2건이 조회된다.
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
    void findAreaByKeyword() {
        List<AreaResponse> areas = findAllArea(accessToken, "page=50&size=10");
        assertThat(areas).hasSize(3);

        areas = findAllArea(accessToken, "page=50&size=10&keyword=");
        assertThat(areas).hasSize(3);

        areas = findAllArea(accessToken, "page=50&size=10&keyword=서울특별시");
        assertThat(areas).hasSize(3);

        areas = findAllArea(accessToken, "page=50&size=10&keyword=서울특별시 ");
        assertThat(areas).hasSize(2);

        areas = findAllArea(accessToken, "page=1&size=10&keyword=서울시");
        assertThat(areas).hasSize(0);

        areas = findAllArea(accessToken, "page=1&size=10&keyword= 잠실");
        assertThat(areas).hasSize(1);

        areas = findAllArea(accessToken, "page=1&size=10&keyword=잠실");
        assertThat(areas).hasSize(1);

        areas = findAllArea(accessToken, "page=1&size=10&keyword=잠실 ");
        assertThat(areas).hasSize(0);
    }

    /**
     * Feature: 지역 조회
     * <p>
     * Scenario: 부문을 페이징 조회한다.
     * <p>
     * Given 사용자가 로그인 되어있다. 법정동은 기등록된 상태(서울특별시 이하 지역)이다.
     * <p>
     * When 지역 1Page 20Size를 정렬(기준:id 방향:오름차순) 조회한다. Then 1~20까지의 Sector가 조회된다.
     * <p>
     * When 지역 1Page 20Size를 정렬(기준:id 방향:내림차순) 조회한다. Then 100~81까지의 Sector가 조회된다.
     * <p>
     * When 지역 2Page 40Size를 정렬(기준:id 방향:오름차순) 조회한다. Then 21~40까지의 Sector가 조회된다.
     * <p>
     * When 지역 -1Page -1Size를 정렬(기준:id 방향:abc) 조회한다. Then 1의 Sector가 조회된다. (기본 값 : 1Page, 1Size,
     * 방향:오름차순)
     * <p>
     * When 지역 1Page 20Size를 정렬(기준:test-id 방향:오름차순) 조회한다. Then BadRequest가 발생한다.
     */
    @DisplayName("지역 페이징 조회")
    @Test
    void pagingFindArea() {
        // 지역 1Page 20Size를 정렬(기준:id 방향:오름차순) 조회
        List<AreaResponse> areas
            = findAllArea(accessToken, "page=1&size=20&sortedBy=id&direction=asc");
        assertThat(areas).hasSize(20);

        // 지역 1Page 20Size를 정렬(기준:id 방향:내림차순) 조회
        areas = findAllArea(accessToken, "page=1&size=20&sortedBy=id&direction=desc");
        assertThat(areas).hasSize(20);

        // 지역 2Page 20Size를 정렬(기준:id 방향:오름차순) 조회
        areas = findAllArea(accessToken, "page=2&size=20&sortedBy=id&direction=asc");
        assertThat(areas).hasSize(20);

        // Page, Size, Direction 오기입 조회 (자동 값 : 1Page, 1Size, 방향:오름차순)
        areas = findAllArea(accessToken, "page=-1&size=1&sortedBy=id&direction=asc");
        assertThat(areas).hasSize(1);

        areas = findAllArea(accessToken, "page=1&size=-1&sortedBy=id&direction=asc");
        assertThat(areas).hasSize(1);

        areas = findAllArea(accessToken, "page=1&size=51&sortedBy=id&direction=asc");
        assertThat(areas).hasSize(50);

        areas = findAllArea(accessToken, "page=1&size=1&sortedBy=id&direction=abc");
        assertThat(areas).hasSize(1);

        areas = findAllArea(accessToken, "size=1&sortedBy=id&direction=abc");
        assertThat(areas).hasSize(1);

        areas = findAllArea(accessToken, "page=1&sortedBy=id&direction=abc");
        assertThat(areas).hasSize(10);

        areas = findAllArea(accessToken, "page=1&size=1&sortedBy=id");
        assertThat(areas).hasSize(1);

        // 유효하지 않은 필드로 정렬
        findAllAreaWithWrongParameter(accessToken, "page=ㄱ&size=1&sortedBy=id");
        findAllAreaWithWrongParameter(accessToken, "page=1&size=ㄴ&sortedBy=id");
        findAllAreaWithWrongParameter(accessToken,
            "page=1&size=20&sortedBy=ㄷ&direction=asc");
    }

    private List<AreaResponse> findAllArea(String accessToken, String parameter) {
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

    private void findAllAreaWithWrongParameter(String accessToken, String parameter) {
        given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/areas?" + parameter)
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
