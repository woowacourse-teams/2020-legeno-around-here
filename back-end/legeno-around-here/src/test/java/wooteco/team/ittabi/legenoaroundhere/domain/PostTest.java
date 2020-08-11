package wooteco.team.ittabi.legenoaroundhere.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_PASSWORD;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Nickname;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Password;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

public class PostTest {

    private final User user = User.builder()
        .email(new Email(TEST_EMAIL))
        .nickname(new Nickname(TEST_NICKNAME))
        .password(new Password(TEST_PASSWORD))
        .build();

    private final Sector sector = Sector.builder()
        .name(TEST_SECTOR_NAME)
        .description(TEST_SECTOR_DESCRIPTION)
        .creator(user)
        .lastModifier(user)
        .state(SectorState.PUBLISHED)
        .build();

    @DisplayName("길이 검증 - 예외 발생")
    @Test
    void validateLength_OverLength_ThrownException() {
        String overLengthInput = "aaaaaaaaaaaaaaaaaaaaa";
        assertThatThrownBy(() -> new Post(user, overLengthInput, sector))
            .isInstanceOf(WrongUserInputException.class);
    }

    @DisplayName("같은 상태인지 확인")
    @Test
    void isSameState_SameState_True() {
        Post post = new Post(user, TEST_WRITING, sector);
        post.setState(State.DELETED);

        assertThat(post.isSameState(State.DELETED)).isTrue();
    }

    @DisplayName("다른 상태인지 확인")
    @Test
    void isNotSameState_DifferentState_True() {
        Post post = new Post(user, TEST_WRITING, sector);
        post.setState(State.DELETED);

        assertThat(post.isNotSameState(State.PUBLISHED)).isTrue();
    }
}
