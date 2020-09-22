package wooteco.team.ittabi.legenoaroundhere.service;

import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.team.ittabi.legenoaroundhere.domain.user.Email;
import wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth.Mail;
import wooteco.team.ittabi.legenoaroundhere.domain.user.mailauth.MailAuth;
import wooteco.team.ittabi.legenoaroundhere.dto.MailAuthCheckRequest;
import wooteco.team.ittabi.legenoaroundhere.dto.MailAuthCreateRequest;
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

    private void saveMailAuth(String email, int authNumber) {
        MailAuth mailAuth = new MailAuth(email, authNumber);
        mailAuthRepository.save(mailAuth);
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
        MailAuth mailAuth = mailAuthRepository.findByEmail(email)
            .orElseThrow(() -> new NotExistsException("인증되지 않은 이메일입니다."));

        validateAuthNumber(mailAuth, mailAuthCheckRequest.getAuthNumber());
    }

    private void validateAuthNumber(MailAuth mailAuth, Integer authNumber) {
        if (mailAuth.isDifferentAuthNumber(authNumber)) {
            throw new WrongUserInputException("인증 정보가 일치하지 않습니다.");
        }
    }
}
