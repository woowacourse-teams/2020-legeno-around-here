package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AUTH_NUMBER;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_UPDATE_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_INVALID_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_OTHER_PASSWORD;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth.MailAuth;
import wooteco.team.ittabi.legenoaroundhere.dto.LoginRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCheckRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserOtherResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserPasswordUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.AlreadyExistUserException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;
import wooteco.team.ittabi.legenoaroundhere.repository.MailAuthRepository;

class UserServiceTest extends ServiceTest {

    private static final String TEST_PREFIX = "user_";
    private static final String NOT_EXIST_PREFIX = "not_exist_";
    private static final int TOKEN_MIN_SIZE = 1;

    @Autowired
    private UserService userService;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @MockBean
    private MailAuthRepository mailAuthRepository;

    @BeforeEach
    void setUp() {
        MailAuth mailAuth = new MailAuth(TEST_USER_EMAIL, TEST_AUTH_NUMBER);
        when(mailAuthRepository.findByEmail(any())).thenReturn(java.util.Optional.of(mailAuth));

        User user = createUser(TEST_PREFIX + TEST_USER_EMAIL,
            TEST_USER_NICKNAME,
            TEST_USER_PASSWORD);
        setAuthentication(user);
    }

    @DisplayName("가입 여부 확인 - 가입되지 않은 메일일 경우")
    @Test
    void checkJoined_Success() {
        UserCheckRequest userCheckRequest
            = new UserCheckRequest("check_joined" + TEST_USER_EMAIL);

        assertThatCode(() -> userService.checkJoined(userCheckRequest))
            .doesNotThrowAnyException();
    }

    @DisplayName("가입 여부 확인 테스트 - 이미 가입된 메일일 경우")
    @Test
    void checkJoined_AlreadyExist_ThrowException() {
        UserCheckRequest userCheckRequest
            = new UserCheckRequest(TEST_USER_EMAIL);

        assertThatThrownBy(() -> userService.checkJoined(userCheckRequest))
            .isInstanceOf(AlreadyExistUserException.class);
    }

    @DisplayName("User 생성")
    @Test
    void createUser_Success() {
        UserCreateRequest userCreateRequest =
            new UserCreateRequest(
                "createuser_" + TEST_USER_EMAIL,
                TEST_USER_NICKNAME,
                TEST_USER_PASSWORD,
                TEST_AREA_ID,
                TEST_AUTH_NUMBER
            );
        Long userId = userService.createUser(userCreateRequest);

        assertThat(userId).isNotNull();
    }

    @DisplayName("User 생성, 실패 - 중복된 이메일")
    @Test
    void createUser_DuplicatedEmail_ThrownException() {
        UserCreateRequest userCreateRequest = new UserCreateRequest(
            TEST_PREFIX + TEST_USER_EMAIL,
            TEST_USER_NICKNAME,
            TEST_USER_PASSWORD,
            TEST_AREA_ID,
            TEST_AUTH_NUMBER
        );

        assertThatThrownBy(() -> userService.createUser(userCreateRequest))
            .isInstanceOf(WrongUserInputException.class);
    }

    @DisplayName("로그인")
    @Test
    void login_Success() {
        TokenResponse response = userService.login(new LoginRequest(TEST_USER_EMAIL,
            TEST_USER_PASSWORD));
        assertThat(response.getAccessToken()).isNotEmpty();
        assertThat(response.getAccessToken()).hasSizeGreaterThan(TOKEN_MIN_SIZE);
    }

    @DisplayName("로그인 - 존재하지 않는 이메일")
    @Test
    void login_IfEmailNotExist_ThrowException() {
        assertThatThrownBy(
            () -> userService.login(new LoginRequest(NOT_EXIST_PREFIX + TEST_USER_EMAIL,
                TEST_USER_PASSWORD)))
            .isInstanceOf(NotExistsException.class);
    }

