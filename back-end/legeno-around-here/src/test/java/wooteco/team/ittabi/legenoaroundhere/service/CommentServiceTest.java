package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageTestConstants.EMPTY_MULTIPART_FILES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostTestConstants.TEST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_ANOTHER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_MY_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_PASSWORD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.team.ittabi.legenoaroundhere.aws.S3Uploader;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.CommentRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest extends AuthServiceTest {

    private User user;
    private User another;
    private PostResponse postResponse;
    private PostService postService;
    private CommentService commentService;

    @Mock
    private S3Uploader s3Uploader;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, new ImageService(s3Uploader));
        commentService = new CommentService(postService, commentRepository);

        user = createUser(TEST_MY_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
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
        assertThat(createdCommentResponse.getUser()).isEqualTo(UserResponse.from(user));
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
        assertThat(foundCommentResponse.getUser()).isEqualTo(createdCommentResponse.getUser());
    }

//    @DisplayName("댓글 조회 - 실패, 찾는 Comment가 이미 삭제 됐을 경우")
//    @Test
//    void findComment_AlreadyDeletedComment_ThrownException() {
//        CommentRequest commentRequest = new CommentRequest(TEST_WRITING);
//        CommentResponse commentResponse = commentService.createComment(postResponse.getId(), commentRequest);
//        commentService.deleteComment(commentResponse.getId());
//
//        assertThatThrownBy(() -> commentService.findComment(commentResponse.getId()))
//            .isInstanceOf(NotExistsException.class);
//    }

    @DisplayName("댓글 조회 - 실패, 찾는 Comment ID가 없을 경우")
    @Test
    void findComment_HasNotCommentId_ThrownException() {
        Long invalidId = -1L;

        assertThatThrownBy(() -> commentService.findComment(invalidId))
            .isInstanceOf(NotExistsException.class);
    }

}
