package wooteco.team.ittabi.legenoaroundhere.domain.area;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;

@Getter
@Entity
@Table(name = "regional_relationship")
class RegionalRelationship extends BaseEntity {

    @ManyToOne
    //todo: check
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @ManyToOne
    //todo: check
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    protected RegionalRelationship() {
    }

    RegionalRelationship(Area area, Region region) {
        //todo: validateNull
        this.area = area;
        this.region = region;
    }
}
