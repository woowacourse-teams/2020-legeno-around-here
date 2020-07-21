package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LegalAreaTest {

    @DisplayName("1개 이상의 region을 활용한 LegalArea 생성 - 성공")
    @Test
    void construct_Succeed() {
        //given
        List<Region> regions = Arrays.asList(
            new Region("서울시", RegionDepth.ONE),
            new Region("서대문구", RegionDepth.TWO),
            new Region("홍제동", RegionDepth.THREE));

        //when
        LegalArea legalArea = new LegalArea(regions);

        //then
        assertThat(legalArea).isNotNull();
    }

    @DisplayName("1개 미만의 region을 활용한 LegalArea 생성 - 실패")
    @Test
    void construct_InvalidSize_ThrowIllegalArgumentException() {
        assertThatThrownBy(() -> new LegalArea(Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(LegalArea.INVALID_SIZE_ERROR);
    }

    @DisplayName("가장 작은 단위의 Region 조회")
    @Test
    void getSmallestRegion_WithThreeRegions_RegionWhichIsSmallest() {
        //given
        Region expectedSmallestRegion = new Region("홍제동", RegionDepth.THREE);
        List<Region> regions = Arrays.asList(
            new Region("서울시", RegionDepth.ONE),
            new Region("서대문구", RegionDepth.TWO),
            expectedSmallestRegion
        );
        LegalArea legalArea = new LegalArea(regions);

        //when
        Region actualSmallestRegion = legalArea.getSmallestRegion();

        //then
        assertThat(actualSmallestRegion).isEqualTo(expectedSmallestRegion);
    }
}