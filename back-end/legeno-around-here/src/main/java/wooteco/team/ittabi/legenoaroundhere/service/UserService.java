package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.Collections;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
import wooteco.team.ittabi.legenoaroundhere.dto.UserRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;
import wooteco.team.ittabi.legenoaroundhere.infra.JwtTokenGenerator;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtTokenGenerator jwtTokenGenerator;

    @Transactional
    public Long createUser(UserRequest userRequest) {
        User persistUser = createUserBy(userRequest, Role.USER);
        return persistUser.getId();
    }

    private User createUserBy(UserRequest userCreateRequest, Role userRole) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
            .email(new Email(userCreateRequest.getEmail()))
            .nickname(new Nickname(userCreateRequest.getNickname()))
            .password(new Password(passwordEncoder.encode(userCreateRequest.getPassword())))
            .roles(Collections.singletonList(userRole.getRoleName()))
            .build());
    }

    @Transactional
    public Long createAdmin(UserRequest userCreateRequest) {
        User persistUser = createUserBy(userCreateRequest, Role.ADMIN);
        return persistUser.getId();
    }

    public TokenResponse login(LoginRequest loginRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User user = userRepository.findByEmail(new Email(loginRequest.getEmail()))
            .orElseThrow(() -> new NotExistsException("가입되지 않은 회원입니다."));

        if (!passwordEncoder.matches(
            loginRequest.getPassword(), user.getPasswordByString())) {
            throw new WrongUserInputException("잘못된 비밀번호입니다.");
        }
        return new TokenResponse(
            jwtTokenGenerator.createToken(user.getEmailByString(), user.getRoles()));
    }

    public UserResponse findUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return UserResponse.from(user);
    }

    public UserResponse updateUser(UserRequest userUpdateRequest) {
        return null;
    }

    public void deleteUser() {
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(new Email(email))
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
