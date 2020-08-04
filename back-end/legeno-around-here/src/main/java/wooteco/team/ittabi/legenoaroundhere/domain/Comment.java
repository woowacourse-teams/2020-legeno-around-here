package wooteco.team.ittabi.legenoaroundhere.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.UserInputException;

@Entity
@Table(name = "comment")
@Getter
@Setter
@ToString(exclude = {"post", "user"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    private static final int MAX_LENGTH = 20;

    @Column(nullable = false)
    private String writing;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(User user, Post post, String writing) {
        validateLength(writing);
        this.writing = writing;
        this.state = State.PUBLISHED;
        this.user = user;
        this.post = post;
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
