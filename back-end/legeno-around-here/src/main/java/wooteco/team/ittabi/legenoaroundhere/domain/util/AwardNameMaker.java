package wooteco.team.ittabi.legenoaroundhere.domain.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AwardNameMaker {

    protected static final String SECTOR_CREATOR_AWARD_NAME_SUFFIX = " 부문 창시자";

    public static String makeSectorCreatorAwardName(Sector sector) {
        return sector.getName() + SECTOR_CREATOR_AWARD_NAME_SUFFIX;
    }
}
