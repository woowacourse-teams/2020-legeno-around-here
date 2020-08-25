package wooteco.team.ittabi.legenoaroundhere.service;

import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AREA_ID;
import static wooteco.team.ittabi.legenoaroundhere.utils.constants.AreaConstants.TEST_AUTH_NUMBER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.jdbc.Sql;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/init-table.sql")
public abstract class ServiceTest {

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected IAuthenticationFacade authenticationFacade;

    protected User createUser(String email, String nickname, String password) {
        UserCreateRequest userCreateRequest
            = new UserCreateRequest(email, nickname, password, TEST_AREA_ID, TEST_AUTH_NUMBER);
        Long userId = userService.createUser(userCreateRequest);

        return userRepository.findById(userId)
            .orElseThrow(() -> new NotExistsException("해당하는 사용자가 존재하지 않습니다."));
    }

    protected void setAuthentication(User user) {
        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        org.springframework.security.core.Authentication authToken = new UsernamePasswordAuthenticationToken(
            user, "TestCredentials", userDetails.getAuthorities());
        authenticationFacade.setAuthentication(authToken);
    }
}
