package wooteco.team.ittabi.legenoaroundhere.domain.post;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.area.Area;
import wooteco.team.ittabi.legenoaroundhere.domain.post.image.Image;
import wooteco.team.ittabi.legenoaroundhere.domain.post.zzang.PostZzang;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

@Entity
@Table(name = "post")
@Getter
@Setter
@ToString(exclude = {"comments", "images", "postZzangs"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    private static final int MAX_LENGTH = 20;

    @Lob
    @Column(nullable = false)
    private String writing;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PostZzang> postZzangs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @ManyToOne
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @Builder
    public Post(String writing, Area area, Sector sector, User creator) {
        validateLength(writing);
        this.writing = writing;
        this.area = area;
        this.sector = sector;
        this.state = State.PUBLISHED;
        this.creator = creator;
    }

    private void validateLength(String writing) {
        if (writing.length() > MAX_LENGTH) {
            throw new WrongUserInputException(MAX_LENGTH + "글자를 초과했습니다!");
        }
    }

    public boolean isSameState(State state) {
        return Objects.equals(this.state, state);
    }

    public boolean isNotSameState(State state) {
        return !Objects.equals(this.state, state);
    }

    public int getPostZzangCount() {
        return postZzangs.size();
    }

    public boolean existPostZzangBy(User user) {
        return postZzangs.stream()
            .anyMatch(postZzang -> postZzang.isSameCreator(user));
    }

    public PostZzang findPostZzangBy(User user) {
        return postZzangs.stream()
            .filter(postZzang -> postZzang.isSameCreator(user))
            .findFirst()
            .orElseGet(() -> new PostZzang(this, user));
    }

    public void addPostZzang(PostZzang postZzang) {
        postZzangs.add(postZzang);
    }

    public void removePostZzang(PostZzang postZzang) {
        postZzangs.remove(postZzang);
    }
}
