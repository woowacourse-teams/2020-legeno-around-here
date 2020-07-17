package wooteco.team.ittabi.legenoaroundhere.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PostTest {

    @DisplayName("길이 검증 - 예외 발생, 기준 길이 초과")
    @Test
    void validateLength_OverLength_ThrownException() {
        String overLengthInput = "aaaaaaaaaaaaaaaaaaaaa";
        assertThatThrownBy(() -> new Post(overLengthInput))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
