package wooteco.team.ittabi.legenoaroundhere.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import wooteco.team.ittabi.legenoaroundhere.exception.UserInputException;

@Entity
public class Post extends BaseEntity {

    private static final int LIMIT_LENGTH = 20;

    private String writing;
    @Enumerated(EnumType.STRING)
    private State state;

    protected Post() {
    }

    public Post(String writing) {
        validateLength(writing);
        this.writing = writing;
        this.state = State.PUBLISHED;
    }

    private void validateLength(String writing) {
        if (writing.length() > LIMIT_LENGTH) {
            throw new UserInputException(LIMIT_LENGTH + "글자를 초과했습니다!");
        }
    }

    public boolean isSameState(State state) {
        return Objects.equals(this.state, state);
    }

    public boolean isNotSameState(State state) {
        return !Objects.equals(this.state, state);
    }

    public String getWriting() {
        return writing;
    }

    public void setWriting(String writing) {
        this.writing = writing;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Post{" +
            "writing='" + writing + '\'' +
            ", state=" + state +
            '}';
    }
}
