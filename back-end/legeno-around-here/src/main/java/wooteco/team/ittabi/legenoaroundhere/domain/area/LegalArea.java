package wooteco.team.ittabi.legenoaroundhere.domain.area;

import java.util.Map;
import javax.persistence.Entity;

@Entity
public class LegalArea extends Area {

    public LegalArea() {
    }

    public LegalArea(Map<RegionDepth, RegionalRelationship> regions) {
        super(regions);
    }
}
