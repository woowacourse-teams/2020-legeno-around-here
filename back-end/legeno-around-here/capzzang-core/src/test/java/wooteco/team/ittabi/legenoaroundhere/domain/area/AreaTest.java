package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.domain.util.constants.AreaConstants.TEST_AREA_FIRST_DEPTH_NAME;
import static wooteco.team.ittabi.legenoaroundhere.domain.util.constants.AreaConstants.TEST_AREA_FOURTH_DEPTH_NAME;
import static wooteco.team.ittabi.legenoaroundhere.domain.util.constants.AreaConstants.TEST_AREA_SECOND_DEPTH_NAME;
import static wooteco.team.ittabi.legenoaroundhere.domain.util.constants.AreaConstants.TEST_AREA_THIRD_DEPTH_NAME;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AreaTest {

    private static final String EMPTY = "";
    private static final Area FIRST_DEPTH_AREA;
    private static final Area SECOND_DEPTH_AREA;
    private static final Area THIRD_DEPTH_AREA;
    private static final Area FOURTH_DEPTH_AREA;

    static {
        FIRST_DEPTH_AREA = Area.builder()
            .fullName(TEST_AREA_FIRST_DEPTH_NAME)
            .firstDepthName(TEST_AREA_FIRST_DEPTH_NAME)
            .secondDepthName(EMPTY)
            .thirdDepthName(EMPTY)
            .fourthDepthName(EMPTY)
            .build();
        SECOND_DEPTH_AREA = Area.builder()
            .fullName(TEST_AREA_FIRST_DEPTH_NAME + " " + TEST_AREA_SECOND_DEPTH_NAME)
            .firstDepthName(TEST_AREA_FIRST_DEPTH_NAME)
            .secondDepthName(TEST_AREA_SECOND_DEPTH_NAME)
            .thirdDepthName(EMPTY)
            .fourthDepthName(EMPTY)
            .build();
        THIRD_DEPTH_AREA = Area.builder()
            .fullName(TEST_AREA_FIRST_DEPTH_NAME + " " + TEST_AREA_SECOND_DEPTH_NAME + " "
                + TEST_AREA_THIRD_DEPTH_NAME)
            .firstDepthName(TEST_AREA_FIRST_DEPTH_NAME)
            .secondDepthName(TEST_AREA_SECOND_DEPTH_NAME)
            .thirdDepthName(TEST_AREA_THIRD_DEPTH_NAME)
            .fourthDepthName(EMPTY)
            .build();
        FOURTH_DEPTH_AREA = Area.builder()
            .fullName(TEST_AREA_FIRST_DEPTH_NAME + " " + TEST_AREA_SECOND_DEPTH_NAME + " "
                + TEST_AREA_THIRD_DEPTH_NAME + " " + TEST_AREA_FOURTH_DEPTH_NAME)
            .firstDepthName(TEST_AREA_FIRST_DEPTH_NAME)
            .secondDepthName(TEST_AREA_SECOND_DEPTH_NAME)
            .thirdDepthName(TEST_AREA_THIRD_DEPTH_NAME)
            .fourthDepthName(TEST_AREA_FOURTH_DEPTH_NAME)
            .build();
    }

    @DisplayName("1번째 이름까지 있다면, 1번째 이름을 반환")
    @Test
    void getLastDepthName_ExistsFirstDepthName_FirstDepthName() {
        assertThat(FIRST_DEPTH_AREA.getLastDepthName()).isEqualTo(TEST_AREA_FIRST_DEPTH_NAME);
    }

    @DisplayName("2번째 이름까지 있다면, 2번째 이름을 반환")
    @Test
    void getLastDepthName_ExistsSecondDepthName_SecondDepthName() {
        assertThat(SECOND_DEPTH_AREA.getLastDepthName()).isEqualTo(TEST_AREA_SECOND_DEPTH_NAME);
    }

    @DisplayName("3번째 이름까지 있다면, 3번째 이름을 반환")
    @Test
    void getLastDepthName_ExistsThirdDepthName_ThirdDepthName() {
        assertThat(THIRD_DEPTH_AREA.getLastDepthName()).isEqualTo(TEST_AREA_THIRD_DEPTH_NAME);
    }

    @DisplayName("4번째 이름까지 있다면, 4번째 이름을 반환")
    @Test
    void getLastDepthName_ExistsFourthDepthName_FourthDepthName() {
        assertThat(FOURTH_DEPTH_AREA.getLastDepthName()).isEqualTo(TEST_AREA_FOURTH_DEPTH_NAME);
    }
}
