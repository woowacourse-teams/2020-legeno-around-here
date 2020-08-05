package wooteco.team.ittabi.legenoaroundhere.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import wooteco.team.ittabi.legenoaroundhere.domain.area.LegalArea;
import wooteco.team.ittabi.legenoaroundhere.domain.area.RegionalRelationship;

@ActiveProfiles("test")
@SpringBootTest
class LegalAreaRepositoryTest {

    @Autowired
    private LegalAreaRepository legalAreaRepository;

    @DisplayName("LegalArea 저장")
    @Test
    void save() {
        LegalArea legalArea = new LegalArea();
        assertThat(legalArea.getId()).isNull();

        legalArea = legalAreaRepository.save(legalArea);
        assertThat(legalArea.getId()).isNotNull();
    }

    //todo: 하드하게 넣은 값들 제거
    @Transactional
    @Test
    void findByCode_InitialDataStored_() {
        LegalArea legalArea = legalAreaRepository.findByCode("1100000000");
        List<RegionalRelationship> regionalRelationships = legalArea.getRegionalRelationships();
        assertThat(regionalRelationships).hasSize(3);
        for (RegionalRelationship regionalRelationship : regionalRelationships) {
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
        List<RegionalRelationship> regionalRelationships = legalArea.getRegionalRelationships();
        assertThat(regionalRelationships).hasSize(3);
        for (RegionalRelationship regionalRelationship : regionalRelationships) {
            assertThat(regionalRelationship).isNotNull();
        }
    }
}
