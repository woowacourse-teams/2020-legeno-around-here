package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.EMPTY_MULTIPART_FILES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_REQUEST;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_PASSWORD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.team.ittabi.legenoaroundhere.domain.post.like.LikeState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.LikeResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;

public class LikeServiceTest extends ServiceTest {

    private User user;
    private PostResponse postResponse;

    @Autowired
    private PostService postService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private SectorService sectorService;

    @BeforeEach
    void setUp() {
        user = createUser(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        setAuthentication(user);

        Long sectorId = sectorService.createSector(TEST_SECTOR_REQUEST).getId();
        PostCreateRequest postCreateRequest
            = new PostCreateRequest(TEST_WRITING, EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        postResponse = postService.createPost(postCreateRequest);
    }

    @DisplayName("비활성화된 좋아요를 활성화")
    @Test
    void pressLike_activeLike_SuccessToActiveLike() {
        LikeResponse activateLikeResponse = likeService.pressLike(postResponse.getId());

        assertThat(activateLikeResponse.getLikeCount()).isEqualTo(1L);
        assertThat(activateLikeResponse.getLikeState()).isEqualTo(LikeState.ACTIVATED.name());
    }

    @DisplayName("활성화된 좋아요를 비활성화")
    @Test
    void pressLike_inactiveLike_SuccessToInactiveLike() {
        likeService.pressLike(postResponse.getId());
        LikeResponse inactivatedLikeResponse = likeService.pressLike(postResponse.getId());

        assertThat(inactivatedLikeResponse.getLikeCount()).isEqualTo(0L);
        assertThat(inactivatedLikeResponse.getLikeState()).isEqualTo(LikeState.INACTIVATED.name());
    }
}
