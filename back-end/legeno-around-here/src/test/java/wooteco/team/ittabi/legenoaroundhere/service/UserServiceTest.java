package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_OTHER_PASSWORD;
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
import wooteco.team.ittabi.legenoaroundhere.dto.UserPasswordUpdateRequest;
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

    @DisplayName("User 생성")
    @Test
    void createUser_Success() {
        UserCreateRequest userCreateRequest =
            new UserCreateRequest(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD,
                TEST_AREA_ID);
        Long userId = userService.createUser(userCreateRequest);

        assertThat(userId).isNotNull();
    }

    @DisplayName("User 생성, 실패 - 중복된 이메일")
    @Test
    void createUser_DuplicatedEmail_ThrownException() {
        UserCreateRequest userCreateRequest = new UserCreateRequest(TEST_USER_EMAIL,
            TEST_USER_NICKNAME, TEST_USER_PASSWORD, TEST_AREA_ID);
        userService.createUser(userCreateRequest);

        assertThatThrownBy(() -> userService.createUser(userCreateRequest))
            .isInstanceOf(WrongUserInputException.class);
    }

    @DisplayName("로그인")
    @Test
    void login_Success() {
        UserCreateRequest userCreateRequest =
            new UserCreateRequest(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD,
                TEST_AREA_ID);
        userService.createUser(userCreateRequest);

        TokenResponse response = userService.login(new LoginRequest(TEST_USER_EMAIL,
            TEST_USER_PASSWORD));
        assertThat(response.getAccessToken()).hasSizeGreaterThan(TOKEN_MIN_SIZE);
    }

    @DisplayName("로그인 - 존재하지 않는 이메일")
    @Test
    void login_IfEmailNotExist_ThrowException() {
        assertThatThrownBy(() -> userService.login(new LoginRequest(TEST_USER_EMAIL,
            TEST_USER_PASSWORD)))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("로그인 - 비밀번호가 틀렸을 때")
    @Test
    void login_IfPasswordIsWrong_ThrowException() {
        UserCreateRequest userCreateRequest = new UserCreateRequest(TEST_USER_EMAIL,
            TEST_USER_NICKNAME, TEST_USER_PASSWORD, TEST_AREA_ID);
        userService.createUser(userCreateRequest);

        assertThatThrownBy(() -> userService.login(new LoginRequest(TEST_USER_EMAIL, "wrong")))
            .isInstanceOf(WrongUserInputException.class);
    }

    @DisplayName("email로 User 찾기 (UserDetails 오버라이딩 메서드)")
    @Test
    void loadUserByUsername_Success() {
        UserCreateRequest userCreateRequest =
            new UserCreateRequest(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD,
                TEST_AREA_ID);
        userService.createUser(userCreateRequest);

        User user = (User) userService.loadUserByUsername(TEST_USER_EMAIL);

        assertThat(user.getEmailByString()).isEqualTo(TEST_USER_EMAIL);
    }

    @DisplayName("내 정보 찾기")
    @Test
    void findUser_Success() {
        User user = createUser(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        setAuthentication(user);

        assertThat(userService.findUser().getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(userService.findUser().getNickname()).isEqualTo(TEST_USER_NICKNAME);
    }

    @DisplayName("내 정보 수정")
    @Test
    void updateMyInfo_Success() {
        User createdUser = createUser(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        setAuthentication(createdUser);
        UserUpdateRequest userUpdateRequest
            = new UserUpdateRequest("newname", TEST_AREA_ID, null);

        UserResponse updatedUserResponse = userService.updateMyInfo(userUpdateRequest);

        assertThat(updatedUserResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(updatedUserResponse.getNickname()).isEqualTo("newname");
    }

    @DisplayName("내 비밀번호 수정, 성공")
    @Test
    void changeMyPassword_Success() {
        User createdUser = createUser(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        setAuthentication(createdUser);

        UserPasswordUpdateRequest userPasswordUpdateRequest
            = new UserPasswordUpdateRequest(TEST_USER_OTHER_PASSWORD);

        userService.changeMyPassword(userPasswordUpdateRequest);

        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_OTHER_PASSWORD);
        assertThat(userService.login(loginRequest)).isInstanceOf(TokenResponse.class);
    }

    @DisplayName("내 비밀번호 수정, 예외 발생 - 기존과 동일한 비밀번호")
    @Test
    void changeMyPassword_SamePassword_ThrownException() {
        User createdUser = createUser(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        setAuthentication(createdUser);

        UserPasswordUpdateRequest userPasswordUpdateRequest
            = new UserPasswordUpdateRequest(TEST_USER_PASSWORD);

        assertThatThrownBy(() -> userService.changeMyPassword(userPasswordUpdateRequest))
            .isInstanceOf(WrongUserInputException.class);
    }

    @DisplayName("회원 탈퇴")
    @Test
    void deleteUser_Success() {
        User createdUser = createUser(TEST_USER_EMAIL, TEST_USER_NICKNAME, TEST_USER_PASSWORD);
        setAuthentication(createdUser);

        userService.deleteUser();

        User authUser = (User) authenticationFacade.getAuthentication()
            .getPrincipal();
        assertThatThrownBy(() -> userService.loadUserByUsername(authUser.getEmailByString()))
            .isInstanceOf(UsernameNotFoundException.class);
    }
}
