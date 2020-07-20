package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegionLevelTest {

    @DisplayName("정렬 학습")
    @Test
    void sort() {
        //given
        List<RegionLevel> regionLevels = Arrays.asList(RegionLevel.LEVEL_1,
            RegionLevel.LEVEL_3,
            RegionLevel.LEVEL_2);

        //when
        Collections.sort(regionLevels);

        //then
        assertThat(regionLevels).isEqualTo(Arrays.asList(
            RegionLevel.LEVEL_1,
            RegionLevel.LEVEL_2,
            RegionLevel.LEVEL_3
        ));
    }

}