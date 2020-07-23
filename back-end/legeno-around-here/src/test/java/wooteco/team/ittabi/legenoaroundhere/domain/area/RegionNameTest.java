package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RegionNameTest {

    @DisplayName("1글자 이상, 10글자 이하의 글자를 활용한 RegionName 생성 - 성공")
    @Test
    void construct_nameLengthUnder10OrOver1_Constructed() {
        RegionName regionName = new RegionName("홍제동");
        assertThat(regionName).isNotNull();
    }

    @DisplayName("1글자 미만, 10글자 초과의 글자를 활용한 RegionName 생성 - 실패")
    @ParameterizedTest
    @ValueSource(strings = {"", "10자이상의RegionName"})
    void construct_nameLengthUnder1OrOver10_throwIllegalArgumentException(String regionName) {
        assertThatThrownBy(() -> new RegionName(regionName))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(RegionName.INVALID_LENGTH_ERROR);
    }
}