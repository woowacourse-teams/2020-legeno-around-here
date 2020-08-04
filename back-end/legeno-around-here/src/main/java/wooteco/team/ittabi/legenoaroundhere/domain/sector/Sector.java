package wooteco.team.ittabi.legenoaroundhere.domain.sector;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Sector extends BaseEntity {

    @Embedded
    private SectorName name;

    @Embedded
    private SectorDescription description;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "last_modifier_id", nullable = false)
    private User lastModifier;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private SectorState state;

    @Builder
    public Sector(String name, String description, User creator, User lastModifier,
        SectorState state) {
        this.name = new SectorName(name);
        this.description = new SectorDescription(description);
        validate(creator, lastModifier, state);
        this.creator = creator;
        this.lastModifier = lastModifier;
        this.state = state;
    }

    private void validate(User creator, User lastModifier, SectorState state) {
        Objects.requireNonNull(creator, "Creator는 Null일 수 없습니다.");
        Objects.requireNonNull(lastModifier, "LastModifier는 Null일 수 없습니다.");
        Objects.requireNonNull(state, "SectorState는 Null일 수 없습니다.");
    }

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

    public String getStringName() {
        return this.name.getName();
    }

    public String getStringDescription() {
        return this.description.getDescription();
    }
}
