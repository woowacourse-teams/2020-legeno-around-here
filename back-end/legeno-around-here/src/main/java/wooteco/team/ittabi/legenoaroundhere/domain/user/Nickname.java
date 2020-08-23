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
public class Nickname {

    private static final int MAX_LENGTH = 10;

    @Column(nullable = false)
    private String nickname;

    public Nickname(String nickname) {
        validate(nickname);
        this.nickname = nickname;
    }

    private void validate(String nickname) {
        validateNull(nickname);
        validateEmpty(nickname);
        validateSpace(nickname);
        validateLength(nickname);
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

    private void validateSpace(String nickname) {
        if (nickname.contains(" ")) {
            throw new WrongUserInputException("닉네임은 공백을 포함할 수 없습니다.");
        }
    }

    private void validateLength(String nickname) {
        if (nickname.length() > MAX_LENGTH) {
            throw new WrongUserInputException("닉네임은 " + MAX_LENGTH + "글자 이하여야 합니다.");
        }
    }
}
