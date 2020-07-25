package wooteco.team.ittabi.legenoaroundhere.service;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_EMAIL;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_NICKNAME;
import static wooteco.team.ittabi.legenoaroundhere.constants.UserTestConstants.TEST_PASSWORD;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;

@DataJpaTest
@Import(UserService.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("User 생성")
    void createUser() {
        UserCreateRequest userCreateRequest =
            new UserCreateRequest(TEST_EMAIL, TEST_NICKNAME, TEST_PASSWORD);
        Long userId = userService.createUser(userCreateRequest);

        assertThat(userId).isNotNull();
    }
}
