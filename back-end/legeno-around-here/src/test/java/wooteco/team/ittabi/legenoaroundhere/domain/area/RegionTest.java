package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RegionTest {

    @DisplayName("region 생성 성공")
    @ParameterizedTest
    @ValueSource(strings = {"서울시", "서대문구", "홍제동"})
    void construct(String regionName) {
        Region regionLevelOne = new Region(regionName);
        assertThat(regionLevelOne).isNotNull();
    }
}