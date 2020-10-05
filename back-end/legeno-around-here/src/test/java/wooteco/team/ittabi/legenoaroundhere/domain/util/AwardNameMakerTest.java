package wooteco.team.ittabi.legenoaroundhere.domain.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.ranking.RankingCriteria;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

class AwardNameMakerTest {

    private static final Post POST;
    private static final Sector SECTOR;
    private static final Area AREA;
    private static final LocalDate TEST_START_TIME = LocalDate.parse("2019-01-01");
    private static final LocalDate TEST_END_TIME = LocalDate.parse("2019-02-01");

    static {
        User user = User.builder()
            .email(TEST_USER_EMAIL)
            .nickname(TEST_USER_NICKNAME)
            .password(TEST_USER_PASSWORD)
            .build();
        SECTOR = Sector.builder()
            .name("이색 경험")
            .description(TEST_SECTOR_DESCRIPTION)
            .creator(user)
            .lastModifier(user)
            .state(SectorState.PUBLISHED)
            .build();
        AREA = Area.builder()
            .fullName("서울특별시 양천구")
            .firstDepthName("서울특별시")
            .secondDepthName("양천구")
            .thirdDepthName("")
            .fourthDepthName("")
            .build();
        POST = Post.builder()
            .writing("test writing")
            .postImages(new ArrayList<>())
            .area(AREA)
            .sector(SECTOR)
            .build();
    }

    @DisplayName("부문 창시자 상 이름 만들기")
    @Test
    void makeSectorCreatorAwardName() {
        assertThat(AwardNameMaker.makeSectorCreatorAwardName(SECTOR))
            .isEqualTo("이색 경험 부문 창시자");
    }

    @DisplayName("인기 자랑글 상 이름 만들기")
    @Test
    void makePopularPostAwardName() {
        String result = AwardNameMaker.makePopularPostAwardName(
            POST, 1, AREA, TEST_START_TIME, TEST_END_TIME, RankingCriteria.LAST_MONTH);
        assertThat(result).isEqualTo("2019년 1월 양천구 이색 경험 부문 1위");
    }

    @DisplayName("인기 자랑글 상 이름 만들기 - 지원하지 않는 RankingCriteria 예외처리")
    @Test
    void makePopularPostAwardName_IfRankingCriteriaUnsupported_ThrowException() {
        assertThatThrownBy(() -> AwardNameMaker.makePopularPostAwardName(
            POST, 1, AREA, TEST_START_TIME, TEST_END_TIME, RankingCriteria.YESTERDAY)
        ).isInstanceOf(UnsupportedOperationException.class);
    }
}
