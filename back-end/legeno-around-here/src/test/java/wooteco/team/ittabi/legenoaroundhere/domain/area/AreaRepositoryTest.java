package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AreaRepositoryTest {

    @Autowired
    AreaRepository areaRepository;

    @DisplayName("Test Data 초기 설정 확인")
    @Test
    void init_checkingArea() {
        List<Area> areas = areaRepository.findAll();
        assertThat(areas).hasSize(493);
    }
}