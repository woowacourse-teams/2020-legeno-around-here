package wooteco.team.ittabi.legenoaroundhere.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

class PostSearchTest {

    private static final long AREA_ID = 1L;

    @DisplayName("생성자에 null 또는 empty가 들어가는 경우 빈 List 필드 생성")
    @Test
    void constructor_NullOrEmpty_IsSizeZero() {
        PostSearch postSearch = new PostSearch(null, null);

        assertThat(postSearch.getAreaId()).isNull();
        assertThat(postSearch.getSectorIds()).hasSize(0);

        postSearch = new PostSearch(null, "");

        assertThat(postSearch.getAreaId()).isNull();
        assertThat(postSearch.getSectorIds()).hasSize(0);
    }

    @DisplayName("생성자에 구분자가 없는 문자열(숫자)이 들어가는 경우")
    @Test
    void constructor_HasNotDelimiterNumber_Success() {
        String sectorIds = "1";

        PostSearch postSearch = new PostSearch(AREA_ID, sectorIds);

        assertThat(postSearch.getAreaId()).isEqualTo(AREA_ID);
        assertThat(postSearch.getSectorIds()).hasSize(1);
        assertThat(postSearch.getSectorIds()).contains(Long.valueOf(sectorIds));
    }

    @DisplayName("생성자에 구분자가 있는 문자열(숫자)이 들어가는 경우")
    @Test
    void constructor_HasDelimiterNumber_Success() {
        String sectorIds = "1,3,4";

        PostSearch postSearch = new PostSearch(AREA_ID, sectorIds);

        assertThat(postSearch.getAreaId()).isEqualTo(AREA_ID);
        assertThat(postSearch.getSectorIds()).hasSize(3);
    }

    @DisplayName("생성자에 올바르지 않은 문자열(숫자)이 들어가는 경우")
    @Test
    void constructor_WrongIds_Success() {
        assertThatThrownBy(() -> new PostSearch(AREA_ID, "1 "))
            .isInstanceOf(WrongUserInputException.class);

        assertThatThrownBy(() -> new PostSearch(AREA_ID, "ㄱ"))
            .isInstanceOf(WrongUserInputException.class);
    }

    @DisplayName("AreaId만 있는 경우, True")
    @Test
    void isAreaFilter_HasOnlyAreaId_True() {
        PostSearch areaFilter = new PostSearch(AREA_ID, "");

        assertThat(areaFilter.isAreaFilter()).isTrue();
    }

    @DisplayName("AreaId만 있는 경우가 아니면, false")
    @Test
    void isAreaFilter_HasOnlyAreaId_False() {
        PostSearch noFilter = new PostSearch(null, "");
        PostSearch sectorFilter = new PostSearch(null, "1");
        PostSearch areaAndSectorFilter = new PostSearch(AREA_ID, "1");

        assertThat(noFilter.isAreaFilter()).isFalse();
        assertThat(sectorFilter.isAreaFilter()).isFalse();
        assertThat(areaAndSectorFilter.isAreaFilter()).isFalse();
    }

    @DisplayName("SectorIds만 있는 경우, True")
    @Test
    void isSectorFilter_HasOnlySectorIds_True() {
        PostSearch sectorFilter = new PostSearch(null, "1");

        assertThat(sectorFilter.isSectorFilter()).isTrue();
    }

    @DisplayName("SectorIds만 있는 경우가 아니면, false")
    @Test
    void isSectorFilter_HasNotOnlySectorIds_True() {
        PostSearch noFilter = new PostSearch(null, "");
        PostSearch areaFilter = new PostSearch(AREA_ID, "");
        PostSearch areaAndSectorFilter = new PostSearch(AREA_ID, "1");

        assertThat(noFilter.isSectorFilter()).isFalse();
        assertThat(areaFilter.isSectorFilter()).isFalse();
        assertThat(areaAndSectorFilter.isSectorFilter()).isFalse();
    }

    @DisplayName("SectorIds, AreaId가 모두 없는 경우, True")
    @Test
    void isSectorFilter_HasNoIds_True() {
        PostSearch noFilter = new PostSearch(null, "");

        assertThat(noFilter.isNotExistsFilter()).isTrue();
    }

    @DisplayName("SectorIds, AreaId가 하나라도 있는 경우, False")
    @Test
    void isNoFilter_HasIds_False() {
        PostSearch areaFilter = new PostSearch(AREA_ID, "");
        PostSearch sectorFilter = new PostSearch(null, "1");
        PostSearch areaAndSectorFilter = new PostSearch(AREA_ID, "1");

        assertThat(areaFilter.isNotExistsFilter()).isFalse();
        assertThat(sectorFilter.isNotExistsFilter()).isFalse();
        assertThat(areaAndSectorFilter.isNotExistsFilter()).isFalse();
    }

    @DisplayName("SectorIds, AreaId가 모두 없는 경우, True")
    @Test
    void isNoFilter_HasNotIds_False() {
        PostSearch noFilter = new PostSearch(null, "");

        assertThat(noFilter.isNotExistsFilter()).isTrue();
    }
}