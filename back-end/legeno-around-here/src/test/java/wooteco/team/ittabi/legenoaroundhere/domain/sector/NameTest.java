package wooteco.team.ittabi.legenoaroundhere.domain.sector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

class NameTest {

    @DisplayName("생성자 테스트, 성공")
    @ParameterizedTest
    @ValueSource(strings = {"01234567890123456789", " ㅎ ", "          22          ",
        "2                    2", "lowerCase"})
    void constructor_Success(String input) {
        Name name = new Name(input);

        String expected = input.trim().replaceAll(" +", " ").toUpperCase();
        assertThat(name.getName()).isEqualTo(expected);
    }

    @DisplayName("생성자 테스트, 예외 발생 - name이 Null이거나 Empty")
    @ParameterizedTest
    @NullAndEmptySource
    void constructor_NameNullOrEmpty_ThrownException(String input) {
        assertThatThrownBy(() -> new Description(input))
            .isInstanceOf(WrongUserInputException.class);
    }

    @DisplayName("생성자 테스트, 예외 발생 - name이 공백 제거하면 empty")
    @ParameterizedTest
    @ValueSource(strings = {" ", "  ", "   "})
    void constructor_NameEmptyAfterTrim_ThrownException(String input) {
        assertThatThrownBy(() -> new Name(input))
            .isInstanceOf(WrongUserInputException.class);
    }

    @DisplayName("생성자 테스트, 예외 발생 - 20자 초과")
    @ParameterizedTest
    @ValueSource(strings = {"1", "a", "일"})
    void constructor_isOver20_ThrownException(String letter) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= 20; i++) {
            stringBuilder.append(letter);
        }

        assertThatThrownBy(() -> new Name(stringBuilder.toString()))
            .isInstanceOf(WrongUserInputException.class);
    }
}