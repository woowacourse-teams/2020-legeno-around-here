package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegionDepthTest {

    @DisplayName("정렬 학습")
    @Test
    void sort() {
        //given
        List<RegionDepth> regionDepths = Arrays.asList(RegionDepth.LEVEL_1,
            RegionDepth.LEVEL_3,
            RegionDepth.LEVEL_2);

        //when
        Collections.sort(regionDepths);

        //then
        assertThat(regionDepths).isEqualTo(Arrays.asList(
            RegionDepth.LEVEL_1,
            RegionDepth.LEVEL_2,
            RegionDepth.LEVEL_3
        ));
    }

}