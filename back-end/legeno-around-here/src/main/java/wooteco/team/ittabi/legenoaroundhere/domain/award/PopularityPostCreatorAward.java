package wooteco.team.ittabi.legenoaroundhere.domain.award;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@ToString
@SQLDelete(sql = "UPDATE area SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class PopularityPostCreatorAward extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "awardee_id", nullable = false)
    private User awardee;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PopularCycle cycle;

    @Column(nullable = false)
    private LocalDate PeriodStart;

    @Column(nullable = false)
    private LocalDate PeriodEnd;

    @Column(nullable = false)
    private Long ranking;

    public String getCycleName() {
        return cycle.name();
    }
}
