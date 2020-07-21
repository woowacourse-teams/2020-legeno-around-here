package wooteco.team.ittabi.legenoaroundhere.domain.area;

class Region implements Comparable<Region> {

    private final RegionName name;
    private final RegionDepth depth;

    Region(String name, RegionDepth depth) {
        this(new RegionName(name), depth);
    }

    Region(RegionName name, RegionDepth depth) {
        this.name = name;
        this.depth = depth;
    }

    @Override
    public int compareTo(Region other) {
        return this.depth.compareTo(other.depth);
    }
}
