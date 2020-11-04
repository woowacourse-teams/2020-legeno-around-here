package wooteco.team.ittabi.legenoaroundhere.service.report;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.AreaConstants.TEST_AUTH_NUMBER;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.CommentConstants.TEST_COMMENT_INVALID_ID;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.CommentConstants.TEST_COMMENT_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.ImageConstants.TEST_EMPTY_IMAGES;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.PostConstants.TEST_POST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.ReportConstants.TEST_REPORT_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.SectorConstants.TEST_SECTOR_REQUEST;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.UserConstants.TEST_USER_PASSWORD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth.MailAuth;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentReportResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.CommentResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.PostCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.ReportCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserSimpleResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.MailAuthRepository;
import wooteco.team.ittabi.legenoaroundhere.service.CommentService;
import wooteco.team.ittabi.legenoaroundhere.service.PostService;
import wooteco.team.ittabi.legenoaroundhere.service.SectorService;
import wooteco.team.ittabi.legenoaroundhere.service.ServiceTest;

class CommentReportServiceTest extends ServiceTest {

    private static final String TEST_PREFIX = "comment_";

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private SectorService sectorService;

    @MockBean
    private MailAuthRepository mailAuthRepository;

    @Autowired
    private CommentReportService commentReportService;

    private User user;
    private Long postId;

    @BeforeEach
    void setUp() {
        MailAuth mailAuth = new MailAuth(TEST_USER_EMAIL, TEST_AUTH_NUMBER);
        when(mailAuthRepository.findByEmail(any()))
            .thenReturn(java.util.Optional.of(mailAuth));

        user = createUser(TEST_PREFIX + TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        setAuthentication(user);

        Long sectorId = sectorService.createSector(TEST_SECTOR_REQUEST).getId();
        PostCreateRequest postCreateRequest = new PostCreateRequest(TEST_POST_WRITING,
            TEST_EMPTY_IMAGES, TEST_AREA_ID, sectorId);
        postId = postService.createPost(postCreateRequest).getId();
    }

    @DisplayName("댓글 신고 - 성공")
    @Test
    void createCommentReport_Success() {
        CommentRequest commentRequest = new CommentRequest(TEST_COMMENT_WRITING);
        CommentResponse comment = commentService.createComment(postId, commentRequest);
        Long commentId = comment.getId();

        ReportCreateRequest reportCreateRequest
            = new ReportCreateRequest(commentId, TEST_REPORT_WRITING);
        CommentReportResponse commentReport
            = commentReportService.createCommentReport(reportCreateRequest);

        assertThat(commentReport.getId()).isNotNull();
        assertThat(commentReport.getReportWriting()).isEqualTo(TEST_REPORT_WRITING);
        assertThat(commentReport.getCommentWriting()).isEqualTo(TEST_COMMENT_WRITING);
        assertThat(commentReport.getReporter()).isEqualTo(UserSimpleResponse.from(user));
        assertThat(commentReport.getCreatedAt()).isNotNull();
        assertThat(commentReport.getModifiedAt()).isNotNull();
    }

    @DisplayName("댓글 신고 - 예외 발생, 유효하지 않은 댓글 ID")
    @Test
    void createCommentReport_InvalidCommentId_ThrownException() {
        ReportCreateRequest reportCreateRequest
            = new ReportCreateRequest(TEST_COMMENT_INVALID_ID, TEST_REPORT_WRITING);

        assertThatThrownBy(() -> commentReportService.createCommentReport(reportCreateRequest))
            .isInstanceOf(NotExistsException.class);
    }
}
