package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LegalAreaTest {

    @DisplayName("LegalArea 생성")
    @Test
    void construct() {
        List<Region> regions = Arrays.asList(
            new Region("서울시", RegionDepth.ONE),
            new Region("서대문구", RegionDepth.TWO),
            new Region("홍제동", RegionDepth.THREE));
        LegalArea legalArea = new LegalArea(regions);
        assertThat(legalArea).isNotNull();
    }

    @DisplayName("가장 작은 단위의 Region 조회")
    @Test
    void getSmallestRegion() {
        Region expectedSmallestRegion = new Region("홍제동", RegionDepth.THREE);
        List<Region> regions = Arrays.asList(
            new Region("서울시", RegionDepth.ONE),
            new Region("서대문구", RegionDepth.TWO),
            expectedSmallestRegion
        );

        LegalArea legalArea = new LegalArea(regions);
        Region actualSmallestRegion = legalArea.getSmallestRegion();
        assertThat(actualSmallestRegion).isEqualTo(expectedSmallestRegion);
    }
}