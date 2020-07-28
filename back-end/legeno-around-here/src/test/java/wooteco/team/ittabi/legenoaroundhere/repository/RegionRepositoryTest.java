package wooteco.team.ittabi.legenoaroundhere.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Region;
import wooteco.team.ittabi.legenoaroundhere.domain.area.RegionName;

@DataJpaTest
class RegionRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @DisplayName("regionName을 활용한 Region 저장")
    @ParameterizedTest
    @ValueSource(strings = {"서울시", "서대문구", "홍제동"})
    void save(String regionName) {
        Region region = new Region(regionName);
        assertThat(region.getId()).isNull();
        assertThat(region.getArea()).isNull();
        assertThat(region.getName()).isEqualTo(new RegionName(regionName));
        assertThat(region.getCreatedAt()).isNull();
        assertThat(region.getModifiedAt()).isNull();

        region = regionRepository.save(region);
        assertThat(region.getId()).isNotNull();
        assertThat(region.getArea()).isNull();
        assertThat(region.getName()).isEqualTo(new RegionName(regionName));
        assertThat(region.getCreatedAt()).isNotNull();
        assertThat(region.getModifiedAt()).isNotNull();
    }

    //todo: findById()
}
