package wooteco.team.ittabi.legenoaroundhere.domain.area;

import java.util.Map;
import javax.persistence.Entity;

@Entity
public class LegalArea extends Area {

    protected LegalArea() {
    }

    public LegalArea(Map<RegionDepth, RegionalRelationship> regions) {
        super(regions);
    }
}
