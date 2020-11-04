package wooteco.team.ittabi.legenoaroundhere.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthUtilsTest {

    @DisplayName("AUTH NUMBER를 랜덤으로 생성한다.")
    @Test
    void makeRandomAuthNumber() {
        for (int i = 0; i < 100; i++) {
            int randomAuthNumber = AuthUtils.makeRandomAuthNumber();

            assertThat(randomAuthNumber).isGreaterThanOrEqualTo(100_000_000);
            assertThat(randomAuthNumber).isLessThan(1_000_000_000);
        }
    }
}
