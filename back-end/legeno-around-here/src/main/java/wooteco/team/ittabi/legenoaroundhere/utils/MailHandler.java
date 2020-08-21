package wooteco.team.ittabi.legenoaroundhere.utils;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailHandler {

    private JavaMailSender javaMailSender;
    private MimeMessage mimeMessage;
    private MimeMessageHelper mimeMessageHelper;

    public MailHandler(JavaMailSender javaMailSender) throws MessagingException {
        this.javaMailSender = javaMailSender;
        mimeMessage = javaMailSender.createMimeMessage();
        mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
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
        try {
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}