package wooteco.team.ittabi.legenoaroundhere.domain.area;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Region extends BaseEntity {

    @Embedded
    @Column(nullable = false)
    private RegionName name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RegionDepth depth;

    Region(RegionName name, RegionDepth depth) {
        this.name = name;
        this.depth = depth;
    }

    public Region(String name, RegionDepth depth) {
        this(new RegionName(name), depth);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Region region = (Region) o;
        return name.equals(region.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Region{" +
            "name=" + name +
            '}';
    }
}
