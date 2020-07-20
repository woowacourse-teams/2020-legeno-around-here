package wooteco.team.ittabi.legenoaroundhere.domain.area;

import java.util.Collections;
import java.util.List;

class Regions {

    private static final int DIRRERENCE_BETWEEN_INDEX_AND_SIZE = 1;

    private final List<Region> regions;

    Regions(List<Region> regions) {
        this.regions = regions;
    }

    Region getSmallestRegion() {
        Collections.sort(regions);
        return regions.get(getLastIndex());
    }

    private int getLastIndex() {
        return regions.size() - DIRRERENCE_BETWEEN_INDEX_AND_SIZE;
    }
}
