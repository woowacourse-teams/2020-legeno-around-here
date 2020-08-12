package wooteco.team.ittabi.legenoaroundhere.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtTokenGeneratorTest {

    private static final int MIN_SIZE = 1;

    private JwtTokenGenerator jwtTokenGenerator;

    @BeforeEach
    void setUp() {
        jwtTokenGenerator = new JwtTokenGenerator("testSecretKey", 1L);
    }

    @Test
    @DisplayName("토큰 생성")
    void createToken() {
        List<String> roles = new ArrayList<>();
        assertThat(jwtTokenGenerator.createToken(TEST_USER_EMAIL, roles))
            .hasSizeGreaterThanOrEqualTo(MIN_SIZE);
    }
}
