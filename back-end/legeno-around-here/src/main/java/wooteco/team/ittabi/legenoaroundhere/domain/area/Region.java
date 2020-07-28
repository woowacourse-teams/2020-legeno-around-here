package wooteco.team.ittabi.legenoaroundhere.domain.area;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;

@Getter
@Entity
public class Region extends BaseEntity {

    @Embedded
    @Column(nullable = false)
    private RegionName name;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;

    public Region(String name) {
        this(new RegionName(name));
    }

    public Region(RegionName name) {
        this.name = name;
    }

    protected Region() {
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
