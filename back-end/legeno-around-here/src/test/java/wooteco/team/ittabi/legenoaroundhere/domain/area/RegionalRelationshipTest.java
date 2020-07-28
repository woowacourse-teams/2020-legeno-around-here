package wooteco.team.ittabi.legenoaroundhere.domain.area;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RegionalRelationshipTest {

    @DisplayName("Area와 Region을 활용한 AreaRegion 생성 - 성공")
    @Test
    void construct_ValidAreaValidRegion_RegionalRelationship() {
        Area area = new LegalArea();
        Region region = new Region();
        RegionalRelationship regionalRelationship = new RegionalRelationship(area, region);
        assertThat(regionalRelationship).isNotNull();
    }

    @DisplayName("null인 Area 혹은 Region을 활용한 AreaRegion 생성 - 실패")
    @ParameterizedTest
    @MethodSource({"getCasesForConstructWithNullAreaOrNullRegion"})
    void construct_NullAreaNullRegion_throwIllegalArgumentException(Area area, Region region) {
        assertThatThrownBy(() -> new RegionalRelationship(area, region))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(String.format("%s 혹은 %s가 null 입니다.", area, region));
    }

    private static Stream<Arguments> getCasesForConstructWithNullAreaOrNullRegion() {
        return Stream.of(
            Arguments.of(new LegalArea(), null),
            Arguments.of(null, new Region()),
            Arguments.of(null, null)
        );
    }
}
