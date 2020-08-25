package wooteco.team.ittabi.legenoaroundhere.domain.user;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Password {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 16;

    @Column(nullable = false)
    private String password;

    public Password(String password) {
        validate(password);
        this.password = password;
    }

    private void validate(String password) {
        validateEmpty(password);
    }

    private void validateEmpty(String password) {
        if (Objects.isNull(password) || password.isEmpty()) {
            throw new WrongUserInputException("비밀번호가 비어있습니다.");
        }
    }
}
