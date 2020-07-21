package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegionsTest {

    @DisplayName("1개 이상의 region을 활용한 Regions 생성 - 성공")
    @Test
    void constructSucceed() {
        Regions regions = new Regions(
            Collections.singletonList(new Region("홍제동", RegionLevel.LEVEL_3)));

        assertThat(regions).isNotNull();
    }

    @DisplayName("1개 미만의 region을 활용한 Regions 생성 - 실패")
    @Test
    void constructWithInvalidSizeThrowIllegalArgumentException() {
        assertThatThrownBy(() -> new Regions(Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Regions.INVALID_SIZE_ERROR);
    }

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