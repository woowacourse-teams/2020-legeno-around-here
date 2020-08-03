package wooteco.team.ittabi.legenoaroundhere.domain.sector;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@ToString
public class Sector extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "last_modifier_id", nullable = false)
    private User lastModifier;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private SectorState state;

    public void update(Sector sector) {
        this.name = sector.name;
        this.description = sector.description;
        this.lastModifier = sector.lastModifier;
    }

    public void setState(SectorState state) {
        this.state = state;
    }

    public boolean isNotUsed() {
        return !state.isUsed();
    }

    public boolean isUsed() {
        return state.isUsed();
    }
}
