package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.Collections;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
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
import wooteco.team.ittabi.legenoaroundhere.repository.AreaRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AreaRepository areaRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final IAuthenticationFacade authenticationFacade;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public Long createUser(UserRequest userRequest) {
        User persistUser = createUserBy(userRequest, Role.USER);
        return persistUser.getId();
    }

    private User createUserBy(UserRequest userCreateRequest, Role userRole) {
        Area area = findAreaBy(userCreateRequest.getAreaId());

        return userRepository.save(User.builder()
            .email(new Email(userCreateRequest.getEmail()))
            .nickname(new Nickname(userCreateRequest.getNickname()))
            .password(new Password(passwordEncoder.encode(userCreateRequest.getPassword())))
            .roles(Collections.singletonList(userRole.getRoleName()))
            .area(area)
            .build());
    }

    private Area findAreaBy(Long areaId) {
        if (Objects.isNull(areaId)) {
            return null;
        }
        return areaRepository.findById(areaId)
            .filter(Area::isUsed)
            .orElseThrow(() -> new WrongUserInputException("입력하신 지역이 존재하지 않습니다."));
    }

    @Transactional
    public Long createAdmin(UserRequest userCreateRequest) {
        User persistUser = createUserBy(userCreateRequest, Role.ADMIN);
        return persistUser.getId();
    }

    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(new Email(loginRequest.getEmail()))
            .orElseThrow(() -> new NotExistsException("가입되지 않은 회원입니다."));

        if (!passwordEncoder.matches(
            loginRequest.getPassword(), user.getPasswordByString())) {
            throw new WrongUserInputException("잘못된 비밀번호입니다.");
        }
        return new TokenResponse(
            jwtTokenGenerator.createToken(user.getEmailByString(), user.getRoles()));
    }

    @Transactional(readOnly = true)
    public UserResponse findUser() {
        User user = (User) authenticationFacade.getPrincipal();
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateUser(UserRequest userUpdateRequest) {
        User user = (User) authenticationFacade.getPrincipal();
        User persistUser = userRepository.findByEmail(user.getEmail())
            .orElseThrow(() -> new NotExistsException("사용자를 찾을 수 없습니다."));

        updatePersistUser(persistUser, userUpdateRequest);

        return UserResponse.from(persistUser);
    }

    private void updatePersistUser(User persistUser, UserRequest userUpdateRequest) {
        Nickname newNickname = new Nickname(userUpdateRequest.getNickname());
        Password newPassword = new Password(
            passwordEncoder.encode(userUpdateRequest.getPassword()));
        Area newArea = findAreaBy(userUpdateRequest.getAreaId());

        persistUser.setNickname(newNickname);
        persistUser.setPassword(newPassword);
        persistUser.setArea(newArea);
    }

    @Transactional
    public void deleteUser() {
        User user = (User) authenticationFacade.getPrincipal();
        User persistUser = userRepository.findByEmail(user.getEmail())
            .orElseThrow(() -> new NotExistsException("사용자를 찾을 수 없습니다."));
        userRepository.delete(persistUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(new Email(email))
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
