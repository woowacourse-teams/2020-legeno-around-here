package wooteco.team.ittabi.legenoaroundhere.domain.user;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Embeddable;

@Embeddable
public class Email {

    private static final String EMAIL_FORMAT = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

    private String email;

    public Email() {
    }

    public Email(String email) {
        validate(email);
        this.email = email;
    }

    private void validate(String email) {
        validateNull(email);
        validateFormat(email);
    }

    private void validateNull(String email) {
        if (Objects.isNull(email)) {
            throw new IllegalArgumentException("이메일이 null입니다.");
        }
    }

    private void validateFormat(String email) {
        Pattern p = Pattern.compile(EMAIL_FORMAT);
        Matcher m = p.matcher(email);

        if (!m.matches()) {
            throw new IllegalArgumentException("이메일 형식이 잘못됐습니다.");
        }
    }
}
