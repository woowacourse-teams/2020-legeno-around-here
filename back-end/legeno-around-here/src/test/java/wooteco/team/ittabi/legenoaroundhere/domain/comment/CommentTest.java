package wooteco.team.ittabi.legenoaroundhere.domain.comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_COMMENT_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.PostConstants.TEST_POST_WRITING;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_DESCRIPTION;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.SectorConstants.TEST_SECTOR_NAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.post.State;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.SectorState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAvailableException;

class CommentTest {

    private User creator;
    private Comment comment;
    private Post post;

    @BeforeEach
    void setUp() {
        creator = User.builder()
            .email(TEST_USER_EMAIL)
            .nickname(TEST_USER_NICKNAME)
            .password(TEST_USER_PASSWORD)
            .build();

        post = Post.builder()
            .writing(TEST_POST_WRITING)
            .creator(creator)
            .sector(Sector.builder()
                .state(SectorState.PUBLISHED)
                .name(TEST_SECTOR_NAME)
                .description(TEST_SECTOR_DESCRIPTION)
                .creator(creator)
                .lastModifier(creator)
                .build())
            .build();

        comment = new Comment(creator, TEST_COMMENT_WRITING);
        comment.setPost(post);
    }

    @DisplayName("Zzang을 눌렀을 때, 예외 발생 - Comment가 유효한 상태가 아닐 경우")
    @Test
    void pressZzang_CommentNotAvailable_ThrownException() {
        comment.setState(State.DELETED);

        assertThatThrownBy(() -> comment.pressZzang(creator))
            .isInstanceOf(NotAvailableException.class);
    }

    @DisplayName("Zzang을 눌렀을 때, 예외 발생 - Post가 유효한 상태가 아닐 경우")
    @Test
    void pressZzang_PostNotAvailable_ThrownException() {
        post.setState(State.DELETED);

        assertThatThrownBy(() -> comment.pressZzang(creator))
            .isInstanceOf(NotAvailableException.class);
    }

    @DisplayName("Zzang을 눌렀을 때, Zzang 생성 - Zzang이 없을 경우")
    @Test
    void pressZzang_HasNotZzange_CreateZzang() {
        comment.pressZzang(creator);

        List<User> zzangCreators = comment.getZzangs().stream()
            .map(CommentZzang::getCreator)
            .collect(Collectors.toList());

        assertThat(zzangCreators).contains(creator);
    }

    @DisplayName("Zzang을 눌렀을 때, Zzang 제거 - Zzang이 있을 경우")
    @Test
    void pressZzang_HasZzange_DeleteZzang() {
        comment.pressZzang(creator);
        comment.pressZzang(creator);

        List<User> zzangCreators = comment.getZzangs().stream()
            .map(CommentZzang::getCreator)
            .collect(Collectors.toList());

        assertThat(zzangCreators).doesNotContain(creator);
    }
}
