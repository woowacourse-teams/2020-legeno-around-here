package wooteco.team.ittabi.legenoaroundhere.domain.area;

import java.util.Map;

abstract class Area {

    private static final int MIN_SIZE = 1;
    static final String INVALID_SIZE_ERROR = String
        .format("region의 갯수가 최소 갯수인 %d보다 작습니다.", MIN_SIZE);

    private final Map<RegionDepth, Region> regions;

    protected Area(
        Map<RegionDepth, Region> regions) {
        validate(regions);
        this.regions = regions;
    }

    Region getSmallestRegion() {
        RegionDepth smallestRegionDepth = regions.keySet().stream()
            .reduce((depth1, depth2) -> depth1.compareTo(depth2) > 0 ? depth1 : depth2)
            .orElseThrow(() -> new IllegalStateException());
        return regions.get(smallestRegionDepth);
    }

    private void validate(Map<RegionDepth, Region> regions) {
        if (isNotValidSize(regions)) {
            throw new IllegalArgumentException(INVALID_SIZE_ERROR);
        }
    }

    private boolean isNotValidSize(Map<RegionDepth, Region> regions) {
        return regions.size() < MIN_SIZE;
    }
}
