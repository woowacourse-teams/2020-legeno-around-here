package wooteco.team.ittabi.legenoaroundhere.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_PASSWORD;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordTest {

    @Test
    @DisplayName("생성자 테스트")
    void constructor() {
        assertThat(new Password(TEST_PASSWORD)).isInstanceOf(Password.class);
    }

    @Test
    @DisplayName("생성자 테스트 - 패스워드가 null일 때")
    void constructor_IfPasswordIsNull_ThrowException() {
        assertThatThrownBy(() -> new Password(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("password가 null 입니다.");
    }

    @ParameterizedTest
    @DisplayName("생성자 테스트 - 길이가 올바르지 않을 때")
    @ValueSource(strings = {"", "1234567", "이패스워드는열여섯글자를넘습니다."})
    void constructor_IfLengthIsWrong_ThrowException(String input) {
        assertThatThrownBy(() -> new Password(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("비밀번호는 8 ~ 16 자여야 합니다.");
    }
}
