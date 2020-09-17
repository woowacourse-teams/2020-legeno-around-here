package wooteco.team.ittabi.legenoaroundhere.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class UserResponseTest {

    private static final Email TEST_EMAIL = new Email(TEST_USER_EMAIL);

    @Autowired
    protected UserRepository userRepository;

    @Test
    void from_ActivatedUser() {
        User user = userRepository.findByEmail(TEST_EMAIL)
            .orElseThrow(() -> new NotExistsException("존재하지 않는 회원입니다."));

        assertThat(user.isDeactivated()).isFalse();

        UserResponse userResponse = UserResponse.from(user);
        assertThat(userResponse.getId()).isEqualTo(user.getId());
        assertThat(userResponse.getEmail()).isEqualTo(user.getEmail().getEmail());
        assertThat(userResponse.getNickname()).isEqualTo(user.getNickname());
        assertThat(userResponse.getImage()).isEqualTo(UserImageResponse.of(user.getImage()));
        assertThat(userResponse.getArea()).isEqualTo(AreaResponse.of(user.getArea()));
        assertThat(userResponse.getAwardsCount()).isEqualTo(AwardsCountResponse.of(user));
    }

    @Test
    void from_DeactivatedUser() {
        User user = userRepository.findByEmail(TEST_EMAIL)
            .orElseThrow(() -> new NotExistsException("존재하지 않는 회원입니다."));
        user.deactivate();

        assertThat(user.isDeactivated()).isTrue();

        UserResponse userResponse = UserResponse.from(user);
        assertThat(userResponse.getId()).isEqualTo(user.getId());
        assertThat(userResponse.getEmail()).isEqualTo(UserResponse.DEACTIVATED_USER_EMAIL);
        assertThat(userResponse.getNickname()).isEqualTo(UserResponse.DEACTIVATED_USER_NICKNAME);
        assertThat(userResponse.getImage()).isNull();
        assertThat(userResponse.getArea()).isNull();
        assertThat(userResponse.getAwardsCount()).isEqualTo(AwardsCountResponse.ofInitialValue());
    }
}
