package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageTestConstants.EMPTY_MULTIPART_FILES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostTestConstants.TEST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_ANOTHER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_EMAIL;
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
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.State;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.LikeResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.repository.CommentRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.LikeRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest extends AuthServiceTest {

    private User user;
    private User another;
    private PostResponse postResponse;
    private PostService postService;
    private CommentService commentService;
    private LikeService likeService;

    @Mock
    private S3Uploader s3Uploader;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @BeforeEach
    void setUp() {
        likeService = new LikeService(postRepository, likeRepository);
        commentService = new CommentService(postRepository, commentRepository,
            authenticationFacade);
        postService = new PostService(postRepository, commentService,
            new ImageService(s3Uploader, authenticationFacade), likeService, authenticationFacade
        );

        user = createUser(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        another = createUser(TEST_ANOTHER_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        setAuthentication(user);

        PostRequest postRequest = new PostRequest(TEST_WRITING, EMPTY_MULTIPART_FILES);
        postResponse = postService.createPost(postRequest);
    }

    @DisplayName("비활성화된 좋아요를 활성화")
    @Test
    void pressLike_activeLike_SuccessToActiveLike() {
        LikeResponse activateLikeResponse = likeService.pressLike(postResponse.getId(), user);

        assertThat(activateLikeResponse.getLikeCount()).isEqualTo(1L);
        assertThat(activateLikeResponse.getState()).isEqualTo(State.ACTIVATED);
    }

    @DisplayName("활성화된 좋아요를 비활성화")
    @Test
    void pressLike_inactiveLike_SuccessToInactiveLike() {
        likeService.pressLike(postResponse.getId(), user);
        LikeResponse inactivatedLikeResponse = likeService.pressLike(postResponse.getId(), user);

        assertThat(inactivatedLikeResponse.getLikeCount()).isEqualTo(0L);
        assertThat(inactivatedLikeResponse.getState()).isEqualTo(State.INACTIVATED);
    }
}
