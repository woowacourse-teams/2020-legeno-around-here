package wooteco.team.ittabi.legenoaroundhere.domain.area;

import java.util.List;

class LegalArea extends Area {

    LegalArea(Regions regions) {
        super(regions);
    }

    LegalArea(List<Region> regions) {
        super(new Regions(regions));
    }


    @Override
    Region findSmallestRegion() {
        return regions.getSmallestRegion();
    }
}
