package wooteco.team.ittabi.legenoaroundhere.domain.area;

class LegalArea extends Area {

    LegalArea(Regions regions) {
        super(regions);
    }

    @Override
    Region findSmallestRegion() {
        return regions.getSmallestRegion();
    }
}
