package wooteco.team.ittabi.legenoaroundhere.service;

import java.util.Optional;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth.AuthNumber;
import wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth.Mail;
import wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth.MailAuth;
import wooteco.team.ittabi.legenoaroundhere.dto.MailAuthCheckRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.MailAuthCreateRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.MailAuthFindPasswordRequest;
import wooteco.team.ittabi.legenoaroundhere.exception.FailedSendMailException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;
import wooteco.team.ittabi.legenoaroundhere.repository.MailAuthRepository;
import wooteco.team.ittabi.legenoaroundhere.utils.AuthUtils;
import wooteco.team.ittabi.legenoaroundhere.utils.MailHandler;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailAuthService {

    private final JavaMailSender javaMailSender;
    private final MailAuthRepository mailAuthRepository;

    @Transactional
    public void publishAuth(MailAuthCreateRequest mailAuthCreateRequest) {
        int authNumber = AuthUtils.makeRandomAuthNumber();
        String email = mailAuthCreateRequest.getEmail();

        saveMailAuth(email, authNumber);
        sendMailAuth(Mail.builder()
            .email(email)
            .subject("우리동네캡짱 회원 가입 인증 메일")
            .text("<p> 회원가입 인증번호 : " + authNumber + "</p>")
            .build());
    }

    @Transactional
    public void publishAuth(MailAuthFindPasswordRequest mailAuthFindPasswordRequest) {
        int authNumber = AuthUtils.makeRandomAuthNumber();
        String email = mailAuthFindPasswordRequest.getEmail();

        saveMailAuth(email, authNumber);
        sendMailAuth(Mail.builder()
            .email(email)
            .subject("우리동네캡짱 비밀번호 찾기 인증 메일")
            .text("<p> 비밀번호 찾기 인증번호 : " + authNumber + "</p>")
            .build());
    }

    private void saveMailAuth(String email, int authNumber) {
        Optional<MailAuth> mailAuth = findByEmail(new Email(email));

        if (mailAuth.isPresent()) {
            updateAuthNumber(mailAuth.get(), authNumber);
            return;
        }
        mailAuthRepository.save(new MailAuth(email, authNumber));
    }

    private void updateAuthNumber(MailAuth mailAuth, int authNumber) {
        mailAuth.setAuthNumber(new AuthNumber(authNumber));
    }

    public void sendMailAuth(Mail mail) {
        try {
            MailHandler mailHandler = new MailHandler(javaMailSender);
            mailHandler.setTo(mail.getEmail());
            mailHandler.setSubject(mail.getSubject());
            mailHandler.setText(mail.getText());
            mailHandler.send();
        } catch (MessagingException e) {
            throw new FailedSendMailException("인증 메일 전송에 실패하였습니다.");
        }
    }

    @Transactional
    public void checkAuth(MailAuthCheckRequest mailAuthCheckRequest) {
        Email email = new Email(mailAuthCheckRequest.getEmail());
        MailAuth mailAuth = findByEmail(email)
            .orElseThrow(() -> new NotExistsException("인증되지 않은 이메일입니다."));

        validateAuthNumber(mailAuth, mailAuthCheckRequest.getAuthNumber());
    }

    private Optional<MailAuth> findByEmail(Email email) {
        return mailAuthRepository.findByEmail(email);
    }

    private void validateAuthNumber(MailAuth mailAuth, Integer authNumber) {
        if (mailAuth.isDifferentAuthNumber(authNumber)) {
            throw new WrongUserInputException("인증 정보가 일치하지 않습니다.");
        }
    }
}
