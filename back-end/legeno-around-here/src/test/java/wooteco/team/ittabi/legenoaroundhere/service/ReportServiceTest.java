package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AUTH_NUMBER;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_EMPTY_MULTIPART_FILES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_REPORT_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_REQUEST;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth.MailAuth;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostReportCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostReportResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.repository.MailAuthRepository;

public class ReportServiceTest extends ServiceTest {

    private static final String TEST_PREFIX = "post_";

    @Autowired
    private ReportService reportService;

    @Autowired
    private PostService postService;

    @Autowired
    private SectorService sectorService;

    @MockBean
    private MailAuthRepository mailAuthRepository;

    private Long postId;

    @BeforeEach
    void setUp() {
        MailAuth mailAuth = new MailAuth(TEST_USER_EMAIL, TEST_AUTH_NUMBER);
        when(mailAuthRepository.findByEmail(any())).thenReturn(java.util.Optional.of(mailAuth));

        User user = createUser(TEST_PREFIX + TEST_USER_EMAIL,
            TEST_USER_NICKNAME,
            TEST_USER_PASSWORD);
        setAuthentication(user);

        SectorResponse sector = sectorService.createSector(TEST_SECTOR_REQUEST);
        Long sectorId = sector.getId();
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_IMAGE_EMPTY_MULTIPART_FILES, TEST_AREA_ID, sectorId);
        postId = postService.createPost(postCreateRequest).getId();
    }

    @DisplayName("글 신고 조회 - 성공")
    @Test
    void findPostReport_SuccessToCreate() {
        PostReportCreateRequest postReportCreateRequest = new PostReportCreateRequest(
            TEST_POST_REPORT_WRITING);

        PostReportResponse postReportCreateResponse = postService
            .createPostReport(postId, postReportCreateRequest);

        PostReportResponse postReportResponse = reportService
            .findPostReport(postReportCreateResponse.getId());

        assertThat(postReportResponse.getId()).isNotNull();
        assertThat(postReportResponse.getWriting()).isEqualTo(TEST_POST_REPORT_WRITING);
    }

}
