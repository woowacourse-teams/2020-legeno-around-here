package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.List;
import java.util.Objects;
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
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.domain.user.UserImage;
import wooteco.team.ittabi.legenoaroundhere.dto.LoginRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.TokenResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserAssembler;
import wooteco.team.ittabi.legenoaroundhere.dto.UserCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserImageResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserPasswordUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.UserResponse;
import wooteco.team.ittabi.legenoaroundhere.dto.UserUpdateRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.NeedEmailAuthException;
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

    @Transactional
    public Long createUser(UserCreateRequest userCreateRequest) {
        Area area = findAreaBy(userCreateRequest.getAreaId());
        User user = UserAssembler.assemble(userCreateRequest, area);
        try {
            User createdUser = userRepository.save(user);
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
    public TokenResponse login(LoginRequest loginRequest) {
        List<User> a = userRepository.findAll();
        User user = userRepository.findByEmail(new Email(loginRequest.getEmail()))
            .orElseThrow(() -> new NotExistsException("가입되지 않은 회원입니다."));
        checkEmailAuth(user);
        checkPassword(loginRequest, user);
        return new TokenResponse(
            jwtTokenGenerator.createToken(user.getEmailByString(), user.getRoles()));
    }

    private void checkEmailAuth(User user) {
        if (user.isNotAuthenticatedByEmail()) {
            throw new NeedEmailAuthException("메일 인증이 필요합니다.");
        }
    }

    private void checkPassword(LoginRequest loginRequest, User user) {
        if (!PASSWORD_ENCODER.matches(
            loginRequest.getPassword(), user.getPasswordByString())) {
            throw new WrongUserInputException("잘못된 비밀번호입니다.");
        }
    }

    @Transactional(readOnly = true)
    public UserResponse findUser() {
        User user = (User) authenticationFacade.getPrincipal();
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateMyInfo(UserUpdateRequest userUpdateRequest) {
        User user = (User) authenticationFacade.getPrincipal();
        User foundUser = userRepository.findByEmail(user.getEmail())
            .orElseThrow(() -> new NotExistsException("사용자를 찾을 수 없습니다."));
        Area area = findAreaBy(userUpdateRequest.getAreaId());
        UserImage userImage = findUserImageBy(userUpdateRequest.getImageId());

        foundUser.setNickname(userUpdateRequest.getNickname());
        foundUser.setArea(area);
        foundUser.setImage(userImage);

        return UserResponse.from(foundUser);
    }

    @Transactional
    public void changeMyPassword(UserPasswordUpdateRequest userPasswordUpdateRequest) {
        User user = (User) authenticationFacade.getPrincipal();
        User foundUser = userRepository.findByEmail(user.getEmail())
            .orElseThrow(() -> new NotExistsException("사용자를 찾을 수 없습니다."));

        String password = userPasswordUpdateRequest.getPassword();
        if (PASSWORD_ENCODER.matches(password, user.getPasswordByString())) {
            throw new WrongUserInputException("기존의 비밀번호와 동일한 비밀번호입니다.");
        }

        String encodePassword = PASSWORD_ENCODER.encode(password);
        foundUser.setPassword(encodePassword);
    }

    private UserImage findUserImageBy(Long userImageId) {
        if (Objects.isNull(userImageId)) {
            return null;
        }
        return userImageRepository.findById(userImageId)
            .orElseThrow(() -> new NotExistsException("유효하지 않은 Image입니다."));
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

    @Transactional
    public UserImageResponse uploadUserImage(MultipartFile userImageFile) {
        UserImage userImage = imageUploader.uploadUserImage(userImageFile);
        UserImage savedUserImage = userImageRepository.save(userImage);

        return UserImageResponse.of(savedUserImage);
    }
}
