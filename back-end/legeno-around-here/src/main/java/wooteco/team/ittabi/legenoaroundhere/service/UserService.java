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
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Long createUser(UserCreateRequest userCreateRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User persistUser = userRepository.save(User.builder()
            .email(new Email(userCreateRequest.getEmail()))
            .nickname(new Nickname(userCreateRequest.getNickname()))
            .password(new Password(passwordEncoder.encode(userCreateRequest.getPassword())))
            .roles(Collections.singletonList("ROLE_USER"))
            .build());
        return persistUser.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(new Email(username))
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
