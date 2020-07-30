package wooteco.team.ittabi.legenoaroundhere.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_PASSWORD;

import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wooteco.team.ittabi.legenoaroundhere.dto.LoginRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.service.UserService;

@SpringBootTest
class JwtTokenDecoderTest {

    @Autowired
    private JwtTokenDecoder jwtTokenDecoder;

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    private UserService userService;

    private String token;

    @BeforeEach
    void setUp() {
        token = jwtTokenGenerator.createToken(TEST_EMAIL, Collections.singletonList("ROLE_USER"));
    }

    @Test
    @DisplayName("토큰으로부터 Authentication 얻기")
    void getAuthentication() {
        userService.createUser(new UserCreateRequest(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD));
        userService.login(new LoginRequest(TEST_EMAIL, TEST_PASSWORD));

        assertThat(jwtTokenDecoder.getAuthentication(token))
            .isInstanceOf(Authentication.class);
    }

    @Test
    @DisplayName("토큰으로부터 Authentication 얻기 - 존재하지 않는 사용자 예외처리")
    void getAuthentication_IfNotExistUser_ThrowException() {
        assertThatThrownBy(() -> jwtTokenDecoder.getAuthentication(token))
            .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("토큰 유효성 검증 - 유효한 토큰이면 true")
    void isValidToken_IfValidToken_ReturnTrue() {
        assertThat(jwtTokenDecoder.isValidToken(token)).isTrue();
    }

    @Test
    @DisplayName("토큰 유효성 검증 - 유효하지 않은 토큰이면 false")
    void isValidToken_IfInvalidToken_ReturnFalse() {
        assertThat(jwtTokenDecoder.isValidToken("ㅋ")).isFalse();
    }
}
