package wooteco.team.ittabi.legenoaroundhere.config.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_ADMIN_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.team.ittabi.legenoaroundhere.domain.user.AuthProvider;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

class UserPrincipalTest {

    private static final User user;

    static {
        user = new User(TEST_ADMIN_EMAIL, TEST_ADMIN_NICKNAME, TEST_USER_PASSWORD, null, null);
        user.setProvider(AuthProvider.LOCAL);
    }

    @DisplayName("정적팩터리 메서드 - UserPrincipal 반환")
    @Test
    void create_ReturnUserPrincipal() {
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        assertThat(userPrincipal.getUsername()).isEqualTo(user.getUsername());
        assertThat(userPrincipal.getId()).isEqualTo(user.getId());
        assertThat(userPrincipal.getEmail()).isEqualTo(user.getEmail().getEmail());
        assertThat(userPrincipal.getPassword()).isEqualTo(user.getPassword());
        assertThat(userPrincipal.getAuthorities()).isEqualTo(user.getAuthorities());
        assertThat(userPrincipal.getRoles()).isEqualTo(user.getRoles());
        assertThat(userPrincipal.getAttributes()).isNull();
    }
}
