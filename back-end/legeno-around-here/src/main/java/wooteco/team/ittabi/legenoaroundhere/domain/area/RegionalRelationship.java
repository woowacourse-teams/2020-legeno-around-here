package wooteco.team.ittabi.legenoaroundhere.domain.area;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;

@Getter
@Entity
@Table(name = "regional_relationship")
public class RegionalRelationship extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    protected RegionalRelationship() {
    }

    public RegionalRelationship(Area area, Region region) {
        validate(area, region);
        this.area = area;
        this.region = region;
    }

    private void validate(Area area, Region region) {
        if (Objects.isNull(area) || Objects.isNull(region)) {
            throw new IllegalArgumentException(String.format("%s 혹은 %s가 null 입니다.", area, region));
        }
    }

    RegionDepth getDepth() {
        return region.getDepth();
    }

    @Override
    public String toString() {
        return "RegionalRelationship{" +
            "area=" + area +
            ", region=" + region +
            '}';
    }
}
