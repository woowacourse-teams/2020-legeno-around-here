package wooteco.team.ittabi.legenoaroundhere.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class PostTest {

    @Test
    void validateLength_OverLength_ThrownException() {
        String overLengthInput = "aaaaaaaaaaaaaaaaaaaaa";
        assertThatThrownBy(() -> new Post(overLengthInput))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
