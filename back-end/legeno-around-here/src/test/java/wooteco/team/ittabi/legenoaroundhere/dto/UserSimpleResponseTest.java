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
class UserSimpleResponseTest {

    private static final Email TEST_EMAIL = new Email(TEST_USER_EMAIL);

    @Autowired
    private UserRepository userRepository;

    @DisplayName("정적 팩터리 메서드, 활성화(일반) 유저일경우 - UserSimpleResponse 반환")
    @Test
    void from_ActivatedUser_ReturnUserSimpleResponse() {
        User user = userRepository.findByEmail(TEST_EMAIL)
            .orElseThrow(() -> new NotExistsException("존재하지 않는 회원입니다."));

        assertThat(user.isDeactivated()).isFalse();

        UserSimpleResponse userSimpleResponse = UserSimpleResponse.from(user);
        assertThat(userSimpleResponse.getId()).isEqualTo(user.getId());
        assertThat(userSimpleResponse.getEmail()).isEqualTo(user.getEmail().getEmail());
        assertThat(userSimpleResponse.getNickname()).isEqualTo(user.getNickname());
        assertThat(userSimpleResponse.getImage()).isEqualTo(UserImageResponse.of(user.getImage()));
    }

    @DisplayName("정적 팩터리 메서드, 비활성화(탈퇴) 유저일 경우 - 필드 대체된 UserSimpleResponse 반환")
    @Test
    void from_DeactivatedUser_ReturnReplacedUserSimpleResponse() {
        User user = userRepository.findByEmail(TEST_EMAIL)
            .orElseThrow(() -> new NotExistsException("존재하지 않는 회원입니다."));
        user.deactivate();

        assertThat(user.isDeactivated()).isTrue();

        UserSimpleResponse userSimpleResponse = UserSimpleResponse.from(user);
        assertThat(userSimpleResponse.getId()).isEqualTo(user.getId());
        assertThat(userSimpleResponse.getEmail()).isEqualTo(UserResponse.DEACTIVATED_USER_EMAIL);
        assertThat(userSimpleResponse.getNickname())
            .isEqualTo(UserResponse.DEACTIVATED_USER_NICKNAME);
        assertThat(userSimpleResponse.getImage()).isEqualTo(UserImageResponse.of(user.getImage()));
    }
}
