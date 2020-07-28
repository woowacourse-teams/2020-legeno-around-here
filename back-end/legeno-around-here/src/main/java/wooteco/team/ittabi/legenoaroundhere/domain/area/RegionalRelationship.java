package wooteco.team.ittabi.legenoaroundhere.domain.area;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;

@Entity
@Table(name = "regional_relationship")
class RegionalRelationship extends BaseEntity {

    @ManyToOne
    //todo: check
    @JoinColumn(name = "area_id")
    private Area area;

    @ManyToOne
    //todo: check
    @JoinColumn(name = "region_id")
    private Region region;

    protected RegionalRelationship() {
    }

    RegionalRelationship(Area area, Region region) {
        this.area = area;
        this.region = region;
    }
}
