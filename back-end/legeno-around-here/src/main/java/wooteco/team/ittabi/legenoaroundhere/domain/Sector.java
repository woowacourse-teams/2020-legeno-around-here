package wooteco.team.ittabi.legenoaroundhere.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class Sector extends BaseEntity {

    @Column(unique = true)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "create_user_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "modify_user_id")
    private User lastModifier;

    public void update(Sector sector) {
        this.name = sector.name;
        this.description = sector.description;
        this.lastModifier = sector.lastModifier;
    }
}
