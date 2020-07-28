package wooteco.team.ittabi.legenoaroundhere.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import wooteco.team.ittabi.legenoaroundhere.domain.area.LegalArea;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Region;
import wooteco.team.ittabi.legenoaroundhere.domain.area.RegionDepth;

@DataJpaTest
class LegalAreaRepositoryTest {

    @Autowired
    private LegalAreaRepository legalAreaRepository;

    @DisplayName("LegalArea 저장")
    @Test
    void save() {
        Map<RegionDepth, Region> regions = new HashMap<RegionDepth, Region>() {{
            put(RegionDepth.ONE, new Region("서울시"));
            put(RegionDepth.TWO, new Region("서대문구"));
            put(RegionDepth.THREE, new Region("홍제동"));
        }};
        LegalArea legalArea = new LegalArea(regions);
        assertThat(legalArea.getId()).isNull();

        legalArea = legalAreaRepository.save(legalArea);
        assertThat(legalArea.getId()).isNotNull();
    }
}
