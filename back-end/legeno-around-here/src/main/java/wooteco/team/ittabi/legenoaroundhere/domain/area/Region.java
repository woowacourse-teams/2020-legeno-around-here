package wooteco.team.ittabi.legenoaroundhere.domain.area;

class Region implements Comparable<Region> {

    private final String name;
    private final RegionLevel level;

    Region(String name, RegionLevel level) {
        this.name = name;
        this.level = level;
    }

    @Override
    public int compareTo(Region other) {
        return this.level.compareTo(other.level);
    }
}
