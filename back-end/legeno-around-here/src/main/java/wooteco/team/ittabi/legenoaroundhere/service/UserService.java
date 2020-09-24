package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Role;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.domain.user.UserImage;
import wooteco.team.ittabi.legenoaroundhere.dto.LoginRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserAssembler;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCheckRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserImageResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserOtherResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserPasswordUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserPasswordWithAuthUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.AlreadyExistUserException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;
import wooteco.team.ittabi.legenoaroundhere.infra.JwtTokenGenerator;
import wooteco.team.ittabi.legenoaroundhere.repository.AreaRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.UserImageRepository;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;
import wooteco.team.ittabi.legenoaroundhere.utils.ImageUploader;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;
    private final AreaRepository areaRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final IAuthenticationFacade authenticationFacade;
    private final ImageUploader imageUploader;
    private final MailAuthService mailAuthService;
    private final NotificationService notificationService;

    @Transactional
    public void checkJoined(UserCheckRequest userCheckRequest) {
        Email email = new Email(userCheckRequest.getEmail());
        Optional<User> foundUser = userRepository.findByEmail(email);

        foundUser.ifPresent(user -> {
            throw new AlreadyExistUserException("이미 가입된 회원입니다.");
        });
    }

    @Transactional
    public Long createUser(UserCreateRequest userCreateRequest) {
        mailAuthService.checkAuth(userCreateRequest.toMailAuthCheckRequest());
        Area area = findAreaBy(userCreateRequest.getAreaId());
        User user = UserAssembler.assemble(userCreateRequest, area);
        try {
            User createdUser = userRepository.save(user);
            notificationService.notifyJoinNotification(createdUser);
            return createdUser.getId();
        } catch (DataIntegrityViolationException e) {
            throw new WrongUserInputException(
                "[" + userCreateRequest.getEmail() + "] 이메일은 이미 사용중입니다.");
        }
    }

    private Area findAreaBy(Long areaId) {
        if (Objects.isNull(areaId)) {
            return null;
        }
        return areaRepository.findById(areaId)
            .orElseThrow(() -> new WrongUserInputException("입력하신 지역이 존재하지 않습니다."));
    }

    @Transactional
    public TokenResponse loginAdmin(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        User user = findUserByEmail(email);
        validateAdmin(user);
        return getTokenResponse(loginRequest, user);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(new Email(email))
            .orElseThrow(() -> new NotExistsException("가입되지 않은 회원입니다."));
    }

    private TokenResponse getTokenResponse(LoginRequest loginRequest, User user) {
        validateDeactivated(user);
        checkPassword(loginRequest, user);
        String token = jwtTokenGenerator.createToken(user.getUsername(), user.getRoles());
        return new TokenResponse(token);
    }

    private void validateDeactivated(User user) {
        if (user.isDeactivated()) {
            throw new NotExistsException("탈퇴한 회원입니다.");
        }
    }

    private void validateAdmin(User user) {
        if (user.hasNotRole(Role.ADMIN)) {
            throw new NotAuthorizedException("관리자가 아닙니다.");
        }
    }

    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        User user = findUserByEmail(email);
        return getTokenResponse(loginRequest, user);
    }

    private void checkPassword(LoginRequest loginRequest, User user) {
        if (!PASSWORD_ENCODER.matches(
            loginRequest.getPassword(), user.getPassword())) {
            throw new WrongUserInputException("잘못된 비밀번호입니다.");
        }
    }

    @Transactional(readOnly = true)
    public UserResponse findMe() {
        User user = findPrincipal();
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateMe(UserUpdateRequest userUpdateRequest) {
        User user = findPrincipal();
        Area area = findAreaBy(userUpdateRequest.getAreaId());
        UserImage userImage = findUserImageBy(userUpdateRequest.getImageId());

        user.setNickname(userUpdateRequest.getNickname());
        user.setArea(area);
        user.setImage(userImage);

        return UserResponse.from(user);
    }

    private User findPrincipal() {
        User user = (User) authenticationFacade.getPrincipal();

        return findById(user.getId());
    }

    private User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotExistsException("ID : " + userId + " 에 해당하는 User가 없습니다!"));
    }

    @Transactional
    public void changeMyPasswordWithAuth(
        UserPasswordWithAuthUpdateRequest userPasswordWithAuthUpdateRequest) {
        User user = findPrincipal();
        String password = userPasswordWithAuthUpdateRequest.getPassword();

        changeMyPassword(user, password);
    }

    @Transactional
    public void changeMyPasswordWithoutAuth(UserPasswordUpdateRequest userPasswordUpdateRequest) {
        String email = userPasswordUpdateRequest.getEmail();
        String password = userPasswordUpdateRequest.getPassword();
        User user = findUserByEmail(email);

        changeMyPassword(user, password);
    }

    private void changeMyPassword(User user, String password) {
        if (PASSWORD_ENCODER.matches(password, user.getPassword())) {
            throw new WrongUserInputException("기존의 비밀번호와 동일한 비밀번호입니다.");
        }

        String encodePassword = PASSWORD_ENCODER.encode(password);
        user.setPassword(encodePassword);
    }

    private UserImage findUserImageBy(Long userImageId) {
        if (Objects.isNull(userImageId)) {
            return null;
        }
        return userImageRepository.findById(userImageId)
            .orElseThrow(() -> new NotExistsException("유효하지 않은 Image입니다."));
    }

    @Transactional
    public void deactivateMe() {
        User user = findPrincipal();
        user.deactivate();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(new Email(email))
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public UserImageResponse uploadUserImage(MultipartFile userImageFile) {
        UserImage userImage = imageUploader.uploadUserImage(userImageFile);
        UserImage savedUserImage = userImageRepository.save(userImage);

        return UserImageResponse.of(savedUserImage);
    }

    @Transactional(readOnly = true)
    public UserOtherResponse findUser(Long userId) {
        User user = findById(userId);
        validateDeactivated(user);
        return UserOtherResponse.from(user);
    }
}
