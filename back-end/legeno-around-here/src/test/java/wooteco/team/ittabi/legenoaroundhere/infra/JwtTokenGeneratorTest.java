package wooteco.team.ittabi.legenoaroundhere.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_EMAIL;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtTokenGeneratorTest {

    private static final int MIN_SIZE = 1;

    //Todo: lombok 으로 하자 ㅎㅎ
    @Autowired
    JwtTokenGenerator jwtTokenGenerator;

    @Test
    @DisplayName("토큰 생성")
    void createToken() {
        List<String> roles = new ArrayList<>();
        assertThat(jwtTokenGenerator.createToken(TEST_EMAIL, roles))
            .hasSizeGreaterThanOrEqualTo(MIN_SIZE);
    }
}
