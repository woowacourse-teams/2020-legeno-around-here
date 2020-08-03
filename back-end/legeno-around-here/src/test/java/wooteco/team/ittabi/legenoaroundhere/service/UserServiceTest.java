package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_MY_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_PASSWORD;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Nickname;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Password;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.LoginRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.infra.JwtTokenGenerator;

@DataJpaTest
@Import({UserService.class, JwtTokenGenerator.class})
class UserServiceTest {

    private static final int TOKEN_MIN_SIZE = 1;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("User 생성")
    void createUser() {
        UserCreateRequest userCreateRequest =
            new UserCreateRequest(TEST_MY_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        Long userId = userService.createUser(userCreateRequest);

        assertThat(userId).isNotNull();
    }

    @Test
    @DisplayName("로그인")
    void login() {
        UserCreateRequest userCreateRequest =
            new UserCreateRequest(TEST_MY_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        userService.createUser(userCreateRequest);

        TokenResponse response = userService.login(new LoginRequest(TEST_MY_EMAIL, TEST_PASSWORD));
        assertThat(response.getAccessToken()).hasSizeGreaterThan(TOKEN_MIN_SIZE);
    }

    @Test
    @DisplayName("로그인 - 존재하지 않는 이메일")
    void login_IfEmailNotExist_ThrowException() {
        assertThatThrownBy(() -> userService.login(new LoginRequest(TEST_MY_EMAIL, TEST_PASSWORD)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("로그인 - 비밀번호가 틀렸을 때")
    void login_IfPasswordIsWrong_ThrowException() {
        UserCreateRequest userCreateRequest =
            new UserCreateRequest(TEST_MY_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        userService.createUser(userCreateRequest);

        assertThatThrownBy(() -> userService.login(new LoginRequest(TEST_MY_EMAIL, "wrong")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("email로 User 찾기 (UserDetails 오버라이딩 메서드)")
    void loadUserByUsername() {
        UserCreateRequest userCreateRequest =
            new UserCreateRequest(TEST_MY_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        userService.createUser(userCreateRequest);

        User user = (User) userService.loadUserByUsername(TEST_MY_EMAIL);

        assertThat(user.getEmailByString()).isEqualTo(TEST_MY_EMAIL);
    }

    @Test
    @DisplayName("내정보 찾기")
    void findUser() {
        User user = User.builder()
            .email(new Email(TEST_MY_EMAIL))
            .nickname(new Nickname(TEST_NICKNAME))
            .password(new Password(TEST_PASSWORD))
            .build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        assertThat(userService.findUser(authentication).getEmail()).isEqualTo(TEST_MY_EMAIL);
        assertThat(userService.findUser(authentication).getNickname()).isEqualTo(TEST_NICKNAME);
    }
}
