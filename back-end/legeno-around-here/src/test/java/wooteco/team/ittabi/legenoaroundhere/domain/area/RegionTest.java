package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class RegionTest {

    @DisplayName("region 생성 성공")
    @ParameterizedTest
    @MethodSource({"getCasesForConstruct"})
    void construct(String regionName, RegionDepth regionDepth) {
        Region regionLevelOne = new Region(regionName, regionDepth);
        assertThat(regionLevelOne).isNotNull();
    }

    private static Stream<Arguments> getCasesForConstruct() {
        return Stream.of(
            Arguments.of("서울시", RegionDepth.ONE),
            Arguments.of("서대문구", RegionDepth.TWO),
            Arguments.of("홍제동", RegionDepth.THREE)
        );
    }
}
