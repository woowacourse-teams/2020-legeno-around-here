package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.Collections;
import javax.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Nickname;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Password;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.domain.user.UserRole;
import wooteco.team.ittabi.legenoaroundhere.dto.LoginRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.UserInputException;
import wooteco.team.ittabi.legenoaroundhere.infra.JwtTokenGenerator;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtTokenGenerator jwtTokenGenerator;

    public UserService(UserRepository userRepository, JwtTokenGenerator jwtTokenGenerator) {
        this.userRepository = userRepository;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    @Transactional
    public Long createUser(UserCreateRequest userCreateRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User persistUser = userRepository.save(User.builder()
            .email(new Email(userCreateRequest.getEmail()))
            .nickname(new Nickname(userCreateRequest.getNickname()))
            .password(new Password(passwordEncoder.encode(userCreateRequest.getPassword())))
            .roles(Collections.singletonList(UserRole.USER.getRoleName()))
            .build());
        return persistUser.getId();
    }

    public TokenResponse login(LoginRequest loginRequest) {
        // Todo: PasswordEncoder 를 config 에서 빈 등록 하도록 수정 해야함
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User user = userRepository.findByEmail(new Email(loginRequest.getEmail()))
            .orElseThrow(() -> new UserInputException("가입되지 않은 회원입니다."));

        if (!passwordEncoder.matches(
            loginRequest.getPassword(), user.getPasswordByString())) {
            throw new UserInputException("잘못된 비밀번호입니다.");
        }
        return new TokenResponse(
            jwtTokenGenerator.createToken(user.getEmailByString(), user.getRoles()));
    }

    // Todo: email 로 find 하는데 메서드 이름이 loadUserByUserName 이어서 이상함..
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(new Email(email))
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
