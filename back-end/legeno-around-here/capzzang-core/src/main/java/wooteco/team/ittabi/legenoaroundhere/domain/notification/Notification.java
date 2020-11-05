package wooteco.team.ittabi.legenoaroundhere.domain.notification;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.comment.Comment;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
@SQLDelete(sql = "UPDATE notification SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Table(indexes = @Index(columnList = "receiver_id", name="idx_notification_receiver"))
public class Notification extends BaseEntity {

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    @Builder.Default
    private Comment comment = null;

    @ManyToOne
    @JoinColumn(name = "sector_id")
    @Builder.Default
    private Sector sector = null;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @Builder.Default
    private Post post = null;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Builder.Default
    private User user = null;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 0")
    @Builder.Default
    private Boolean isRead = Boolean.FALSE;

    public boolean isDifferentReceiver(User user) {
        return !this.receiver.equals(user);
    }

    public void read() {
        this.isRead = Boolean.TRUE;
    }
}
