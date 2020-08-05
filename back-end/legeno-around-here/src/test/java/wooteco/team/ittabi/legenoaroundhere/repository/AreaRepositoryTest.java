package wooteco.team.ittabi.legenoaroundhere.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.area.LegalArea;
import wooteco.team.ittabi.legenoaroundhere.domain.area.RegionDepth;
import wooteco.team.ittabi.legenoaroundhere.domain.area.RegionalRelationship;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class AreaRepositoryTest {
    @Autowired
    private AreaRepository<? extends Area> areaRepository;

    @Test
    void dependencyInjection() {
        assertThat(areaRepository).isNotNull();
    }

    @Test
    void findByCode_InitialDataStored_() {
        Area area = areaRepository.findByCode("1100000000");
        Map<RegionDepth, RegionalRelationship> regionalRelationships = area
            .getRegionalRelationships();
        assertThat(regionalRelationships).hasSize(3);
        for (RegionalRelationship regionalRelationship : regionalRelationships.values()) {
            assertThat(regionalRelationship).isNotNull();
        }
    }

    @Test
    void findById_InitialDataStored_Persisted() {
        Area area = areaRepository.findById(1L)
            .orElseThrow(IllegalArgumentException::new);
        assertThat(area.getCode()).isEqualTo("1100000000");
        Map<RegionDepth, RegionalRelationship> regionalRelationships = area
            .getRegionalRelationships();
        assertThat(regionalRelationships).hasSize(3);
        for (RegionalRelationship regionalRelationship : regionalRelationships.values()) {
            assertThat(regionalRelationship).isNotNull();
        }
    }
}
