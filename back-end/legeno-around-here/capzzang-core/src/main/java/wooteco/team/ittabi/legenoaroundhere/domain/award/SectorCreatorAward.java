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
import wooteco.team.ittabi.legenoaroundhere.domain.sector.Sector;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@SQLDelete(sql = "UPDATE sector_creator_award SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Table(indexes = {
    @Index(name="idx_sector_creator_award_awardee", columnList = "awardee_id"),
    @Index(name="idx_sector_creator_award_sector", columnList = "sector_id")
})
public class SectorCreatorAward extends AwardEntity {

    @OneToOne
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    @Column(nullable = false)
    private LocalDate date;

    @Builder
    private SectorCreatorAward(String name, User awardee, Sector sector, LocalDate date) {
        super(name, awardee);
        this.sector = sector;
        this.date = date;
    }
}
