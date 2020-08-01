package wooteco.team.ittabi.legenoaroundhere.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.UserInputException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
public class Post extends BaseEntity {

    private static final int MAX_LENGTH = 20;

    private String writing;

    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public Post(User user, String writing) {
        validateLength(writing);
        this.writing = writing;
        this.state = State.PUBLISHED;
        this.user = user;
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
