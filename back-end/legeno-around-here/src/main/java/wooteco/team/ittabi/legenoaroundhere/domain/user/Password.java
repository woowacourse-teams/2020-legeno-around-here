package wooteco.team.ittabi.legenoaroundhere.domain.user;

import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Password {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 16;

    private String password;

    public Password() {
    }

    public Password(String password) {
        validate(password);
        this.password = password; // Todo: 암호화 해야 됨? => security reference B-Crypto
    }

    private void validate(String password) {
        validateNull(password);
        validateLength(password);
    }

    private void validateNull(String password) {
        if (Objects.isNull(password)) {
            throw new IllegalArgumentException("password가 null 입니다.");
        }
    }

    private void validateLength(String password) {
        if (password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("비밀번호는 8 ~ 16 자여야 합니다.");
        }
    }
}
