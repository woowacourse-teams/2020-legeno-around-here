package wooteco.team.ittabi.legenoaroundhere.domain.sector;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum SectorState {

    PUBLISHED(true),
    DELETED(false);

    private final boolean used;

    SectorState(boolean used) {
        this.used = used;
    }

    public static Iterable<SectorState> getAllAvailable() {
        return Arrays.stream(SectorState.values())
            .filter(sector -> sector.used)
            .collect(Collectors.toList());
    }

    public boolean isUsed() {
        return used;
    }
}
