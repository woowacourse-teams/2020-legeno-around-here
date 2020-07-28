package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LegalAreaTest {

    @DisplayName("기본 생성자로 LegalArea 생성 - 성공")
    @Test
    void construct_LegalArea() {
        //when
        LegalArea legalArea = new LegalArea();

        //then
        assertThat(legalArea).isNotNull();
    }

    @DisplayName("1개 미만의 region을 활용한 LegalArea 생성 - 실패")
    @Test
    void construct_InvalidSize_ThrowIllegalArgumentException() {
        assertThatThrownBy(() -> new LegalArea(Collections.emptyMap()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(LegalArea.INVALID_SIZE_ERROR);
    }

    @Test
    void construct_RegionsNull_ThrowIllegalArgumentException() {
        assertThatThrownBy(() -> new LegalArea(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(LegalArea.NOT_ALLOWED_NULL);
    }

    //todo: check
//    @DisplayName("다양한 Region의 조합에서 가장 작은 단위의 Region 조회")
//    @ParameterizedTest
//    @MethodSource("getCasesForGetSmallestRegion")
//    void getSmallestRegion_SmallestRegion(Map<RegionDepth, Region> regions,
//        Region expectedSmallestRegion) {
//        LegalArea legalArea = new LegalArea(regions);
//        Region actualSmallestRegion = legalArea.getSmallestRegion();
//        assertThat(actualSmallestRegion).isEqualTo(expectedSmallestRegion);
//    }
//
//    private static Stream<Arguments> getCasesForGetSmallestRegion() {
//        return Stream.of(
//            Arguments.of(
//                new HashMap<RegionDepth, Region>() {{
//                    put(RegionDepth.ONE, new Region("서울시"));
//                    put(RegionDepth.TWO, new Region("서대문구"));
//                    put(RegionDepth.THREE, new Region("홍제동"));
//                }}, new Region("홍제동")),
//            Arguments.of(
//                new HashMap<RegionDepth, Region>() {{
//                    put(RegionDepth.ONE, new Region("서울시"));
//                    put(RegionDepth.TWO, new Region("서대문구"));
//                }}, new Region("서대문구")),
//            Arguments.of(
//                new HashMap<RegionDepth, Region>() {{
//                    put(RegionDepth.ONE, new Region("서울시"));
//                }}, new Region("서울시"))
//        );
//    }
}
