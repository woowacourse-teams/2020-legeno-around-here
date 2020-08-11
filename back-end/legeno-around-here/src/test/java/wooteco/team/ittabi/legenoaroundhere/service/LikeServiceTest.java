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
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.team.ittabi.legenoaroundhere.domain.State;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.LikeResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;

public class LikeServiceTest extends ServiceTest {

    private User user;
    private User another;
    private PostResponse postResponse;

    @Autowired
    private PostService postService;

    @Autowired
    private LikeService likeService;

    @BeforeEach
    void setUp() {
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
