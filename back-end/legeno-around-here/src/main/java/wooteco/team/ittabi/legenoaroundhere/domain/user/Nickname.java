package wooteco.team.ittabi.legenoaroundhere.domain.user;

import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Nickname {

    private static final int MAX_LENGTH = 10;

    private String nickname;

    protected Nickname() {
    }

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
            throw new IllegalArgumentException("닉네임이 null 입니다.");
        }
    }

    private void validateEmpty(String nickname) {
        if (nickname.isEmpty()) {
            throw new IllegalArgumentException("닉네임이 비어있습니다.");
        }
    }

    private void validateSpace(String nickname) {
        if (nickname.contains(" ")) {
            throw new IllegalArgumentException("닉네임은 공백을 포함할 수 없습니다.");
        }
    }

    private void validateLength(String nickname) {
        if (nickname.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("닉네임은 " + MAX_LENGTH + "글자 이하여야 합니다.");
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
        Nickname nickname1 = (Nickname) o;
        return Objects.equals(nickname, nickname1.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }

    @Override
    public String toString() {
        return "Nickname{" +
            "nickname='" + nickname + '\'' +
            '}';
    }
}
