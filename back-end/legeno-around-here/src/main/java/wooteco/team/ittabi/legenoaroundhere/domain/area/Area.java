package wooteco.team.ittabi.legenoaroundhere.domain.area;

abstract class Area {

    protected Regions regions;

    Area(Regions regions) {
        this.regions = regions;
    }

    abstract Region getSmallestRegion();

}
