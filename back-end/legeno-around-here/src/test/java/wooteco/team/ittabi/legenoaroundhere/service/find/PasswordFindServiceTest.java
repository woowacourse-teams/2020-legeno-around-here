package wooteco.team.ittabi.legenoaroundhere.service.find;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_NEW_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import wooteco.team.ittabi.legenoaroundhere.dto.PasswordFindRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;
import wooteco.team.ittabi.legenoaroundhere.service.MailAuthService;
import wooteco.team.ittabi.legenoaroundhere.service.ServiceTest;

public class PasswordFindServiceTest extends ServiceTest {

    private static final String NOT_EXIST_PREFIX = "no";

    @MockBean
    private MailAuthService mailAuthService;

    @Autowired
    private UserRepository userRepository;

    private PasswordFindService passwordFindService;

    @BeforeEach
    void setUp() {
        passwordFindService = new PasswordFindService(userRepository, mailAuthService);
    }

    @DisplayName("비밀번호 찾기 성공")
    @Test
    void findPassword() {
        PasswordFindRequest passwordFindRequest = new PasswordFindRequest(TEST_USER_NICKNAME,
            TEST_USER_EMAIL);

        assertThatCode(() -> passwordFindService.findPassword(passwordFindRequest))
            .doesNotThrowAnyException();
    }

    @DisplayName("비밀번호 찾기 예외, DB에 닉네임이 존재하지 않는 경우")
    @Test
    void findPassword_NotExistsNickname_ThrownException() {
        PasswordFindRequest passwordFindRequest = new PasswordFindRequest(
            NOT_EXIST_PREFIX + TEST_USER_NICKNAME, TEST_USER_EMAIL);

        assertThatThrownBy(() -> passwordFindService.findPassword(passwordFindRequest))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("비밀번호 찾기 예외, DB에 이메일이 존재하지 않는 경우")
    @Test
    void findPassword_NotExistsEmail_ThrownException() {
        PasswordFindRequest passwordFindRequest = new PasswordFindRequest(
            TEST_USER_NICKNAME, NOT_EXIST_PREFIX + TEST_NEW_USER_EMAIL);

        assertThatThrownBy(() -> passwordFindService.findPassword(passwordFindRequest))
            .isInstanceOf(NotExistsException.class);
    }
}
