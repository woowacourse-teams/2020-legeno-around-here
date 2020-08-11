package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageTestConstants.EMPTY_MULTIPART_FILES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostTestConstants.TEST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_ANOTHER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_PASSWORD;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;

public class CommentServiceTest extends ServiceTest {

    private User user;
    private User another;
    private PostResponse postResponse;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        user = createUser(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        another = createUser(TEST_ANOTHER_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        setAuthentication(user);
        PostRequest postRequest = new PostRequest(TEST_WRITING, EMPTY_MULTIPART_FILES);
        postResponse = postService.createPost(postRequest);
    }

    @DisplayName("댓글 생성 - 성공")
    @Test
    void createComment_SuccessToCreate() {
        CommentRequest commentRequest = new CommentRequest(TEST_WRITING);

        CommentResponse createdCommentResponse = commentService
            .createComment(postResponse.getId(), commentRequest);

        assertThat(createdCommentResponse.getId()).isNotNull();
        assertThat(createdCommentResponse.getWriting()).isEqualTo(TEST_WRITING);
        assertThat(createdCommentResponse.getCreator()).isEqualTo(UserResponse.from(user));
    }

    @DisplayName("댓글 조회 - 성공")
    @Test
    void findComment_SuccessToFind() {
        CommentRequest commentRequest = new CommentRequest(TEST_WRITING);
        CommentResponse createdCommentResponse = commentService
            .createComment(postResponse.getId(), commentRequest);

        CommentResponse foundCommentResponse = commentService
            .findComment(createdCommentResponse.getId());

        assertThat(foundCommentResponse.getId()).isEqualTo(createdCommentResponse.getId());
        assertThat(foundCommentResponse.getWriting())
            .isEqualTo(createdCommentResponse.getWriting());
        assertThat(foundCommentResponse.getCreator())
            .isEqualTo(createdCommentResponse.getCreator());
    }


    @DisplayName("댓글 조회 - 성공, POST ID로 조회 후 댓글 개수 확인")
    @Test
    void findPost_FindByPostID_SuccessToFind() {
        CommentRequest commentRequest = new CommentRequest(TEST_WRITING);

        CommentResponse commentResponse = commentService
            .createComment(postResponse.getId(), commentRequest);
        commentService.createComment(postResponse.getId(), commentRequest);
        commentService.createComment(postResponse.getId(), commentRequest);
        commentService.deleteComment(commentResponse.getId());

        PostResponse foundPostResponse = postService.findPost(postResponse.getId());
        assertThat(foundPostResponse.getComments()).hasSize(2);
    }

    @DisplayName("댓글 조회 - 실패, 찾는 Comment가 이미 삭제 됐을 경우")
    @Test
    void findComment_AlreadyDeletedComment_ThrownException() {
        CommentRequest commentRequest = new CommentRequest(TEST_WRITING);
        CommentResponse commentResponse = commentService
            .createComment(postResponse.getId(), commentRequest);
        commentService.deleteComment(commentResponse.getId());

        assertThatThrownBy(() -> commentService.findComment(commentResponse.getId()))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("댓글 조회 - 실패, 찾는 Comment ID가 없을 경우")
    @Test
    void findComment_HasNotCommentId_ThrownException() {
        Long invalidId = -1L;

        assertThatThrownBy(() -> commentService.findComment(invalidId))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("댓글 조회 - 성공")
    @Test
    void findAllComment_SuccessToFind() {
        CommentRequest commentRequest = new CommentRequest(TEST_WRITING);
        commentService.createComment(postResponse.getId(), commentRequest);

        List<CommentResponse> commentResponses = commentService
            .findAllComment(postResponse.getId());

        assertThat(commentResponses).hasSize(1);
    }

    @DisplayName("댓글 삭제 - 성공")
    @Test
    void deleteComment_SuccessToDelete() {
        CommentRequest commentRequest = new CommentRequest(TEST_WRITING);
        CommentResponse commentResponse = commentService
            .createComment(postResponse.getId(), commentRequest);

        commentService.deleteComment(commentResponse.getId());

        List<CommentResponse> commentResponses = commentService
            .findAllComment(postResponse.getId());
        assertThat(commentResponses).hasSize(0);
    }

    @DisplayName("댓글 삭제 - 실패, 댓글 작성자가 아님")
    @Test
    void deleteComment_IfNotCreator_ThrowException() {
        CommentRequest commentRequest = new CommentRequest(TEST_WRITING);
        CommentResponse commentResponse = commentService
            .createComment(postResponse.getId(), commentRequest);

        setAuthentication(another);
        assertThatThrownBy(
            () -> commentService.deleteComment(commentResponse.getId()))
            .isInstanceOf(NotAuthorizedException.class);
    }
}