    @DisplayName("로그인 - 비밀번호가 틀렸을 때")
    @Test
    void login_IfPasswordIsWrong_ThrowException() {
        assertThatThrownBy(() -> userService.login(new LoginRequest(TEST_USER_EMAIL, "wrong")))
            .isInstanceOf(WrongUserInputException.class);
    }

    @DisplayName("email로 User 찾기 (UserDetails 오버라이딩 메서드)")
    @Test
    void loadUserByUsername_Success() {
        User user = (User) userService.loadUserByUsername(TEST_USER_EMAIL);

        assertThat(user.getUsername()).isEqualTo(TEST_USER_EMAIL);
    }

    @DisplayName("내 정보 찾기")
    @Test
    void findMe_Success() {
        assertThat(userService.findMe().getEmail()).isEqualTo(TEST_PREFIX + TEST_USER_EMAIL);
        assertThat(userService.findMe().getNickname()).isEqualTo(TEST_USER_NICKNAME);
    }

    @DisplayName("내 정보 수정")
    @Test
    void updateMe_Success() {
        User theOtherUser = userRepository.findByEmail(new Email(TEST_UPDATE_EMAIL))
            .orElseThrow(() -> new NotExistsException("존재하지 않는 회원입니다."));
        setAuthentication(theOtherUser);

        UserUpdateRequest userUpdateRequest
            = new UserUpdateRequest("newname", TEST_AREA_ID, null);
        UserResponse updatedUserResponse = userService.updateMe(userUpdateRequest);

        assertThat(updatedUserResponse.getEmail()).isEqualTo(TEST_UPDATE_EMAIL);
        assertThat(updatedUserResponse.getNickname()).isEqualTo("newname");
    }

    @DisplayName("내 비밀번호 수정, 성공")
    @Test
    void changeMyPassword_Success() {
        User updateUser = userRepository.findByEmail(new Email(TEST_UPDATE_EMAIL))
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        setAuthentication(updateUser);

        UserPasswordUpdateRequest userPasswordUpdateRequest
            = new UserPasswordUpdateRequest(TEST_USER_OTHER_PASSWORD);

        userService.changeMyPassword(userPasswordUpdateRequest);
        LoginRequest loginRequest = new LoginRequest(TEST_UPDATE_EMAIL, TEST_USER_OTHER_PASSWORD);

        assertThat(userService.login(loginRequest)).isInstanceOf(TokenResponse.class);
    }

    @DisplayName("내 비밀번호 수정, 예외 발생 - 기존과 동일한 비밀번호")
    @Test
    void changeMyPassword_SamePassword_ThrownException() {
        UserPasswordUpdateRequest userPasswordUpdateRequest
            = new UserPasswordUpdateRequest(TEST_USER_PASSWORD);

        assertThatThrownBy(() -> userService.changeMyPassword(userPasswordUpdateRequest))
            .isInstanceOf(WrongUserInputException.class);
    }

    @DisplayName("회원 탈퇴")
    @Test
    void deleteMe_Success() {
        userService.deleteMe();

        User authUser = (User) authenticationFacade.getAuthentication()
            .getPrincipal();
        assertThatThrownBy(() -> userService.loadUserByUsername(authUser.getUsername()))
            .isInstanceOf(UsernameNotFoundException.class);
    }

    @DisplayName("회원 조회, 성공")
    @Test
    void findUser_Success() {
        UserOtherResponse user = userService.findUser(TEST_USER_ID);

        assertThat(user.getId()).isEqualTo(TEST_USER_ID);
        assertThat(user.getEmail()).isEqualTo(TEST_USER_EMAIL);
        assertThat(user.getNickname()).isEqualTo(TEST_USER_NICKNAME);
        assertThat(user.getAwardsCount()).isNotNull();
    }

    @DisplayName("회원 조회, 예외 발생 - 유효하지 않은 ID")
    @Test
    void findUser_InvalidId_ThrownException() {
        assertThatThrownBy(() -> userService.findUser(TEST_USER_INVALID_ID))
            .isInstanceOf(NotExistsException.class);
    }
}
