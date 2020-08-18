package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.LoginRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

class UserServiceTest extends ServiceTest {

    private static final int TOKEN_MIN_SIZE = 1;

    @Autowired
    private UserService userService;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @Test
    @DisplayName("User 생성")
    void createUser_Success() {
        UserCreateRequest userCreateRequest =
            new UserCreateRequest(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD,
                TEST_AREA_ID);
        Long userId = userService.createUser(userCreateRequest);

        assertThat(userId).isNotNull();
    }

    @Test
    @DisplayName("User 생성, 실패 - 중복된 이메일")
    void createUser_DuplicationEmail_ThrownException() {
        UserCreateRequest userCreateRequest = new UserCreateRequest(TEST_USER_EMAIL,
            TEST_USER_NICKNAME, TEST_USER_PASSWORD, TEST_AREA_ID);
        userService.createUser(userCreateRequest);

        assertThatThrownBy(() -> userService.createUser(userCreateRequest))
            .isInstanceOf(WrongUserInputException.class);
    }

    @Test
    @DisplayName("로그인")
    void login() {
        UserCreateRequest userCreateRequest =
            new UserCreateRequest(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD,
                TEST_AREA_ID);
        userService.createUser(userCreateRequest);

        TokenResponse response = userService.login(new LoginRequest(TEST_USER_EMAIL,
            TEST_USER_PASSWORD));
        assertThat(response.getAccessToken()).hasSizeGreaterThan(TOKEN_MIN_SIZE);
    }

    @Test
    @DisplayName("로그인 - 존재하지 않는 이메일")
    void login_IfEmailNotExist_ThrowException() {
        assertThatThrownBy(() -> userService.login(new LoginRequest(TEST_USER_EMAIL,
            TEST_USER_PASSWORD)))
            .isInstanceOf(NotExistsException.class);
    }

    @Test
    @DisplayName("로그인 - 비밀번호가 틀렸을 때")
    void login_IfPasswordIsWrong_ThrowException() {
        UserCreateRequest userCreateRequest = new UserCreateRequest(TEST_USER_EMAIL,
            TEST_USER_NICKNAME, TEST_USER_PASSWORD, TEST_AREA_ID);
        userService.createUser(userCreateRequest);

        assertThatThrownBy(() -> userService.login(new LoginRequest(TEST_USER_EMAIL, "wrong")))
            .isInstanceOf(WrongUserInputException.class);
    }

    @Test
    @DisplayName("email로 User 찾기 (UserDetails 오버라이딩 메서드)")
    void loadUserByUsername() {
        UserCreateRequest userCreateRequest =
            new UserCreateRequest(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD,
                TEST_AREA_ID);
        userService.createUser(userCreateRequest);

        User user = (User) userService.loadUserByUsername(TEST_USER_EMAIL);

        assertThat(user.getEmailByString()).isEqualTo(TEST_USER_EMAIL);
    }

    @Test
    @DisplayName("내 정보 찾기")
    void findUser() {
        User user = createUser(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        setAuthentication(user);

        assertThat(userService.findUser().getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(userService.findUser().getNickname()).isEqualTo(TEST_USER_NICKNAME);
    }

    @Test
    @DisplayName("내 정보 수정")
    void updateUser() {
        User createdUser = createUser(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        setAuthentication(createdUser);
        UserUpdateRequest userUpdateRequest
            = new UserUpdateRequest("newname", "newpassword", TEST_AREA_ID, null);

        UserResponse updatedUserResponse = userService.updateUser(userUpdateRequest);

        assertThat(updatedUserResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(updatedUserResponse.getNickname()).isEqualTo("newname");

        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, "newpassword");
        assertThat(userService.login(loginRequest)).isInstanceOf(TokenResponse.class);
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteUser() {
        User createdUser = createUser(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        setAuthentication(createdUser);

        userService.deleteUser();

        User authUser = (User) authenticationFacade.getAuthentication()
            .getPrincipal();
        assertThatThrownBy(() -> userService.loadUserByUsername(authUser.getEmailByString()))
            .isInstanceOf(UsernameNotFoundException.class);
    }
}
