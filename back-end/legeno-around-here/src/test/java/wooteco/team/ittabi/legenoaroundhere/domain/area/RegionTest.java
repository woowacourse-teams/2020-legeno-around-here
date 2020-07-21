package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RegionTest {

    @DisplayName("region 생성 성공")
    @ParameterizedTest
    @ValueSource(strings = {"서울시", "서대문구", "홍제동"})
    void construct(String regionName) {
        Region regionLevelOne = new Region(regionName, RegionDepth.ONE);
        assertThat(regionLevelOne).isNotNull();
    }

    @DisplayName("비교 확인")
    @Test
    void compareTo() {
        Region regionDepthOne = new Region("서울시", RegionDepth.ONE);
        Region regionDepthTwo = new Region("서대문구", RegionDepth.TWO);
        Region regionDepthTwoAnother = new Region("마포구", RegionDepth.TWO);

        assertThat(regionDepthOne.compareTo(regionDepthTwo)).isEqualTo(-1);
        assertThat(regionDepthTwo.compareTo(regionDepthTwoAnother)).isEqualTo(0);
    }
}