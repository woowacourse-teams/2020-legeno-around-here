package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_ADMIN_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_ADMIN_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_ADMIN_PASSWORD;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_PASSWORD;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.LoginRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;
import wooteco.team.ittabi.legenoaroundhere.infra.JwtTokenGenerator;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;

@AutoConfigureWebMvc
@DataJpaTest
@Import({UserService.class, JwtTokenGenerator.class})
class UserServiceTest {

    private static final int TOKEN_MIN_SIZE = 1;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("User 생성")
    void createUser() {
        UserRequest userRequest =
            new UserRequest(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        Long userId = userService.createUser(userRequest);

        assertThat(userId).isNotNull();
    }

    @Test
    @DisplayName("Admin 생성")
    void createAdmin() {
        UserRequest adminCreateRequest =
            new UserRequest(TEST_ADMIN_EMAIL, TEST_ADMIN_NICKNAME, TEST_ADMIN_PASSWORD);
        Long userId = userService.createAdmin(adminCreateRequest);

        assertThat(userId).isNotNull();
    }

    @Test
    @DisplayName("로그인")
    void login() {
        UserRequest userRequest =
            new UserRequest(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        userService.createUser(userRequest);

        TokenResponse response = userService.login(new LoginRequest(TEST_EMAIL, TEST_PASSWORD));
        assertThat(response.getAccessToken()).hasSizeGreaterThan(TOKEN_MIN_SIZE);
    }

    @Test
    @DisplayName("로그인 - 존재하지 않는 이메일")
    void login_IfEmailNotExist_ThrowException() {
        assertThatThrownBy(() -> userService.login(new LoginRequest(TEST_EMAIL, TEST_PASSWORD)))
            .isInstanceOf(NotExistsException.class);
    }

    @Test
    @DisplayName("로그인 - 비밀번호가 틀렸을 때")
    void login_IfPasswordIsWrong_ThrowException() {
        UserRequest userRequest =
            new UserRequest(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        userService.createUser(userRequest);

        assertThatThrownBy(() -> userService.login(new LoginRequest(TEST_EMAIL, "wrong")))
            .isInstanceOf(WrongUserInputException.class);
    }

    @Test
    @DisplayName("email로 User 찾기 (UserDetails 오버라이딩 메서드)")
    void loadUserByUsername() {
        UserRequest userRequest =
            new UserRequest(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        userService.createUser(userRequest);

        User user = (User) userService.loadUserByUsername(TEST_EMAIL);

        assertThat(user.getEmailByString()).isEqualTo(TEST_EMAIL);
    }

    @Test
    @DisplayName("내 정보 찾기")
    void findUser() {
        User user = createUser(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        setAuthentication(user);

        assertThat(userService.findUser().getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(userService.findUser().getNickname()).isEqualTo(TEST_NICKNAME);
    }

    private User createUser(String email, String nickname, String password) {
        UserRequest userRequest = new UserRequest(email, nickname, password);
        Long userId = userService.createUser(userRequest);

        return userRepository.findById(userId)
            .orElseThrow(() -> new NotExistsException("해당하는 사용자가 존재하지 않습니다."));
    }

    private void setAuthentication(User user) {
        UserDetails userDetails = userService.loadUserByUsername(user.getEmailByString());
        Authentication authToken = new UsernamePasswordAuthenticationToken(
            user, "TestCredentials", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    @Test
    @DisplayName("내 정보 수정")
    void updateUser() {
        User createdUser = createUser(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        setAuthentication(createdUser);
        UserRequest userUpdateRequest = new UserRequest(null, "newname", "newpassword");

        UserResponse updatedUserResponse = userService.updateUser(userUpdateRequest);

        assertThat(updatedUserResponse.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(updatedUserResponse.getNickname()).isEqualTo("newname");

        LoginRequest loginRequest = new LoginRequest(TEST_EMAIL, "newpassword");
        assertThat(userService.login(loginRequest)).isInstanceOf(TokenResponse.class);
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteUser() {
        User createdUser = createUser(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        setAuthentication(createdUser);

        userService.deleteUser();

        User authUser = (User) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        assertThatThrownBy(() -> userService.loadUserByUsername(authUser.getEmailByString()))
            .isInstanceOf(UsernameNotFoundException.class);
    }
}
