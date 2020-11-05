package wooteco.team.ittabi.legenoaroundhere.domain.award;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.domain.util.constants.PostConstants.TEST_POST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.domain.util.constants.SectorConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.domain.util.constants.SectorConstants.TEST_SECTOR_NAME;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

class PopularPostAwardTest {

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .email("test@test.com")
            .nickname("테스트닉네임")
            .password("password")
            .area(null)
            .image(null)
            .build();

        post = Post.builder()
            .writing(TEST_POST_WRITING)
            .creator(user)
            .sector(Sector.builder()
                .state(SectorState.PUBLISHED)
                .name(TEST_SECTOR_NAME)
                .description(TEST_SECTOR_DESCRIPTION)
                .creator(user)
                .lastModifier(user)
                .build())
            .build();
    }

    @Test
    void isTopBy() {
        PopularPostAward award = PopularPostAward.builder()
            .name("상이름")
            .awardee(user)
            .post(post)
            .ranking(2)
            .startDate(LocalDate.parse("2018-01-01"))
            .endDate(LocalDate.parse("2018-01-02"))
            .build();
        assertThat(award.isTopBy(3)).isTrue();
        assertThat(award.isTopBy(2)).isTrue();
        assertThat(award.isTopBy(1)).isFalse();
    }
}
