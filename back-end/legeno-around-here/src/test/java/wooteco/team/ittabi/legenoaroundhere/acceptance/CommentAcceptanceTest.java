package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostTestConstants.TEST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_PASSWORD;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

public class CommentAcceptanceTest extends AcceptanceTest {

    /**
     * Feature: 댓글 관리
     * <p>
     * Scenario: 댓글을 관리한다.
     * <p>
     * When 댓글을 등록한다. Then 댓글이 등록되었다.
     * <p>
     * When 댓글 목록을 조회한다. Then 댓글 목록을 응답받는다. And 댓글 목록은 n개이다.
     * <p>
     * When 댓글을 조회한다. Then 글을 응답 받는다.
     * <p>
     * When 댓글을 삭제한다. Then 댓글이 삭제 상태로 변경된다. And 다시 댓글을 조회할 수 없다. And 댓글 목록은 n-1개이다.
     */
    @DisplayName("댓글 관리")
    @Test
    void manageComment() {

        // 로그인
        createUser(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        TokenResponse tokenResponse = login(TEST_EMAIL, TEST_PASSWORD);
        String accessToken = tokenResponse.getAccessToken();

        // 이미지가 포함되지 않은 글 등록
        String postLocation = createPostWithoutImage(accessToken);
        Long postId = getIdFromUrl(postLocation);
        PostResponse postResponse = findPost(postId, accessToken);

        // 댓글 등록 및 조회
        String commentLocation = createComment(postResponse.getId(), accessToken);
        Long commentId = getIdFromUrl(commentLocation);

        CommentResponse commentResponse = findComment(postResponse.getId(), commentId, accessToken);

        assertThat(commentResponse.getId()).isEqualTo(commentId);
        assertThat(commentResponse.getWriting()).isEqualTo(TEST_WRITING);

        // 댓글 목록 조회
        List<CommentResponse> commentResponses = findAllComment(postResponse.getId(), accessToken);
        assertThat(commentResponses).hasSize(1);

        // 삭제
        deleteComment(postResponse.getId(), commentId, accessToken);
        findNotExistsComment(postResponse.getId(), commentId, accessToken);

        List<CommentResponse> reFoundPostResponses = findAllComment(postResponse.getId(),
            accessToken);

        assertThat(reFoundPostResponses).hasSize(0);
    }

    private List<CommentResponse> findAllComment(Long postId, String accessToken) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/posts/" + postId + "/comments")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .jsonPath()
            .getList(".", CommentResponse.class);
    }

    private void findNotExistsComment(Long postId, Long commentId, String accessToken) {
        given()
            .header("X-AUTH-TOKEN", accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/posts/" + postId + "/comments/" + commentId)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private void deleteComment(Long postId, Long commentId, String accessToken) {
        given()
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .delete("/posts/" + postId + "/comments/" + commentId)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private String createComment(Long postId, String accessToken) {

        Map<String, String> params = new HashMap<>();
        params.put("writing", TEST_WRITING);

        return given()
            .log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .post("/posts/" + postId + "/comments")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");
    }

    private CommentResponse findComment(Long postId, Long commentId, String accessToken) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/posts/" + postId + "/comments/" + commentId)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(CommentResponse.class);
    }
}
