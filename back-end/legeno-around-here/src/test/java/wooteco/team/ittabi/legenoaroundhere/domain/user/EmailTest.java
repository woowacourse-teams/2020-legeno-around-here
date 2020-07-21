package wooteco.team.ittabi.legenoaroundhere.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTest {

    private static final String TEST_EMAIL = "test@test.com";

    @Test
    @DisplayName("생성자 테스트")
    void constructor() {    // Todo: 생성자테스트 네이밍
        assertThat(new Email(TEST_EMAIL)).isInstanceOf(Email.class);
    }

    @ParameterizedTest
    @DisplayName("생성자 테스트 - 이메일 주소가 잘못 됐을 때")
    @ValueSource(strings = {"", " ", "a@", "@c.com", "a@@a.com"})
    void constructor_IfWrongEmail_ThrowException(String input) {    // Todo: 생성자테스트 네이밍
        assertThatThrownBy(() -> new Email(input))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
