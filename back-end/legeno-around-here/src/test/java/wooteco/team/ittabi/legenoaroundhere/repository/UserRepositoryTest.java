package wooteco.team.ittabi.legenoaroundhere.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_MY_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserTestConstants.TEST_PASSWORD;

import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Nickname;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Password;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@ActiveProfiles("test")
@Transactional
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Base Entity 필드에 값이 잘 들어오는지 확인")
    @Test
    void save() {
        User notSavedUser = User.builder()
            .email(new Email(TEST_MY_EMAIL))
            .nickname(new Nickname(TEST_NICKNAME))
            .password(new Password(TEST_PASSWORD))
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
