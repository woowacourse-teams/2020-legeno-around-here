package wooteco.team.ittabi.legenoaroundhere.domain.sector;

public enum SectorState {

    PUBLISHED(true),
    DELETED(false);

    private final boolean used;

    SectorState(boolean used) {
        this.used = used;
    }

    public boolean isUsed() {
        return used;
    }
}
