package wooteco.team.ittabi.legenoaroundhere.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AreaRepositoryTest {

    private static final int INIT_DATA_SIZE = 493;

    @Autowired
    AreaRepository areaRepository;

    @DisplayName("Test Data 초기 설정 확인")
    @Test
    void init_checkingArea() {
        List<Area> areas = areaRepository.findAll();
        assertThat(areas).hasSize(INIT_DATA_SIZE);
    }
}
