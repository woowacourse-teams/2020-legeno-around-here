package wooteco.team.ittabi.legenoaroundhere.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

class PostSearchFilterTest {

    @DisplayName("생성자에 null 또는 empty가 들어가는 경우 빈 List 필드 생성")
    @Test
    void constructor_NullOrEmpty_IsSizeZero() {
        PostSearchFilter postSearchFilter = new PostSearchFilter(null, null);

        assertThat(postSearchFilter.getAreaIds()).hasSize(0);
        assertThat(postSearchFilter.getSectorIds()).hasSize(0);

        postSearchFilter = new PostSearchFilter("", null);

        assertThat(postSearchFilter.getAreaIds()).hasSize(0);
        assertThat(postSearchFilter.getSectorIds()).hasSize(0);

        postSearchFilter = new PostSearchFilter(null, "");

        assertThat(postSearchFilter.getAreaIds()).hasSize(0);
        assertThat(postSearchFilter.getSectorIds()).hasSize(0);

        postSearchFilter = new PostSearchFilter("", "");

        assertThat(postSearchFilter.getAreaIds()).hasSize(0);
        assertThat(postSearchFilter.getSectorIds()).hasSize(0);
    }

    @DisplayName("생성자에 구분자가 없는 문자열(숫자)이 들어가는 경우")
    @Test
    void constructor_HasNotDelimiterNumber_Success() {
        String areaIds = "1";
        String sectorIds = "1";

        PostSearchFilter postSearchFilter = new PostSearchFilter(areaIds, sectorIds);

        assertThat(postSearchFilter.getAreaIds()).hasSize(1);
        assertThat(postSearchFilter.getAreaIds()).contains(Long.valueOf(areaIds));
        assertThat(postSearchFilter.getSectorIds()).hasSize(1);
        assertThat(postSearchFilter.getSectorIds()).contains(Long.valueOf(sectorIds));
    }

    @DisplayName("생성자에 구분자가 있는 문자열(숫자)이 들어가는 경우")
    @Test
    void constructor_HasDelimiterNumber_Success() {
        String areaIds = "1,2";
        String sectorIds = "1,3,4";

        PostSearchFilter postSearchFilter = new PostSearchFilter(areaIds, sectorIds);

        assertThat(postSearchFilter.getAreaIds()).hasSize(2);
        assertThat(postSearchFilter.getSectorIds()).hasSize(3);
    }

    @DisplayName("생성자에 올바르지 않은 문자열(숫자)이 들어가는 경우")
    @ParameterizedTest
    @CsvSource(value = {"ㄱ,1", "1,ㄱ", "ㄱ,ㄱ", "a,1", "1,a", "a,a"})
    void constructor_WrongIds_Success(String areaIds, String sectorIds) {
        assertThatThrownBy(() -> new PostSearchFilter("1", "1 "));
        assertThatThrownBy(() -> new PostSearchFilter(areaIds, sectorIds))
            .isInstanceOf(WrongUserInputException.class);
    }

    @DisplayName("AreaIds만 있는 경우, True")
    @Test
    void isAreaFilter_HasOnlyAreaIds_True() {
        PostSearchFilter areaFilter = new PostSearchFilter("1", "");

        assertThat(areaFilter.isAreaFilter()).isTrue();
    }

    @DisplayName("AreaIds만 있는 경우가 아니면, false")
    @Test
    void isAreaFilter_HasOnlyAreaIds_False() {
        PostSearchFilter noFilter = new PostSearchFilter("", "");
        PostSearchFilter sectorFilter = new PostSearchFilter("", "1");
        PostSearchFilter areaAndSectorFilter = new PostSearchFilter("1", "1");

        assertThat(noFilter.isAreaFilter()).isFalse();
        assertThat(sectorFilter.isAreaFilter()).isFalse();
        assertThat(areaAndSectorFilter.isAreaFilter()).isFalse();
    }

    @DisplayName("SectorIds만 있는 경우, True")
    @Test
    void isSectorFilter_HasOnlySectorIds_True() {
        PostSearchFilter sectorFilter = new PostSearchFilter("", "1");

        assertThat(sectorFilter.isSectorFilter()).isTrue();
    }

    @DisplayName("SectorIds만 있는 경우가 아니면, false")
    @Test
    void isSectorFilter_HasNotOnlySectorIds_True() {
        PostSearchFilter noFilter = new PostSearchFilter("", "");
        PostSearchFilter areaFilter = new PostSearchFilter("1", "");
        PostSearchFilter areaAndSectorFilter = new PostSearchFilter("1", "1");

        assertThat(noFilter.isSectorFilter()).isFalse();
        assertThat(areaFilter.isSectorFilter()).isFalse();
        assertThat(areaAndSectorFilter.isSectorFilter()).isFalse();
    }

    @DisplayName("SectorIds, AreaIds가 모두 없는 경우, True")
    @Test
    void isSectorFilter_HasNoIds_True() {
        PostSearchFilter noFilter = new PostSearchFilter("", "");

        assertThat(noFilter.isNoFilter()).isTrue();
    }

    @Test
    void isNoFilter() {
        PostSearchFilter areaFilter = new PostSearchFilter("1", "");
        PostSearchFilter sectorFilter = new PostSearchFilter("", "1");
        PostSearchFilter areaAndSectorFilter = new PostSearchFilter("1", "1");

        assertThat(areaFilter.isNoFilter()).isFalse();
        assertThat(sectorFilter.isNoFilter()).isFalse();
        assertThat(areaAndSectorFilter.isNoFilter()).isFalse();
    }
}