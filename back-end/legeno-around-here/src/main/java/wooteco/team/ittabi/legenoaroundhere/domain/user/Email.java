package wooteco.team.ittabi.legenoaroundhere.domain.user;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class Email {

    private static final String EMAIL_FORMAT = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_FORMAT);

    @Column(nullable = false, unique = true)
    private String email;

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
            throw new WrongUserInputException("이메일이 null입니다.");
        }
    }

    private void validateFormat(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);

        if (!matcher.matches()) {
            throw new WrongUserInputException("이메일 형식이 잘못됐습니다.");
        }
    }
}
