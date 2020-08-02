package wooteco.team.ittabi.legenoaroundhere.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.exception.UserInputException;

@Entity
@Table(name = "post")
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    private static final int MAX_LENGTH = 20;

    private String writing;

    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

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

    public void addImage(Image image) {
        images.add(image);
        image.setPost(this);
    }

    public boolean isSameState(State state) {
        return Objects.equals(this.state, state);
    }

    public boolean isNotSameState(State state) {
        return !Objects.equals(this.state, state);
    }

}
