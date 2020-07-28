package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegionalRelationshipTest {

    @DisplayName("Area와 Region을 활용한 AreaRegion 생성")
    @Test
    void construct() {
        Area area = new LegalArea();
        Region region = new Region();
        RegionalRelationship regionalRelationship = new RegionalRelationship(area, region);
        assertThat(regionalRelationship).isNotNull();
    }
}
