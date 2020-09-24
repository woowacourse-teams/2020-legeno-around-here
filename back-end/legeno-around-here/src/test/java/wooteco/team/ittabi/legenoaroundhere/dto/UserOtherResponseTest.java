package wooteco.team.ittabi.legenoaroundhere.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.UserConstants.TEST_USER_EMAIL;

import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("정적 팩터리 메서드, 활성화(일반) 유저일경우 - UserOtherResponse 반환")
    @Test
    void from_ActivatedUser_ReturnUserOtherResponse() {
        User user = userRepository.findByEmail(TEST_EMAIL)
            .orElseThrow(() -> new NotExistsException("존재하지 않는 회원입니다."));

        assertThat(user.isDeactivated()).isFalse();

        UserOtherResponse userOtherResponse = UserOtherResponse.from(user);
        assertThat(userOtherResponse.getId()).isEqualTo(user.getId());
        assertThat(userOtherResponse.getNickname()).isEqualTo(user.getNickname());
        assertThat(userOtherResponse.getImage()).isEqualTo(UserImageResponse.of(user.getImage()));
        assertThat(userOtherResponse.getAwardsCount()).isEqualTo(AwardsCountResponse.of(user));
    }

    @DisplayName("정적 팩터리 메서드, 비활성화(탈퇴) 유저일 경우 - 필드 대체된 UserOtherResponse 반환")
    @Test
    void from_DeactivatedUser_ReturnReplacedUserOtherResponse() {
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
