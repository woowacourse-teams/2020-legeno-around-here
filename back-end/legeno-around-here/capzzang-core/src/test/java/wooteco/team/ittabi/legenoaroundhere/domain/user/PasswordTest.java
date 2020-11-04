package wooteco.team.ittabi.legenoaroundhere.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.domain.util.constants.UserConstants.TEST_USER_PASSWORD;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordTest {

    @Test
    @DisplayName("생성자 테스트")
    void constructor() {
        assertThat(new Password(TEST_USER_PASSWORD)).isInstanceOf(Password.class);
    }

    @Test
    @DisplayName("생성자 테스트 - 패스워드가 null일 때")
    void constructor_IfPasswordIsNull_ThrowException() {
        assertThatThrownBy(() -> new Password(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("생성자 테스트 - 패스워드가 빈 문자열일 때")
    void constructor_IfPasswordIsEmptyString_ThrowException() {
        assertThatThrownBy(() -> new Password(""))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
