package wooteco.team.ittabi.legenoaroundhere.service.find;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Nickname;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.dto.PasswordFindRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.repository.UserRepository;
import wooteco.team.ittabi.legenoaroundhere.service.MailAuthService;
import wooteco.team.ittabi.legenoaroundhere.utils.AuthUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordFindService {

    private final UserRepository userRepository;
    private final MailAuthService mailAuthService;

    public void findPassword(PasswordFindRequest passwordFindRequest) {
        Nickname nickname = new Nickname(passwordFindRequest.getNickname());
        Email email = new Email(passwordFindRequest.getEmail());
        validateExistUser(nickname, email);
        int authNumber = AuthUtils.makeRandomAuthNumber();
        resetUserPassword(email, authNumber);
        mailAuthService.sendAuthMail(email.getEmail(), authNumber);
    }

    private void resetUserPassword(Email email, int authNumber) {
        User user = findUserByEmail(email);
        user.setPassword(String.valueOf(authNumber));
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
