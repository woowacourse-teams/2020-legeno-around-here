package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegionTest {

    @DisplayName("region 생성")
    @Test
    void counstruct() {
        Region regionLevelOne = new Region("서울시", RegionDepth.LEVEL_1);
        assertThat(regionLevelOne).isNotNull();
        Region regionLevelTwo = new Region("서대문구", RegionDepth.LEVEL_2);
        assertThat(regionLevelTwo).isNotNull();
        Region regionLevelThree = new Region("홍제동", RegionDepth.LEVEL_3);
        assertThat(regionLevelThree).isNotNull();
    }

    @DisplayName("비교 학습")
    @Test
    void compareTo() {
        Region regionLevelOne = new Region("서울시", RegionDepth.LEVEL_1);
        Region regionLevelTwo = new Region("서대문구", RegionDepth.LEVEL_2);
        Region regionLevelTwoAnother = new Region("마포구", RegionDepth.LEVEL_2);

        assertThat(regionLevelOne.compareTo(regionLevelTwo)).isEqualTo(-1);
        assertThat(regionLevelTwo.compareTo(regionLevelTwoAnother)).isEqualTo(0);
    }
}