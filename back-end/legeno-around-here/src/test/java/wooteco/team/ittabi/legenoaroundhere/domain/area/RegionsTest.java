package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegionsTest {

    @DisplayName("가장 작은 단위의 Region 조회")
    @Test
    void getSmallestRegion() {
        Region expectedSmallestRegion = new Region("홍제동", RegionLevel.LEVEL_3);
        Regions regions = new Regions(Arrays.asList(
            new Region("서울시", RegionLevel.LEVEL_1),
            expectedSmallestRegion,
            new Region("서대문구", RegionLevel.LEVEL_2)
        ));

        Region actualSmallestRegion = regions.getSmallestRegion();
        assertThat(actualSmallestRegion).isEqualTo(expectedSmallestRegion);
    }
}