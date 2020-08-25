package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AUTH_NUMBER;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_EMPTY_IMAGES;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_INVALID_ID;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth.MailAuth;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostReportCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.PostReportResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.SectorResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
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

    private User user;
    private Long postId;
    private Long sectorId;

    @BeforeEach
    void setUp() {
        MailAuth mailAuth = new MailAuth(TEST_USER_EMAIL, TEST_AUTH_NUMBER);
        when(mailAuthRepository.findByEmail(any())).thenReturn(java.util.Optional.of(mailAuth));

        user = createUser(TEST_PREFIX + TEST_USER_EMAIL,
            TEST_USER_NICKNAME,
            TEST_USER_PASSWORD);
        setAuthentication(user);

        SectorResponse sector = sectorService.createSector(TEST_SECTOR_REQUEST);
        sectorId = sector.getId();
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        postId = postService.createPost(postCreateRequest).getId();
    }


    @DisplayName("글 신고 생성 - 성공")
    @Test
    void createPostReport_SuccessToCreate() {
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        Long postId = postService.createPost(postCreateRequest).getId();

        PostReportCreateRequest postReportCreateRequest = new PostReportCreateRequest(
            TEST_POST_REPORT_WRITING);

        PostReportResponse postReportResponse = reportService
            .createPostReport(postId, postReportCreateRequest);

        assertThat(postReportResponse.getId()).isNotNull();
        assertThat(postReportResponse.getWriting()).isEqualTo(TEST_POST_REPORT_WRITING);
        assertThat(postReportResponse.getCreator()).isEqualTo(UserResponse.from(user));
    }

    @DisplayName("글 신고 조회 - 성공")
    @Test
    void findPostReport_SuccessToFind() {
        PostReportCreateRequest postReportCreateRequest = new PostReportCreateRequest(
            TEST_POST_REPORT_WRITING);

        PostReportResponse postReportCreateResponse = reportService
            .createPostReport(postId, postReportCreateRequest);

        PostReportResponse postReportResponse = reportService
            .findPostReport(postReportCreateResponse.getId());

        assertThat(postReportResponse.getId()).isNotNull();
        assertThat(postReportResponse.getWriting()).isEqualTo(TEST_POST_REPORT_WRITING);
    }

    @DisplayName("글 신고 조회 - 예외 발생, ID가 없는 경우")
    @Test
    void findPostReport_HasNotId_ThrowException() {
        assertThatThrownBy(() -> reportService.findPostReport(TEST_POST_INVALID_ID))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("페이지 별 글 신고 조회 - 성공")
    @Test
    void findPostReportByPage_SuccessToFind() {
        PostReportCreateRequest postReportCreateRequest = new PostReportCreateRequest(
            TEST_POST_REPORT_WRITING);
        PostReportResponse postReportResponse = reportService
            .createPostReport(postId, postReportCreateRequest);

        Page<PostReportResponse> postReportPage = reportService
            .findPostReportByPage(Pageable.unpaged());

        assertThat(postReportPage.getContent()).contains(postReportResponse);
    }

    @DisplayName("글 신고 삭제 - 성공")
    @Test
    void deletePostReport_SuccessToDelete() {
        PostReportCreateRequest postReportCreateRequest = new PostReportCreateRequest(
            TEST_POST_REPORT_WRITING);

        PostReportResponse postReportCreateResponse = reportService
            .createPostReport(postId, postReportCreateRequest);

        reportService.deletePostReport(postReportCreateResponse.getId());

        assertThatThrownBy(() -> reportService.findPostReport(postReportCreateResponse.getId()));
    }

    @DisplayName("글 신고 삭제 - 예외 발생, ID가 없는 경우")
    @Test
    void deletePostReport_HasNotId_ThrowException() {
        assertThatThrownBy(() -> reportService.deletePostReport(TEST_POST_INVALID_ID))
            .isInstanceOf(NotExistsException.class);
    }
}
