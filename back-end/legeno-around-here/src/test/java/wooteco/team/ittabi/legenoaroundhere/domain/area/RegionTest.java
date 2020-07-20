package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegionTest {

    @DisplayName("region 생성")
    @Test
    void counstruct() {
        Region regionLevelOne = new Region("서울시", RegionLevel.LEVEL_1);
        assertThat(regionLevelOne).isNotNull();
        Region regionLevelTwo = new Region("서대문구", RegionLevel.LEVEL_2);
        assertThat(regionLevelTwo).isNotNull();
        Region regionLevelThree = new Region("홍제동", RegionLevel.LEVEL_3);
        assertThat(regionLevelThree).isNotNull();
    }
}