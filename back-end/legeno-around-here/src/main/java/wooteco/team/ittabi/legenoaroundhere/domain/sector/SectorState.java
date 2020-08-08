package wooteco.team.ittabi.legenoaroundhere.domain.sector;

public enum SectorState {

    PUBLISHED("등록", true),
    DELETED("삭제", false),
    PENDING("승인 신청", false);

    private final String name;
    private final boolean used;

    SectorState(String name, boolean used) {
        this.name = name;
        this.used = used;
    }

    public String getName() {
        return name;
    }

    public boolean isUsed() {
        return used;
    }
}
