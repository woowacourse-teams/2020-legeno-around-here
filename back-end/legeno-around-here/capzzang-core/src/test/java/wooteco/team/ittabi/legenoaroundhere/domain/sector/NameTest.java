package wooteco.team.ittabi.legenoaroundhere.domain.sector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

class NameTest {

    @DisplayName("정적 팩터리 메서드 테스트, 성공")
    @ParameterizedTest
    @ValueSource(strings = {"01234567890123456789", " ㅎ ", "          22          ",
        "2                    2", "lowerCase"})
    void of_Success(String input) {
        Name name = Name.of(input);

        String expected = input.trim().replaceAll(" +", " ").toUpperCase();
        assertThat(name.getName()).isEqualTo(expected);
    }

    @DisplayName("정적 팩터리 메서드 테스트, 예외 발생 - name이 Null이거나 Empty")
    @ParameterizedTest
    @NullAndEmptySource
    void of_NameNullOrEmpty_ThrownException(String input) {
        assertThatThrownBy(() -> Name.of(input))
            .isInstanceOf(WrongUserInputException.class);
    }

    @DisplayName("정적 팩터리 메서드 테스트, 예외 발생 - name이 공백 제거하면 empty")
    @ParameterizedTest
    @ValueSource(strings = {" ", "  ", "   "})
    void of_NameEmptyAfterTrim_ThrownException(String input) {
        assertThatThrownBy(() -> Name.of(input))
            .isInstanceOf(WrongUserInputException.class);
    }

    @DisplayName("정적 팩터리 메서드 테스트, 예외 발생 - 20자 초과")
    @ParameterizedTest
    @ValueSource(strings = {"1", "a", "일"})
    void of_isOver20_ThrownException(String letter) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= 20; i++) {
            stringBuilder.append(letter);
        }

        assertThatThrownBy(() -> Name.of(stringBuilder.toString()))
            .isInstanceOf(WrongUserInputException.class);
    }

    @DisplayName("정적 팩터리 메서드 테스트(getKeywordName) - 성공, 대문자 변환")
    @Test
    void getKeywordName_Success() {
        String expected = "Expected";
        Name name = Name.of(expected);

        assertThat(name.getName()).isEqualTo(expected.toUpperCase());
    }
}
