package wooteco.team.ittabi.legenoaroundhere.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Region;
import wooteco.team.ittabi.legenoaroundhere.domain.area.RegionDepth;
import wooteco.team.ittabi.legenoaroundhere.domain.area.RegionName;

@ActiveProfiles("test")
@SpringBootTest
class RegionRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @DisplayName("regionName 및 regionDepth를 활용한 Region 저장")
    @ParameterizedTest
    @MethodSource({"getCasesForSave"})
    void save(String regionName, RegionDepth regionDepth) {
        Region region = new Region(regionName, regionDepth);
        assertThat(region.getId()).isNull();
        assertThat(region.getName()).isEqualTo(new RegionName(regionName));
        assertThat(region.getDepth()).isEqualTo(regionDepth);
        assertThat(region.getCreatedAt()).isNull();
        assertThat(region.getModifiedAt()).isNull();

        region = regionRepository.save(region);
        assertThat(region.getId()).isNotNull();
        assertThat(region.getName()).isEqualTo(new RegionName(regionName));
        assertThat(region.getDepth()).isEqualTo(regionDepth);
        assertThat(region.getCreatedAt()).isNotNull();
        assertThat(region.getModifiedAt()).isNotNull();
    }

    private static Stream<Arguments> getCasesForSave() {
        return Stream.of(
            Arguments.of("서울시", RegionDepth.ONE),
            Arguments.of("서대문구", RegionDepth.TWO),
            Arguments.of("홍제동", RegionDepth.THREE)
        );
    }
}
