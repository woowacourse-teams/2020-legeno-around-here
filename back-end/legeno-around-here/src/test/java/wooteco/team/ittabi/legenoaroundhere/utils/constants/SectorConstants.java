package wooteco.team.ittabi.legenoaroundhere.utils.constants;

import wooteco.team.ittabi.legenoaroundhere.dto.SectorRequest;

public class SectorConstants {

    public static final Long TEST_SECTOR_INVALID_ID = -1L;

    public static final Long TEST_SECTOR_ID = 1L;
    public static final String TEST_SECTOR_NAME = "SectorName";
    public static final String TEST_SECTOR_DESCRIPTION = "SectorDescription";
    public static final SectorRequest TEST_SECTOR_REQUEST
        = new SectorRequest(TEST_SECTOR_NAME, TEST_SECTOR_DESCRIPTION);

    public static final String TEST_SECTOR_ANOTHER_NAME = "AnotherSectorName";
    public static final String TEST_SECTOR_ANOTHER_DESCRIPTION = "AnotherSectorDescription";
    public static final SectorRequest TEST_SECTOR_ANOTHER_REQUEST
        = new SectorRequest(TEST_SECTOR_ANOTHER_NAME, TEST_SECTOR_ANOTHER_DESCRIPTION);

    public static final String TEST_SECTOR_REASON = "이유";
}
