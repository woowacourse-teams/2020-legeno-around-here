package wooteco.team.ittabi.legenoaroundhere.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.area.LegalArea;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Region;
import wooteco.team.ittabi.legenoaroundhere.domain.area.RegionDepth;
import wooteco.team.ittabi.legenoaroundhere.domain.area.RegionalRelationship;

@DataJpaTest
class RegionalRelationshipRepositoryTest {
    //todo: check areaRepository
    @Autowired
    private LegalAreaRepository legalAreaRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RegionalRelationshipRepository regionalRelationshipRepository;

    @Test
    void findByArea() {
        //given
        LegalArea legalArea = legalAreaRepository.findById(1L).orElseThrow(
            IllegalArgumentException::new);

        //when
        List<RegionalRelationship> regionalRelationships = regionalRelationshipRepository.findByArea(legalArea);

        //then
        assertThat(regionalRelationships).hasSize(3);
        for (RegionalRelationship regionalRelationship : regionalRelationships) {
            assertThat(regionalRelationship.getId()).isNotNull();
            assertThat(regionalRelationship.getArea()).isEqualTo(legalArea);
            assertThat(regionalRelationship.getRegion()).isNotNull();
        }
    }

    @DisplayName("영속화된 LegalArea, Region을 활용한 RegionalRelationship 저장")
    @Test
    void save() {
        //given
        Region region = new Region("서울시", RegionDepth.ONE);
        regionRepository.save(region);

        LegalArea legalArea = new LegalArea("1100000000");
        legalAreaRepository.save(legalArea);

        RegionalRelationship regionalRelationship = new RegionalRelationship(legalArea, region);
        assertThat(regionalRelationship.getId()).isNull();
        legalArea.addRegion(regionalRelationship);

        //when
        regionalRelationship = regionalRelationshipRepository.save(regionalRelationship);

        //then
        assertThat(regionalRelationship.getId()).isNotNull();
        assertThat(regionalRelationship.getArea()).isEqualTo(legalArea);
        assertThat(regionalRelationship.getRegion()).isEqualTo(region);
    }
}
