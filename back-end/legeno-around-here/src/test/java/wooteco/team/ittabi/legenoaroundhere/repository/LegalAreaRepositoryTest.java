package wooteco.team.ittabi.legenoaroundhere.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.area.LegalArea;
import wooteco.team.ittabi.legenoaroundhere.domain.area.RegionDepth;
import wooteco.team.ittabi.legenoaroundhere.domain.area.RegionalRelationship;

@ActiveProfiles("test")
@Transactional
@DataJpaTest
class LegalAreaRepositoryTest {

    @Autowired
    private LegalAreaRepository legalAreaRepository;

    @DisplayName("LegalArea 저장")
    @Test
    void save() {
        //given
        LegalArea legalArea = new LegalArea("testCode");
        assertThat(legalArea.getId()).isNull();

        //when
        legalArea = legalAreaRepository.save(legalArea);

        //then
        assertThat(legalArea.getId()).isNotNull();
    }

    //todo: 하드하게 넣은 값들 제거
    @Transactional
    @Test
    void findByCode_InitialDataStored_() {
        Area legalArea = legalAreaRepository.findByCode("1100000000");
        Map<RegionDepth, RegionalRelationship> regionalRelationships = legalArea.getRegionalRelationships();
        assertThat(regionalRelationships).hasSize(3);
        for (RegionalRelationship regionalRelationship : regionalRelationships.values()) {
            assertThat(regionalRelationship).isNotNull();
        }
    }

    //todo: 하드하게 넣은 값들 제거
    @Transactional
    @Test
    void findById_InitialDataStored_Persisted() {
        LegalArea legalArea = legalAreaRepository.findById(1L)
            .orElseThrow(IllegalArgumentException::new);
        assertThat(legalArea.getCode()).isEqualTo("1100000000");
        Map<RegionDepth, RegionalRelationship> regionalRelationships = legalArea.getRegionalRelationships();
        assertThat(regionalRelationships).hasSize(3);
        for (RegionalRelationship regionalRelationship : regionalRelationships.values()) {
            assertThat(regionalRelationship).isNotNull();
        }
    }
}
