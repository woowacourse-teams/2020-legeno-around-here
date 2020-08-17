package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_EMPTY_MULTIPART_FILES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_INVALID_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_REQUEST;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_OTHER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentZzangResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;

public class CommentServiceTest extends ServiceTest {

    private User user;
    private User another;
    private PostResponse postResponse;

    @Autowired
    private SectorService sectorService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        user = createUser(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        another = createUser(TEST_USER_OTHER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        setAuthentication(user);

        Long sectorId = sectorService.createSector(TEST_SECTOR_REQUEST).getId();
        PostCreateRequest postCreateRequest
            = new PostCreateRequest(TEST_POST_WRITING, TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_ID, sectorId);
        postResponse = postService.createPost(postCreateRequest);
    }

    @DisplayName("댓글 생성 - 성공")
    @Test
    void createComment_SuccessToCreate() {
        CommentRequest commentRequest = new CommentRequest(TEST_POST_WRITING);

        CommentResponse createdCommentResponse = commentService
            .createComment(postResponse.getId(), commentRequest);

        assertThat(createdCommentResponse.getId()).isNotNull();
        assertThat(createdCommentResponse.getWriting()).isEqualTo(TEST_POST_WRITING);
        assertThat(createdCommentResponse.getCreator()).isEqualTo(UserResponse.from(user));
        assertThat(createdCommentResponse.getZzang()).isNotNull();
    }

    @DisplayName("댓글 조회 - 성공")
    @Test
    void findComment_SuccessToFind() {
        CommentRequest commentRequest = new CommentRequest(TEST_POST_WRITING);
        CommentResponse createdCommentResponse = commentService
            .createComment(postResponse.getId(), commentRequest);

        CommentResponse foundCommentResponse = commentService
            .findComment(createdCommentResponse.getId());

        assertThat(foundCommentResponse.getId()).isEqualTo(createdCommentResponse.getId());
        assertThat(foundCommentResponse.getWriting())
            .isEqualTo(createdCommentResponse.getWriting());
        assertThat(foundCommentResponse.getCreator())
            .isEqualTo(createdCommentResponse.getCreator());
        assertThat(createdCommentResponse.getZzang()).isNotNull();
    }


    @DisplayName("댓글 조회 - 성공, POST ID로 조회 후 댓글 개수 확인")
    @Test
    void findPost_FindByPostID_SuccessToFind() {
        CommentRequest commentRequest = new CommentRequest(TEST_POST_WRITING);

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
        CommentRequest commentRequest = new CommentRequest(TEST_POST_WRITING);
        CommentResponse commentResponse = commentService
            .createComment(postResponse.getId(), commentRequest);
        commentService.deleteComment(commentResponse.getId());

        assertThatThrownBy(() -> commentService.findComment(commentResponse.getId()))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("댓글 조회 - 실패, 찾는 Comment ID가 없을 경우")
    @Test
    void findComment_HasNotCommentId_ThrownException() {
        assertThatThrownBy(() -> commentService.findComment(TEST_POST_INVALID_ID))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("댓글 조회 - 성공")
    @Test
    void findAllComment_SuccessToFind() {
        CommentRequest commentRequest = new CommentRequest(TEST_POST_WRITING);
        commentService.createComment(postResponse.getId(), commentRequest);

        List<CommentResponse> commentResponses = commentService
            .findAllCommentBy(postResponse.getId());

        assertThat(commentResponses).hasSize(1);
    }

    @DisplayName("댓글 삭제 - 성공")
    @Test
    void deleteComment_SuccessToDelete() {
        CommentRequest commentRequest = new CommentRequest(TEST_POST_WRITING);
        CommentResponse commentResponse = commentService
            .createComment(postResponse.getId(), commentRequest);

        commentService.deleteComment(commentResponse.getId());

        List<CommentResponse> commentResponses = commentService
            .findAllCommentBy(postResponse.getId());
        assertThat(commentResponses).hasSize(0);
    }

    @DisplayName("댓글 삭제 - 실패, 댓글 작성자가 아님")
    @Test
    void deleteComment_IfNotCreator_ThrowException() {
        CommentRequest commentRequest = new CommentRequest(TEST_POST_WRITING);
        CommentResponse commentResponse = commentService
            .createComment(postResponse.getId(), commentRequest);

        setAuthentication(another);
        assertThatThrownBy(
            () -> commentService.deleteComment(commentResponse.getId()))
            .isInstanceOf(NotAuthorizedException.class);
    }


    @DisplayName("비활성화된 좋아요를 활성화")
    @Test
    void pressCommentZzang_activeCommentZzang_SuccessToActiveCommentZzang() {
        CommentRequest commentRequest = new CommentRequest(TEST_POST_WRITING);

        Long commentId = commentService.createComment(postResponse.getId(), commentRequest).getId();

        commentService.pressZzang(commentId);

        CommentResponse comment = commentService.findComment(commentId);
        CommentZzangResponse zzang = comment.getZzang();

        assertThat(zzang.getCount()).isEqualTo(1L);
        assertThat(zzang.isActivated()).isTrue();
    }

    @DisplayName("활성화된 좋아요를 비활성화")
    @Test
    void pressCommentZzang_inactiveCommentZzang_SuccessToInactiveCommentZzang() {
        CommentRequest commentRequest = new CommentRequest(TEST_POST_WRITING);

        Long commentId = commentService.createComment(postResponse.getId(), commentRequest).getId();

        commentService.pressZzang(commentId);
        commentService.pressZzang(commentId);

        CommentResponse comment = commentService.findComment(commentId);
        CommentZzangResponse zzang = comment.getZzang();

        assertThat(zzang.getCount()).isEqualTo(0L);
        assertThat(zzang.isActivated()).isFalse();
    }
}
