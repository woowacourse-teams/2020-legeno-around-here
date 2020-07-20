package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class LegalAreaTest {

    @Test
    void construct() {
        List<Region> regions = Arrays.asList(
            new Region("서울시", RegionLevel.LEVEL_1),
            new Region("서대문구", RegionLevel.LEVEL_2),
            new Region("홍제동", RegionLevel.LEVEL_3));
        LegalArea legalArea = new LegalArea(regions);
        assertThat(legalArea).isNotNull();
    }

    @Test
    void findSmallestRegion() {
        Region expectedSmallestRegion = new Region("홍제동", RegionLevel.LEVEL_3);
        List<Region> regions = Arrays.asList(
            new Region("서울시", RegionLevel.LEVEL_1),
            new Region("서대문구", RegionLevel.LEVEL_2),
            expectedSmallestRegion
        );

        LegalArea legalArea = new LegalArea(regions);
        Region actualSmallestRegion = legalArea.findSmallestRegion();
        assertThat(actualSmallestRegion).isEqualTo(expectedSmallestRegion);
    }
}