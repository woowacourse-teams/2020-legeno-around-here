package wooteco.team.ittabi.legenoaroundhere.domain.area;

import java.util.Collections;
import java.util.List;

class Regions {

    private static final int DIFFERENCE_BETWEEN_INDEX_AND_SIZE = 1;
    private static final int MIN_SIZE = 1;
    static final String INVALID_SIZE_ERROR = String
        .format("region의 갯수가 최소 갯수인 %d보다 작습니다.", MIN_SIZE);

    private final List<Region> regions;

    Regions(List<Region> regions) {
        validate(regions);
        Collections.sort(regions);
        this.regions = Collections.unmodifiableList(regions);
    }

    Region getSmallestRegion() {
        return regions.get(getLastIndex());
    }

    //todo: validate region 레벨 1, 레벨 2
    private void validate(List<Region> regions) {
        if (isNotValidSize(regions)) {
            throw new IllegalArgumentException(
                INVALID_SIZE_ERROR);
        }
    }

    private boolean isNotValidSize(List<Region> regions) {
        return regions.size() < MIN_SIZE;
    }

    private int getLastIndex() {
        return regions.size() - DIFFERENCE_BETWEEN_INDEX_AND_SIZE;
    }
}
