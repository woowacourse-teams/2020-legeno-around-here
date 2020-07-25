package wooteco.team.ittabi.legenoaroundhere.domain.user;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Email {

    private static final String EMAIL_FORMAT = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_FORMAT);

    private String email;

    protected Email() {
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
        Matcher matcher = EMAIL_PATTERN.matcher(email);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("이메일 형식이 잘못됐습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Email email1 = (Email) o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "Email{" +
            "email='" + email + '\'' +
            '}';
    }
}
