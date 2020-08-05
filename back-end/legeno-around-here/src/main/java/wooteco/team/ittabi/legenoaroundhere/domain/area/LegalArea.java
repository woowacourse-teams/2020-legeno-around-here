package wooteco.team.ittabi.legenoaroundhere.domain.area;

import java.util.Map;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Legal")
public class LegalArea extends Area {

    public LegalArea() {
        super();
    }

    public LegalArea(String code) {
        super(code);
    }

    public LegalArea(String code, Map<RegionDepth, RegionalRelationship> regions) {
        super(code, regions);
    }
}
