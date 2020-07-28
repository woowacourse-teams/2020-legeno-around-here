package wooteco.team.ittabi.legenoaroundhere.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wooteco.team.ittabi.legenoaroundhere.domain.area.LegalArea;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Region;
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

    @DisplayName("영속화된 LegalArea, Region을 활용한 RegionalRelationship 저장")
    @Test
    void save() {
        //given
        LegalArea legalArea = new LegalArea();
        legalAreaRepository.save(legalArea);

        Region region = new Region("서울시");
        regionRepository.save(region);

        RegionalRelationship regionalRelationship = new RegionalRelationship(legalArea, region);
        assertThat(regionalRelationship.getId()).isNull();

        //when
        regionalRelationship = regionalRelationshipRepository.save(regionalRelationship);

        //then
        assertThat(regionalRelationship.getId()).isNotNull();
        assertThat(regionalRelationship.getArea()).isEqualTo(legalArea);
        assertThat(regionalRelationship.getRegion()).isEqualTo(region);
    }
}
