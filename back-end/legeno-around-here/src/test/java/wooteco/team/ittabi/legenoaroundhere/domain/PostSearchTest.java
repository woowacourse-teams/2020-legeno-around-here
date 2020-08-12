package wooteco.team.ittabi.legenoaroundhere.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

class PostSearchTest {

    @DisplayName("생성자에 null 또는 empty가 들어가는 경우 빈 List 필드 생성")
    @Test
    void constructor_NullOrEmpty_IsSizeZero() {
        PostSearch postSearch = new PostSearch(null, null);

        assertThat(postSearch.getAreaIds()).hasSize(0);
        assertThat(postSearch.getSectorIds()).hasSize(0);

        postSearch = new PostSearch("", null);

        assertThat(postSearch.getAreaIds()).hasSize(0);
        assertThat(postSearch.getSectorIds()).hasSize(0);

        postSearch = new PostSearch(null, "");

        assertThat(postSearch.getAreaIds()).hasSize(0);
        assertThat(postSearch.getSectorIds()).hasSize(0);

        postSearch = new PostSearch("", "");

        assertThat(postSearch.getAreaIds()).hasSize(0);
        assertThat(postSearch.getSectorIds()).hasSize(0);
    }

    @DisplayName("생성자에 구분자가 없는 문자열(숫자)이 들어가는 경우")
    @Test
    void constructor_HasNotDelimiterNumber_Success() {
        String areaIds = "1";
        String sectorIds = "1";

        PostSearch postSearch = new PostSearch(areaIds, sectorIds);

        assertThat(postSearch.getAreaIds()).hasSize(1);
        assertThat(postSearch.getAreaIds()).contains(Long.valueOf(areaIds));
        assertThat(postSearch.getSectorIds()).hasSize(1);
        assertThat(postSearch.getSectorIds()).contains(Long.valueOf(sectorIds));
    }

    @DisplayName("생성자에 구분자가 있는 문자열(숫자)이 들어가는 경우")
    @Test
    void constructor_HasDelimiterNumber_Success() {
        String areaIds = "1,2";
        String sectorIds = "1,3,4";

        PostSearch postSearch = new PostSearch(areaIds, sectorIds);

        assertThat(postSearch.getAreaIds()).hasSize(2);
        assertThat(postSearch.getSectorIds()).hasSize(3);
    }

    @DisplayName("생성자에 올바르지 않은 문자열(숫자)이 들어가는 경우")
    @ParameterizedTest
    @CsvSource(value = {"ㄱ,1", "1,ㄱ", "ㄱ,ㄱ", "a,1", "1,a", "a,a"})
    void constructor_WrongIds_Success(String areaIds, String sectorIds) {
        assertThatThrownBy(() -> new PostSearch("1", "1 "));
        assertThatThrownBy(() -> new PostSearch(areaIds, sectorIds))
            .isInstanceOf(WrongUserInputException.class);
    }

    @DisplayName("AreaIds만 있는 경우, True")
    @Test
    void isAreaFilter_HasOnlyAreaIds_True() {
        PostSearch areaFilter = new PostSearch("1", "");

        assertThat(areaFilter.isAreaFilter()).isTrue();
    }

    @DisplayName("AreaIds만 있는 경우가 아니면, false")
    @Test
    void isAreaFilter_HasOnlyAreaIds_False() {
        PostSearch noFilter = new PostSearch("", "");
        PostSearch sectorFilter = new PostSearch("", "1");
        PostSearch areaAndSectorFilter = new PostSearch("1", "1");

        assertThat(noFilter.isAreaFilter()).isFalse();
        assertThat(sectorFilter.isAreaFilter()).isFalse();
        assertThat(areaAndSectorFilter.isAreaFilter()).isFalse();
    }

    @DisplayName("SectorIds만 있는 경우, True")
    @Test
    void isSectorFilter_HasOnlySectorIds_True() {
        PostSearch sectorFilter = new PostSearch("", "1");

        assertThat(sectorFilter.isSectorFilter()).isTrue();
    }

    @DisplayName("SectorIds만 있는 경우가 아니면, false")
    @Test
    void isSectorFilter_HasNotOnlySectorIds_True() {
        PostSearch noFilter = new PostSearch("", "");
        PostSearch areaFilter = new PostSearch("1", "");
        PostSearch areaAndSectorFilter = new PostSearch("1", "1");

        assertThat(noFilter.isSectorFilter()).isFalse();
        assertThat(areaFilter.isSectorFilter()).isFalse();
        assertThat(areaAndSectorFilter.isSectorFilter()).isFalse();
    }

    @DisplayName("SectorIds, AreaIds가 모두 없는 경우, True")
    @Test
    void isSectorFilter_HasNoIds_True() {
        PostSearch noFilter = new PostSearch("", "");

        assertThat(noFilter.isNoFilter()).isTrue();
    }

    @Test
    void isNoFilter() {
        PostSearch areaFilter = new PostSearch("1", "");
        PostSearch sectorFilter = new PostSearch("", "1");
        PostSearch areaAndSectorFilter = new PostSearch("1", "1");

        assertThat(areaFilter.isNoFilter()).isFalse();
        assertThat(sectorFilter.isNoFilter()).isFalse();
        assertThat(areaAndSectorFilter.isNoFilter()).isFalse();
    }
}