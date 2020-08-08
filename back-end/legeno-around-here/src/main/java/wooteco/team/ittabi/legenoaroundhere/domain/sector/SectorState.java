package wooteco.team.ittabi.legenoaroundhere.domain.sector;

import java.util.Arrays;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

public enum SectorState {

    PUBLISHED("등록", true),
    DELETED("삭제", false),
    PENDING("승인 신청", false),
    APPROVED("승인", true),
    REJECTED("반려", false);

    private final String name;
    private final boolean used;

    SectorState(String name, boolean used) {
        this.name = name;
        this.used = used;
    }

    public static SectorState of(String name) {
        return Arrays.stream(SectorState.values())
            .filter(sectorState -> sectorState.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new WrongUserInputException("Sector 상태를 잘못 입력하셨습니다."));
    }

    public String getName() {
        return name;
    }

    public boolean isUsed() {
        return used;
    }
}
