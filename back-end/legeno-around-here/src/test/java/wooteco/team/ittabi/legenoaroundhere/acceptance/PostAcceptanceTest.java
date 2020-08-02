package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageTestConstants.TEST_IMAGE_DIR;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostTestConstants.TEST_WRITING;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.aws.S3Uploader;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostAcceptanceTest {

    @MockBean
    private S3Uploader s3Uploader;

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
     * When 이미지가 포함된 글을 등록한다. Then 이미지가 포함된 글이 등록되었다.
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

        // 이미지가 포함되지 않은 글 등록
        String postWithoutImageLocation = createPostWithoutImage();
        Long postWithoutImageId = getIdFromUrl(postWithoutImageLocation);

        PostResponse postWithoutImageResponse = findPost(postWithoutImageId);

        assertThat(postWithoutImageResponse.getId()).isEqualTo(postWithoutImageId);
        assertThat(postWithoutImageResponse.getWriting()).isEqualTo(TEST_WRITING);

        // 이미지가 포함된 글 등록
        String postWithImageLocation = createPostWithImage();
        Long postWithImageId = getIdFromUrl(postWithImageLocation);

        PostResponse postWithImageResponse = findPost(postWithImageId);

        assertThat(postWithImageResponse.getId()).isEqualTo(postWithImageId);
        assertThat(postWithImageResponse.getWriting()).isEqualTo(TEST_WRITING);
        assertThat(postWithImageResponse.getImages()).hasSize(2);

        // 목록 조회
        List<PostResponse> postResponses = findAllPost();
        assertThat(postResponses).hasSize(2);

        // 수정
        String updatedWriting = "BingBong and Jamie";
        updatePost(postWithoutImageId, updatedWriting);

        PostResponse updatedPostResponse = findPost(postWithoutImageId);

        assertThat(updatedPostResponse.getId()).isEqualTo(postWithoutImageId);
        assertThat(updatedPostResponse.getWriting()).isEqualTo(updatedWriting);

        // 조회
        PostResponse postFindResponse = findPost(postWithoutImageId);

        assertThat(postFindResponse.getId()).isEqualTo(postWithoutImageId);
        assertThat(postFindResponse.getWriting()).isEqualTo(updatedWriting);

        // 삭제
        deletePost(postWithoutImageId);
        findNotExistsPost(postWithoutImageId);

        List<PostResponse> foundPostResponses = findAllPost();

        assertThat(foundPostResponses).hasSize(1);
    }

    private void findNotExistsPost(Long id) {
        given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/posts/" + id)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private void deletePost(Long id) {
        given()
            .when()
            .delete("/posts/" + id)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private void updatePost(Long id, String writing) {
        Map<String, String> params = new HashMap<>();
        params.put("writing", writing);

        given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put("/posts/" + id)
            .then()
            .statusCode(HttpStatus.OK.value());
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

    private String createPostWithoutImage() {

        return given()
            .log().all()
            .formParam("writing", TEST_WRITING)
            .config(RestAssuredConfig.config()
                .encoderConfig(encoderConfig().defaultContentCharset("UTF-8")))
            .when()
            .post("/posts")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");
    }

    private String createPostWithImage() {
        when(s3Uploader.upload(any(MultipartFile.class), eq("images"))).thenReturn("imageUrl");

        // TODO: 2020/07/28 이미지를 포함했을 때 한글이 안 나오는 문제
        return given()
            .log().all()
            .formParam("writing", TEST_WRITING)
            .config(RestAssuredConfig.config()
                .encoderConfig(encoderConfig().defaultContentCharset("UTF-8")))
            .multiPart("images", new File(TEST_IMAGE_DIR + "right_image1.jpg"))
            .multiPart("images", new File(TEST_IMAGE_DIR + "right_image2.jpg"))
            .when()
            .post("/posts")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");
    }
}

