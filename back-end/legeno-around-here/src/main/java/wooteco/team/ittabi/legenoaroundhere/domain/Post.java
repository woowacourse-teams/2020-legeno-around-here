package wooteco.team.ittabi.legenoaroundhere.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.exception.UserInputException;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    private static final int MAX_LENGTH = 20;

    private String writing;

    @Enumerated(EnumType.STRING)
    private State state;

    public Post(String writing) {
        validateLength(writing);
        this.writing = writing;
        this.state = State.PUBLISHED;
    }

    private void validateLength(String writing) {
        if (writing.length() > MAX_LENGTH) {
            throw new UserInputException(MAX_LENGTH + "글자를 초과했습니다!");
        }
    }

    public boolean isSameState(State state) {
        return Objects.equals(this.state, state);
    }

    public boolean isNotSameState(State state) {
        return !Objects.equals(this.state, state);
    }

}
