package wooteco.team.ittabi.legenoaroundhere.config;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setProtocol("SMTP");
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);

        return javaMailSender;
    }
}
