package wooteco.team.ittabi.legenoaroundhere.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_PASSWORD;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRepositoryTest {

    private static final String TEST_PREFIX = "userrepository_";

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Base Entity 필드에 값이 잘 들어오는지 확인")
    @Test
    void save() {
        User notSavedUser = User.builder()
            .email(TEST_PREFIX + TEST_USER_EMAIL)
            .nickname(TEST_USER_NICKNAME)
            .password(TEST_USER_PASSWORD)
            .build();
        assertThat(notSavedUser.getId()).isNull();
        assertThat(notSavedUser.getCreatedAt()).isNull();
        assertThat(notSavedUser.getModifiedAt()).isNull();

        User savedUser = userRepository.save(notSavedUser);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getModifiedAt()).isNotNull();
    }
}
