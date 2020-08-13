package wooteco.team.ittabi.legenoaroundhere.domain.post.comment;

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
import wooteco.team.ittabi.legenoaroundhere.domain.post.zzang.ZzangState;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"comment", "creator"})
public class CommentZzang extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ZzangState zzangState;

    public CommentZzang(Comment comment, User creator) {
        this.comment = comment;
        this.creator = creator;
        this.zzangState = ZzangState.INACTIVATED;
    }
}
