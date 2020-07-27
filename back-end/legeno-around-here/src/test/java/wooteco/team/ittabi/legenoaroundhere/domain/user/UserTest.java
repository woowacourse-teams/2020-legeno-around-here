package wooteco.team.ittabi.legenoaroundhere.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_NAME;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_PASSWORD;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("생성자 테스트")
    void constructor() {
        Email email = new Email(TEST_EMAIL);
        Nickname nickname = new Nickname(TEST_NAME);
        Password password = new Password(TEST_PASSWORD);

        assertThat(new User(email, nickname, password)).isInstanceOf(User.class);
    }
}
