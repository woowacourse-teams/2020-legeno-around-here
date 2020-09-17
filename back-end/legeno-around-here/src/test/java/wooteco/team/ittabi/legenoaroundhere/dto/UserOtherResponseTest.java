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
class UserOtherResponseTest {

    private static final Email TEST_EMAIL = new Email(TEST_USER_EMAIL);

    @Autowired
    protected UserRepository userRepository;

    @Test
    void from_ActivatedUser() {
        User user = userRepository.findByEmail(TEST_EMAIL)
            .orElseThrow(() -> new NotExistsException("존재하지 않는 회원입니다."));

        assertThat(user.isDeactivated()).isFalse();

        UserOtherResponse userOtherResponse = UserOtherResponse.from(user);
        assertThat(userOtherResponse.getId()).isEqualTo(user.getId());
        assertThat(userOtherResponse.getNickname()).isEqualTo(user.getNickname());
        assertThat(userOtherResponse.getImage()).isEqualTo(UserImageResponse.of(user.getImage()));
        assertThat(userOtherResponse.getAwardsCount()).isEqualTo(AwardsCountResponse.of(user));
    }

    @Test
    void from_DeactivatedUser() {
        User user = userRepository.findByEmail(TEST_EMAIL)
            .orElseThrow(() -> new NotExistsException("존재하지 않는 회원입니다."));
        user.deactivate();

        assertThat(user.isDeactivated()).isTrue();

        UserOtherResponse userOtherResponse = UserOtherResponse.from(user);
        assertThat(userOtherResponse.getId()).isEqualTo(user.getId());
        assertThat(userOtherResponse.getNickname())
            .isEqualTo(UserResponse.DEACTIVATED_USER_NICKNAME);
        assertThat(userOtherResponse.getImage()).isEqualTo(UserImageResponse.of(user.getImage()));
        assertThat(userOtherResponse.getAwardsCount()).isEqualTo(AwardsCountResponse.of(user));
    }
}