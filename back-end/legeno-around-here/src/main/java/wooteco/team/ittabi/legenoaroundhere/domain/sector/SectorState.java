package wooteco.team.ittabi.legenoaroundhere.domain.sector;

import java.util.Arrays;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

public enum SectorState {

    PUBLISHED("등록", "사용", true),
    DELETED("삭제", "삭제", false),
    PENDING("승인 신청", "승인 신청", false),
    APPROVED("승인", "사용", true),
    REJECTED("반려", "반려", false);

    private final String name;
    private final String exceptionName;
    private final boolean used;

    SectorState(String name, String exceptionName, boolean used) {
        this.name = name;
        this.exceptionName = exceptionName;
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

    public String getExceptionName() {
        return exceptionName;
    }

    public boolean isUsed() {
        return used;
    }
}
