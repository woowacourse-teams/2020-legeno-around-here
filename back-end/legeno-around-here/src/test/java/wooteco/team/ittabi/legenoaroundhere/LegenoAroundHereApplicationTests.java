package wooteco.team.ittabi.legenoaroundhere;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
@SpringBootTest
class LegenoAroundHereApplicationTests {

    @Test
    void contextLoads() {
    }
}
