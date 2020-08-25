package wooteco.team.ittabi.legenoaroundhere.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.CommentConstants.TEST_COMMENT_OTHER_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.CommentConstants.TEST_COMMENT_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_EMPTY_IMAGES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_PASSWORD;
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
import wooteco.team.ittabi.legenoaroundhere.dto.CommentResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;

public class CommentAcceptanceTest extends AcceptanceTest {

    private String accessToken;
    private Long postId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        // 로그인
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        accessToken = tokenResponse.getAccessToken();

        Long sectorId = createSector();
        String postLocation = createPostWithoutImage(accessToken, sectorId);
        postId = getIdFromUrl(postLocation);
    }

    /**
     * Feature: 댓글 관리
     * <p>
     * Scenario: 댓글을 관리한다.
     * <p>
     * When 댓글을 등록한다. Then 댓글이 등록되었다.
     * <p>
     * When 댓글 목록을 조회한다. Then 댓글 목록을 응답받는다. And 댓글 목록은 n개이다.
     * <p>
     * When 댓글을 조회한다. Then 댓글을 응답 받는다.
     * <p>
     * When 댓글을 수정한다. Then 댓글이 수정된다.
     * <p>
     * When 댓글을 삭제한다. Then 댓글이 삭제 상태로 변경된다. And 다시 댓글을 조회할 수 없다. And 댓글 목록은 n-1개이다.
     */
    @DisplayName("댓글 관리")
    @Test
    void manageComment() {
        // 댓글 등록 및 조회
        String commentLocation = createComment(postId, accessToken);
        Long commentId = getIdFromUrl(commentLocation);

        CommentResponse commentResponse = findComment(accessToken, commentId);

        assertThat(commentResponse.getId()).isEqualTo(commentId);
        assertThat(commentResponse.getWriting()).isEqualTo(TEST_COMMENT_WRITING);
        assertThat(commentResponse.getZzang()).isNotNull();

        // 댓글 목록 조회
        List<CommentResponse> commentResponses = findAllComment(postId, accessToken);
        assertThat(commentResponses).hasSize(1);

        // 수정
        commentResponse = updateComment(accessToken, commentId, TEST_COMMENT_OTHER_WRITING);
        assertThat(commentResponse.getId()).isEqualTo(commentId);
        assertThat(commentResponse.getWriting()).isEqualTo(TEST_COMMENT_OTHER_WRITING);
        assertThat(commentResponse.getZzang()).isNotNull();

        // 삭제
        deleteComment(accessToken, commentId);
        findNotExistsComment(accessToken, commentId);

        List<CommentResponse> reFoundPostResponses = findAllComment(postId, accessToken);

        assertThat(reFoundPostResponses).hasSize(0);
    }

    /**
     * Feature: 댓글 관리
     * <p>
     * Scenario: 댓글을 관리한다.
     * <p>
     * Given 댓글이 등록되어 있다.
     * <p>
     * When 대댓글을 등록한다. Then 대댓글이 등록된다.
     * <p>
     * When 댓글 목록을 조회한다. Then 대댓글이 아닌 댓글 목록을 응답받는다. And 댓글 목록은 n개이다.
     * <p>
     * When 대댓글을 삭제한다. Then 댓글 목록 조회 및 댓글 조회시 조회되지 않는다.
     * <p>
     * When 대댓글이 달리지 않은 댓글을 삭제한다. Then 댓글 목록 조회 및 댓글 조회시 조회되지 않는다.
     * <p>
     * When 대댓글이 달린 댓글을 삭제한다. Then 댓글 목록 조회 및 댓글 조회시 내용이 null이 조회된다.
     * <p>
     * When 삭제된 댓글의 대댓글이 모두 삭제된다. Then 댓글 목록 조회 및 댓글 조회시 조회되지 않는다.
     */
    @DisplayName("대댓글 관리")
    @Test
    void manageCocomment() {
        // 댓글 등록 및 조회
        String commentLocation = createComment(postId, accessToken);
        Long commentId = getIdFromUrl(commentLocation);

        CommentResponse commentResponse = findComment(accessToken, commentId);
        assertThat(commentResponse.getCocomments()).isEmpty();

        // 대댓글 생성 및 조회
        String cocommentLocation =
            createCocomment(accessToken, commentId, TEST_COMMENT_OTHER_WRITING);
        Long cocommentId = getIdFromUrl(cocommentLocation);

        CommentResponse cocommentResponse = findComment(accessToken, cocommentId);
        assertThat(cocommentResponse.getWriting()).isEqualTo(TEST_COMMENT_OTHER_WRITING);
        assertThat(cocommentResponse.getCocomments()).isEmpty();

        commentResponse = findComment(accessToken, commentId);
        List<CommentResponse> cocomments = commentResponse.getCocomments();
        assertThat(cocomments).hasSize(1);
        assertThat(cocomments.get(0).getWriting()).isEqualTo(TEST_COMMENT_OTHER_WRITING);

        // 댓글 목록 조회
        List<CommentResponse> commentResponses = findAllComment(postId, accessToken);
        assertThat(commentResponses).hasSize(1);

        // 대댓글 삭제
        deleteComment(accessToken, cocommentId);
        findNotExistsComment(accessToken, cocommentId);
        commentResponse = findComment(accessToken, commentId);
        assertThat(commentResponse.getCocomments()).isEmpty();

        // 댓글 삭제
        deleteComment(accessToken, commentId);
        findNotExistsComment(accessToken, commentId);
        commentResponses = findAllComment(postId, accessToken);
        assertThat(commentResponses).isEmpty();

        // 댓글 및 대댓글 재생성
        commentLocation = createComment(postId, accessToken);
        commentId = getIdFromUrl(commentLocation);

        cocommentLocation = createCocomment(accessToken, commentId, TEST_COMMENT_OTHER_WRITING);
        cocommentId = getIdFromUrl(cocommentLocation);

        cocommentLocation = createCocomment(accessToken, commentId, TEST_COMMENT_OTHER_WRITING);
        Long cocommentOtherId = getIdFromUrl(cocommentLocation);

        // 댓글 삭제
        deleteComment(accessToken, commentId);
        commentResponse = findComment(accessToken, commentId);
        assertThat(commentResponse.getWriting()).isNull();
        assertThat(commentResponse.getCocomments()).hasSize(2);
        commentResponses = findAllComment(postId, accessToken);
        assertThat(commentResponses).hasSize(1);

        // 대댓글 삭제 - 다 지우는 경우 댓글도 삭제됨
        deleteComment(accessToken, cocommentId);
        findNotExistsComment(accessToken, cocommentId);
        commentResponse = findComment(accessToken, commentId);
        assertThat(commentResponse.getCocomments()).hasSize(1);
        commentResponses = findAllComment(postId, accessToken);
        assertThat(commentResponses).hasSize(1);

        deleteComment(accessToken, cocommentOtherId);
        findNotExistsComment(accessToken, cocommentOtherId);
        findNotExistsComment(accessToken, commentId);
        commentResponses = findAllComment(postId, accessToken);
        assertThat(commentResponses).isEmpty();
    }

    /**
     * Feature: 댓글의 좋아요 기능
     * <p>
     * Scenario: 좋아요 버튼을 누르고, 좋아요 수를 표시한다.
     * <p>
     * When 댓글을 등록한다. Then 좋아요 수가 0인 댓글이 등록되었다. 댓글의 짱을 누르지 않은 상태이다.
     * <p>
     * When 좋아요 버튼을 누른다. Then 성공했다. 댓글의 짱을 누른 상태이다.
     * <p>
     * When 좋아요 버튼이 눌러진 상태에서 다시 좋아요 버튼을 누른다. Then 성공했다. 댓글의 짱을 누르지 않은 상태이다.
     */
    @DisplayName("댓글의 좋아요 관리")
    @Test
    void manageCommentZzang() {
        // 댓글 등록
        String commentLocation = createComment(postId, accessToken);
        Long commentId = getIdFromUrl(commentLocation);
        CommentResponse comment = findComment(accessToken, commentId);

        // 댓글 생성 시 초기 댓글 좋아요 수
        assertThat(comment.getZzang().getCount()).isEqualTo(0L);
        assertThat(comment.getZzang().isActivated()).isFalse();

        // 비활성화된 좋아요를 활성화
        pressCommentZzang(comment.getId(), accessToken);
        comment = findComment(accessToken, commentId);

        assertThat(comment.getZzang().getCount()).isEqualTo(1L);
        assertThat(comment.getZzang().isActivated()).isTrue();

        // 활성화된 좋아요를 비활성화
        pressCommentZzang(comment.getId(), accessToken);
        comment = findComment(accessToken, commentId);

        assertThat(comment.getZzang().getCount()).isEqualTo(0L);
        assertThat(comment.getZzang().isActivated()).isFalse();

        // 비활성화된 좋아요를 다시 활성화
        pressCommentZzang(comment.getId(), accessToken);
        comment = findComment(accessToken, commentId);

        assertThat(comment.getZzang().getCount()).isEqualTo(1L);
        assertThat(comment.getZzang().isActivated()).isTrue();
    }

    private Long createSector() {
        String accessToken = getCreateAdminToken();

        Map<String, String> params = new HashMap<>();
        params.put("name", TEST_SECTOR_NAME);
        params.put("description", TEST_SECTOR_DESCRIPTION);

        String location = given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/admin/sectors")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");

        return getIdFromUrl(location);
    }

    private String getCreateAdminToken() {
        TokenResponse tokenResponse = login(TEST_ADMIN_EMAIL, TEST_ADMIN_PASSWORD);
        return tokenResponse.getAccessToken();
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

    private void findNotExistsComment(String accessToken, Long commentId) {
        given()
            .header("X-AUTH-TOKEN", accessToken)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/comments/" + commentId)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private void deleteComment(String accessToken, Long commentId) {
        given()
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .delete("/comments/" + commentId)
            .then()
            .log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private String createPostWithoutImage(String accessToken, Long sectorId) {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);

        return given()
            .header("X-AUTH-TOKEN", accessToken)
            .body(postCreateRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/posts")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");
    }

    private String createComment(Long postId, String accessToken) {
        Map<String, String> params = new HashMap<>();
        params.put("writing", TEST_COMMENT_WRITING);

        return given()
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

    private String createCocomment(String accessToken, Long commentId, String writing) {
        Map<String, String> params = new HashMap<>();
        params.put("writing", writing);

        return given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .post("/posts/" + postId + "/comments/" + commentId + "/comments")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .header("Location");
    }

    private CommentResponse updateComment(String accessToken, Long commentId, String writing) {
        Map<String, String> params = new HashMap<>();
        params.put("writing", writing);

        return given()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .put("/comments/" + commentId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(CommentResponse.class);
    }

    private CommentResponse findComment(String accessToken, Long commentId) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .get("/comments/" + commentId)
            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(CommentResponse.class);
    }

    private void pressCommentZzang(Long commentId, String accessToken) {
        given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .header("X-AUTH-TOKEN", accessToken)
            .when()
            .post("/comments/" + commentId + "/zzangs")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
