package wooteco.team.ittabi.legenoaroundhere.domain.sector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.team.ittabi.legenoaroundhere.exception.InvalidParameterException;

class SectorDescriptionTest {

    @DisplayName("생성자 테스트, 성공")
    @ParameterizedTest
    @ValueSource(strings = {"테스트 설명임.", "It's Test Description. ", "01001  012 230"})
    void constructor_Success(String input) {
        SectorDescription sectorDescription = new SectorDescription(input);

        String expected = input.trim().replaceAll(" +", " ");
        assertThat(sectorDescription.getDescription()).isEqualTo(expected);
    }

    @DisplayName("생성자 테스트, 예외 발생 - description이 Null이거나 Empty")
    @ParameterizedTest
    @NullAndEmptySource
    void constructor_DescriptionNullOrEmpty_ThrownException(String input) {
        assertThatThrownBy(() -> new SectorDescription(input))
            .isInstanceOf(InvalidParameterException.class);
    }

    @DisplayName("생성자 테스트, 예외 발생 - name이 공백 제거하면 empty")
    @ParameterizedTest
    @ValueSource(strings = {" ", "  ", "   "})
    void constructor_DescriptionEmptyAfterTrim_ThrownException(String input) {
        assertThatThrownBy(() -> new SectorDescription(input))
            .isInstanceOf(InvalidParameterException.class);
    }

    @DisplayName("생성자 테스트, 예외 발생 - 100자 초과")
    @ParameterizedTest
    @ValueSource(strings = {"1", "a", "일"})
    void constructor_isOver100_ThrownException(String letter) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= 100; i++) {
            stringBuilder.append(letter);
        }

        assertThatThrownBy(() -> new SectorDescription(stringBuilder.toString()))
            .isInstanceOf(InvalidParameterException.class);
    }
}