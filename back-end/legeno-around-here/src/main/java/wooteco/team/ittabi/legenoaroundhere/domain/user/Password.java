package wooteco.team.ittabi.legenoaroundhere.domain.user;

import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import wooteco.team.ittabi.legenoaroundhere.exception.UserInputException;

@Getter
@Setter
@Embeddable
public class Password {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 16;

    private String password;

    protected Password() {
    }

    public Password(String password) {
        validate(password);
        this.password = password;
    }

    private void validate(String password) {
        validateEmpty(password);
    }

    private void validateEmpty(String password) {
        if (Objects.isNull(password) || password.isEmpty()) {
            throw new UserInputException("비밀번호가 비어있습니다.");
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
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }

    @Override
    public String toString() {
        return "Password{" +
            "password='" + password + '\'' +
            '}';
    }
}
