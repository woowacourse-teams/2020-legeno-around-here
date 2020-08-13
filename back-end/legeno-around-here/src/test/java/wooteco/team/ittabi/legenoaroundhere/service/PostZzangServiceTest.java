package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_EMPTY_MULTIPART_FILES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_REQUEST;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.team.ittabi.legenoaroundhere.domain.post.zzang.ZzangState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostZzangResponse;

public class PostZzangServiceTest extends ServiceTest {

    private User user;
    private PostResponse postResponse;

    @Autowired
    private PostService postService;

    @Autowired
    private PostZzangService postZzangService;

    @Autowired
    private SectorService sectorService;

    @BeforeEach
    void setUp() {
        user = createUser(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        setAuthentication(user);

        Long sectorId = sectorService.createSector(TEST_SECTOR_REQUEST).getId();
        PostCreateRequest postCreateRequest
            = new PostCreateRequest(TEST_POST_WRITING, TEST_IMAGE_EMPTY_MULTIPART_FILES,
            TEST_AREA_ID, sectorId);
        postResponse = postService.createPost(postCreateRequest);
    }

    @DisplayName("비활성화된 좋아요를 활성화")
    @Test
    void pressPostZzang_activePostZzang_SuccessToActivePostZzang() {
        PostZzangResponse activatePostZzangResponse = postZzangService
            .pressPostZzang(postResponse.getId());

        assertThat(activatePostZzangResponse.getCount()).isEqualTo(1L);
        assertThat(activatePostZzangResponse.getState())
            .isEqualTo(ZzangState.ACTIVATED.name());
    }

    @DisplayName("활성화된 좋아요를 비활성화")
    @Test
    void pressPostZzang_inactivePostZzang_SuccessToInactivePostZzang() {
        postZzangService.pressPostZzang(postResponse.getId());
        PostZzangResponse inactivatedPostZzangResponse = postZzangService
            .pressPostZzang(postResponse.getId());

        assertThat(inactivatedPostZzangResponse.getCount()).isEqualTo(0L);
        assertThat(inactivatedPostZzangResponse.getState())
            .isEqualTo(ZzangState.INACTIVATED.name());
    }
}
