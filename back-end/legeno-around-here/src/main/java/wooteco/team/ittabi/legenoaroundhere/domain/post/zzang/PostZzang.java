package wooteco.team.ittabi.legenoaroundhere.domain.post.zzang;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"post", "creator"})
public class PostZzang extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ZzangState zzangState;

    public PostZzang(Post post, User creator) {
        this.post = post;
        this.creator = creator;
        this.zzangState = ZzangState.INACTIVATED;
    }

    public boolean isSameCreator(User user) {
        return Objects.equals(this.creator, user);
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void inactivate(Post post) {
        post.removePostZzang(this);
        this.zzangState = ZzangState.INACTIVATED;
        this.post = post;
    }

    public void activate(Post post) {
        post.addPostZzang(this);
        this.zzangState = ZzangState.ACTIVATED;
        this.post = post;
    }
}
