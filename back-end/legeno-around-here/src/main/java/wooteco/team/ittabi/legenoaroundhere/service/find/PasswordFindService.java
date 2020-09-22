package wooteco.team.ittabi.legenoaroundhere.service.find;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Nickname;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth.Mail;
import wooteco.team.ittabi.legenoaroundhere.dto.PasswordFindRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;
import wooteco.team.ittabi.legenoaroundhere.service.MailAuthService;
import wooteco.team.ittabi.legenoaroundhere.utils.AuthUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordFindService {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private final UserRepository userRepository;
    private final MailAuthService mailAuthService;

    @Transactional
    public void findPassword(PasswordFindRequest passwordFindRequest) {
        Nickname nickname = new Nickname(passwordFindRequest.getNickname());
        Email email = new Email(passwordFindRequest.getEmail());
        validateExistUser(nickname, email);
        int authNumber = AuthUtils.makeRandomAuthNumber();
        resetUserPassword(email, authNumber);
        mailAuthService.sendMailAuth(Mail.builder()
            .email(email.getEmail())
            .subject("우리동네캡짱 " + nickname.getNickname() + "님의 변경된 비밀번호입니다.")
            .text("<p> 변경된 비밀번호: " + authNumber + "</p><p>변경된 비밀번호로 로그인 후 비밀번호를 수정해주세요!</p>")
            .build());
    }

    private void resetUserPassword(Email email, int authNumber) {
        User user = findUserByEmail(email);
        user.setPassword(PASSWORD_ENCODER.encode(String.valueOf(authNumber)));
    }

    private User findUserByEmail(Email email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new NotExistsException("Email : " + email + " 에 해당하는 User가 없습니다!"));
    }

    private void validateExistUser(Nickname nickname, Email email) {
        if (isNotExistsUserByNickname(nickname)) {
            log.info("Not Exist Nickname : {}", nickname);
            throw new NotExistsException("해당 닉네임이 존재하지 않습니다!");
        }

        if (isNotExistsUserByEmail(email)) {
            log.info("Not Exist email : {}", email);
            throw new NotExistsException("해당 이메일이 존재하지 않습니다!");
        }
    }

    private boolean isNotExistsUserByNickname(Nickname nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    private boolean isNotExistsUserByEmail(Email email) {
        return !userRepository.existsByEmail(email);
    }
}
