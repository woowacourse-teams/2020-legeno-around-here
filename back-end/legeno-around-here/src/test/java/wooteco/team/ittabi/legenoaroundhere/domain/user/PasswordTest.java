package wooteco.team.ittabi.legenoaroundhere.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordTest {

    private static final String TEST_PASSWORD = "testPassword";

    @Test
    @DisplayName("생성자 테스트")
    void constructor() {
        assertThat(new Password(TEST_PASSWORD)).isInstanceOf(Password.class);
    }

    @ParameterizedTest
    @DisplayName("생성자 테스트 - 길이가 올바르지 않을 때")
    @ValueSource(strings = {"", "1234567", "이패스워드는열여섯글자를넘습니다."})
    void constructor_IfLengthIsWrong_ThrowException(String input) {
        assertThatThrownBy(() ->new Password(input))
            .isInstanceOf(IllegalArgumentException.class);
    }
}