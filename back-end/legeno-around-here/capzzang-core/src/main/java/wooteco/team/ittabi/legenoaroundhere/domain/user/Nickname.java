package wooteco.team.ittabi.legenoaroundhere.domain.user;

import java.util.Arrays;
import java.util.List;
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
public class Nickname {

    private static final int MAX_LENGTH = 30;
    private static final List<String> FORBIDDEN_SUBSTRINGS = Arrays.asList(
        "&", "=", "_", "\'", "-", "+", ",", "<", ">", "..");

    @Column(nullable = false)
    private String nickname;

    public Nickname(String nickname) {
        validate(nickname);
        this.nickname = nickname;
    }

    private void validate(String nickname) {
        validateNull(nickname);
        validateEmpty(nickname);
        validateLength(nickname);
        validateContainForbiddenStrings(nickname);
    }

    private void validateNull(String nickname) {
        if (Objects.isNull(nickname)) {
            throw new WrongUserInputException("닉네임이 null 입니다.");
        }
    }

    private void validateEmpty(String nickname) {
        if (nickname.isEmpty()) {
            throw new WrongUserInputException("닉네임이 비어있습니다.");
        }
    }

    private void validateLength(String nickname) {
        if (nickname.length() > MAX_LENGTH) {
            throw new WrongUserInputException("닉네임은 " + MAX_LENGTH + "글자 이하여야 합니다.");
        }
    }

    private void validateContainForbiddenStrings(String nickname) {
        for (String forbiddenSubstring : FORBIDDEN_SUBSTRINGS) {
            validateContainForbiddenString(nickname, forbiddenSubstring);
        }
    }

    private void validateContainForbiddenString(String nickname, String forbiddenSubstring) {
        if (nickname.contains(forbiddenSubstring)) {
            throw new WrongUserInputException(
                "닉네임은 " + forbiddenSubstring + "을 포함할 수 없습니다.");
        }
    }
}
