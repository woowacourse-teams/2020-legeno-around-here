package wooteco.team.ittabi.legenoaroundhere.domain.area;

import java.util.Objects;

class Region {

    private final RegionName name;

    Region(String name) {
        this(new RegionName(name));
    }

    Region(RegionName name) {
        this.name = name;
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
