package wooteco.team.ittabi.legenoaroundhere.domain.award;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import wooteco.team.ittabi.legenoaroundhere.domain.post.Post;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@SQLDelete(sql = "UPDATE popular_post_award SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Table(indexes = @Index(name="idx_popular_post_award_awardee", columnList = "awardee_id"))
public class PopularPostAward extends AwardEntity {

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private Integer ranking;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Builder
    private PopularPostAward(String name, User awardee, Post post, Integer ranking,
        LocalDate startDate, LocalDate endDate) {
        super(name, awardee);
        this.post = post;
        this.ranking = ranking;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isTopBy(int number) {
        return ranking <= number;
    }

}
