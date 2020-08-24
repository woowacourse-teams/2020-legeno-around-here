package wooteco.team.ittabi.legenoaroundhere.domain.sector;

import java.util.Arrays;
import java.util.stream.Collectors;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

public enum SectorState {
    PUBLISHED("등록", "사용", true, true),
    PENDING("승인 신청", "승인 신청", false, true),
    APPROVED("승인", "사용", true, true),
    REJECTED("반려", "반려", false, false);

    private final String name;
    private final String exceptionName;
    private final boolean used;
    private final boolean unique;

    SectorState(String name, String exceptionName, boolean used, boolean unique) {
        this.name = name;
        this.exceptionName = exceptionName;
        this.used = used;
        this.unique = unique;
    }

    public static SectorState of(String name) {
        return Arrays.stream(values())
            .filter(sectorState -> sectorState.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new WrongUserInputException("Sector 상태를 잘못 입력하셨습니다."));
    }

    public static Iterable<SectorState> getAllAvailable() {
        return Arrays.stream(values())
            .filter(sector -> sector.used)
            .collect(Collectors.toList());
    }

    public String getName() {
        return this.name;
    }

    public String getExceptionName() {
        return this.exceptionName;
    }

    public boolean isUsed() {
        return this.used;
    }

    public boolean isUnique() {
        return this.unique;
    }
}
