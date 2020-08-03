package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.Collections;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Nickname;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Password;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Role;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.LoginRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.UserInputException;
import wooteco.team.ittabi.legenoaroundhere.infra.JwtTokenGenerator;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtTokenGenerator jwtTokenGenerator;

    @Transactional
    public Long createUser(UserCreateRequest userCreateRequest) {
        User persistUser = createUserBy(userCreateRequest, Role.USER);
        return persistUser.getId();
    }

    private User createUserBy(UserCreateRequest userCreateRequest, Role user) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
            .email(new Email(userCreateRequest.getEmail()))
            .nickname(new Nickname(userCreateRequest.getNickname()))
            .password(new Password(passwordEncoder.encode(userCreateRequest.getPassword())))
            .roles(Collections.singletonList(user.getRoleName()))
            .build());
    }

    @Transactional
    public Long createAdmin(UserCreateRequest userCreateRequest) {
        User persistUser = createUserBy(userCreateRequest, Role.ADMIN);
        return persistUser.getId();
    }

    public TokenResponse login(LoginRequest loginRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User user = userRepository.findByEmail(new Email(loginRequest.getEmail()))
            .orElseThrow(() -> new NotExistsException("가입되지 않은 회원입니다."));

        if (!passwordEncoder.matches(
            loginRequest.getPassword(), user.getPasswordByString())) {
            throw new UserInputException("잘못된 비밀번호입니다.");
        }
        return new TokenResponse(
            jwtTokenGenerator.createToken(user.getEmailByString(), user.getRoles()));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(new Email(email))
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    public UserResponse findUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return UserResponse.from(user);
    }
}
