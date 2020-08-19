package wooteco.team.ittabi.legenoaroundhere.domain.post;

import static org.assertj.core.api.Assertions.assertThat;
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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

class PostTest {

    private final User user = User.builder()
        .email(TEST_USER_EMAIL)
        .nickname(TEST_USER_NICKNAME)
        .password(TEST_USER_PASSWORD)
        .build();

    private final Sector sector = Sector.builder()
        .name(TEST_SECTOR_NAME)
        .description(TEST_SECTOR_DESCRIPTION)
        .creator(user)
        .lastModifier(user)
        .state(SectorState.PUBLISHED)
        .build();

    private final Area area = Area.builder()
        .fullName(TEST_AREA_FULL_NAME)
        .firstDepthName(TEST_AREA_FIRST_DEPTH_NAME)
        .secondDepthName(TEST_AREA_SECOND_DEPTH_NAME)
        .thirdDepthName(TEST_AREA_THIRD_DEPTH_NAME)
        .fourthDepthName(TEST_AREA_FOURTH_DEPTH_NAME)
        .build();

    @DisplayName("길이 검증 - 예외 발생")
    @Test
    void validateLength_OverLength_ThrownException() {
        StringBuilder overLengthInput = new StringBuilder();
        for (int i = 0; i <= 2000; i++) {
            overLengthInput.append("a");
        }
        assertThatThrownBy(() -> new Post(overLengthInput.toString(), area, sector, user))
            .isInstanceOf(WrongUserInputException.class);
    }

    @Test
    void getWriting_wordCount10_thenReturn10() {
        StringBuilder writing = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            writing.append("0");
        }
        Post post = new Post(writing.toString(), area, sector, user);

        int expected = 10;
        assertThat(post.getWriting(expected).length()).isEqualTo(expected);
    }
}