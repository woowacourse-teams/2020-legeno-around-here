package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
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
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostAcceptanceTest {

    @LocalServerPort
    public int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    /**
     * Feature: 글 관리
     * <p>
     * Scenario: 글을 관리한다.
     * <p>
     * When 글을 등록한다. Then 글이 등록되었다.
     * <p>
     * When 글 목록을 조회한다. Then 글 목록을 응답받는다. And 글 목록은 n개이다.
     * <p>
     * When 글을 조회한다. Then 글을 응답 받는다.
     * <p>
     * When 글을 수정한다. Then 글이 수정되었다.
     * <p>
     * When 글을 삭제한다. Then 글이 삭제 상태로 변경된다. And 다시 글을 조회할 수 없다. And 글 목록은 n-1개이다.
     */
    @DisplayName("글 관리")
    @Test
    void managePost() {

        String expectedWriting = "글을 등록합니다.";
        String location = createPost(expectedWriting);
        Long id = getIdFromUrl(location);
        PostResponse postResponse = findPost(id);
        assertThat(postResponse.getId()).isEqualTo(id);
        assertThat(postResponse.getWriting()).isEqualTo(expectedWriting);

        List<PostResponse> postResponses = findAllPost();
        assertThat(postResponses).hasSize(1);

//         When 글을 수정한다.
//         Then 글이 수정되었다.
//
//         When 글을 조회한다.
//         Then 글을 응답 받는다.
//
//         When 글을 삭제한다.
//         Then 글이 삭제 상태로 변경된다.
//         And 다시 글을 조회할 수 없다.
//         And 글 목록은 n-1개이다.
    }

    private List<PostResponse> findAllPost() {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/posts")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList(".", PostResponse.class);
    }

    private Long getIdFromUrl(String location) {
        int lastIndex = location.lastIndexOf("/");
        return Long.valueOf(location.substring(lastIndex + 1));

    }

    private PostResponse findPost(Long id) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/posts/" + id)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(PostResponse.class);
    }

    private String createPost(String writing) {
        Map<String, String> params = new HashMap<>();
        params.put("writing", writing);

        return given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/posts")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");
    }
}
