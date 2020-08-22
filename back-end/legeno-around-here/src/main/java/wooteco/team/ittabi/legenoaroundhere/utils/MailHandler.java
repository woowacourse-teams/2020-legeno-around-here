package wooteco.team.ittabi.legenoaroundhere.utils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailHandler {

    private static final String DEFAULT_CHARACTER_SET = "UTF-8";

    private final JavaMailSender javaMailSender;
    private final MimeMessage mimeMessage;
    private final MimeMessageHelper mimeMessageHelper;

    public MailHandler(JavaMailSender javaMailSender) throws MessagingException {
        this.javaMailSender = javaMailSender;
        this.mimeMessage = javaMailSender.createMimeMessage();
        this.mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, DEFAULT_CHARACTER_SET);
    }

    public void setFrom(String email) throws MessagingException {
        mimeMessageHelper.setFrom(email);
    }

    public void setTo(String email) throws MessagingException {
        mimeMessageHelper.setTo(email);
    }

    public void setSubject(String subject) throws MessagingException {
        mimeMessageHelper.setSubject(subject);
    }

    public void setText(String text) throws MessagingException {
        mimeMessageHelper.setText(text, true);
    }

    public void send() {
        javaMailSender.send(mimeMessage);
    }
}