package wooteco.team.ittabi.legenoaroundhere.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_NAME;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_PASSWORD;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UserTest {

    @Test
    @DisplayName("생성자 테스트")
    void constructor() {
        Email email = new Email(TEST_EMAIL);
        NickName nickName = new NickName(TEST_NAME);
        Password password = new Password(TEST_PASSWORD);

        assertThat(new User(email, nickName, password)).isInstanceOf(User.class);
    }

    @ParameterizedTest
    @DisplayName("생성자 테스트 - null이 존재할 때")
    @MethodSource("getNullInput")
    void constructor_IfExistNull_ThrowException(Email email, NickName nickName, Password password) {
        assertThatThrownBy(() -> new User(email, nickName, password))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> getNullInput() {
        return Stream.of(
            Arguments.of(null, new NickName(TEST_NAME), new Password(TEST_PASSWORD)),
            Arguments.of(new Email(TEST_EMAIL), null, new Password(TEST_PASSWORD)),
            Arguments.of(new Email(TEST_EMAIL), new NickName(TEST_NAME), null),
            Arguments.of(new Email(TEST_EMAIL), null, null),
            Arguments.of(null, new NickName(TEST_NAME), null),
            Arguments.of(null, null, new Password(TEST_PASSWORD)),
            Arguments.of(null, null, null)
        );
    }
}
