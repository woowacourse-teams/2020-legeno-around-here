package wooteco.team.ittabi.legenoaroundhere.service.report;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.AreaConstants.TEST_AUTH_NUMBER;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.ImageConstants.TEST_IMAGE_CONTENT_TYPE;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.ReportConstants.TEST_REPORT_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.UserConstants.TEST_USER_INVALID_ID;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.UserConstants.TEST_USER_OTHER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.util.constants.UserConstants.TEST_USER_PASSWORD;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth.MailAuth;
import wooteco.team.ittabi.legenoaroundhere.dto.ReportCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserImageResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserReportResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserSimpleResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.MailAuthRepository;
import wooteco.team.ittabi.legenoaroundhere.service.ServiceTest;
import wooteco.team.ittabi.legenoaroundhere.util.TestConverterUtils;

class UserReportServiceTest extends ServiceTest {

    private static final String TEST_PREFIX = "user_";

    @Autowired
    private UserReportService userReportService;

    @MockBean
    private MailAuthRepository mailAuthRepository;

    private User user;
    private User another;

    @BeforeEach
    void setUp() {
        MailAuth mailAuth = new MailAuth(TEST_USER_EMAIL, TEST_AUTH_NUMBER);
        when(mailAuthRepository.findByEmail(any()))
            .thenReturn(java.util.Optional.of(mailAuth));

        user = createUser(TEST_PREFIX + TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        setAuthentication(user);

        another = createUser(TEST_PREFIX + TEST_USER_OTHER_EMAIL, TEST_USER_NICKNAME,
            TEST_USER_PASSWORD);
    }

    @DisplayName("사용자 신고 - 성공, 프로필 사진이 없을 때")
    @Test
    void createUserReport_HasNotImage_Success() {
        ReportCreateRequest reportCreateRequest
            = new ReportCreateRequest(another.getId(), TEST_REPORT_WRITING);

        UserReportResponse userReport = userReportService.createUserReport(reportCreateRequest);
        assertThat(userReport.getId()).isNotNull();
        assertThat(userReport.getReportWriting()).isEqualTo(TEST_REPORT_WRITING);
        assertThat(userReport.getUserImageUrl()).isNull();
        assertThat(userReport.getUserNickname()).isEqualTo(another.getNickname());
        assertThat(userReport.getReporter()).isEqualTo(UserSimpleResponse.from(user));
        assertThat(userReport.getCreatedAt()).isNotNull();
        assertThat(userReport.getModifiedAt()).isNotNull();
    }


    @DisplayName("사용자 신고 - 성공, 프로필 사진이 있을 때")
    @Test
    void createUserReport_HasImage_Success() throws IOException {
        setAuthentication(another);
        MultipartFile multipartFile
            = TestConverterUtils.convert("image1.jpg", TEST_IMAGE_CONTENT_TYPE);
        Long imageId = userService.uploadUserImage(multipartFile).getId();
        UserUpdateRequest userUpdateRequest
            = new UserUpdateRequest(another.getNickname(), another.getArea().getId(), imageId);
        UserResponse userResponse = userService.updateMe(userUpdateRequest);
        UserImageResponse image = userResponse.getImage();
        assertThat(image).isNotNull();

        setAuthentication(user);
        ReportCreateRequest reportCreateRequest
            = new ReportCreateRequest(another.getId(), TEST_REPORT_WRITING);

        UserReportResponse userReport = userReportService.createUserReport(reportCreateRequest);
        assertThat(userReport.getId()).isNotNull();
        assertThat(userReport.getReportWriting()).isEqualTo(TEST_REPORT_WRITING);
        assertThat(userReport.getUserImageUrl()).isEqualTo(image.getUrl());
        assertThat(userReport.getUserNickname()).isEqualTo(another.getNickname());
        assertThat(userReport.getReporter()).isEqualTo(UserSimpleResponse.from(user));
        assertThat(userReport.getCreatedAt()).isNotNull();
        assertThat(userReport.getModifiedAt()).isNotNull();
    }


    @DisplayName("사용자 신고 - 예외 발생, 유효하지 않은 사용자 ID")
    @Test
    void createUserReport_InvalidUserId_ThrownException() {
        ReportCreateRequest reportCreateRequest
            = new ReportCreateRequest(TEST_USER_INVALID_ID, TEST_REPORT_WRITING);

        assertThatThrownBy(() -> userReportService.createUserReport(reportCreateRequest))
            .isInstanceOf(NotExistsException.class);
    }
}
