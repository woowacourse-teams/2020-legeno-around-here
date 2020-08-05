package wooteco.team.ittabi.legenoaroundhere.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_EMAIL;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTest {

    @Test
    @DisplayName("생성자 테스트")
    void constructor() {
        assertThat(new Email(TEST_EMAIL)).isInstanceOf(Email.class);
    }

    @Test
    @DisplayName("생성자 테스트 - 이메일 주소가 잘못 됐을 때")
    void constructor_IfEmailIsNull_ThrowException() {
        assertThatThrownBy(() -> new Email(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이메일이 null입니다.");
    }

    @ParameterizedTest
    @DisplayName("생성자 테스트 - 이메일 주소가 잘못 됐을 때")
    @ValueSource(strings = {"", " ", "a@", "@c.com", "a@@a.com"})
    void constructor_IfWrongEmail_ThrowException(String input) {
        assertThatThrownBy(() -> new Email(input))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이메일 형식이 잘못됐습니다.");
    }
}
