package wooteco.team.ittabi.legenoaroundhere.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_FIRST_DEPTH_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_FOURTH_DEPTH_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_FULL_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_SECOND_DEPTH_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_THIRD_DEPTH_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.post.image.PostImage;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

public class PostTest {

    private static final User USER;
    private static final Sector SECTOR;
    private static final Area AREA;

    static {
        USER = User.builder()
            .email(TEST_USER_EMAIL)
            .nickname(TEST_USER_NICKNAME)
            .password(TEST_USER_PASSWORD)
            .build();
        SECTOR = Sector.builder()
            .name(TEST_SECTOR_NAME)
            .description(TEST_SECTOR_DESCRIPTION)
            .creator(USER)
            .lastModifier(USER)
            .state(SectorState.PUBLISHED)
            .build();
        AREA = Area.builder()
            .fullName(TEST_AREA_FULL_NAME)
            .firstDepthName(TEST_AREA_FIRST_DEPTH_NAME)
            .secondDepthName(TEST_AREA_SECOND_DEPTH_NAME)
            .thirdDepthName(TEST_AREA_THIRD_DEPTH_NAME)
            .fourthDepthName(TEST_AREA_FOURTH_DEPTH_NAME)
            .build();
    }

    @DisplayName("길이 검증 - 예외 발생")
    @Test
    void validateLength_OverLength_ThrownException() {
        StringBuilder overLengthInput = new StringBuilder();
        for (int i = 0; i <= 2000; i++) {
            overLengthInput.append("a");
        }
        assertThatThrownBy(() -> new Post(overLengthInput.toString(), Collections
            .singletonList(PostImage.builder().build()), AREA, SECTOR, USER))
            .isInstanceOf(WrongUserInputException.class);
    }
}
