package wooteco.team.ittabi.legenoaroundhere.domain.area;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@ToString
public class Area extends BaseEntity {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String firstDepthName;

    @Column(nullable = false)
    private String secondDepthName;

    @Column(nullable = false)
    private String thirdDepthName;

    @Column(nullable = false)
    private String fourthDepthName;

    @Column(nullable = false)
    private Boolean used;

    public boolean isUsed() {
        return used;
    }
}
