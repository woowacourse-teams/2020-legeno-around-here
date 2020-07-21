package wooteco.team.ittabi.legenoaroundhere.domain.area;

import java.util.Collections;
import java.util.List;

class LegalArea implements Area {

    private static final int DIFFERENCE_BETWEEN_INDEX_AND_SIZE = 1;
    private static final int MIN_SIZE = 1;
    static final String INVALID_SIZE_ERROR = String
        .format("region의 갯수가 최소 갯수인 %d보다 작습니다.", MIN_SIZE);

    private final List<Region> regions;

    LegalArea(List<Region> regions) {
        validate(regions);
        Collections.sort(regions);
        this.regions = regions;
    }

    @Override
    public Region getSmallestRegion() {
        return regions.get(getLastIndex());
    }

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
