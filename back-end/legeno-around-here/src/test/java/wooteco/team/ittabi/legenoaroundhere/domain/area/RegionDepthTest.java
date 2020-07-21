package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegionDepthTest {

    @DisplayName("정렬 확인")
    @Test
    void sort() {
        //given
        List<RegionDepth> regionDepths = Arrays.asList(
            RegionDepth.ONE,
            RegionDepth.THREE,
            RegionDepth.TWO);

        //when
        Collections.sort(regionDepths);

        //then
        assertThat(regionDepths).isEqualTo(Arrays.asList(
            RegionDepth.ONE,
            RegionDepth.TWO,
            RegionDepth.THREE
        ));
    }

}