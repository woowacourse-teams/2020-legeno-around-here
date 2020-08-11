package wooteco.team.ittabi.legenoaroundhere.domain.post.like;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@Entity
@Getter
@Table(name = "post_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"post", "creator"})
public class Like extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LikeState likeState;

    public Like(Post post, User creator) {
        this.post = post;
        this.creator = creator;
        this.likeState = LikeState.INACTIVATED;
    }

    public boolean isSameUser(User user) {
        return Objects.equals(this.creator, user);
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void inactivate(Post post) {
        post.removeLike(this);
        this.likeState = LikeState.INACTIVATED;
        this.post = post;
    }

    public void activate(Post post) {
        post.addLike(this);
        this.likeState = LikeState.ACTIVATED;
        this.post = post;
    }
}
